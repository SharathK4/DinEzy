package com.example.dinezyfinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 onboardingViewPager;
    private View indicator1, indicator2, indicator3;
    private Button skipButton, startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        onboardingViewPager = findViewById(R.id.onboardingViewPager);
        indicator1 = findViewById(R.id.indicator1);
        indicator2 = findViewById(R.id.indicator2);
        indicator3 = findViewById(R.id.indicator3);
        skipButton = findViewById(R.id.skipButton);
        startButton = findViewById(R.id.startButton);

        setupOnboardingItems();

        onboardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateIndicators(position);
            }
        });

        skipButton.setOnClickListener(v -> navigateToLogin());
        startButton.setOnClickListener(v -> navigateToLogin());
    }

    private void navigateToLogin() {
        Intent intent = new Intent(OnboardingActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void setupOnboardingItems() {
        OnboardingAdapter adapter = new OnboardingAdapter();
        onboardingViewPager.setAdapter(adapter);
    }

    private void updateIndicators(int position) {
        indicator1.setBackgroundResource(position == 0 ? R.drawable.indicator_active : R.drawable.indicator_inactive);
        indicator2.setBackgroundResource(position == 1 ? R.drawable.indicator_active : R.drawable.indicator_inactive);
        indicator3.setBackgroundResource(position == 2 ? R.drawable.indicator_active : R.drawable.indicator_inactive);
    }

    class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder> {

        private final int[] layouts = {
            R.layout.slide_onboarding_1,
            R.layout.slide_onboarding_2,
            R.layout.slide_onboarding_3
        };

        @NonNull
        @Override
        public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new OnboardingViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                    layouts[viewType], parent, false
                )
            );
        }

        @Override
        public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
            // No binding needed as layouts are static
        }

        @Override
        public int getItemCount() {
            return layouts.length;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        class OnboardingViewHolder extends RecyclerView.ViewHolder {
            OnboardingViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }
} 