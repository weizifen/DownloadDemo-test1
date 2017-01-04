package com.example.weizifen.myapplication;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
private FloatingActionButton floatingActionButton;
    private List<Down>downList=new ArrayList<>();
    private downloadAdapter adapter;
    private Down[]downs={new Down("游戏1",R.drawable.apple),new Down("游戏2",R.drawable.banana),new Down("游戏3",R.drawable.cherry),new Down("游戏4",R.drawable.grape)};


    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        floatingActionButton=(FloatingActionButton)findViewById(R.id.Float);



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this,"功能筹备中",Toast.LENGTH_SHORT).show();
            }
        });
        initDown();
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter=new downloadAdapter(downList);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.backup:{
                Toast.makeText(this,"backup",Toast.LENGTH_SHORT).show();
            }
            case R.id.delete:{
                Toast.makeText(this,"delect",Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    /*加载*/
    private void initDown(){
    downList.clear();
        for (int i=0;i<4;i++){
//            Random random=new Random();
//            int index=random.nextInt(downs.length);
//            downList.add(downs[index]);

            downList.add(downs[i]);
        }
    }




}
