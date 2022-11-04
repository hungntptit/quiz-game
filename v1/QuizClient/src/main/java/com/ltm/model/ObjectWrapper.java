/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ltm.model;

import java.io.Serializable;

/**
 *
 * @author hung
 */
public class ObjectWrapper implements Serializable {

    private static final long serialVersionUID = 20210811011L;
    public static final int LOGIN_USER = 1;
    public static final int REPLY_LOGIN_USER = 2;
    
    public static final int REGISTER_USER = 3;
    public static final int REPLY_REGISTER_USER = 4;
    
    public static final int CREATE_ROOM = 5;
    public static final int REPLY_CREATE_ROOM = 6;
    
    public static final int JOIN_ROOM = 7;
    public static final int REPLY_JOIN_ROOM = 8;

    private int performative;
    private Object data;

    public ObjectWrapper() {
        super();
    }

    public ObjectWrapper(int performative, Object data) {
        super();
        this.performative = performative;
        this.data = data;
    }

    public int getPerformative() {
        return performative;
    }

    public void setPerformative(int performative) {
        this.performative = performative;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
