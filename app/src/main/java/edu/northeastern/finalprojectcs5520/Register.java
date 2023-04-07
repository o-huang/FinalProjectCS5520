package edu.northeastern.finalprojectcs5520;

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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import edu.northeastern.finalprojectcs5520.models.User;


public class Register extends AppCompatActivity {

    TextInputEditText editTextUsername;
    TextInputEditText editTextPassword;
    Button registerButton;
    Button loginButton;

    FirebaseAuth mAuth;

    FirebaseDatabase mDatabase;
    DatabaseReference reference;

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
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        editTextUsername = findViewById(R.id.registerUsername);
        editTextPassword = findViewById(R.id.registerPassword);

        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);

        //Instance of database
        mDatabase = FirebaseDatabase.getInstance();
        reference = mDatabase.getReference("users");

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username;
                String password;

                username = String.valueOf(editTextUsername.getText());

                // Setting default password. Note firebase only accept password 6 char long or authentication will fail.
                password = "123456";

                //Check that we have inputted a username
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(Register.this, "Enter A Username", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Appending a random gmail since firebase only accepts email. This work around for now.
                String email = username + "@gmail.com";

                //Firebase built in function where we create a new user register new user
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Register.this, "Register Successful.",
                                            Toast.LENGTH_SHORT).show();

                                    User user = new User(username);
                                    reference.child(username).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), "Added user!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Unable to reset player1!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                    //Goes to login page if successful

                                    DatabaseReference userReference = reference.child(username);

                                    userReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Boolean enteredPersonalInfoBoolean = (Boolean) snapshot.child("personalInfoEntered").getValue();


                                            if (enteredPersonalInfoBoolean) {

                                                //If user entered personal info go to user page
                                                openUserPage();
                                            } else {
                                                //If not go to enter user personal info page
                                                openEnterUserPersonalInfoPage();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(Register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginPage();
            }
        });


    }

    public void openLoginPage() {

        Intent intent = new Intent(this, Login.class);
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

    }

}