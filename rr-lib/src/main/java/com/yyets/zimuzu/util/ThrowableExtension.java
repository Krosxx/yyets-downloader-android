package com.yyets.zimuzu.util;

import cn.vove7.rr_lib.BuildConfig;

/**
 * Created by Vove on 2020/3/17
 */
public class ThrowableExtension {
    public static void printStackTrace(Throwable e) {
        if (BuildConfig.DEBUG) {
            e.printStackTrace();
        }
    }
}
