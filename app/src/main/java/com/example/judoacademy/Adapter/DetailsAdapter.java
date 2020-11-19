package com.example.judoacademy.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.judoacademy.Models.Details;
import com.example.judoacademy.R;

import java.util.ArrayList;


public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.PlantViewHolder> {

    private static final String TAG = "DetailsAdapter";
    ArrayList<Details> plantDetails;
    Context mContext;

    public DetailsAdapter(ArrayList<Details> plantDetails, Context mContext) {
        this.plantDetails = plantDetails;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_detail, parent, false);
        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        final Details object = plantDetails.get(position);
        holder.nameTV.setText(object.getName());
        holder.attendanceTV.setText(object.getStatus());
        if(object.getStatus().equals("Present")){
            holder.attendanceTV.setTextColor(Color.parseColor("#00C146"));
        }else{
            holder.attendanceTV.setTextColor(Color.RED);
        }

    }

    @Override
    public int getItemCount() {
        return plantDetails.size();
    }

    public class PlantViewHolder extends RecyclerView.ViewHolder {

        TextView nameTV,attendanceTV;

        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTV = itemView.findViewById(R.id.nameTV);
            attendanceTV = itemView.findViewById(R.id.attendanceTV);


        }
    }
}