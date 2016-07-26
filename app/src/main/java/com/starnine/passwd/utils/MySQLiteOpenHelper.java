package com.starnine.passwd.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.starnine.passwd.data.PasswdItem;
import com.starnine.passwd.data.TypeItem;

/**
 * Created by licheng on 16-7-26.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper{

    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        SQLiteDatabase sqliteDatabase=getWritableDatabase();
        sqliteDatabase.execSQL("create table typeitem (\n" +
                "id integer PRIMARY KEY autoincrement not null unique,\n" +
                "name text  default('') not null\n" +
                ");");
        sqliteDatabase.execSQL("create table passwditem(\n" +
                "id integer primary key autoincrement not null unique,\n" +
                "pid integer not null,\n" +
                "username text not null default(''),\n" +
                "password text not null default(''),\n" +
                "foreign key(pid) references typeitem(id)\n" +
                ");");
        sqliteDatabase.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addPasswd(PasswdItem passwdItem){
        SQLiteDatabase sqliteDatabase=getWritableDatabase();
        sqliteDatabase.execSQL("insert into passwditem(pid,username,password) values('"+
                passwdItem.getPid()+"','"+passwdItem.getUsername()+"','"+passwdItem.getPassword()+"')");
        sqliteDatabase.close();
    }
    public void delPasswd(PasswdItem passwdItem){
        SQLiteDatabase sqliteDatabase=getWritableDatabase();
        sqliteDatabase.execSQL("delete from passwditem where id="+passwdItem.getId());
        sqliteDatabase.close();
    }
    public void upatePasswd(PasswdItem passwdItem){
        SQLiteDatabase sqliteDatabase=getWritableDatabase();
        sqliteDatabase.execSQL("update passwditem set pid='"+passwdItem.getPassword()
                +"',username='"+passwdItem.getUsername()+"',password='"+
                passwdItem.getPassword()+"' where id="+passwdItem.getId());
        sqliteDatabase.close();
    }

    public void addType(TypeItem typeItem){
        SQLiteDatabase sqliteDatabase=getWritableDatabase();
        sqliteDatabase.execSQL("insert into typeitem(name) values('"+typeItem.getName()+"')");
        sqliteDatabase.close();
    }
    public void delType(TypeItem typeItem){
        SQLiteDatabase sqliteDatabase=getWritableDatabase();
        sqliteDatabase.execSQL("delete from typeitem where id="+typeItem.getId());
        sqliteDatabase.close();
    }
    public void upateType(TypeItem typeItem){
        SQLiteDatabase sqliteDatabase=getWritableDatabase();
        sqliteDatabase.execSQL("update typeitem set name='"+typeItem.getName()+"' where id="+typeItem.getId());
        sqliteDatabase.close();
    }

}
