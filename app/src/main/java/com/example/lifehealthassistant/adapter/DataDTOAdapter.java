package com.example.lifehealthassistant.adapter;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lifehealthassistant.R;
import com.example.lifehealthassistant.activity.HealthDetailActivity;
import com.example.lifehealthassistant.activity.NewsDetailActivity;
import com.example.lifehealthassistant.activity.ShowDietAllActivity;
import com.example.lifehealthassistant.bean.Health;
import com.example.lifehealthassistant.bean.News;
import com.example.lifehealthassistant.config.ServerConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class DataDTOAdapter extends RecyclerView.Adapter<DataDTOAdapter.ViewHolder>{


    private List<News.ResultDTO.DataDTO> datedtoList;

    public DataDTOAdapter(){}
    public DataDTOAdapter(List<News.ResultDTO.DataDTO> dataDTOS){
        datedtoList=dataDTOS;
    }

    @NonNull
    @Override
    public DataDTOAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.datadto_item,parent,false);
        final DataDTOAdapter.ViewHolder holder = new DataDTOAdapter.ViewHolder(view);

        holder.datadtoView.setOnClickListener(new View.OnClickListener() {
             @SuppressLint("ResourceAsColor")
             @Override
             public void onClick(View v) {
                 int position=holder.getAdapterPosition();
                 News.ResultDTO.DataDTO dataDTO=datedtoList.get(position);
                 holder.title_datadto_item.setTextColor(R.color.themeRed);
                 NewsDetailActivity.actionStart(v.getContext(),dataDTO.getUrl());
                 //HealthDetailActivity.actionStart(v.getContext(),userid,health,"show");
             }
         }

        );
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        News.ResultDTO.DataDTO dataDTO=datedtoList.get(position);
        holder.title_datadto_item.setText(dataDTO.getTitle());
        holder.author_datadto_item.setText(dataDTO.getAuthor_name());
        holder.date_datadto_item.setText(dataDTO.getDate());
        holder.pic_datadto_item.getSettings ( ).setJavaScriptEnabled(true) ;
        if(dataDTO.getThumbnail_pic_s()==null||dataDTO.getThumbnail_pic_s().equals("")){
            holder.pic_datadto_item.loadUrl(ServerConfiguration.IP+"pic_diet/blank.png") ;
            Log.d("lzn", "onBindViewHolder: "+ServerConfiguration.IP+"pic_diet/blank.png");
        }
        else{
            holder.pic_datadto_item.loadUrl(dataDTO.getThumbnail_pic_s()) ;
            Log.d("lzn", "onBindViewHolder: "+dataDTO.getThumbnail_pic_s());
        }
        holder.pic_datadto_item.setWebViewClient ( new WebViewClient() {
            /**
             * 是否在 WebView 内加载页面
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            /**
             * WebView 访问 url 出错
             */
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
            /**
             * 错误响应的处理
             */
            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request,
                                            WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
            }
            /**
             * 重新调整图片资源大小
             * @param view
             * @param url
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                imgReset(holder.pic_datadto_item);
            }
        });


    }

    private void imgReset(WebView webView) {
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('img'); " +
                "for(var i=0;i <objs.length;i++){"
                + "var img = objs[i]; " +
                " img.style.maxWidth = '100%'; img.style.height = 'auto'; " +
                "}" +
                "})()");
    }

    @Override
    public int getItemCount() {
        return datedtoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View datadtoView;
        TextView title_datadto_item;
        TextView author_datadto_item;
        TextView date_datadto_item;
        WebView pic_datadto_item;
        //ImageView pic_datadto_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            datadtoView=itemView;
            title_datadto_item=(TextView) itemView.findViewById(R.id.title_datadto_item);
            author_datadto_item=(TextView) itemView.findViewById(R.id.author_datadto_item);
            date_datadto_item=(TextView) itemView.findViewById(R.id.date_datadto_item);
            pic_datadto_item=(WebView) itemView.findViewById(R.id.pic_datadto_item);
        }
    }

}
