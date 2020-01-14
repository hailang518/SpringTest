package com.xy.util;

import com.sun.org.apache.xpath.internal.axes.ChildIterator;
import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author XY
 */
public class BeanFactory {
    private String xmlName;

    private Map<String, Object> map = new HashMap<String, Object>();

    public BeanFactory(String xmlName) {
        this.xmlName = xmlName;
        this.parseXml(xmlName);
    }

    public Object getBean(String beanName) {
        return map.get(beanName);
    }

    private void parseXml(String xmlName) {
        try {
            String rootPath = this.getClass().getClassLoader().getResource("").getPath();
            String xmlPath = rootPath + xmlName;
            SAXReader saxReader = new SAXReader();
            Document doc = saxReader.read(new File(xmlPath));
            Element rootElement = doc.getRootElement();

            Attribute attribute = rootElement.attribute("autowire-default");
            boolean flag = false;
            if (attribute != null){
                flag = true;
            }

            Iterator<Element> iterator = rootElement.elementIterator();
            while (iterator.hasNext()){
                Element childElement = iterator.next();
                Attribute attributeId = childElement.attribute("id");
                Attribute attributeClass = childElement.attribute("class");
                String idName = attributeId.getValue();
                String className = attributeClass.getValue();
                Class<?> clazz = Class.forName(className);

                Object obj = null;
                for (Iterator<Element> childIterator = childElement.elementIterator(); childIterator.hasNext();){
                    Element subChildElement = childIterator.next();
                    if ("property".equals(subChildElement.getName())){
                        // setter注入
                        obj = clazz.newInstance();
                        String subName = subChildElement.attribute("name").getValue();
                        String subRef = subChildElement.attribute("ref").getValue();
                        Object injectObj = map.get(subRef);
                        Field field = clazz.getDeclaredField(subName);
                        field.setAccessible(true);
                        field.set(obj, injectObj);
                    }
                    else if ("constructor".equals(subChildElement.getName())){
                        // 构造器注入
                        String subRef = subChildElement.attribute("ref").getValue();
                        Object injectObj = map.get(subRef);
                        Constructor<?> constructor = clazz.getConstructor(injectObj.getClass().getInterfaces()[0]);
                        obj = constructor.newInstance(injectObj);  // 通过有参构造方法得到对象
                    }
                    else{
                        // 抛出异常
                        throw new ServiceException("通过BeanName获取Bean失败");
                    }
                }

                if (obj == null){
                    if (flag){
                        // 此处自动装配按照byType进行，如按照byName进行，方法类似
                        if ("byType".equals(attribute.getValue())) {
                            // 判断是否有依赖
                            Field[] fields = clazz.getDeclaredFields();
                            for (Field field : fields) {
                                Class fieldType = field.getType();   // 属性的类型，如String.class

                                int count = 0;
                                Object injectObj = null;
                                for (String key : map.keySet()) {
                                    Class interfaceClazz = map.get(key).getClass().getInterfaces()[0];
                                    if (fieldType.getName().equals(interfaceClazz.getName())){
                                        injectObj = map.get(key);
                                        count++;
                                    }
                                }

                                if (count > 1){
                                    // 由于是按照类型装配，需要判断一个接口是否有多个实现，若有多个实现，则不知装配哪一个，故报错
                                    throw new ServiceException("找到BeanName对应接口的多个实现");
                                } else {
                                    obj = clazz.newInstance();
                                    field.setAccessible(true);
                                    field.set(obj, injectObj);  // 自动注入，不需要在代码中写setter方法
                                }
                            }
                        }
                    }
                }

                // 定义的标签没有子节点
                if (obj == null){
                    obj = clazz.newInstance();
                }

                map.put(idName, obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
