package tv.zimuzu.sdk.p4pclient;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import com.google.gson.Gson;
import com.yyets.zimuzu.util.ThrowableExtension;

import java.util.concurrent.locks.ReentrantLock;

public class P4PClient {
    private static final int STATE_ERR = 4;
    private static final int STATE_IDLE = 0;
    private static final int STATE_INITING = 1;
    private static final int STATE_REINITING = 2;
    private static final int STATE_WORKING = 3;
    private static final String TAG = "p4pclient";
    private Context context = null;
    /* access modifiers changed from: private */
    public P4PClientEvent listener = null;
    /* access modifiers changed from: private */
    public ReentrantLock lock = new ReentrantLock();
    /* access modifiers changed from: private */
    public boolean mBound = false;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.i(P4PClient.TAG, "p4pservice connected");
            Messenger unused = P4PClient.this.mService = new Messenger(service);
            boolean unused2 = P4PClient.this.mBound = true;
            boolean isRestart = false;
            P4PClient.this.lock.lock();
            if (P4PClient.this.state == 1 || P4PClient.this.state == 2) {
                if (P4PClient.this.state == 2) {
                    isRestart = true;
                }
                int unused3 = P4PClient.this.state = 3;
                P4PClient.this.lock.unlock();
                if (P4PClient.this.listener == null) {
                    return;
                }
                if (isRestart) {
                    P4PClient.this.listener.onP4PClientRestarted();
                } else {
                    P4PClient.this.listener.onP4PClientInited();
                }
            } else {
                P4PClient.this.lock.unlock();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.i(P4PClient.TAG, "p4pservice disconnected");
            P4PClient.this.lock.lock();
            if (P4PClient.this.state != 3) {
                P4PClient.this.lock.unlock();
                return;
            }
            int unused = P4PClient.this.state = 4;
            P4PClient.this.lock.unlock();
            Messenger unused2 = P4PClient.this.mService = null;
            boolean unused3 = P4PClient.this.mBound = false;
            P4PClient.this.restartService(true);
        }
    };
    private Messenger mRecevierReplyMsg = new Messenger(new ReceiverReplyMsgHandler());
    /* access modifiers changed from: private */
    public Messenger mService = null;
    /* access modifiers changed from: private */
    public int state = 0;

    private class ReceiverReplyMsgHandler extends Handler {
        private ReceiverReplyMsgHandler() {
        }

        public void handleMessage(Message msg) {
            if (msg.what == 6) {
                P4PClient.this.onTaskStat(msg.getData().getString("reply"));
            } else if (msg.what == 7) {
                P4PClient.this.onP4PStarted();
            }
        }
    }

    public void init(Context ctx) {
        Log.d(TAG, "init p4pclient");
        this.context = ctx;
        restartService(false);
    }

    private void checkNeedRestart() {
        this.lock.lock();
        if (this.state != 3) {
            this.lock.unlock();
            return;
        }
        this.state = 4;
        this.lock.unlock();
        restartService(true);
    }

    /* access modifiers changed from: private */
    @SuppressLint("WrongConstant")
    public void restartService(boolean restart) {
        Log.i(TAG, "bind p4pservice");
        if (this.context != null) {
            this.lock.lock();
            if (this.state == 0 || this.state == 4) {
                if (restart) {
                    this.state = 2;
                } else {
                    this.state = 1;
                }
                this.lock.unlock();
                this.mService = null;
                this.mBound = false;
                Intent intent = new Intent(this.context, P4PClientService.class);
                Log.i(TAG, "do bind p4pservice");
                if (!this.context.bindService(intent, this.mConnection, 1)) {
                    Log.i(TAG, "bind p4pservice err-----------------------------------");
                } else {
                    Log.i(TAG, "bind p4pservice OK-----------------------------------");
                }
            } else {
                this.lock.unlock();
            }
        }
    }

    public void unInit() {
        this.lock.lock();
        this.state = 0;
        this.lock.unlock();
        this.context.unbindService(this.mConnection);
    }

    public boolean start(String tracker, String htracker, String stun, String uid, String token) {
        Log.d(TAG, "start p4pclient");
        if (!this.mBound) {
            return false;
        }
        Message msg = Message.obtain((Handler) null, 1, 0, 0);
        Bundle bundle = new Bundle();
        bundle.putString("tracker", tracker);
        bundle.putString("htracker", htracker);
        bundle.putString("stun", stun);
        bundle.putString("uid", uid);
        bundle.putString("token", token);
        msg.setData(bundle);
        try {
            msg.replyTo = this.mRecevierReplyMsg;
            this.mService.send(msg);
        } catch (DeadObjectException e0) {
            Log.w(TAG, e0.toString());
            checkNeedRestart();
        } catch (Exception e) {
            Log.w(TAG, e.toString());
            return false;
        }
        return true;
    }

    public void stop() {
        if (this.mBound) {
            try {
                this.mService.send(Message.obtain((Handler) null, 2, 0, 0));
            } catch (Exception e) {
                Log.w(TAG, e.toString());
            }
        }
    }

    public boolean startTask(String fileId, String url, String storePath, String maskStorePath) {
        if (!this.mBound) {
            return false;
        }
        Message msg = Message.obtain((Handler) null, 3, 0, 0);
        Bundle bundle = new Bundle();
        bundle.putString("fileId", fileId);
        bundle.putString("url", url);
        bundle.putString("storePath", storePath);
        bundle.putString("maskStorePath", maskStorePath);
        msg.setData(bundle);
        try {
            this.mService.send(msg);
        } catch (DeadObjectException e0) {
            Log.w(TAG, e0.toString());
            checkNeedRestart();
        } catch (Exception e) {
            Log.w(TAG, e.toString());
            return false;
        }
        return true;
    }

    public boolean stopTask(String fileId) {
        if (!this.mBound) {
            return false;
        }
        Message msg = Message.obtain((Handler) null, 4, 0, 0);
        Bundle bundle = new Bundle();
        bundle.putString("fileId", fileId);
        msg.setData(bundle);
        try {
            this.mService.send(msg);
        } catch (DeadObjectException e0) {
            Log.w(TAG, e0.toString());
            checkNeedRestart();
        } catch (Exception e) {
            Log.w(TAG, e.toString());
            return false;
        }
        return true;
    }

    public void enableLogConsole() {
        if (this.mBound) {
            try {
                this.mService.send(Message.obtain((Handler) null, 5, 0, 0));
            } catch (DeadObjectException e0) {
                Log.w(TAG, e0.toString());
                checkNeedRestart();
            } catch (Exception e) {
                Log.w(TAG, e.toString());
            }
        }
    }

    public void queryStat() {
        if (this.mBound) {
            Message msg = Message.obtain((Handler) null, 6, 0, 0);
            try {
                msg.replyTo = this.mRecevierReplyMsg;
                this.mService.send(msg);
            } catch (DeadObjectException e0) {
                Log.w(TAG, e0.toString());
                checkNeedRestart();
            } catch (Exception e) {
                Log.w(TAG, e.toString());
            }
        }
    }

    public void setEventListener(P4PClientEvent listener2) {
        this.listener = listener2;
    }

    public void onTaskStat(String stat) {
        if (stat != null && stat.length() != 0) {
            try {
                this.listener.onTaskStat((P4PStat) new Gson().fromJson(stat, P4PStat.class));
            } catch (Exception e) {
                ThrowableExtension.printStackTrace(e);
            }
        }
    }

    public void onP4PStarted() {
        try {
            this.listener.onP4PClientStarted();
        } catch (Exception e) {
            ThrowableExtension.printStackTrace(e);
        }
    }
}
