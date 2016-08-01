package com.starnine.passwd.utils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * Created by licheng on 16-8-1.
 */
public class DESTest {


    public static void main(String[] args) {
        String content = "nihaozho钥ngguozheshi";
        // 密码长度必须是8的倍数
        String password = "abcd1995";
        System.out.println("密　钥：" + password);
        System.out.println("加密前：" + content);
        String result = encrypt(content, password);
        System.out.println("加密后：" + result);
        String decryResult = decrypt(result, password);
        System.out.println("解密后：" + decryResult);
    }

    public static String encrypt(String content, String key) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            byte[] result = cipher.doFinal(content.getBytes());
            return byte2Hex(result);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    private final static char[] hexarray = "0123456789ABCDEF".toCharArray();

    public static String byte2Hex(byte[] b) {
        char[] hex = new char[b.length * 2];
        for (int j = 0; j < b.length; j++) {
            int v = b[j] & 0xff;
            hex[j * 2] = hexarray[v >>> 4];
            hex[j * 2 + 1] = hexarray[v & 0x0f];
        }
        return new String(hex);
    }

    public static byte[] Hex2byte(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static String decrypt(String s, String key) {
        try {
            byte[] content = Hex2byte(s);
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
            byte[] result = cipher.doFinal(content);
            return new String(result);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }


}
