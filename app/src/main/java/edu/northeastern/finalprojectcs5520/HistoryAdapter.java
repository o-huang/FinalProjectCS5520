package edu.northeastern.finalprojectcs5520;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context context;
    private List<HistoryActivity.Record> records;

    public HistoryAdapter(Context context, List<HistoryActivity.Record> records) {
        this.context = context;
        this.records = records;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryActivity.Record record = records.get(position);
        holder.dateTextView.setText(record.getDate());
        holder.weightTextView.setText(record.getRecordWeight());
        holder.bodyFatTextView.setText(record.getBodyFatPercent());
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        TextView weightTextView;
        TextView bodyFatTextView;

        ViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            weightTextView = itemView.findViewById(R.id.weightTextView);
            bodyFatTextView = itemView.findViewById(R.id.bodyFatTextView);
        }
    }
}

