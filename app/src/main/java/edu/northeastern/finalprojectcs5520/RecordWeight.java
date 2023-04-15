package edu.northeastern.finalprojectcs5520;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RecordWeight extends AppCompatActivity {

    EditText bodyWeight;
    EditText bodyFat;
    CheckBox sharePublic;
    Button submitButton;

    FirebaseAuth auth;
    FirebaseUser currentUser;
    FirebaseDatabase mDatabase;
    DatabaseReference reference;

    String username;
    String bodyWeightValue;
    String bodyFatValue;
    boolean sharePublicValue;
    Map info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_weight);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        bodyWeight = findViewById(R.id.bodyWeight);
        bodyFat = findViewById(R.id.bodyFat);
        sharePublic = findViewById(R.id.sharePublic);
        submitButton = findViewById(R.id.submitButton);

        //Instance of database
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        reference = mDatabase.getReference("users");

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRecordedWeight();
            }
        });
    }


    public void saveRecordedWeight() {
        //Get date with format
        SimpleDateFormat formatter = new SimpleDateFormat("MM_dd_yyyy");
        Date date = new Date();
        String formattedDate = formatter.format(date);

        //Check that body weight and body fat fields are not empty
        if (TextUtils.isEmpty(bodyWeight.getText()) || TextUtils.isEmpty(bodyFat.getText())) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        //Get inputs from the fields
        bodyWeightValue = String.valueOf(bodyWeight.getText());
        bodyFatValue = String.valueOf(bodyFat.getText());
        sharePublicValue = sharePublic.isChecked();


        //Initialize new hashmap with info from our fields
        info = new HashMap<>();
        info.put("recordWeight", bodyWeightValue);
        info.put("bodyFatPercent", bodyFatValue);
        info.put("public", sharePublicValue);

        //Updates the current user weight in database
        Map<String, Object> userWeightAndFatRateUpdate = new HashMap<>();

        userWeightAndFatRateUpdate.put("currentWeight", bodyWeightValue);

        reference.child(username).updateChildren(userWeightAndFatRateUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                System.out.println("Finished updating user weight and bmi.");
            }
        });


        //Get current username
        currentUser = auth.getCurrentUser();
        String email = currentUser.getEmail();
        username = email.split("@")[0];

        //Callback function where it get the info to calculate the bmi
        calculateBmiCallBack(new FireStoreCallback() {
            @Override
            public void calculateBmi(String bmi) {
                //add bmi
                info.put("bmi", bmi);

                reference.child(username).child("recordWeights").child(formattedDate).setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            openUserPage();
                            Toast.makeText(getApplicationContext(), "Successfully Recorded Weight!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed To Record Weight!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }


    public void openUserPage() {
        //Directs user to user page.
        Intent intent = new Intent(this, UserMainActivity.class);
        startActivity(intent);
        finish();
    }

    private void calculateBmiCallBack(FireStoreCallback fireStoreCallback) {
        //Get height inches and feet from database and calculates bmi

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String heightInches = (String) snapshot.child(username).child("heightInches").getValue();
                String heightFeet = (String) snapshot.child(username).child("heightFeet").getValue();

                float totalHeight = Float.parseFloat(heightInches) + (Float.parseFloat(heightFeet) * 12);

                float bmi = (Float.parseFloat(bodyWeightValue) / totalHeight / totalHeight) * 703;

                String roundedBmi = String.format("%.2f", bmi);
                fireStoreCallback.calculateBmi(roundedBmi);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("There was error");
            }
        });
    }

    private interface FireStoreCallback {
        void calculateBmi(String bmi);
    }
}

