package tv.zimuzu.sdk.p4pclient;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

public class P4PClientService extends Service implements P4PClientJni.P4PClientJniEvent {
    static final int MSG_ENABLECONSOLE = 5;
    static final int MSG_P4PSTART = 1;
    static final int MSG_P4PSTARTED = 7;
    static final int MSG_P4PSTOP = 2;
    static final int MSG_P4PTASK_START = 3;
    static final int MSG_P4PTASK_STOP = 4;
    static final int MSG_STAT = 6;
    private static final String TAG = "p4pclient";
    final Messenger mMessenger = new Messenger(new IncomingHandler());
    private volatile String statStr;

    class IncomingHandler extends Handler {
        IncomingHandler() {
        }

        public void handleMessage(Message msg) {
            P4PClientService.this.handleRemoteMessage(msg);
        }
    }

    public IBinder onBind(Intent intent) {
        return this.mMessenger.getBinder();
    }

    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "create p4pclient service");
        P4PClientJni.getInstance();
        P4PClientJni.setEventListener(this);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return 1;
    }

    public void onDestroy() {
        this.statStr = "";
        P4PClientJni.getInstance();
        P4PClientJni.setEventListener((P4PClientJni.P4PClientJniEvent) null);
        P4PClientJni.getInstance().stop();
        super.onDestroy();
    }

    /* access modifiers changed from: private */
    public void handleRemoteMessage(Message msg) {
        switch (msg.what) {
            case 1:
                P4PClientJni.getInstance().start(msg.getData().getString("tracker"),
                        msg.getData().getString("htracker"),
                        msg.getData().getString("stun"),
                        msg.getData().getString("uid"),
                        msg.getData().getString("token"));
                try {
                    Thread.sleep(300);
                } catch (Exception e) {
                }
                Messenger client = msg.replyTo;
                Message replyMsg = Message.obtain((Handler) null, 7);
                replyMsg.setData(new Bundle());
                try {
                    client.send(replyMsg);
                    return;
                } catch (Exception e2) {
                    return;
                }
            case 2:
                this.statStr = "";
                P4PClientJni.getInstance().stop();
                return;
            case 3:
                P4PClientJni.getInstance().startTask(
                        msg.getData().getString("fileId"),
                        msg.getData().getString("url"),
                        msg.getData().getString("storePath"),
                        msg.getData().getString("maskStorePath"));
                return;
            case 4:
                P4PClientJni.getInstance().stopTask(msg.getData().getString("fileId"));
                return;
            case 5:
                P4PClientJni.getInstance().enableLogConsole();
                return;
            case 6:
                Messenger client2 = msg.replyTo;
                Message replyMsg2 = Message.obtain((Handler) null, 6);
                Bundle bundle = new Bundle();
                bundle.putString("reply", this.statStr);
                replyMsg2.setData(bundle);
                try {
                    client2.send(replyMsg2);
                    return;
                } catch (Exception e3) {
                    return;
                }
            default:
                return;
        }
    }

    public void onTaskStat(String stat) {
        this.statStr = stat;
    }
}
