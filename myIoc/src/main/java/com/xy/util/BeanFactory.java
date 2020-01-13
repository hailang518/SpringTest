package com.xy.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author XY
 */
public class BeanFactory {

    Map<String, Object> map = new HashMap<String, Object>();

    public Object newInstance(String xmlName) {
        return parseXml(xmlName);
    }

    private Object parseXml(String xmlName) {
        String rootPath = this.getClass().getClassLoader().getResource("/").getPath();
        String xmlPath = rootPath + File.separator + xmlName;

        return null;
    }
}
