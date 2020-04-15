package tv.zimuzu.sdk.p4pclient;

import android.util.Log;
import java.util.concurrent.locks.ReentrantLock;

public class P4PClientJni {
    private static P4PClientJniEvent event = null;
    private static P4PClientJni instance = null;
    private static ReentrantLock lock = new ReentrantLock();

    public interface P4PClientJniEvent {
        void onTaskStat(String str);
    }

    /* access modifiers changed from: package-private */
    public native void enableLogConsole();

    /* access modifiers changed from: package-private */
    public native void start(String str, String str2, String str3, String str4, String str5);

    /* access modifiers changed from: package-private */
    public native void startTask(String str, String str2, String str3, String str4);

    /* access modifiers changed from: package-private */
    public native void stop();

    /* access modifiers changed from: package-private */
    public native void stopTask(String str);

    static {
        try {
            System.loadLibrary("p4pclient");
        } catch (Exception e) {
            Log.e("p4plcient", "load p4pclient err!");
        }
    }

    public static P4PClientJni getInstance() {
        if (instance == null) {
            instance = new P4PClientJni();
        }
        return instance;
    }

    public static void deInstance() {
        if (instance != null) {
            instance.stop();
        }
        instance = null;
    }

    public static void setEventListener(P4PClientJniEvent e) {
        lock.lock();
        event = e;
        lock.unlock();
    }

    public static void onTaskStat(String stat) {
        lock.lock();
        try {
            if (event != null) {
                event.onTaskStat(stat);
            }
        } catch (Exception e) {
        } finally {
            lock.unlock();
        }
    }
}
