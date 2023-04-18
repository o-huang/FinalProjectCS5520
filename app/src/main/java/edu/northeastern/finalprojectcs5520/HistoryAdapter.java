package edu.northeastern.finalprojectcs5520;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context context;
    private List<HistoryActivity.Record> records;
    private DatabaseReference reference;
    private String username;
    private static final int REQUEST_IMAGE_CAPTURE = 1;


    public HistoryAdapter(Context context, List<HistoryActivity.Record> records,DatabaseReference reference, String username) {
        this.context = context;
        this.records = records;
        this.reference = reference;
        this.username = username;
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
        holder.bmiTextView.setText("Bmi: " + record.getBmi());

        holder.sharePublicCheckBox.setChecked(record.isPublic());
        holder.sharePublicCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Update the 'public' field in the database when the checkbox state changes
            String date = record.getDate();
            reference.child(username).child("recordWeights").child(date).child("public").setValue(isChecked);
        });


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
        TextView bmiTextView;
        ImageButton btnPhoto;
        ImageView ivProfile;
        CheckBox sharePublicCheckBox;


        ViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            weightTextView = itemView.findViewById(R.id.weightTextView);
            bodyFatTextView = itemView.findViewById(R.id.bodyFatTextView);
            bmiTextView = itemView.findViewById(R.id.bmiTextView);
            btnPhoto = itemView.findViewById(R.id.btnPhoto);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            sharePublicCheckBox = itemView.findViewById(R.id.sharePublicCheckBox);
            sharePublicCheckBox.setClickable(true);
        }
    }
}
