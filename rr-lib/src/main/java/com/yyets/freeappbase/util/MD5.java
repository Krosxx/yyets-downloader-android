package com.yyets.freeappbase.util;

import android.text.TextUtils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static String md5(String string) {
        if (!TextUtils.isEmpty(string)) {
            try {
                String result = "";
                for (byte b : MessageDigest.getInstance("MD5").digest(string.getBytes())) {
                    String temp = Integer.toHexString(b & 255);
                    if (temp.length() == 1) {
                        temp = "0" + temp;
                    }
                    result = result + temp;
                }
                return result;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String md5(byte[] arr) {
        if (arr != null) {
            try {
                String result = "";
                for (byte b : MessageDigest.getInstance("MD5").digest(arr)) {
                    String temp = Integer.toHexString(b & 255);
                    if (temp.length() == 1) {
                        temp = "0" + temp;
                    }
                    result = result + temp;
                }
                return result;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static byte[] md5Arr(String string) {
        try {
            return MessageDigest.getInstance("MD5").digest(string.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
