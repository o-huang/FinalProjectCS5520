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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_personal_info);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
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
        goalFatRate = findViewById(R.id.editGoalFateRate);
        goalBMI = findViewById(R.id.editGoalBMI);


        //Get current username
        currentUser = auth.getCurrentUser();
        String email = currentUser.getEmail();
        username = email.split("@")[0];

        //get back user info and set it to the text view.
        getUserInfo(new FireStoreCallback() {
            @Override
            public void getUserInfoCallback(HashMap userInfo) {
                age.setText((CharSequence) userInfo.get("age"));
                heightFeet.setText((CharSequence) userInfo.get("heightFeet"));
                heightInches.setText((CharSequence) userInfo.get("heightInches"));
                currentWeight.setText((CharSequence) userInfo.get("currentWeight"));
                goalWeight.setText((CharSequence) userInfo.get("goalWeight"));
                goalFatRate.setText((CharSequence) userInfo.get("goalFatRate"));
                goalBMI.setText((CharSequence) userInfo.get("goalBMI"));

                //submit changes and save change to database
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveEditUserInfo();
                    }
                });
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
        String editAgeText = String.valueOf(age.getText());
        String editHeightFeetText = String.valueOf(heightFeet.getText());
        String editHeightInchesText = String.valueOf(heightInches.getText());
        String editCurrentWeightText = String.valueOf(currentWeight.getText());
        String editGoalWeightText = String.valueOf(goalWeight.getText());
        String editGoalFatRateText = String.valueOf(goalFatRate.getText());
        String editGoalBMIText = String.valueOf(goalBMI.getText());


        //Checks that the fields are filled out
        if (TextUtils.isEmpty(age.getText()) || TextUtils.isEmpty(heightFeet.getText()) || TextUtils.isEmpty(heightInches.getText()) || TextUtils.isEmpty(currentWeight.getText())
                || TextUtils.isEmpty(goalWeight.getText()) || TextUtils.isEmpty(goalBMI.getText()) || TextUtils.isEmpty(goalFatRate.getText())
        ) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

//Check that age is between 0 and 200
        if(Float.parseFloat(String.valueOf(age.getText()))< 0 || Float.parseFloat(String.valueOf(age.getText())) > 200){
            Toast.makeText(this, "Please enter a age between 0 and 200.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Check that feet is between 0 and 10
        if(Float.parseFloat(String.valueOf(heightFeet.getText()))< 0 || Float.parseFloat(String.valueOf(heightFeet.getText())) > 10){
            Toast.makeText(this, "Please enter height feet between 0 and 10.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Check that inches is between 1 and 11
        if(Float.parseFloat(String.valueOf(heightInches.getText()))< 0 || Float.parseFloat(String.valueOf(heightInches.getText())) > 11){
            Toast.makeText(this, "Please enter height inches between 0 and 11.", Toast.LENGTH_SHORT).show();
            return;
        }


        //Check that currentWeight is between 0 and 1000
        if(Float.parseFloat(String.valueOf(currentWeight.getText()))< 0 || Float.parseFloat(String.valueOf(currentWeight.getText())) > 1000){
            Toast.makeText(this, "Please enter current weight between 0 and 1000.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Check that goalWeight is between 0 and 1000
        if(Float.parseFloat(String.valueOf(goalWeight.getText()))< 0 || Float.parseFloat(String.valueOf(goalWeight.getText())) > 1000){
            Toast.makeText(this, "Please enter goal weight between 0 and 1000.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Check that goal bmi is between 0 and 100
        if(Float.parseFloat(String.valueOf(goalBMI.getText()))< 0 || Float.parseFloat(String.valueOf(goalBMI.getText())) > 100){
            Toast.makeText(this, "Please enter goal bmi between 0 and 100.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Check that goal fat rate is between 0 and 100
        if(Float.parseFloat(String.valueOf(goalFatRate.getText()))< 0 || Float.parseFloat(String.valueOf(goalFatRate.getText())) > 100){
            Toast.makeText(this, "Please enter goal fat rate between 0 and 100.", Toast.LENGTH_SHORT).show();
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
}