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
import com.google.android.material.textfield.TextInputLayout;
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

    TextInputLayout age;
    TextInputLayout heightFeet;
    TextInputLayout heightInches;
    TextInputLayout currentWeight;
    TextInputLayout goalWeight;
    TextInputLayout goalBMI;
    TextInputLayout goalFatRate;
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
        goalFatRate = findViewById(R.id.goalFatRate);
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



        ageText = String.valueOf(age.getEditText().getText());
        heightFeetText = String.valueOf(heightFeet.getEditText().getText());
        heightInchesText = String.valueOf(heightInches.getEditText().getText());
        currentWeightText = String.valueOf(currentWeight.getEditText().getText());
        goalWeightText = String.valueOf(goalWeight.getEditText().getText());
        goalFatRateText = String.valueOf(goalFatRate.getEditText().getText());
        goalBMIText = String.valueOf(goalBMI.getEditText().getText());

        if (!validateAge() | !validateHeightFeet() | !validateHeightInches() | !validateCurrentWeight() | !validateGoalWeight() |
                !validateGoalBMI() | !validateGoalFatRate()) {
            return;
        }


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
                openRecordWeightPage();
                System.out.println("Finished inputting new user information");
            }
        });
    }

    public void openRecordWeightPage() {
        Intent intent = new Intent(this, RecordWeight.class);
        startActivity(intent);
    }

    private boolean validateAge() {
        if (ageText.isEmpty()) {
            age.setError("Field can't be empty");
            return false;
        } else if (Float.parseFloat(ageText) < 0 || Float.parseFloat(ageText) > 200) {
            age.setError("Please enter a age between 0 and 200.");
            return false;
        } else {
            age.setError(null);
            return true;
        }
    }

    private boolean validateHeightFeet() {
        if (heightFeetText.isEmpty()) {
            heightFeet.setError("Field can't be empty");
            return false;
        } else if (Float.parseFloat(heightFeetText) < 0 || Float.parseFloat(heightFeetText) > 10) {
            heightFeet.setError("Please enter height feet between 0 and 10.");
            return false;
        } else {
            heightFeet.setError(null);
            return true;
        }
    }

    private boolean validateHeightInches() {
        if (heightInchesText.isEmpty()) {
            heightInches.setError("Field can't be empty");
            return false;
        } else if (Float.parseFloat(heightInchesText) < 0 || Float.parseFloat(heightInchesText) > 11) {
            heightInches.setError("Please enter height inches between 0 and 10.");
            return false;
        } else {
            heightInches.setError(null);
            return true;
        }
    }

    private boolean validateCurrentWeight() {
        if (currentWeightText.isEmpty()) {
            currentWeight.setError("Field can't be empty");
            return false;
        } else if (Float.parseFloat(currentWeightText) < 0 || Float.parseFloat(currentWeightText) > 1000) {
            currentWeight.setError("Please enter current weight between 0 and 1000.");
            return false;
        } else {
            currentWeight.setError(null);
            return true;
        }
    }

    private boolean validateGoalWeight() {
        if (goalWeightText.isEmpty()) {
            goalWeight.setError("Field can't be empty");
            return false;
        } else if (Float.parseFloat(goalWeightText) < 0 || Float.parseFloat(goalWeightText) > 1000) {
            goalWeight.setError("Please enter goal weigh between 0 and 1000.");
            return false;
        } else {
            goalWeight.setError(null);
            return true;
        }
    }

    private boolean validateGoalBMI() {
        if (goalBMIText.isEmpty()) {
            goalBMI.setError("Field can't be empty");
            return false;
        } else if (Float.parseFloat(goalBMIText) < 0 || Float.parseFloat(goalBMIText) > 100) {
            goalBMI.setError("Please enter goal bmi between 0 and 100.");
            return false;
        } else {
            goalBMI.setError(null);
            return true;
        }
    }

    private boolean validateGoalFatRate() {
        if (goalFatRateText.isEmpty()) {
            goalFatRate.setError("Field can't be empty");
            return false;
        } else if (Float.parseFloat(goalFatRateText) < 0 || Float.parseFloat(goalFatRateText) > 100) {
            goalFatRate.setError("Please enter goal fat rate between 0 and 100.");
            return false;
        } else {
            goalFatRate.setError(null);
            return true;
        }
    }





}