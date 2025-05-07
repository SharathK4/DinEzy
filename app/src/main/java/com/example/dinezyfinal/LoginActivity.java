package com.example.dinezyfinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private Button continueButton, googleSignInButton, emailSignInButton;
    private EditText phoneEditText;
    private TextView loginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        setupListeners();
    }

    private void initViews() {
        continueButton = findViewById(R.id.continueButton);
        googleSignInButton = findViewById(R.id.googleSignInButton);
        emailSignInButton = findViewById(R.id.emailSignInButton);
        loginTextView = findViewById(R.id.loginTextView);
    }

    private void setupListeners() {
        continueButton.setOnClickListener(v -> {
            // In a real app, you would validate and authenticate the user
            navigateToHome();
        });

        googleSignInButton.setOnClickListener(v -> {
            // In a real app, you would implement Google Sign In
            Toast.makeText(LoginActivity.this, "Google Sign In clicked", Toast.LENGTH_SHORT).show();
            navigateToHome();
        });

        emailSignInButton.setOnClickListener(v -> {
            // In a real app, you would show email sign-in UI
            Toast.makeText(LoginActivity.this, "Email Sign In clicked", Toast.LENGTH_SHORT).show();
            navigateToHome();
        });

        loginTextView.setOnClickListener(v -> {
            // In a real app, you would show login UI for existing users
            Toast.makeText(LoginActivity.this, "Login clicked", Toast.LENGTH_SHORT).show();
        });
    }

    private void navigateToHome() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
} 