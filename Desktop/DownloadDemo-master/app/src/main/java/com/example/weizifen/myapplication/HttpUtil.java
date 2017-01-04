package com.example.weizifen.myapplication;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by weizifen on 17/1/3.
 */
/*获取json数据*/
public class HttpUtil {
   public static void sendOkHttpRequest(String string, Callback callback)
   {
       OkHttpClient client=new OkHttpClient();
       Request request=new Request.Builder().url(string).build();
       client.newCall(request).enqueue(callback);
   }
}
