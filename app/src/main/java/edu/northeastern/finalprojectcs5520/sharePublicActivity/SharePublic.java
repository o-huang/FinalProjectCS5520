package edu.northeastern.finalprojectcs5520.sharePublicActivity;

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

import edu.northeastern.finalprojectcs5520.R;

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
                    if(myList!= null){
                        myList.forEach((key, value) -> {
                            if ((Boolean) ((HashMap) value).get("public")) {

                                String date = key.toString();

                                String[] dateList = date.split("_");
                                System.out.println(dateList);
                                String dateCorrectFormat = dateList[0] +"/"+dateList[1]+"/"+dateList[2] ;

                                String bodyWeight = "Weight: " + (String) ((HashMap) value).get("recordWeight");
                                String bodyFat = "Body Fat: " + (String) ((HashMap) value).get("bodyFatPercent");

                                sharePublicUserInfoList.add(new SharePublicInfo(username, dateCorrectFormat, bodyWeight, bodyFat));
                            }
                        });
                    }

                }
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setAdapter() {
        SharePublicRecyclerAdapter adapter = new SharePublicRecyclerAdapter(sharePublicUserInfoList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        sharePublicRecyclerView.setLayoutManager(layoutManager);
        sharePublicRecyclerView.setItemAnimator(new DefaultItemAnimator());
        sharePublicRecyclerView.setAdapter(adapter);
    }
}