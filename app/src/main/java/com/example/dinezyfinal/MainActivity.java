package com.example.dinezyfinal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.provider.FontRequest;
import androidx.core.provider.FontsContractCompat;

public class MainActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 1500; // 1.5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Preload fonts
        preloadFonts();

        // Use a Handler to show splash screen for a short time, then launch OnboardingActivity
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, OnboardingActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_DELAY);
    }

    private void preloadFonts() {
        FontRequest request = new FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                "Italiana|Instrument Sans|Albert Sans|Ephesis",
                R.array.com_google_android_gms_fonts_certs);

        FontsContractCompat.requestFont(this, request,
                new FontsContractCompat.FontRequestCallback() {
                    @Override
                    public void onTypefaceRetrieved(android.graphics.Typeface typeface) {
                        // Font loaded successfully
                    }

                    @Override
                    public void onTypefaceRequestFailed(int reason) {
                        // Font loading failed
                    }
                }, new Handler(Looper.getMainLooper()));
    }
}