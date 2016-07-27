package com.starnine.passwd.utils;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by licheng on 16-7-26.
 */
public class LoadSave {
    public static String Save(File file){
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
        RandomAccessFile fin1 = null,fin2=null,fout=null;
        byte[]bytes=new byte[1024];
        try {
            fout=new RandomAccessFile(file,"rw");
            fin1=new RandomAccessFile("/data/data/com.starnine.passwd/shared_prefs/config.xml","r");
            fin2=new RandomAccessFile("/data/data/com.starnine.passwd/databases/passwd.db","r");
            byte[]magic=new byte[]{'P','A','S','S','W','D',0x0e,0x0f};
            fout.write(magic);
            fout.seek(24);
            int cs=24,cl=0,ds,dl=0,len;
            while((len=fin1.read(bytes))>0){
                cl+=len;
                fout.write(bytes,0,len);
            }
            ds=cs+cl;
            fout.seek(ds);
            while((len=fin2.read(bytes))>0){
                dl+=len;
                fout.write(bytes,0,len);
            }
            fout.seek(8);
            fout.writeInt(cs);
            fout.writeInt(cl);
            fout.writeInt(ds);
            fout.writeInt(dl);
            fin1.close();
            fin2.close();
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void Load(File file){
        RandomAccessFile fin = null,fout1=null,fout2=null;
        byte[]bytes=new byte[1024];
        try {
            fin=new RandomAccessFile(file,"r");
            new File("/data/data/com.starnine.passwd/shared_prefs").mkdirs();
            new File("/data/data/com.starnine.passwd/databases").mkdirs();
            fout1=new RandomAccessFile("/data/data/com.starnine.passwd/shared_prefs/config.xml","rw");
            fout2=new RandomAccessFile("/data/data/com.starnine.passwd/databases/passwd.db","rw");
            byte[]magic=new byte[8];
            fin.read(magic);
            byte[]magic2=new byte[]{'P','A','S','S','W','D',0x0e,0x0f};
            for(int i=0;i<magic.length;i++){
                if(magic[i]!=magic2[i]){
                    throw new Exception("Wrong Magic");
                }
            }
            int cs,cl,ds,dl;
            cs=fin.readInt();
            cl=fin.readInt();
            ds=fin.readInt();
            dl=fin.readInt();
            fin.seek(cs);
            int len;
            while(cl>0){
                len=fin.read(bytes);
                if(len>cl){
                    fout1.write(bytes,0,cl);
                    cl=0;
                }else{
                    fout1.write(bytes,0,len);
                    cl-=len;
                }
            }
            fin.seek(ds);
            while(dl>0){
                len=fin.read(bytes);
                if(len>dl){
                    fout2.write(bytes,0,dl);
                    dl=0;
                }else{
                    fout2.write(bytes,0,len);
                    dl-=len;
                }
            }
            fin.close();
            fout1.close();
            fout2.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
