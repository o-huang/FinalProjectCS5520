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
import com.google.android.material.textfield.TextInputLayout;
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

    TextInputLayout bodyWeight;
    TextInputLayout bodyFat;
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

        //Get inputs from the fields
        bodyWeightValue = String.valueOf(bodyWeight.getEditText().getText());
        bodyFatValue = String.valueOf(bodyFat.getEditText().getText());
        sharePublicValue = sharePublic.isChecked();

        if (!validateWeight() || !validateBodyFat()) {
            return;
        }

        //Initialize new hashmap with info from our fields
        info = new HashMap<>();
        info.put("recordWeight", bodyWeightValue);
        info.put("bodyFatPercent", bodyFatValue);
        info.put("public", sharePublicValue);

        //Get current username
        currentUser = auth.getCurrentUser();
        String email = currentUser.getEmail();
        username = email.split("@")[0];

        reference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String heightInches = (String) snapshot.child("heightInches").getValue();
                String heightFeet = (String) snapshot.child("heightFeet").getValue();

                float totalHeight = Float.parseFloat(heightInches) + (Float.parseFloat(heightFeet) * 12);

                float bmi = (Float.parseFloat(bodyWeightValue) / totalHeight / totalHeight) * 703;

                String roundedBmi = String.format("%.2f", bmi);

                info.put("bmi", roundedBmi);

                reference.child(username).child("recordWeights").child(formattedDate).updateChildren(info)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Successfully Recorded Weight!", Toast.LENGTH_SHORT).show();
                                    openUserPage();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Failed To Record Weight!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("There was error");
            }
        });
    }


    public void openUserPage() {
        //Directs user to user page.
        Intent intent = new Intent(this, UserMainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean validateWeight() {
        if (bodyWeightValue.isEmpty()) {
            bodyWeight.setError("Field can't be empty");
            return false;
        } else if (Float.parseFloat(bodyWeightValue) < 0 || Float.parseFloat(bodyWeightValue) > 1000) {
            bodyWeight.setError("Please enter a weight 0 and 1000.");
            return false;
        } else {
            bodyWeight.setError(null);
            return true;
        }
    }

    private boolean validateBodyFat() {
        if (bodyFatValue.isEmpty()) {
            bodyFat.setError("Field can't be empty");
            return false;
        } else if (Float.parseFloat(bodyFatValue) < 0 || Float.parseFloat(bodyFatValue) > 100) {
            bodyFat.setError("Please enter body fat % between 0 and 100.");
            return false;
        } else {
            bodyFat.setError(null);
            return true;
        }
    }
}

