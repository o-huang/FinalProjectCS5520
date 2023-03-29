package edu.northeastern.finalprojectcs5520;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class SharePublic extends AppCompatActivity {

    DatabaseReference reference;

    private ArrayList<SharePublicInfo> sharePublicUserInfoList;
    private RecyclerView sharePublicRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_public);

        sharePublicUserInfoList = new ArrayList<>();
        sharePublicRecyclerView = findViewById(R.id.sharePublicRecyclerView);
        reference = FirebaseDatabase.getInstance().getReference().child("users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String username = dataSnapshot.child("username").getValue().toString();

                    HashMap myList = (HashMap) dataSnapshot.child("recordWeights").getValue();

                    myList.forEach((key, value) -> {
                        if ((Boolean)((HashMap)value).get("public") == true) {
                            System.out.println("in herererere");
                            String date = key.toString();
                            String bodyWeight= ((HashMap)value).get("recordWeight").toString();
                            String bodyFat = ((HashMap)value).get("bodyFatPercent").toString();

                            sharePublicUserInfoList.add(new SharePublicInfo(username,date,bodyWeight,bodyFat));
                        }

                    });


                }
                setAdapter();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        System.out.println("hiiiiiiiii");
        System.out.println(sharePublicUserInfoList);
    }

    private void setAdapter(){
        SharePublicRecyclerAdapter adapter = new SharePublicRecyclerAdapter(sharePublicUserInfoList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        sharePublicRecyclerView.setLayoutManager(layoutManager);
        sharePublicRecyclerView.setItemAnimator(new DefaultItemAnimator());
        sharePublicRecyclerView.setAdapter(adapter);
    }
}