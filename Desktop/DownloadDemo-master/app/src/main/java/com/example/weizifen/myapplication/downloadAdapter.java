package com.example.weizifen.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by weizifen on 17/1/3.
 */

public class downloadAdapter extends RecyclerView.Adapter <downloadAdapter.ViewHolder>{
    private Context mcontext;
    private List<Down>mDown;
    String bodyString;
    String url;
    /*http在子线程,想要更新数据 得再传到主线程*/

    public downloadAdapter(List<Down>Downs) {
        this.mDown=Downs;
    }

    private static final String TAG = "downloadAdapter";
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mcontext==null)
        {
            mcontext=parent.getContext();
        }
        View view= LayoutInflater.from(mcontext).inflate(R.layout.download_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(mcontext,"获取下载数据,筹备中",Toast.LENGTH_SHORT).show();
                int position=holder.getAdapterPosition();
                Down down=mDown.get(position);
                final Intent intent=new Intent(mcontext,DownloadPage.class);
                intent.putExtra(DownloadPage.DOWNLOAD_NAME,down.getName());
                intent.putExtra(DownloadPage.DOWNLOAD_IMAGEID,down.getImageId());
                /*网页json也从这里传*/
                bodyString(position,intent);
//               mHandler=new android.os.Handler(){
//                   @Override
//                   public void handleMessage(Message msg) {
//                       super.handleMessage(msg);
//                       if (msg.what==1){
//                           String url=(String)msg.obj;
//                           Log.d(TAG, url);
//                           intent.putExtra("DOWNLOAD_Url",url);
//
//
//
//                       }
//                   }
//               };
                try {
                    Thread.sleep(500);
                    intent.putExtra(DownloadPage.DOWNLOAD_URL,url);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mcontext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Down down=mDown.get(position);
        Glide.with(mcontext).load(down.getImageId()).into(holder.downloadImage);
        holder.downloadText.setText(down.getName());


    }

    @Override
    public int getItemCount() {
        return mDown.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView downloadImage;
        TextView downloadText;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView=(CardView)itemView;
            downloadImage=(ImageView)itemView.findViewById(R.id.download_image);
            downloadText=(TextView)itemView.findViewById(R.id.download_text);


        }
    }



    private void bodyString(final int position, final Intent intent){
         /*json数据*/
        HttpUtil.sendOkHttpRequest("http://sandbox.resource.skydragon-inc.cn/GSPS/Demo/GSPS_DEMO.json", new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call,  Response response) throws IOException {

                      bodyString= response.body().string();

                JSONArray jsonArray= null;
                try {
                    jsonArray = new JSONArray(bodyString);
                    JSONObject jsonObject=jsonArray.getJSONObject(position);
                    url=jsonObject.getString("gameUrl");
                    Log.d(TAG, url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


        }
        });
    }

}
