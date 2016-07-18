package com.starnine.passwd.data;

/**
 * Created by licheng on 16-7-18.
 * 保存用户名，密码，id，所属目录id
 */
public class PasswdItem {
    private int id;
    private int pid;
    private String username;
    private String password;

    public int getPid() {
        return pid;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
