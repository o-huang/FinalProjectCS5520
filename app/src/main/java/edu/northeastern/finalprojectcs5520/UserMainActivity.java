package edu.northeastern.finalprojectcs5520;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.northeastern.finalprojectcs5520.sharePublicActivity.SharePublic;

public class UserMainActivity extends AppCompatActivity {

    TextView currentUserName;
    Button logoutButton;
    FirebaseUser currentUser;
    FirebaseAuth auth;


    private Button recordWeight;
    private Button sharePublic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        auth = FirebaseAuth.getInstance();
        logoutButton = findViewById(R.id.logoutButton);
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

        //Logout button so that user can logout and return to login page
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                openLoginPage();
            }
        });

        recordWeight = findViewById(R.id.recordWeight);
        recordWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRecordWeightPage();
            }
        });

        sharePublic = findViewById(R.id.share);
        sharePublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSharePublicPage();
            }
        });
    }

    public void openLoginPage() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    public void openRecordWeightPage() {
        Intent intent = new Intent(this, RecordWeight.class);
        startActivity(intent);

    }

    public void openSharePublicPage() {
        Intent intent = new Intent(this, SharePublic.class);
        startActivity(intent);

    }
}