package com.example.cityclean;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<Report> itemList;
    private Context context;

    public MyAdapter(Context context, List<Report> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Report item = itemList.get(position);
        holder.date.setText(item.getDate());

        // set image
        int imageResId = context.getResources().getIdentifier(item.getImagePath(), "drawable", context.getPackageName());
        holder.image.setImageResource(imageResId);

        // go to detial page
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReportDetial.class);
            intent.putExtra("id", item.getId());
            context.startActivity(intent);
        });

        //edit
        holder.editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReportDetial.class);
            intent.putExtra("id", item.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        ImageView image;
        Button editButton;

        ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.item_date);
            image = itemView.findViewById(R.id.item_image);
            editButton = itemView.findViewById(R.id.item_edit);
        }
    }
}

