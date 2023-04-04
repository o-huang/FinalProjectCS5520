package edu.northeastern.finalprojectcs5520;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserDisplay extends AppCompatActivity {
    TextView currentUserName;
    FirebaseUser currentUser;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_display);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        auth = FirebaseAuth.getInstance();
        currentUserName = findViewById(R.id.currentUser);
        currentUser = auth.getCurrentUser();



        //Check if there is a user. If not goes to login page.
        if (currentUser == null) {
            openLoginPage();
        } else {
            //Getting username from firebase and setting the textview
            String email = currentUser.getEmail();
            String username = email.split("@")[0];
            currentUserName.setText(username);
        }


    }

    public void openLoginPage() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
}