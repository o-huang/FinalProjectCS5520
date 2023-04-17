package edu.northeastern.finalprojectcs5520;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.northeastern.finalprojectcs5520.authenticationActivities.Login;
import edu.northeastern.finalprojectcs5520.userPersonalInfoActivities.EditUserPersonalInfo;
import edu.northeastern.finalprojectcs5520.userPersonalInfoActivities.UserPersonalInfo;
import edu.northeastern.finalprojectcs5520.sharePublicActivity.SharePublic;

public class UserMainActivity extends AppCompatActivity {

    TextView currentUserName;
    Button logoutButton;
    FirebaseUser currentUser;
    FirebaseAuth auth;
    Button historyButton;

    String username;
    private Button recordWeight;
    private Button sharePublic;
    private Button userDisplay;
    private Button editUserInfo;

    private Button achievement;
    FirebaseDatabase mDatabase;
    DatabaseReference reference;
    Boolean checkPersonalInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        auth = FirebaseAuth.getInstance();
        logoutButton = findViewById(R.id.logoutButton);
        currentUserName = findViewById(R.id.currentUser);
        currentUser = auth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance();
        reference = mDatabase.getReference("users");

        //Check if there is a user. If not goes to login page.
        if (currentUser == null) {
            openLoginPage();
        } else {
            //Getting username from firebase and setting the textview
            String email = currentUser.getEmail();
            username = email.split("@")[0];
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


                DatabaseReference userReference = reference.child(username);

                userReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        checkPersonalInfo = (Boolean) snapshot.child("personalInfoEntered").getValue();

                        if (checkPersonalInfo) {
                            openRecordWeightPage();
                        } else {
                            openRecordUserPersonalInfoPage();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        sharePublic = findViewById(R.id.share);
        sharePublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSharePublicPage();
            }
        });

        userDisplay = findViewById(R.id.user);
        userDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUserDisplayPage();
            }
        });



        achievement = findViewById(R.id.achievements);
        achievement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAchievementPage();
            }
        });

        historyButton = findViewById(R.id.historyButton);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHistoryPage();
            }
        });

    }

    public void openUserPage() {
        Intent intent = new Intent(this, UserMainActivity.class);
        startActivity(intent);
        finish();
    }


    public void openLoginPage() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    public void openRecordUserPersonalInfoPage() {
        Intent intent = new Intent(this, UserPersonalInfo.class);
        startActivity(intent);
    }

    public void openRecordWeightPage() {
        Intent intent = new Intent(this, RecordWeight.class);
        startActivity(intent);
    }

    public void openSharePublicPage() {
        Intent intent = new Intent(this, SharePublic.class);
        startActivity(intent);
    }


    public void openUserDisplayPage() {
        Intent intent = new Intent(this, UserDisplay.class);
        startActivity(intent);
    }

    public void openAchievementPage() {
        Intent intent = new Intent(this, Achievement.class);
        startActivity(intent);

    }

    public void openHistoryPage() {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }
}