package cn.vove7.rrzmz;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

/**
 * # App
 * <p>
 * Created on 2020/4/15
 *
 * @author Vove
 */
public class App extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
