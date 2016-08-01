package com.starnine.passwd.data;

import android.util.Log;

import com.starnine.passwd.utils.DESTest;
import com.starnine.passwd.utils.User;

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
        //TODO Magic is here
        StackTraceElement stack[] = (new Throwable()).getStackTrace();
        StackTraceElement ste=stack[1];

        if(ste.getClassName().equals("com.starnine.passwd.utils.MySQLiteOpenHelper")){
            return password;
        }else {
            String data = DESTest.decrypt(password, User.getKey());
            if (data == null) data = "";
            return data;
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setPassword(String password) {
        //TODO Magic is here
        StackTraceElement stack[] = (new Throwable()).getStackTrace();
        StackTraceElement ste=stack[1];

        if(ste.getClassName().equals("com.starnine.passwd.utils.MySQLiteOpenHelper")){
            this.password=password;
        }else{
            this.password = DESTest.encrypt(password,User.getKey());
            if(this.password==null)this.password="";
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
