package com.example.dinezyfinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private ImageView backButton;
    private ImageView profileImage;
    private TextView nameText;
    private TextView emailText;
    private TextView phoneText;
    private Button editProfileButton;
    private Button myOrdersButton;
    private Button favoritesButton;
    private Button addressesButton;
    private Button paymentMethodsButton;
    private Button helpButton;
    private Button logoutButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        initViews();
        setupUserInfo();
        setupListeners();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        profileImage = findViewById(R.id.profileImage);
        nameText = findViewById(R.id.nameText);
        emailText = findViewById(R.id.emailText);
        phoneText = findViewById(R.id.phoneText);
        editProfileButton = findViewById(R.id.editProfileButton);
        myOrdersButton = findViewById(R.id.myOrdersButton);
        favoritesButton = findViewById(R.id.favoritesButton);
        addressesButton = findViewById(R.id.addressesButton);
        paymentMethodsButton = findViewById(R.id.paymentMethodsButton);
        helpButton = findViewById(R.id.helpButton);
        logoutButton = findViewById(R.id.logoutButton);
    }

    private void setupUserInfo() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // User is logged in, display their info
            String name = user.getDisplayName();
            String email = user.getEmail();
            String phone = user.getPhoneNumber();

            nameText.setText(name != null && !name.isEmpty() ? name : "User");
            emailText.setText(email != null && !email.isEmpty() ? email : "No email");
            phoneText.setText(phone != null && !phone.isEmpty() ? phone : "No phone number");
        } else {
            // User is not logged in, show default info
            nameText.setText("Guest User");
            emailText.setText("Not signed in");
            phoneText.setText("");
            
            // Hide some buttons for guests
            editProfileButton.setVisibility(View.GONE);
            myOrdersButton.setVisibility(View.GONE);
            favoritesButton.setVisibility(View.GONE);
            addressesButton.setVisibility(View.GONE);
            paymentMethodsButton.setVisibility(View.GONE);
            
            // Change logout button to sign in
            logoutButton.setText("Sign In");
        }
    }

    private void setupListeners() {
        backButton.setOnClickListener(v -> finish());
        
        editProfileButton.setOnClickListener(v -> {
            Toast.makeText(this, "Edit Profile clicked", Toast.LENGTH_SHORT).show();
            // Implement edit profile functionality
        });
        
        myOrdersButton.setOnClickListener(v -> {
            Toast.makeText(this, "My Orders clicked", Toast.LENGTH_SHORT).show();
            // Navigate to orders activity
        });
        
        favoritesButton.setOnClickListener(v -> {
            Toast.makeText(this, "Favorites clicked", Toast.LENGTH_SHORT).show();
            // Navigate to favorites activity
        });
        
        addressesButton.setOnClickListener(v -> {
            Toast.makeText(this, "Addresses clicked", Toast.LENGTH_SHORT).show();
            // Navigate to addresses activity
        });
        
        paymentMethodsButton.setOnClickListener(v -> {
            Toast.makeText(this, "Payment Methods clicked", Toast.LENGTH_SHORT).show();
            // Navigate to payment methods activity
        });
        
        helpButton.setOnClickListener(v -> {
            Toast.makeText(this, "Help clicked", Toast.LENGTH_SHORT).show();
            // Navigate to help activity
        });
        
        logoutButton.setOnClickListener(v -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                // User is logged in, log them out
                mAuth.signOut();
                Toast.makeText(this, "Successfully logged out", Toast.LENGTH_SHORT).show();
                navigateToLogin();
            } else {
                // User is not logged in, navigate to login
                navigateToLogin();
            }
        });
    }
    
    private void navigateToLogin() {
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
} 