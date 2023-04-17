package edu.northeastern.finalprojectcs5520;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.bumptech.glide.Glide;

import android.content.Context;


import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private ArrayList<Record> records;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference reference;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        records = new ArrayList<>();
        adapter = new HistoryAdapter(this, records);
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
                String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);

                boolean recordFound = false;
                for (Record record : records) {
                    if (record.getDate().equals(date)) {
                        record.setRecordWeight(recordWeight);
                        record.setBodyFatPercent(bodyFatPercent);
                        record.setImageUrl(imageUrl);
                        adapter.notifyDataSetChanged();
                        recordFound = true;
                        break;
                    }
                }

                if (!recordFound) {
                    Record record = new Record(recordWeight, bodyFatPercent, date, imageUrl);
                    records.add(record);
                    adapter.notifyDataSetChanged();
                }
                Log.d("HistoryActivity", "Image URL: " + imageUrl);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            String imageUrl = data.getStringExtra("image_url");
            int position = data.getIntExtra("position", -1);

            if (position != -1) {
                Record record = records.get(position);
                record.setImageUrl(imageUrl);
                adapter.notifyItemChanged(position);
            }
        }
    }


    public static class Record {
        private String recordWeight;
        private String bodyFatPercent;
        private String date;
        private String imageUrl;


        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }


        public Record() {
            // Default constructor required for calls to DataSnapshot.getValue(Record.class)
        }

        public Record(String recordWeight, String bodyFatPercent, String date, String imageUrl) {
            this.recordWeight = recordWeight;
            this.bodyFatPercent = bodyFatPercent;
            this.date = date;
            this.imageUrl = imageUrl;

        }

        public void setRecordWeight(String recordWeight) {
            this.recordWeight = recordWeight;
        }

        public void setBodyFatPercent(String bodyFatPercent) {
            this.bodyFatPercent = bodyFatPercent;
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

        public String getImageUrl() {
            return imageUrl;
        }
    }
}
