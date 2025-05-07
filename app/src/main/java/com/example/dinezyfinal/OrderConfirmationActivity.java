package com.example.dinezyfinal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class OrderConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        // Automatically move to order tracking after a delay
        new Handler(Looper.getMainLooper()).postDelayed(this::navigateToTracking, 2000);
    }

    private void navigateToTracking() {
        Intent intent = new Intent(OrderConfirmationActivity.this, TrackingActivity.class);
        startActivity(intent);
        finish();
    }
} 