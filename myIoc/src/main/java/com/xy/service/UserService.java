package com.xy.service;

import com.xy.dao.Dao;

/**
 * @author XY
 */
public class UserService {
    private Dao dao;

    public void find(){
        System.out.println("userService");
        dao.query();
    }

    public Dao getDao() {
        return dao;
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }
}
