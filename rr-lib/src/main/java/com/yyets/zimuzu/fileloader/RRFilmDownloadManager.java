package com.yyets.zimuzu.fileloader;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.yyets.zimuzu.db.DBCache;
import com.yyets.zimuzu.db.bean.FilmCacheBean;
import com.yyets.zimuzu.util.ThrowableExtension;
import com.yyets.zimuzu.util.ZimuzuHelper;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import cn.vove7.rr_lib.InitCp;
import tv.zimuzu.sdk.p4pclient.P4PClient;
import tv.zimuzu.sdk.p4pclient.P4PClientEvent;
import tv.zimuzu.sdk.p4pclient.P4PStat;


public class RRFilmDownloadManager implements FileLoadingListener, P4PClientEvent {
    private static int MAX_RUNNING_TASK = 4;
    public static final RRFilmDownloadManager instance = new RRFilmDownloadManager();
    private static File maskSaveFile;
    /* access modifiers changed from: private */
    public volatile boolean isP4pInit = false;

    public HashMap<String, FilmCacheBean> mFilmCacheMap = new HashMap<>();
    private LinkedBlockingQueue<FilmCacheBean> waitingQueue = new LinkedBlockingQueue<>();


    private CopyOnWriteArrayList<FileLoadingListener> mLoadingListenerList = new CopyOnWriteArrayList<>();
    /* access modifiers changed from: private */
    public P4PClientEvent p4pListener;
    /* access modifiers changed from: private */
    public P4PClient p4pclient;
    private Timer timer;

    List<FilmCacheBean> unbeginCache = new ArrayList<>();

    RRFilmDownloadManager() {
    }

    boolean beganDl = false;

    private boolean callInit = false;

    public void init() {
        Context ctx = InitCp.androidContext;
        if (callInit) {
            Log.w("RRFilmDownloadManager", "aleardy callInit");
            return;
        }
        callInit = true;
        timer = new Timer(true);
        this.p4pclient = new P4PClient();
        this.p4pclient.setEventListener(this);
        this.p4pclient.init(ctx);
        this.timer.schedule(new RRFilmDownloadManager$1(this, ctx), 5000, 5000);
    }

    public void destroy() {
        try {
            this.timer.cancel();
            this.timer.purge();
        } catch (Exception e) {
        }
        this.timer = null;
        try {
            this.p4pclient.stop();
            this.p4pclient.unInit();
        } catch (Exception e2) {
        }
        callInit = false;
        isP4pInit = false;
        this.p4pclient = null;
    }

    public P4PClient getP4PClient() {
        return this.p4pclient;
    }

    public boolean isP4pInit() {
        return this.isP4pInit;
    }

    public void setP4PListener(P4PClientEvent p4pListener2) {
        this.p4pListener = p4pListener2;
    }

    public void addLoadingListener(FileLoadingListener listener) {
        this.mLoadingListenerList.add(listener);
    }

    public void removeLoadingListener(FileLoadingListener listener) {
        this.mLoadingListenerList.remove(listener);
    }

    public static boolean hasUncompleteTask() {
        ArrayList<FilmCacheBean> cacheList = DBCache.instance.getAllCacheItems();
        for (int i = 0; i < cacheList.size(); i++) {
            if (!cacheList.get(i).isFinished()) {
                return true;
            }
        }
        return false;
    }

    public static void downloadUncompleteTask(File saveFileDir, File saveMaskDir) {
        ArrayList<FilmCacheBean> cacheList = DBCache.instance.getAllCacheItems();
        for (int i = 0; i < cacheList.size(); i++) {
            FilmCacheBean filmCache = cacheList.get(i);
            if (!filmCache.isFinished()) {
                instance.resumeFilmDownload(filmCache, saveFileDir, saveMaskDir);
            }
        }
    }

    public void downloadFilm(FilmCacheBean cacheBean) {
        resumeFilmDownload(cacheBean, getDownloadDir(1), getDownloadDir(0));
        if (this.waitingQueue.contains(cacheBean)) {
            cacheBean.mLoadRate = 0;
        }
        DBCache.instance.updateDownloadPosition(cacheBean);
    }

