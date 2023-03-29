package edu.northeastern.finalprojectcs5520.sharePublicActivity;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.northeastern.finalprojectcs5520.R;

public class SharePublicRecyclerAdapter extends RecyclerView.Adapter<SharePublicViewHolder> {

    private ArrayList<SharePublicInfo> sharePublicList;

    public SharePublicRecyclerAdapter(ArrayList<SharePublicInfo> sharePublicList) {
        this.sharePublicList = sharePublicList;
    }


    @NonNull
    @Override
    public SharePublicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new SharePublicViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.share_public_list_items, null));
    }

    @Override
    public void onBindViewHolder(@NonNull SharePublicViewHolder holder, int position) {
        holder.username.setText(sharePublicList.get(position).getUsername());
        holder.date.setText(sharePublicList.get(position).getDate());
        holder.bodyWeight.setText(sharePublicList.get(position).getBodyWeight());
        holder.bodyFat.setText(sharePublicList.get(position).getBodyFat());
    }

    @Override
    public int getItemCount() {
        return sharePublicList.size();
    }


}
