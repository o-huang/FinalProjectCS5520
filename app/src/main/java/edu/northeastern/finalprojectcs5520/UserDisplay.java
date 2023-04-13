package edu.northeastern.finalprojectcs5520;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.northeastern.finalprojectcs5520.authenticationActivities.Login;
import edu.northeastern.finalprojectcs5520.sharePublicActivity.SharePublic;

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

    String goalWeight;
    String goalBMI;
    String goalFatRate;

    TextView goalWeightDisplay;
    TextView goalBMIDisplay;
    TextView goalFatRateDisplay;

    String username;

    private Button shareButton;
    private Button diaryButton;
    private Button homeButton;


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


                heightFeet = snapshot.child("heightFeet").getValue().toString();
                heightInches = snapshot.child("heightInches").getValue().toString();
                currentWeight = snapshot.child("currentWeight").getValue().toString();


                if (currentWeight.equals("0")) {
                    Toast.makeText(getApplicationContext(), "Please record your weight.", Toast.LENGTH_LONG).show();
                }



                String currentFatRate = "0";
                snapshot.child("recordWeights").getChildren();
                for(DataSnapshot data : snapshot.child("recordWeights").getChildren()) {
                    currentFatRate = data.child("bodyFatPercent").getValue().toString();
                }
                System.err.println("********** currentFatRate " + currentFatRate);

                Double height = Double.parseDouble(heightFeet) * 12 + Double.parseDouble(heightInches);

                String personalInfoEntered;
                personalInfoEntered = snapshot.child("personalInfoEntered").getValue().toString();
                if (personalInfoEntered.equals("true")) {
                    currentBMI = Double.parseDouble(currentWeight) / (height * height) * 703;
                } else {
                    currentBMI=0.0;
                };

                System.err.println("********** currentBMI "+ currentBMI);


                // Update the display current weight, BMI, and Fat Rate
                curWeightDisplay = findViewById(R.id.current_weight);
                curWeightDisplay.setText(currentWeight + "lbs");

                curBMIDisplay = findViewById(R.id.current_bmi);
                if (curBMIDisplay != null) {
                curBMIDisplay.setText(String.format("%.2f", currentBMI));}

                curFatRateDisplay = findViewById(R.id.current_fat_rate);
                curFatRateDisplay.setText(currentFatRate + "%");

                goalWeight = snapshot.child("goalWeight").getValue().toString();
                goalBMI = snapshot.child("goalBMI").getValue().toString();
                goalFatRate = snapshot.child("goalFatRate").getValue().toString();

                goalWeightDisplay = findViewById(R.id.goal_weight);
                goalWeightDisplay.setText(goalWeight + "lbs");

                goalBMIDisplay = findViewById(R.id.goal_bmi);
                goalBMIDisplay.setText(String.format("%.2f", Double.parseDouble(goalBMI)));

                goalFatRateDisplay = findViewById(R.id.goal_fat_rate);
                goalFatRateDisplay.setText(goalFatRate + "%");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        shareButton = findViewById(R.id.share_button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSharePublicPage();
            }
        });


//        diaryButton = findViewById(R.id.diary_button);
//        diaryButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openHistoryPage();
//            }
//        });

        homeButton = findViewById(R.id.home_button);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUserMainPage();
            }
        });

    }

    public void openLoginPage() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    public void openSharePublicPage() {
        Intent intent = new Intent(this, SharePublic.class);
        startActivity(intent);
        finish();
    }

//    public void openHistoryPage() {
//        Intent intent = new Intent(this, History.class);
//        startActivity(intent);
//        finish();
//    }

    public void openUserMainPage() {
        Intent intent = new Intent(this, UserMainActivity.class);
        startActivity(intent);
        finish();
    }
}