    public static void getRealFileName(FilmCacheBean cacheBean, File saveFileDir, File saveMaskDir) {
        String name;
        File saveFile;
        if (cacheBean != null) {
            if (StringUtils.isBlank(cacheBean.mFileName) || !cacheBean.mFileName.startsWith("/")) {
                if (!"".equals(cacheBean.mSeason) || !"".equals(cacheBean.mEpisode)) {
                    name = cacheBean.mFilmName + ".s" + cacheBean.mSeason + "e" + cacheBean.mEpisode + "." + cacheBean.mFormatted;
                } else {
                    name = cacheBean.mFilmName + "." + cacheBean.mFormatted;
                }
                saveFile = new File(saveFileDir, name);
                maskSaveFile = new File(saveMaskDir, name + ".mask");
                cacheBean.mFileName = saveFile.getPath();
            } else {
                saveFile = new File(cacheBean.mFileName);
                maskSaveFile = new File(saveMaskDir, saveFile.getName() + ".mask");
            }
            if (cacheBean.mLength == 0) {
                cacheBean.mLength = cacheBean.mSize;
            }
            if (StringUtils.isEmpty(cacheBean.mP4PUrl)) {
                cacheBean.mP4PUrl = "yyets://N=" + cacheBean.mFilmName + "|S=" + cacheBean.mLength + "|H=" + cacheBean.mFileId + "|";
                if (saveFile.exists()) {
                    saveFile.delete();
                }
            }
        }
    }

    public void resumeFilmDownload(FilmCacheBean cacheBean, File saveFileDir, File saveMaskDir) {
        init();
        if (cacheBean != null && isP4pInit()) {
            getRealFileName(cacheBean, saveFileDir, saveMaskDir);
            if (this.mFilmCacheMap.size() < MAX_RUNNING_TASK) {
                this.p4pclient.startTask(cacheBean.mFileId, cacheBean.mP4PUrl, cacheBean.mFileName, maskSaveFile.getAbsolutePath());
                this.p4pclient.queryStat();
                this.mFilmCacheMap.put(cacheBean.mDownloadUrl, cacheBean);
            } else if (!this.waitingQueue.contains(cacheBean)) {
                this.waitingQueue.offer(cacheBean);
            }
            unbeginCache.remove(cacheBean);
        } else {
            beganDl = true;
            unbeginCache.add(cacheBean);
        }
    }

    public boolean stopLoading(String fid) {
        this.mFilmCacheMap.remove(fid);
        if (mFilmCacheMap.isEmpty()) {
            destoryLater();
        }
        return this.p4pclient.stopTask(fid);
    }

