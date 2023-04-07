package edu.northeastern.finalprojectcs5520;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserPersonalInfo extends AppCompatActivity {

    EditText age;
    EditText heightFeet;
    EditText heightInches;
    EditText currentWeight;
    Button submitButton;


    FirebaseAuth auth;
    FirebaseUser currentUser;
    FirebaseDatabase mDatabase;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_personal_info);

        //Instance of database
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        reference = mDatabase.getReference("users");

        age = findViewById(R.id.age);
        heightFeet = findViewById(R.id.heightFeet);
        heightInches = findViewById(R.id.heightInches);
        currentWeight = findViewById(R.id.currentWeight);
        submitButton = findViewById(R.id.personalInfoSubmitButton);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUserPersonalInfo();
            }
        });


    }

    public void addUserPersonalInfo() {

        if (TextUtils.isEmpty(age.getText()) || TextUtils.isEmpty(heightFeet.getText()) || TextUtils.isEmpty(heightInches.getText()) || TextUtils.isEmpty(currentWeight.getText())) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String ageText = String.valueOf(age.getText());
        String heightFeetText = String.valueOf(heightFeet.getText());
        String heightInchesText = String.valueOf(heightInches.getText());
        String currentWeightText = String.valueOf(currentWeight.getText());

        System.out.println(ageText);
        System.out.println(heightFeetText);
        System.out.println(heightInchesText);
        System.out.println(currentWeightText);

        //Get current username
        currentUser = auth.getCurrentUser();
        String email = currentUser.getEmail();
        String username = email.split("@")[0];


        reference.child(username).child("age").setValue(ageText).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    System.out.println("Successfully added age");
                } else {
                    Toast.makeText(getApplicationContext(), "Failed To Record age!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        reference.child(username).child("heightFeet").setValue(heightFeetText).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    System.out.println("Successfully added heightFeet");
                } else {
                    Toast.makeText(getApplicationContext(), "Failed To Record height feet!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        reference.child(username).child("heightInches").setValue(heightInchesText).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    System.out.println("Successfully added height inches");
                } else {
                    Toast.makeText(getApplicationContext(), "Failed To Record height inches!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        reference.child(username).child("currentWeight").setValue(currentWeightText).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    System.out.println("Successfully added current weight");
                } else {
                    Toast.makeText(getApplicationContext(), "Failed To Record Current Weight!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        reference.child(username).child("personalInfoEntered").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    System.out.println("Successfully set personal info entered to true");
                } else {
                    Toast.makeText(getApplicationContext(), "Failed To set personal info to true!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        openUserPage();
    }


    public void openUserPage() {
        //Directs user to user page.
        Intent intent = new Intent(this, UserMainActivity.class);
        startActivity(intent);
        finish();
    }
}