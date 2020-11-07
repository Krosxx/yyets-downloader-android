package com.yyets.zimuzu.util;

import android.util.Log;

import cn.vove7.rr_lib.BuildConfig;

/**
 * Created by liben on 2020/11/6
 */
public class RRLog {
    private static final String TAG = "RRLog";

    public static void log(String m) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, m);
        }
    }

}
