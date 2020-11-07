package cn.vove7.rrzmz

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex

/**
 * # App
 *
 *
 * Created on 2020/4/15
 *
 * @author Vove
 */
class App : Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}