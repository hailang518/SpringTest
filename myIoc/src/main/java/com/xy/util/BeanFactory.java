package com.xy.util;

import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.Iterator;

public class BeanFactory {

    public Object newInstance(String xmlName) {
        return parseXml(xmlName);
    }

    private Object parseXml(String xmlName) {
        try {
            String rootPath = this.getClass().getClassLoader().getResource("/").getPath();
            String xmlPath = rootPath + xmlName;
            SAXReader saxReader = new SAXReader();
            Document doc = saxReader.read(new File(xmlPath));
            Element rootElement = doc.getRootElement();
            Iterator iterator = rootElement.elementIterator();
            while (iterator.hasNext()){

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
