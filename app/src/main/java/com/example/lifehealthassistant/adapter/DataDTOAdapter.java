package com.example.lifehealthassistant.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lifehealthassistant.R;
import com.example.lifehealthassistant.activity.HealthDetailActivity;
import com.example.lifehealthassistant.activity.NewsDetailActivity;
import com.example.lifehealthassistant.bean.Health;
import com.example.lifehealthassistant.bean.News;

import java.util.List;

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
             @Override
             public void onClick(View v) {
                 int position=holder.getAdapterPosition();
                 News.ResultDTO.DataDTO dataDTO=datedtoList.get(position);
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
        Log.d("lzn", "onBindViewHolder: "+dataDTO.getTitle());
        holder.title_datadto_item.setText(dataDTO.getTitle());
    }

    @Override
    public int getItemCount() {
        return datedtoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View datadtoView;
        TextView title_datadto_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            datadtoView=itemView;
            title_datadto_item=(TextView) itemView.findViewById(R.id.title_datadto_item);
        }
    }

}
