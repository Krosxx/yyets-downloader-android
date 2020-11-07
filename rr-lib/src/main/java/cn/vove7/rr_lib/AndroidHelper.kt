package cn.vove7.rr_lib

import android.annotation.SuppressLint
import android.content.Context

/**
 * # AndroidHelper
 *
 * Created on 2020/4/14
 *
 * @author Vove
 */
@SuppressLint("DiscouragedPrivateApi")
object AndroidHelper {
    @JvmStatic
    val androidContext: Context by lazy {
        Class.forName("android.app.ActivityThread").getDeclaredMethod("currentApplication")
            .invoke(null) as Context
    }
}