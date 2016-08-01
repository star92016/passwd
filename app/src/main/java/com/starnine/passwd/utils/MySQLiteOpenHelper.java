package com.starnine.passwd.utils;
/** This class can only put in this package,
 *  if you change it ,change PasswdItem meanwile.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.starnine.passwd.data.PasswdItem;
import com.starnine.passwd.data.TypeItem;

import java.util.Vector;

/**
 * Created by licheng on 16-7-26.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper{

    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public MySQLiteOpenHelper(Context context){
        this(context,"passwd.db",null,2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table typeitem (\n" +
                "id integer PRIMARY KEY autoincrement not null unique,\n" +
                "name text  default('') not null\n" +
                ");");
        sqLiteDatabase.execSQL("create table passwditem(\n" +
                "id integer primary key autoincrement not null unique,\n" +
                "pid integer not null,\n" +
                "username text not null default(''),\n" +
                "password text not null default(''),\n" +
                "foreign key(pid) references typeitem(id)\n" +
                ");");
        
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if(oldVersion==1&&newVersion==2){
            Vector<PasswdItem> vector=getAllPasswd(sqLiteDatabase);
            for(PasswdItem pi:vector){
                String p=pi.getPassword();
                p = DESTest.encrypt(p,User.getKey());
                if(p==null)p="";
                pi.setPassword(p);
                upatePasswd(pi,sqLiteDatabase);
            }
        }
    }
    public Vector<PasswdItem> getPasswdByType(TypeItem typeItem){
        return getPasswdByType(typeItem.getId());
    }
    public Vector<PasswdItem> getPasswdByType(int ppid){
        Vector<PasswdItem> vector=new Vector<>();
        SQLiteDatabase sqLiteDatabase=getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from passwditem where pid="+ppid,null);
        int id=cursor.getColumnIndex("id"),pid=cursor.getColumnIndex("pid"),
                username=cursor.getColumnIndex("username"),
                password=cursor.getColumnIndex("password");
        while (cursor.moveToNext()){
            PasswdItem passwdItem=new PasswdItem();
            passwdItem.setId(cursor.getInt(id));
            passwdItem.setPid(cursor.getInt(pid));
            passwdItem.setUsername(cursor.getString(username));
            passwdItem.setPassword(cursor.getString(password));
            vector.add(passwdItem);
        }
        cursor.close();
        sqLiteDatabase.close();
        return vector;
    }
    public Vector<TypeItem> getAllType(){
        Vector<TypeItem> vector=new Vector<>();
        SQLiteDatabase sqLiteDatabase=getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from typeitem",null);
        int id=cursor.getColumnIndex("id"),name=cursor.getColumnIndex("name");
        while (cursor.moveToNext()){
            TypeItem typeItem=new TypeItem();
            typeItem.setId(cursor.getInt(id));
            typeItem.setName(cursor.getString(name));
            vector.add(typeItem);
        }
        cursor.close();
        sqLiteDatabase.close();
        return vector;
    }
    private Vector<PasswdItem> getAllPasswd(SQLiteDatabase sqliteDatabase){
        Vector<PasswdItem> vector=new Vector<>();
        Cursor cursor=sqliteDatabase.rawQuery("select * from passwditem",null);
        int id=cursor.getColumnIndex("id"),pid=cursor.getColumnIndex("pid"),
                username=cursor.getColumnIndex("username"),
                password=cursor.getColumnIndex("password");
        while (cursor.moveToNext()){
            PasswdItem passwdItem=new PasswdItem();
            passwdItem.setId(cursor.getInt(id));
            passwdItem.setPid(cursor.getInt(pid));
            passwdItem.setUsername(cursor.getString(username));
            passwdItem.setPassword(cursor.getString(password));
            vector.add(passwdItem);
        }
        cursor.close();
        return vector;
    }
    public Vector<PasswdItem> getAllPasswd(){
        Vector<PasswdItem> vector;
        SQLiteDatabase sqliteDatabase=getReadableDatabase();
        vector=getAllPasswd(sqliteDatabase);
        sqliteDatabase.close();
        return vector;
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
        sqliteDatabase.execSQL("update passwditem set pid='"+passwdItem.getPid()
                +"',username='"+passwdItem.getUsername()+"',password='"+
                passwdItem.getPassword()+"' where id="+passwdItem.getId());
        sqliteDatabase.close();
    }
    private void upatePasswd(PasswdItem passwdItem,SQLiteDatabase sqliteDatabase){
        sqliteDatabase.execSQL("update passwditem set pid='"+passwdItem.getPid()
                +"',username='"+passwdItem.getUsername()+"',password='"+
                passwdItem.getPassword()+"' where id="+passwdItem.getId());
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
