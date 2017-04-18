package com.example.bito.chatexam;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText emailField;
    private EditText passwordField;
    private Button btnLogin;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailField = (EditText) findViewById(R.id.emailEditId);
        passwordField = (EditText) findViewById(R.id.passwordEditId);
        btnLogin = (Button) findViewById(R.id.submit);
        mFirebaseAuth = FirebaseAuth.getInstance();

        if(mFirebaseAuth.getCurrentUser() != null) {
            loadChatView();
        }

        // Initiate button click listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = emailField.getText().toString().trim();
                final String password = passwordField.getText().toString().trim();

                if(email.isEmpty() && password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Fields must not be empty!", Toast.LENGTH_SHORT).show();
                } else {
                    // Proceed to authentication
                    mFirebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {

                                        // Proceed to ChatView
                                        loadChatView();
                                    } else {
                                        // If a user is not registered
                                        registerUser(email, password);
                                    }
                                }
                            });
                }
            }
        });
    }

    private void registerUser(String email, String password) {

        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            loadChatView();
                        }else {
                            Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void loadChatView() {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
