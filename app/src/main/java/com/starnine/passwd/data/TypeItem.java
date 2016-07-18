package com.starnine.passwd.data;

/**
 * Created by licheng on 16-7-18.
 * 如果id为0表示根目录
 */
public class TypeItem {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
