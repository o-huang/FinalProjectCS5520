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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import edu.northeastern.finalprojectcs5520.models.User;

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

    String username;
    String ageText;
    String heightFeetText;
    String heightInchesText;
    String currentWeightText;

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

        ageText = String.valueOf(age.getText());
        heightFeetText = String.valueOf(heightFeet.getText());
        heightInchesText = String.valueOf(heightInches.getText());
        currentWeightText = String.valueOf(currentWeight.getText());

        //Get current username
        currentUser = auth.getCurrentUser();
        String email = currentUser.getEmail();
        username = email.split("@")[0];

        getUserInfo(new FireStoreCallback() {
            @Override
            public void enterPersonalInfo(HashMap userInfo) {
                reference.child(username).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });


                openRecordWeightPage();
            }
        });


    }


    public void openUserPage() {
        //Directs user to user page.
        Intent intent = new Intent(this, UserMainActivity.class);
        startActivity(intent);
        finish();
    }

    public void openRecordWeightPage() {
        Intent intent = new Intent(this, RecordWeight.class);
        startActivity(intent);
        finish();
    }

    private void getUserInfo(FireStoreCallback fireStoreCallback) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap currentUser = (HashMap) snapshot.child(username).getValue();

                currentUser.put("currentWeight", currentWeightText);
                currentUser.put("age", ageText);
                currentUser.put("heightInches", heightInchesText);
                currentUser.put("heightFeet", heightFeetText);
                currentUser.put("personalInfoEntered", true);
                fireStoreCallback.enterPersonalInfo(currentUser);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("There was error");
            }
        });

    }


    private interface FireStoreCallback {
        void enterPersonalInfo(HashMap userInfo);
    }
}