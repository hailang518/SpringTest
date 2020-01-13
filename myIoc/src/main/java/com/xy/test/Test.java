package com.xy.test;

import com.xy.service.UserService;
import com.xy.util.BeanFactory;

/**
 * @author XY
 */
public class Test {

    public static void main(String[] args) {
        BeanFactory bf = new BeanFactory();
        UserService userService = (UserService)bf.newInstance("applicationContext.xml");
        userService.find();
    }

}
