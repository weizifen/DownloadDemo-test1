package com.example.weizifen.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by weizifen on 17/1/3.
 */

public class DownloadTask extends AsyncTask<String,Integer,Integer> {
    public static final int TYPE_SUCCESS=0;
    public static final int TYPE_FAILED=1;
    public static final int TYPE_PAUSED=2;
    public static final int TYPE_CANCELED=3;
    public static String superfileName;

    private DownloadListener downloadListener;
    private boolean isPaused=false;
    private boolean isCanceled=false;
    private int lastProgress;

    public DownloadTask(DownloadListener downloadListener) {
        super();
        this.downloadListener=downloadListener;
    }

    /*执行耗时操作*/
    @Override
    protected Integer doInBackground(String... strings) {
        InputStream is=null;
        RandomAccessFile saveFile=null;
        File file=null;
        long downloadedLength=0;//已下载长度;
        String downloadedUrl=strings[0];
        String fileName=downloadedUrl.substring(downloadedUrl.lastIndexOf("/"));
        String directory= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        file=new File(directory+fileName);
        superfileName=directory+fileName;
        if (file.exists()){
            downloadedLength=file.length();
        }
        try {
            long contentlength=getContentLength(downloadedUrl);
            if (contentlength==0){
                return TYPE_FAILED;
            }else if (contentlength==downloadedLength){
                return TYPE_SUCCESS;
            }
            OkHttpClient client=new OkHttpClient();
            Request request=new Request.Builder().addHeader("RANGE","bytes="+downloadedLength+"-").url(downloadedUrl).build();
            Response response=client.newCall(request).execute();
            if (response!=null){
                is=response.body().byteStream();
                saveFile=new RandomAccessFile(file,"rw");
                saveFile.seek(downloadedLength);
                byte[] b=new byte[1024];
                int len;
                int total = 0;
                while ((len=is.read(b))!=-1){
                    if (isCanceled){
                        return TYPE_CANCELED;
                    }else if (isPaused){
                        return TYPE_PAUSED;
                    }else {
                        total+=len;
                        saveFile.write(b,0,len);
                        int progress=(int)((total+downloadedLength)*100/contentlength);
                        publishProgress(progress);
                    }
                }
                response.body().close();
                return TYPE_SUCCESS;
            }



        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (saveFile!=null){
                try {
                    saveFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (isCanceled&&file!=null){
                file.delete();
            }

        }


        return TYPE_SUCCESS;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        int progress=values[0];
        if (progress>lastProgress){
            downloadListener.onProgress(progress);
            lastProgress=progress;
        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        switch (integer){
            case TYPE_SUCCESS:
                downloadListener.onSuccess();
                break;
            case TYPE_FAILED:
                downloadListener.onFailed();
                break;
            case TYPE_PAUSED:
                downloadListener.onPaused();
                break;
            case TYPE_CANCELED:
                downloadListener.onCanneled();
                break;
            default:
                break;

        }
    }
    public void pauseDownload(){
        isPaused=true;
    }
    public void cancelDownload(){
        isCanceled=true;
    }



    private long getContentLength(String downUrl) throws IOException {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(downUrl).build();
        Response response=client.newCall(request).execute();
        if (response!=null&&response.isSuccessful()){
            long contentLength=response.body().contentLength();

            response.close();

            return contentLength;
        }
        return 0;



    }

    }




