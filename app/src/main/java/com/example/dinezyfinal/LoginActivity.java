package com.example.dinezyfinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;

    private Button continueButton, googleSignInButton, emailSignInButton;
    private EditText phoneEditText;
    private TextView loginTextView;
    
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            // Initialize Firebase Auth
            mAuth = FirebaseAuth.getInstance();
            
            // Configure Google Sign-In
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        } catch (Exception e) {
            Log.e(TAG, "Error initializing Firebase/Google Sign-In", e);
            Toast.makeText(this, "Error setting up authentication: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        initViews();
        setupListeners();
    }
    
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (mAuth != null) {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if(currentUser != null) {
                // Instead of auto-navigating, show a message that the user is already logged in
                // and give them a button to proceed
                Toast.makeText(this, "Welcome back " + currentUser.getDisplayName(), Toast.LENGTH_SHORT).show();
                // Uncomment the line below to auto-navigate
                // navigateToHome();
            }
        }
    }

    private void initViews() {
        try {
            continueButton = findViewById(R.id.continueButton);
            googleSignInButton = findViewById(R.id.googleSignInButton);
            emailSignInButton = findViewById(R.id.emailSignInButton);
            phoneEditText = findViewById(R.id.phoneEditText);
            loginTextView = findViewById(R.id.loginTextView);
        } catch (Exception e) {
            Log.e(TAG, "Error initializing views", e);
            Toast.makeText(this, "Error initializing app: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setupListeners() {
        continueButton.setOnClickListener(v -> {
            // In a real app, you would validate and authenticate the user with phone number
            Toast.makeText(LoginActivity.this, "Phone auth would be implemented here", Toast.LENGTH_SHORT).show();
            navigateToHome();
        });

        googleSignInButton.setOnClickListener(v -> {
            try {
                // Implement Google Sign In
                signInWithGoogle();
            } catch (Exception e) {
                Log.e(TAG, "Error starting Google Sign-In", e);
                Toast.makeText(this, "Error with Google Sign-In: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
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
    
    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed", e);
                    Toast.makeText(this, "Google Sign In failed. Error code: " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Unexpected error in onActivityResult", e);
            Toast.makeText(this, "Authentication error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void firebaseAuthWithGoogle(String idToken) {
        try {
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithCredential:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                navigateToHome();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithCredential:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication Failed: " + 
                                        (task.getException() != null ? task.getException().getMessage() : "Unknown error"), 
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "Error during Firebase authentication", e);
            Toast.makeText(this, "Firebase auth error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void navigateToHome() {
        try {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Error navigating to home", e);
            Toast.makeText(this, "Error navigating to home: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
} 