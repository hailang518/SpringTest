package com.xy.service;

import com.xy.dao.Dao;

/**
 * @author XY
 */
public class UserService {
    private Dao dao;

    public UserService() {

    }

    public void find(){
        System.out.println("userService");
        dao.query();
    }

    // 构造方法注入dao
    public UserService(Dao dao) {
        this.dao = dao;
    }

    // stter 方法注入
//    public void setDao(Dao dao) {
//        this.dao = dao;
//    }
}
