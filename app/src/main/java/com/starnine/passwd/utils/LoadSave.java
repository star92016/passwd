package com.starnine.passwd.utils;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import java.io.File;

/**
 * Created by licheng on 16-7-26.
 */
public class LoadSave {
    public interface OnSaveFinished{
        public abstract void OnSaveFinished();
    }
    public interface OnLoadFinished{
        public abstract void OnLoadFinished();
    }
    public static void Save(File file,final OnSaveFinished onSaveFinished){
        final Handler handler =new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        onSaveFinished.OnSaveFinished();
                        break;
                }
            }
        };
        new Thread(){
            public void run(){
                //TODO Save
                /**文件的Magicnumber为'PASSWD'+0E+0F
                 * |配置文件起始位置|配置文件长度|
                 * |数据库文件起始位置|数据库文件长度|
                 * |配置文件二进制|数据库文件二进制|
                 *
                 * 如果加密(秘钥应该是公开的)
                 * 文件的Magicnumber为'PASSWD'+0D+0F
                 * |加密数据|
                 *
                 * 程序导入导出数据都是加密的
                 *
                 * 此部分如果Java不好实现可考虑用c++实现，
                 * 生成.so文件
                 * */
                Environment.getDataDirectory();
                handler.sendEmptyMessage(1);
            }
        }.start();
    }
}
