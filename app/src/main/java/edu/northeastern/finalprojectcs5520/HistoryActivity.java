package edu.northeastern.finalprojectcs5520;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private ArrayList<Record> records;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        records = new ArrayList<>();
        adapter = new HistoryAdapter(records);
        recyclerView.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        reference = mDatabase.getReference("users");

        currentUser = auth.getCurrentUser();
        String email = currentUser.getEmail();
        String username = email.split("@")[0];

        reference.child(username).child("recordWeights").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String recordWeight = dataSnapshot.child("recordWeight").getValue(String.class);
                String bodyFatPercent = dataSnapshot.child("bodyFatPercent").getValue(String.class);
                String date = dataSnapshot.getKey();

                Record record = new Record(recordWeight, bodyFatPercent, date);
                records.add(record);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public static class Record {
        private String recordWeight;
        private String bodyFatPercent;
        private String date;

        public Record() {
            // Default constructor required for calls to DataSnapshot.getValue(Record.class)
        }

        public Record(String recordWeight, String bodyFatPercent, String date) {
            this.recordWeight = recordWeight;
            this.bodyFatPercent = bodyFatPercent;
            this.date = date;
        }

        public String getRecordWeight() {
            return recordWeight;
        }

        public String getBodyFatPercent() {
            return bodyFatPercent;
        }

        public String getDate() {
            return date;
        }
    }

    private class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

        private ArrayList<Record> records;

        public HistoryAdapter(ArrayList<Record> records) {
            this.records = records;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Record record = records.get(position);
            holder.recordWeightTextView.setText("Weight: " + record.getRecordWeight());
            holder.bodyFatTextView.setText("Body Fat Percentage: " + record.getBodyFatPercent());
            holder.dateTextView.setText("Date: " + record.getDate());
        }

        @Override
        public int getItemCount() {
            return records.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView recordWeightTextView;
            TextView bodyFatTextView;
            TextView dateTextView;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                recordWeightTextView = itemView.findViewById(R.id.recordWeight);
                bodyFatTextView = itemView.findViewById(R.id.bodyFat);
                dateTextView = itemView.findViewById(R.id.date);
            }
        }
    }
}
