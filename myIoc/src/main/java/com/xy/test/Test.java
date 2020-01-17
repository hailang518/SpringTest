package com.xy.test;

import com.xy.service.OrderService;
import com.xy.service.UserService;
import com.xy.util.AnnotationConfigApplicationContext;
import com.xy.util.BeanFactory;

/**
 * @author XY
 */
public class Test {

    public static void main(String[] args) {
//        BeanFactory bf = new BeanFactory("spring.xml");
//        UserService userService = (UserService) bf.getBean("userService");
//        userService.find();

        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
        ac.scan("com.xy.service");
        OrderService orderService = (OrderService) ac.getBean("orderService");
        orderService.find();
    }

}
