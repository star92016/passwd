package com.starnine.passwd.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by licheng on 16-7-26.
 */
public class User {
    private final static String SALT="2&R3@q;l";
    private static String key="";
    public static boolean CheckUser(Context context,String passwd){
        String key=MD5(passwd);
        String s=MD5(key+SALT);
        SharedPreferences sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        if(sp.getString("passwd","").equals(s)){
            return true;
        }else
            return false;//wrong password
    }
    public static String getKey(){
        return key;
    }
    private static boolean islogin=false;
    public static boolean IsLogin(){
        return islogin;
    }
    public static boolean Login(Context context, String passwd){
        //TODO GET key from passwd
        User.key=passwd;
        return (islogin=CheckUser(context,passwd));
    }
    public static boolean Regist(Context context,String passwd){
        String s=MD5(MD5(passwd)+SALT);
        SharedPreferences sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("passwd",s);
        editor.apply();
        //creat database
        return true;
    }
    private static String MD5(String s){
        final char hexDigits[]={  '0' ,  '1' ,  '2' ,  '3' ,  '4' ,  '5' ,  '6' ,  '7' ,  '8' ,  '9' ,
                'A' ,  'B' ,  'C' ,  'D' ,  'E' ,  'F'  };
        byte [] strTemp = s.getBytes();
        MessageDigest mdTemp = null;
        try {
            mdTemp = MessageDigest.getInstance("MD5" );
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
        mdTemp.update(strTemp);
        byte [] md = mdTemp.digest();
        int  j = md.length;
        char  str[] =  new   char [j *  2 ];
        int  k =  0 ;
        for  ( int  i =  0 ; i < j; i++) {
            byte  byte0 = md[i];
            str[k++] = hexDigits[byte0 >>> 4  &  0xf ];
            str[k++] = hexDigits[byte0 & 0xf ];
        }
        return   new  String(str);
    }
}
