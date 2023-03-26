package edu.northeastern.finalprojectcs5520;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_weight);

        bodyWeight = findViewById(R.id.bodyFat);
        bodyFat = findViewById(R.id.bodyWeight);
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


    public void saveRecordedWeight(){
        SimpleDateFormat formatter = new SimpleDateFormat("MM_dd_yyyy");
        Date date = new Date();
        String formattedDate = formatter.format(date);

        System.out.println(bodyWeight.getText());
        System.out.println(bodyFat.getText());
        System.out.println(sharePublic.isChecked());

        String bodyWeightValue = String.valueOf(bodyWeight.getText());
        String bodyFatValue = String.valueOf(bodyFat.getText());
        boolean sharePublicValue = sharePublic.isChecked();

        Map info = new HashMap<>();
        info.put("recordWeight",bodyWeightValue);
        info.put("bodyFatPercent",bodyFatValue);
        info.put("public", sharePublicValue);

        currentUser = auth.getCurrentUser();
        String email = currentUser.getEmail();
        String username = email.split("@")[0];


        reference.child(username).child("recordWeights").child(formattedDate).setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    openUserPage();
                    Toast.makeText(getApplicationContext(),"Successfully Recorded Weight!",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Failed To Record Weight!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void openUserPage() {
        Intent intent = new Intent(this, UserMainActivity.class);
        startActivity(intent);
        finish();
    }
}