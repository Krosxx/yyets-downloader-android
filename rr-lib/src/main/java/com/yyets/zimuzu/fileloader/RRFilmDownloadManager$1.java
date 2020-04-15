package com.yyets.zimuzu.fileloader;

import android.content.Context;

import java.util.TimerTask;

class RRFilmDownloadManager$1 extends TimerTask {
    private long start = 0;
    final /* synthetic */ RRFilmDownloadManager this$0;
    final /* synthetic */ Context val$ctx;

    RRFilmDownloadManager$1(RRFilmDownloadManager this$02, Context context) {
        this.this$0 = this$02;
        this.val$ctx = context;
    }

    public void run() {
        if (!this.this$0.isP4pInit || this.this$0.p4pclient == null) {
            long now = System.currentTimeMillis();
            if (!(this.this$0.isP4pInit) && now - this.start >= 20000) {
                this.this$0.init();
                this.start = now;
                return;
            }
            return;
        }
        (this.this$0.p4pclient).queryStat();
    }
}
