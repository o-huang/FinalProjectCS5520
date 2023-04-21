package edu.northeastern.finalprojectcs5520.userPersonalInfoActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
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

public class EditUserPersonalInfo extends AppCompatActivity {

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

    String editAgeText;
    String editHeightFeetText;
    String editHeightInchesText;
    String editCurrentWeightText;
    String editGoalWeightText;
    String editGoalFatRateText;
    String editGoalBMIText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_personal_info);

        //Instance of database
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        reference = mDatabase.getReference("users");

        age = findViewById(R.id.editAge);
        heightFeet = findViewById(R.id.editHeightFeet);
        heightInches = findViewById(R.id.editHeightInches);
        currentWeight = findViewById(R.id.editCurrentWeight);
        submitButton = findViewById(R.id.editPersonalInfoSubmitButton);
        goalWeight = findViewById(R.id.editGoalWeight);
        goalFatRate = findViewById(R.id.editGoalFatRate);
        goalBMI = findViewById(R.id.editGoalBMI);


        //Get current username
        currentUser = auth.getCurrentUser();
        String email = currentUser.getEmail();
        username = email.split("@")[0];

        //get back user info and set it to the text view.
        getUserInfo(new FireStoreCallback() {
            @Override
            public void getUserInfoCallback(HashMap userInfo) {

                if((boolean)userInfo.get("personalInfoEntered") == false){
                    Toast.makeText(getApplicationContext(), "Please record a weight before editing personal info.", Toast.LENGTH_LONG).show();
                }else{
                    age.getEditText().setText((CharSequence) userInfo.get("age"));
                    heightFeet.getEditText().setText((CharSequence) userInfo.get("heightFeet"));
                    heightInches.getEditText().setText((CharSequence) userInfo.get("heightInches"));
                    currentWeight.getEditText().setText((CharSequence) userInfo.get("currentWeight"));
                    goalWeight.getEditText().setText((CharSequence) userInfo.get("goalWeight"));
                    goalFatRate.getEditText().setText((CharSequence) userInfo.get("goalFatRate"));
                    goalBMI.getEditText().setText((CharSequence) userInfo.get("goalBMI"));

                    //submit changes and save change to database
                    submitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            saveEditUserInfo();
                        }
                    });
                }
            }
        });


    }

    private void getUserInfo(FireStoreCallback fireStoreCallback) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap currentUser = (HashMap) snapshot.child(username).getValue();
                fireStoreCallback.getUserInfoCallback(currentUser);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("There was error");
            }
        });

    }


    private void saveEditUserInfo() {
        editAgeText = String.valueOf(age.getEditText().getText());
        editHeightFeetText = String.valueOf(heightFeet.getEditText().getText());
        editHeightInchesText = String.valueOf(heightInches.getEditText().getText());
        editCurrentWeightText = String.valueOf(currentWeight.getEditText().getText());
        editGoalWeightText = String.valueOf(goalWeight.getEditText().getText());
        editGoalFatRateText = String.valueOf(goalFatRate.getEditText().getText());
        editGoalBMIText = String.valueOf(goalBMI.getEditText().getText());


        if (!validateAge() | !validateHeightFeet() | !validateHeightInches() | !validateCurrentWeight() | !validateGoalWeight() |
                !validateGoalBMI() | !validateGoalFatRate()) {
            return;
        }

        Map<String, Object> userInfoUpdates = new HashMap<>();

        userInfoUpdates.put("currentWeight", editCurrentWeightText);
        userInfoUpdates.put("age", editAgeText);
        userInfoUpdates.put("heightInches", editHeightInchesText);
        userInfoUpdates.put("heightFeet", editHeightFeetText);
        userInfoUpdates.put("goalWeight", editGoalWeightText);
        userInfoUpdates.put("goalBMI", editGoalBMIText);
        userInfoUpdates.put("goalFatRate", editGoalFatRateText);

        reference.child(username).updateChildren(userInfoUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "Successfully Edited Your Information", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private interface FireStoreCallback {
        void getUserInfoCallback(HashMap userInfo);
    }


    private boolean validateAge() {
        if (editAgeText.isEmpty()) {
            age.setError("Field can't be empty");
            return false;
        } else if (Float.parseFloat(editAgeText) < 0 || Float.parseFloat(editAgeText) > 200) {
            age.setError("Please enter a age between 0 and 200.");
            return false;
        } else {
            age.setError(null);
            return true;
        }
    }

    private boolean validateHeightFeet() {
        if (editHeightFeetText.isEmpty()) {
            heightFeet.setError("Field can't be empty");
            return false;
        } else if (Float.parseFloat(editHeightFeetText) < 0 || Float.parseFloat(editHeightFeetText) > 10) {
            heightFeet.setError("Please enter height feet between 0 and 10.");
            return false;
        } else {
            heightFeet.setError(null);
            return true;
        }
    }

    private boolean validateHeightInches() {
        if (editHeightInchesText.isEmpty()) {
            heightInches.setError("Field can't be empty");
            return false;
        } else if (Float.parseFloat(editHeightInchesText) < 0 || Float.parseFloat(editHeightInchesText) > 11) {
            heightInches.setError("Please enter height inches between 0 and 10.");
            return false;
        } else {
            heightInches.setError(null);
            return true;
        }
    }

    private boolean validateCurrentWeight() {
        if (editCurrentWeightText.isEmpty()) {
            currentWeight.setError("Field can't be empty");
            return false;
        } else if (Float.parseFloat(editCurrentWeightText) < 0 || Float.parseFloat(editCurrentWeightText) > 1000) {
            currentWeight.setError("Please enter current weight between 0 and 1000.");
            return false;
        } else {
            currentWeight.setError(null);
            return true;
        }
    }

    private boolean validateGoalWeight() {
        if (editGoalWeightText.isEmpty()) {
            goalWeight.setError("Field can't be empty");
            return false;
        } else if (Float.parseFloat(editGoalWeightText) < 0 || Float.parseFloat(editGoalWeightText) > 1000) {
            goalWeight.setError("Please enter goal weigh between 0 and 1000.");
            return false;
        } else {
            goalWeight.setError(null);
            return true;
        }
    }

    private boolean validateGoalBMI() {
        if (editGoalBMIText.isEmpty()) {
            goalBMI.setError("Field can't be empty");
            return false;
        } else if (Float.parseFloat(editGoalBMIText) < 0 || Float.parseFloat(editGoalBMIText) > 100) {
            goalBMI.setError("Please enter goal bmi between 0 and 100.");
            return false;
        } else {
            goalBMI.setError(null);
            return true;
        }
    }

    private boolean validateGoalFatRate() {
        if (editGoalFatRateText.isEmpty()) {
            goalFatRate.setError("Field can't be empty");
            return false;
        } else if (Float.parseFloat(editGoalFatRateText) < 0 || Float.parseFloat(editGoalFatRateText) > 100) {
            goalFatRate.setError("Please enter goal fat rate between 0 and 100.");
            return false;
        } else {
            goalFatRate.setError(null);
            return true;
        }
    }


}