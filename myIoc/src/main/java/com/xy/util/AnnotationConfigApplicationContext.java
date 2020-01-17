package com.xy.util;

import com.xy.annotation.Autowired;
import com.xy.annotation.MyService;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author XY
 */
public class AnnotationConfigApplicationContext {

    private static Map map = new HashMap<String, Object>();

    public void scan(String scanPackage){
        try {
            String rootPaht = this.getClass().getClassLoader().getResource("").getPath();
            String scanPackagePath = rootPaht + scanPackage.replaceAll("\\.", "\\\\");
            File file = new File(scanPackagePath);
            String[] fileLists = file.list();
            for (String fileName : fileLists){
                String clazzName = scanPackage + "." + fileName.substring(0, fileName.lastIndexOf("."));
                Class clazz = Class.forName(clazzName);
                if (clazz.isAnnotationPresent(MyService.class)){
                    Object obj = clazz.newInstance();
                    map.put(this.getBeanName(clazz.getSimpleName()), obj);

                    Field[] fields = clazz.getDeclaredFields();
                    for (Field field : fields) {
                        if(field.isAnnotationPresent(Autowired.class)){
                            String fieldName = this.getBeanName(field.getType().getSimpleName());
                            field.setAccessible(true);
                            field.set(obj, map.get(fieldName));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取bean
     * @param beanName
     * @return
     */
    public Object getBean(String beanName) {
        return map.get(beanName);
    }

    /**
     * 通过bean的类型，得到bean的名称
     * @param clazzName
     * @return
     */
    private String getBeanName(String clazzName){
        return clazzName.substring(0, 1).toLowerCase() + clazzName.substring(1);
    }
}