    private void destoryLater() {
        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            if (mFilmCacheMap.isEmpty()) {
                destroy();
            }
        }).start();
    }

    public void clearWaitingQueue() {
        this.waitingQueue.clear();
    }

    public LinkedBlockingQueue<FilmCacheBean> getWaitingQueue() {
        return instance.waitingQueue;
    }

    public void downloadComplete(FilmCacheBean cacheBean, File saveFileDir, File saveMaskDir) {
        FilmCacheBean nextCacheBean;
        if (cacheBean != null) {
            this.p4pclient.stopTask(cacheBean.mFileId);
            DBCache.instance.putInt(ZimuzuHelper.getFilmCacheKey(cacheBean.mFilmName, cacheBean.mSeason, cacheBean.mEpisode), 2);
            this.mFilmCacheMap.remove(cacheBean.mDownloadUrl);
            if (this.isP4pInit && this.mFilmCacheMap.size() < MAX_RUNNING_TASK && (nextCacheBean = this.waitingQueue.poll()) != null) {
                getRealFileName(nextCacheBean, saveFileDir, saveMaskDir);
                this.p4pclient.startTask(nextCacheBean.mFileId, nextCacheBean.mP4PUrl, nextCacheBean.mFileName, maskSaveFile.getAbsolutePath());
                this.p4pclient.queryStat();
                this.mFilmCacheMap.put(nextCacheBean.mDownloadUrl, nextCacheBean);
            }
        }
    }

    public void cancelAllLoading() {
        beganDl = false;
        this.waitingQueue.clear();
        for (FilmCacheBean cacheBean : this.mFilmCacheMap.values()) {
            this.p4pclient.stopTask(cacheBean.mFileId);
            this.p4pclient.queryStat();
        }
        this.mFilmCacheMap.clear();
    }

    public void cancelLoading(FilmCacheBean cacheBean, File saveFileDir, File saveMaskDir) {
        FilmCacheBean nextCacheBean;
        if (cacheBean != null) {
            if (this.waitingQueue.contains(cacheBean)) {
                this.waitingQueue.remove(cacheBean);
                return;
            }
            this.p4pclient.stopTask(cacheBean.mFileId);
            this.p4pclient.queryStat();
            this.mFilmCacheMap.remove(cacheBean.mDownloadUrl);
            if (this.isP4pInit && this.mFilmCacheMap.size() < MAX_RUNNING_TASK && (nextCacheBean = this.waitingQueue.poll()) != null) {
                getRealFileName(nextCacheBean, saveFileDir, saveMaskDir);
                this.p4pclient.startTask(nextCacheBean.mFileId, nextCacheBean.mP4PUrl, nextCacheBean.mFileName, maskSaveFile.getAbsolutePath());
                this.p4pclient.queryStat();
                this.mFilmCacheMap.put(nextCacheBean.mDownloadUrl, nextCacheBean);
            }
        }
    }

    public void onLoadingStarted(String uri) {
        Iterator<FileLoadingListener> it = this.mLoadingListenerList.iterator();
        while (it.hasNext()) {
            it.next().onLoadingStarted(uri);
        }
    }

    public void onLoadingFailed(String uri, String failReason) {
        Iterator<FileLoadingListener> it = this.mLoadingListenerList.iterator();
        while (it.hasNext()) {
            it.next().onLoadingFailed(uri, failReason);
        }
        this.mFilmCacheMap.remove(uri);
        if (mFilmCacheMap.isEmpty()) {
            destoryLater();
        }
    }

    public void onLoadingComplete(String uri, String localPath) {
        FilmCacheBean cacheBean = this.mFilmCacheMap.get(uri);
        if (cacheBean != null) {
            cacheBean.mLoadPosition = cacheBean.mLength;
            DBCache.instance.updateDownloadPosition(cacheBean);
            Iterator<FileLoadingListener> it = this.mLoadingListenerList.iterator();
            while (it.hasNext()) {
                it.next().onLoadingComplete(uri, localPath);
            }
            this.mFilmCacheMap.remove(uri);
            if (mFilmCacheMap.isEmpty()) {
                destoryLater();
            }
        }
    }

    public void onLoadingProgressUpdate(String uri, float prog) {
        Iterator<FileLoadingListener> it = this.mLoadingListenerList.iterator();
        while (it.hasNext()) {
            it.next().onLoadingProgressUpdate(uri, prog);
        }
    }

    public void onLoadingPosition(String uri, long totalLen, long readTotalLen, long readRate) {
        FilmCacheBean cacheBean = this.mFilmCacheMap.get(uri);
        if (cacheBean != null) {
            cacheBean.mLength = totalLen;
            DBCache.instance.updateDownloadPosition(cacheBean);
            Iterator<FileLoadingListener> it = this.mLoadingListenerList.iterator();
            while (it.hasNext()) {
                it.next().onLoadingPosition(uri, totalLen, readTotalLen, readRate);
            }
        }
    }

    private File getDownloadDir(int type) {
        String paths = Environment.getExternalStorageDirectory().getPath();

        return new File(
                paths,
                "Android/data/" + InitCp.androidContext.getPackageName() + "/" + (type == 0 ? "." : "") + "download"
        );
    }

    public void onLoadingCancel(String urlPath, String localPath) {
        this.mFilmCacheMap.remove(urlPath);
        Iterator<FileLoadingListener> it = this.mLoadingListenerList.iterator();
        while (it.hasNext()) {
            it.next().onLoadingCancel(urlPath, localPath);
        }
    }

    private void resumeUnbegan() {
        new Handler().postDelayed(() -> {
            Log.d("RRFilmDownloadManager", "resumeUnbegan  " + unbeginCache.size());
            for (FilmCacheBean filmCacheBean : unbeginCache) {
                downloadFilm(filmCacheBean);
            }
        }, 2000);
    }

    public void onP4PClientInited() {

        Log.d("p4pclient", "p4p client inited");
        this.p4pclient.start("tracker2.zmzfile.com:25289,tracker3.zmzfile.com:25289,tracker4.zmzfile.com:25289,tracker5.zmzfile.com:25289",
                "http://htracker2.zmzfile.com:6105/rt/p4proute,http://htracker3.zmzfile.com:6105/rt/p4proute,http://htracker4.zmzfile.com:6105/rt/p4proute,http://htracker5.zmzfile.com:6105/rt/p4proute",
                "stun.zmzfile.com",
                "1231234",
                "12213423");
        this.p4pclient.enableLogConsole();
        if (beganDl) {
            resumeUnbegan();
        }
        if (this.p4pListener != null) {
            this.p4pListener.onP4PClientInited();
        }
    }

    public void onP4PClientRestarted() {
        Log.d("p4pclient", "p4p client restarted");
        this.p4pclient.start("tracker2.zmzfile.com:25289,tracker3.zmzfile.com:25289,tracker4.zmzfile.com:25289,tracker5.zmzfile.com:25289",
                "http://htracker2.zmzfile.com:6105/rt/p4proute,http://htracker3.zmzfile.com:6105/rt/p4proute,http://htracker4.zmzfile.com:6105/rt/p4proute,http://htracker5.zmzfile.com:6105/rt/p4proute",
                "stun.zmzfile.com",
                "1231234",
                "12213423");
        this.p4pclient.enableLogConsole();
        if (this.p4pListener != null) {
            this.p4pListener.onP4PClientRestarted();
        }
    }

    public void onP4PClientStarted() {
        this.isP4pInit = true;
    }

    public void onTaskStat(P4PStat stat) {
        if (this.p4pListener != null) {
            try {
                this.p4pListener.onTaskStat(stat);
            } catch (Exception e) {
                ThrowableExtension.printStackTrace(e);
            }
        }
    }
}
