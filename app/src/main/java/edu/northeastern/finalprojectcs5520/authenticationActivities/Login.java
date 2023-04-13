package edu.northeastern.finalprojectcs5520.authenticationActivities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.northeastern.finalprojectcs5520.R;
import edu.northeastern.finalprojectcs5520.UserMainActivity;
import edu.northeastern.finalprojectcs5520.userPersonalInfoActivities.UserPersonalInfo;

public class Login extends AppCompatActivity {

    TextInputEditText editTextUsername;
    TextInputEditText editTextPassword;
    Button registerButton;
    Button loginButton;

    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference reference;
    Boolean checkPersonalInfo;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            openUserPage();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        editTextUsername = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);

        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);

        mDatabase = FirebaseDatabase.getInstance();
        reference = mDatabase.getReference("users");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username;
                String password;

                username = String.valueOf(editTextUsername.getText());

                // Setting default password. Note firebase only accept password 6 char long or authentication will fail.
                password = "123456";

                //Check that we have inputted a username
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(Login.this, "Enter A Username", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Appending a random gmail since firebase only accepts email. This work around for now.
                String email = username + "@gmail.com";

                //Firebase built in function where we sign into user
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(Login.this, "Login Successful",
                                            Toast.LENGTH_SHORT).show();

                                    openUserPage();

                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(Login.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterPage();
            }
        });
    }

    public void openRegisterPage() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
        finish();
    }

    public void openUserPage() {
        Intent intent = new Intent(this, UserMainActivity.class);
        startActivity(intent);
        finish();
    }

    public void openEnterUserPersonalInfoPage() {
        Intent intent = new Intent(this, UserPersonalInfo.class);
        startActivity(intent);
        finish();
    }

}