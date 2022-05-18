package com.example.lifehealthassistant.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lifehealthassistant.R;
import com.example.lifehealthassistant.activity.HealthDetailActivity;
import com.example.lifehealthassistant.activity.ShowDiseaseDiActivity;
import com.example.lifehealthassistant.bean.Disease;
import com.example.lifehealthassistant.bean.Health;

import java.util.List;

public class HealthAdapter extends RecyclerView.Adapter<HealthAdapter.ViewHolder>{

    private List<Health> healthList;
    private int userid;

    public HealthAdapter(List<Health> healthList,int userid){

        this.healthList=healthList;
        this.userid=userid;
    }

    public HealthAdapter() {

    }

    @NonNull
    @Override
    public HealthAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.health_item,parent,false);
        final HealthAdapter.ViewHolder holder = new HealthAdapter.ViewHolder(view);

        holder.healthView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  int position=holder.getAdapterPosition();
                  Health health=healthList.get(position);
                  HealthDetailActivity.actionStart(v.getContext(),userid,health,"show");
              }
            }

        );
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HealthAdapter.ViewHolder holder, int position) {
        Health health=healthList.get(position);
        holder.datetime_health_item.setText(health.getDatetimehealth());
    }

    @Override
    public int getItemCount() {
        return healthList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View healthView;
        TextView datetime_health_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            healthView=itemView;
            datetime_health_item=(TextView) itemView.findViewById(R.id.datetime_health_item);
        }
    }
}
