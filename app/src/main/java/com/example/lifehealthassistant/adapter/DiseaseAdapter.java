package com.example.lifehealthassistant.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lifehealthassistant.R;
import com.example.lifehealthassistant.activity.ShowDiseaseDiActivity;
import com.example.lifehealthassistant.bean.Disease;

import java.util.List;

public class DiseaseAdapter extends RecyclerView.Adapter<DiseaseAdapter.ViewHolder>{

    private List<Disease> diseaseList;
    private String userid;

    public DiseaseAdapter(List<Disease> diseaseList,String userid){
        this.diseaseList=diseaseList;
        this.userid=userid;
    }

    @NonNull
    @Override
    public DiseaseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.disease_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.diseaseview.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  int position=holder.getAdapterPosition();
                  Disease disease=diseaseList.get(position);
                  ShowDiseaseDiActivity.actionStart(v.getContext(),userid,disease);
              }
          }

        );
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DiseaseAdapter.ViewHolder holder, int position) {
        Disease disease=diseaseList.get(position);
        holder.diseasename_item.setText(disease.getDiseasename());
        holder.symptom_item.setText("症状："+disease.getSymptom());
        holder.medicine_item.setText("药品："+disease.getMedicine());
    }

    @Override
    public int getItemCount() {
        return diseaseList.size();
    }

    static public class ViewHolder extends RecyclerView.ViewHolder {
        View diseaseview;
        TextView diseasename_item;
        TextView symptom_item;
        TextView medicine_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            diseaseview=itemView;
            diseasename_item=(TextView) itemView.findViewById(R.id.diseasename_item);
            symptom_item=(TextView) itemView.findViewById(R.id.symptom_item);
            medicine_item=(TextView) itemView.findViewById(R.id.medicine_item);
        }
    }
}
