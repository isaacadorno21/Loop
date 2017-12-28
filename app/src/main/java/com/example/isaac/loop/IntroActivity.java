package com.example.isaac.loop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by isaac on 4/10/2017.
 */

public class IntroActivity extends Activity {
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private EditText emailTextbox;
    private EditText passwordTextbox;
    private Button signinButton;
    private Button signupButton;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("users");
        mAuth = FirebaseAuth.getInstance();

        emailTextbox = (EditText) findViewById(R.id.emailTextBox);
        passwordTextbox = (EditText) findViewById(R.id.passwordTextBox);
        signinButton = (Button) findViewById(R.id.signinButton);
        signupButton = (Button) findViewById(R.id.signupButton);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = emailTextbox.getText().toString().trim();
                password = passwordTextbox.getText().toString().trim();

                if (email.equals("") || password.equals("")) {
                    Toast.makeText(IntroActivity.this, "Please fill out the fields.", Toast.LENGTH_LONG).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(IntroActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful()) {
                                        Toast.makeText(IntroActivity.this, "Authentication failed." + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Intent intent = new Intent(IntroActivity.this, ChatRoomActivity.class);

                                        String username;
                                        if (email.contains("@")) {
                                            username = email.substring(0, email.indexOf('@'));
                                        } else {
                                            username = email;
                                        }
                                        intent.putExtra("username", username);

                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        String userID = user.getUid();

                                        ref.child(userID).setValue(username);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailTextbox.getText().toString();
                password = passwordTextbox.getText().toString();
                if (email.equals("") || password.equals("")) {
                    Toast.makeText(IntroActivity.this, "Please fill out the fields.", Toast.LENGTH_LONG).show();
                } else {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(IntroActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful()) {
                                        Toast.makeText(IntroActivity.this, "User Account not found", Toast.LENGTH_LONG).show();
                                    } else {
                                        Intent intent = new Intent(IntroActivity.this, ChatRoomActivity.class);
                                        String username = email.substring(0, email.indexOf('@'));
                                        intent.putExtra("username", username);

                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        String userID = user.getUid();

                                        ref.child(userID).setValue(username);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }
}