package edu.northeastern.finalprojectcs5520;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context context;
    private List<HistoryActivity.Record> records;
    private static final int REQUEST_IMAGE_CAPTURE = 1;


    public HistoryAdapter(Context context, List<HistoryActivity.Record> records) {
        this.context = context;
        this.records = records;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.record_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryActivity.Record record = records.get(position);


        String[] dateList = record.getDate().split("_");

        String dateCorrectFormat = dateList[0] + "/" + dateList[1] + "/" + dateList[2];

        holder.dateTextView.setText("Date: " + dateCorrectFormat);
        holder.weightTextView.setText("Weight: " + record.getRecordWeight());
        holder.bodyFatTextView.setText("Body Fat %: " + record.getBodyFatPercent());

        if (record.getImageUrl() != null && !record.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(record.getImageUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(holder.ivProfile);
        } else {
            holder.ivProfile.setImageResource(R.drawable.error_image);
        }

        holder.btnPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(context, CameraActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            bundle.putString("date", record.getDate()); // Pass the date as an extra
            intent.putExtras(bundle);
            ((Activity) context).startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        });

    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        TextView weightTextView;
        TextView bodyFatTextView;
        ImageButton btnPhoto;
        ImageView ivProfile;


        ViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            weightTextView = itemView.findViewById(R.id.weightTextView);
            bodyFatTextView = itemView.findViewById(R.id.bodyFatTextView);
            btnPhoto = itemView.findViewById(R.id.btnPhoto);
            ivProfile = itemView.findViewById(R.id.ivProfile);
        }
    }
}
