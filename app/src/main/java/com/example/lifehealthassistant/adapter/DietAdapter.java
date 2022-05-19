package com.example.lifehealthassistant.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lifehealthassistant.R;
import com.example.lifehealthassistant.activity.ShowDietActivity;
import com.example.lifehealthassistant.activity.ShowDietDiActivity;
import com.example.lifehealthassistant.activity.ShowDiseaseDiActivity;
import com.example.lifehealthassistant.bean.Diet;
import com.example.lifehealthassistant.bean.Disease;

import java.util.List;

public class DietAdapter extends RecyclerView.Adapter<DietAdapter.ViewHolder>{

    private List<Diet> dietList;
    private String userid;

    public DietAdapter(List<Diet> dietList,String userid){
        this.dietList=dietList;
        this.userid=userid;
    }

    @NonNull
    @Override
    public DietAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diet_item,parent,false);
        final DietAdapter.ViewHolder holder = new DietAdapter.ViewHolder(view);
        holder.dietview.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  int position=holder.getAdapterPosition();
                  Diet diet=dietList.get(position);
                  ShowDietDiActivity.actionStart(v.getContext(),userid,diet);
              }
          }

        );
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DietAdapter.ViewHolder holder, int position) {
        Diet diet=dietList.get(position);
        holder.datetime_diet_item.setText(diet.getDatetime());
        holder.dietname_item.setText(diet.getDietname());
    }

    @Override
    public int getItemCount() {
        return dietList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View dietview;
        TextView dietname_item;
        TextView datetime_diet_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dietview=itemView;
            datetime_diet_item=(TextView) itemView.findViewById(R.id.datetime_diet_item);
            dietname_item=(TextView) itemView.findViewById(R.id.dietname_item);
        }
    }
}
