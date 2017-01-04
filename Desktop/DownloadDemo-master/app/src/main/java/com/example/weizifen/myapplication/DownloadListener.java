package com.example.weizifen.myapplication;

/**
 * Created by weizifen on 17/1/3.
 */

public interface DownloadListener {
    void onProgress(int progress);
    void onSuccess();
    void onFailed();
    void onPaused();
    void onCanneled();
}
