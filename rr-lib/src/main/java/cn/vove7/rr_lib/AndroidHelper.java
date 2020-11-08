package cn.vove7.rr_lib;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * Created by liben on 2020/11/8
 */
public class AndroidHelper {
    @SuppressLint({"DiscouragedPrivateApi", "PrivateApi"})
    public static Context getAndroidContext() {
        try {
            return (Context) Class.forName("android.app.ActivityThread")
                    .getDeclaredMethod("currentApplication")
                    .invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
