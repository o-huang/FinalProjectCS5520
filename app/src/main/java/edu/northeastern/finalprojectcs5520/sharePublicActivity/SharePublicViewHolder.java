package edu.northeastern.finalprojectcs5520.sharePublicActivity;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.finalprojectcs5520.R;

public class SharePublicViewHolder extends RecyclerView.ViewHolder {

    public TextView username;
    public TextView date;
    public TextView bodyWeight;
    public TextView bodyFat;
    public TextView bodyBmi;

    public SharePublicViewHolder(@NonNull View itemView) {
        super(itemView);
        this.username = itemView.findViewById(R.id.public_username);
        this.date = itemView.findViewById(R.id.public_date);
        this.bodyWeight = itemView.findViewById(R.id.public_bodyweight);
        this.bodyFat = itemView.findViewById(R.id.public_bodyfat);
        this.bodyBmi = itemView.findViewById(R.id.public_bmi);
    }
}