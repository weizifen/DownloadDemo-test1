package com.example.weizifen.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * Created by weizifen on 17/1/3.
 */

public class DownloadService extends Service {
    private DownloadTask downloadTask;
    private String downloadUrl;
    public DownloadService() {
    }

    DownloadListener downloadListener = new DownloadListener() {
        @Override
        public void onProgress(int progress) {
            getNotificationManger().notify(1,getNotification("downloading....",progress));

        }

        @Override
        public void onSuccess() {
            downloadTask=null;
            stopForeground(true);
            Toast.makeText(DownloadService.this,"下载成功",Toast.LENGTH_SHORT).show();
            getNotificationManger().notify(1,getNotification("下载成功",-2));




        }


        @Override
        public void onFailed() {
            downloadTask=null;
            stopForeground(true);
            getNotificationManger().notify(1,getNotification("下载失败",-1));
            Toast.makeText(DownloadService.this,"下载失败",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onPaused() {
            downloadTask=null;
            Toast.makeText(DownloadService.this,"下载暂停",Toast.LENGTH_SHORT).show();


        }

        @Override
        public void onCanneled() {
            downloadTask=null;
            stopForeground(true);
            Toast.makeText(DownloadService.this,"取消下载",Toast.LENGTH_SHORT).show();
        }
    };


    private DownloadBinder downloadBinder=new DownloadBinder();
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return downloadBinder;
    }

    class DownloadBinder extends Binder {
        public void startDownload(String url){
            if (downloadTask==null){
                downloadUrl=url;
                downloadTask=new DownloadTask(downloadListener);
                downloadTask.execute(downloadUrl);
                startForeground(1,getNotification("downloading",0));
                Toast.makeText(DownloadService.this,"下载....",Toast.LENGTH_SHORT).show();


            }
        }
        public void paused(){
            if (downloadTask!=null){
                downloadTask.pauseDownload();
            }
        }
        public void cancleDownload(){
            if (downloadTask!=null){
                downloadTask.cancelDownload();
            }
            else if (downloadUrl!=null){
                String fileName=downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                String directory= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                File file=new File(directory+fileName);
                if (file.exists()){
                    file.delete();
                }
                getNotificationManger().cancel(1);
                stopForeground(true);
                Toast.makeText(DownloadService.this,"cancel....",Toast.LENGTH_SHORT).show();


            }
        }
    }

    private NotificationManager getNotificationManger() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    }
    private Notification getNotification(String title, int progress){
        Intent intent=new Intent(this,MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,0);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        builder.setContentIntent(pendingIntent);
        builder.setContentTitle(title);
        if (progress>=0){
            builder.setContentText(progress+"%");
            builder.setProgress(100,progress,false);
            if (progress>100)
            {
                progress=progress-100;
                builder.setProgress(100,progress,false);

            }

        }
        if (progress==-2){
            openFile();

        }
        return builder.build();
    }


    Uri uri;

    private static final String TAG = "DownloadService";
    private void openFile() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        /**
         * Android7.0+禁止应用对外暴露file://uri，改为content://uri；具体参考FileProvider
         */
        uri = FileProvider.getUriForFile(this, "com.example.weizifen.myapplication.fileprovider", new File(DownloadTask.superfileName));
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


        intent.setDataAndType(uri, "application/vnd.android.package-archive");
//        Log.d(TAG,DownloadTask.superfileName);
            startActivity(intent);




    }}
