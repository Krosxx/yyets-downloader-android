package com.yyets.freeappbase.util;

import android.text.TextUtils;

import java.util.regex.Pattern;

public class StringUtil {
    public static String replaceBlank(String str) {
        if (str != null) {
            return Pattern.compile("\\s*|\t|\r|\n").matcher(str).replaceAll("");
        }
        return "";
    }

    public static String getStringSuffix(String str) {
        if (TextUtils.isEmpty(str) || !str.contains(".")) {
            return null;
        }
        return str.substring(str.lastIndexOf("."));
    }
}
