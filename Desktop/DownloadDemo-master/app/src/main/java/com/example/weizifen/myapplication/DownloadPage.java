package com.example.weizifen.myapplication;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.LogTime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class DownloadPage extends AppCompatActivity implements View.OnClickListener {
    public static final String DOWNLOAD_NAME="download_name";
    public  static final String DOWNLOAD_IMAGEID="download_image_id";
    public  static final String DOWNLOAD_URL="url";


    private DownloadService.DownloadBinder downloadBinder;
    String responseData;
    String url;

    private ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            downloadBinder = (DownloadService.DownloadBinder) iBinder;

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_page);
        Intent intent=getIntent();
        String downloadImageName=intent.getStringExtra(DOWNLOAD_NAME);
        int downloadImageId=intent.getIntExtra(DOWNLOAD_IMAGEID,0);
       url=intent.getStringExtra(DOWNLOAD_URL);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        ImageView downloadImageView=(ImageView)findViewById(R.id.download_image_view);
        /*开始*/
       Button start=(Button)findViewById(R.id.start);
        /*暂停*/
        Button pause=(Button)findViewById(R.id.pause);
        /*取消*/
        Button cancel=(Button)findViewById(R.id.cancel);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle(downloadImageName);
        Glide.with(this).load(downloadImageId).into(downloadImageView);
        start.setOnClickListener(this);
        pause.setOnClickListener(this);
        cancel.setOnClickListener(this);

        /*下载*/
        Intent intent1=new Intent(this,DownloadService.class);
        startService(intent1);//启动服务
        bindService(intent1,serviceConnection,BIND_AUTO_CREATE);
        if (ContextCompat.checkSelfPermission(DownloadPage.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(DownloadPage.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);


    }


    private static final String TAG = "DownloadPage";
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start:{


//应该通过意图获取到json数据
                Log.d(TAG, url);
//                String url="http://download.skydragon-inc.cn/GSPS/Demo/fknsg2/deploy_1208_1520/game_signed3.apk";
                downloadBinder.startDownload(url);
                break;
            }
            case R.id.pause:{
                downloadBinder.paused();
//                Toast.makeText(this,"没有授权",Toast.LENGTH_SHORT).show();

                break;
            }
            case R.id.cancel:{
                downloadBinder.cancleDownload();
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case 1:
                if (grantResults.length>0&&grantResults[0]!=PackageManager.PERMISSION_GRANTED);
            {
                Toast.makeText(this,"没有授权",Toast.LENGTH_SHORT).show();
                finish();}
            break;
            default:
                break;


        }
        }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }




}




