package com.yyets.zimuzu.fileloader;

public interface FileLoadingListener {
    void onLoadingCancel(String str, String str2);

    void onLoadingComplete(String str, String str2);

    void onLoadingFailed(String str, String str2);

    void onLoadingPosition(String str, long j, long j2, long j3);

    void onLoadingProgressUpdate(String str, float f);

    void onLoadingStarted(String str);
}
