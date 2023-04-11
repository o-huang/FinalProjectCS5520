package edu.northeastern.finalprojectcs5520;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.icu.number.Precision;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Formatter;

public class UserDisplay extends AppCompatActivity {
    TextView currentUserName;
    FirebaseUser currentUser;
    FirebaseAuth auth;

    FirebaseDatabase database;
    DatabaseReference reference;


    String heightFeet;
    String heightInches;
    String currentWeight;
    String currentFatRate;

    Double currentBMI;

    TextView curWeightDisplay;
    TextView curBMIDisplay;
    TextView curFatRateDisplay;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_display);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        auth = FirebaseAuth.getInstance();
        currentUserName = findViewById(R.id.currentUser);
        currentUser = auth.getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference();

        //Check if there is a user. If not goes to login page.
        if (currentUser == null) {
            openLoginPage();
        } else {
            //Getting username from firebase and setting the textview
            String email = currentUser.getEmail();
            username = email.split("@")[0];
            currentUserName.setText(username);
        }
        reference.child("users").child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                heightFeet = (String) snapshot.child("heightFeet").getValue();
                heightInches = (String) snapshot.child("heightInches").getValue();
                currentWeight = (String) snapshot.child("currentWeight").getValue();
                String currentFatRate = "";
                snapshot.child("recordWeights").getChildren();
                for(DataSnapshot data : snapshot.child("recordWeights").getChildren()) {
                    currentFatRate = data.child("bodyFatPercent").getValue().toString();
                }

                System.err.println("Fat Rate: " + currentFatRate);
                Double height = Double.parseDouble(heightFeet) * 12 + Double.parseDouble(heightInches);
                currentBMI = Double.parseDouble(currentWeight) / (height * height) * 703;
                System.err.println("********** currentBMI "+ currentBMI);

//                currentFatRate = (String) snapshot.child("recordWeights").getValue();
//                System.err.println("********** currentFatRate "+ currentFatRate);

                curWeightDisplay = findViewById(R.id.current_weight);
                curWeightDisplay.setText(currentWeight+ "lbs");

                curBMIDisplay = findViewById(R.id.current_bmi);
                curBMIDisplay.setText(String.format("%.2f", currentBMI));

                curFatRateDisplay = findViewById(R.id.current_fat_rate);
                curFatRateDisplay.setText(currentFatRate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void openLoginPage() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
}