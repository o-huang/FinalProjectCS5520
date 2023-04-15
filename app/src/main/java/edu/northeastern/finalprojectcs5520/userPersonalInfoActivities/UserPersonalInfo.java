package edu.northeastern.finalprojectcs5520.userPersonalInfoActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import edu.northeastern.finalprojectcs5520.R;
import edu.northeastern.finalprojectcs5520.RecordWeight;

public class UserPersonalInfo extends AppCompatActivity {

    EditText age;
    EditText heightFeet;
    EditText heightInches;
    EditText currentWeight;
    EditText goalWeight;
    EditText goalBMI;
    EditText goalFatRate;
    Button submitButton;


    FirebaseAuth auth;
    FirebaseUser currentUser;
    FirebaseDatabase mDatabase;
    DatabaseReference reference;

    String username;
    String ageText;
    String heightFeetText;
    String heightInchesText;
    String currentWeightText;
    String goalWeightText;
    String goalBMIText;
    String goalFatRateText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_personal_info);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        //Instance of database
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        reference = mDatabase.getReference("users");

        age = findViewById(R.id.age);
        heightFeet = findViewById(R.id.heightFeet);
        heightInches = findViewById(R.id.heightInches);
        currentWeight = findViewById(R.id.currentWeight);
        submitButton = findViewById(R.id.personalInfoSubmitButton);
        goalWeight = findViewById(R.id.goalWeight);
        goalFatRate = findViewById(R.id.goalFateRate);
        goalBMI = findViewById(R.id.goalBMI);

        //Get current username
        currentUser = auth.getCurrentUser();
        String email = currentUser.getEmail();
        username = email.split("@")[0];


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUserPersonalInfo();
            }
        });


    }

    public void addUserPersonalInfo() {

        if (TextUtils.isEmpty(age.getText()) || TextUtils.isEmpty(heightFeet.getText()) || TextUtils.isEmpty(heightInches.getText()) || TextUtils.isEmpty(currentWeight.getText())
                || TextUtils.isEmpty(goalWeight.getText()) || TextUtils.isEmpty(goalBMI.getText()) || TextUtils.isEmpty(goalFatRate.getText())
        ) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        ageText = String.valueOf(age.getText());
        heightFeetText = String.valueOf(heightFeet.getText());
        heightInchesText = String.valueOf(heightInches.getText());
        currentWeightText = String.valueOf(currentWeight.getText());
        goalWeightText = String.valueOf(goalWeight.getText());
        goalFatRateText = String.valueOf(goalFatRate.getText());
        goalBMIText = String.valueOf(goalBMI.getText());


        Map<String, Object> userInfoUpdates = new HashMap<>();

        userInfoUpdates.put("currentWeight", currentWeightText);
        userInfoUpdates.put("age", ageText);
        userInfoUpdates.put("heightInches", heightInchesText);
        userInfoUpdates.put("heightFeet", heightFeetText);
        userInfoUpdates.put("goalWeight", goalWeightText);
        userInfoUpdates.put("goalBMI", goalBMIText);
        userInfoUpdates.put("goalFatRate", goalFatRateText);
        userInfoUpdates.put("personalInfoEntered", true);

        reference.child(username).updateChildren(userInfoUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                System.out.println("Finished inputting new user information");
            }
        });
    }

}