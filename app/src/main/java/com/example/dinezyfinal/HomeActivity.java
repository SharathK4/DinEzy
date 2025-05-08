package com.example.dinezyfinal;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements PopularFoodAdapter.OnFoodItemClickListener {

    private ViewPager2 bannerViewPager;
    private RecyclerView popularRecyclerView;
    private View dot1, dot2, dot3, dot4, dot5;
    private ImageView locationIcon, profileIcon;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        setupBanner();
        setupPopularFoods();
        setupListeners();
    }

    private void initViews() {
        bannerViewPager = findViewById(R.id.bannerViewPager);
        popularRecyclerView = findViewById(R.id.popularRecyclerView);
        dot1 = findViewById(R.id.dot1);
        dot2 = findViewById(R.id.dot2);
        dot3 = findViewById(R.id.dot3);
        dot4 = findViewById(R.id.dot4);
        dot5 = findViewById(R.id.dot5);
        locationIcon = findViewById(R.id.locationIcon);
        profileIcon = findViewById(R.id.profileIcon);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
    }

    private void setupBanner() {
        // Banner food images (resource IDs)
        List<Integer> bannerImages = Arrays.asList(
                R.drawable.pizza,
                R.drawable.burger,
                R.drawable.fries,
                R.drawable.salad,
                R.drawable.pasta
        );

        // Set up banner adapter
        BannerAdapter bannerAdapter = new BannerAdapter(bannerImages);
        bannerViewPager.setAdapter(bannerAdapter);
        
        // Add page transformer for better visual effects
        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(40));
        transformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });
        
        bannerViewPager.setPageTransformer(transformer);
        
        // Register page change callback to update dots
        bannerViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateDots(position);
            }
        });
    }

    private void setupPopularFoods() {
        // Create popular food items list
        List<FoodItem> popularFoods = new ArrayList<>();
        popularFoods.add(new FoodItem("Pepperoni Pizza", "$14.50", "pizza"));
        popularFoods.add(new FoodItem("Cheese Burger", "$12.99", "burger"));
        popularFoods.add(new FoodItem("French Fries", "$5.99", "fries"));
        popularFoods.add(new FoodItem("Caesar Salad", "$8.50", "salad"));
        popularFoods.add(new FoodItem("Pasta Carbonara", "$13.99", "pasta"));

        // Set up RecyclerView with horizontal layout
        PopularFoodAdapter adapter = new PopularFoodAdapter(popularFoods, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);
        popularRecyclerView.setLayoutManager(layoutManager);
        popularRecyclerView.setAdapter(adapter);
    }

    private void setupListeners() {
        locationIcon.setOnClickListener(v -> showLocationDialog());
        
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                // Already on home, do nothing
                return true;
            } else if (itemId == R.id.navigation_search) {
                // Handle search
                return true;
            } else if (itemId == R.id.navigation_cart) {
                // Navigate to cart
                navigateToCart();
                return true;
            } else if (itemId == R.id.navigation_profile) {
                // Handle profile
                return true;
            }
            return false;
        });
    }

    private void updateDots(int position) {
        dot1.setBackgroundResource(position == 0 ? R.drawable.dot_selected : R.drawable.dot_unselected);
        dot2.setBackgroundResource(position == 1 ? R.drawable.dot_selected : R.drawable.dot_unselected);
        dot3.setBackgroundResource(position == 2 ? R.drawable.dot_selected : R.drawable.dot_unselected);
        dot4.setBackgroundResource(position == 3 ? R.drawable.dot_selected : R.drawable.dot_unselected);
        dot5.setBackgroundResource(position == 4 ? R.drawable.dot_selected : R.drawable.dot_unselected);
    }

    private void showLocationDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_location_dialog);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        // Set dialog width to match parent
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        ImageView closeButton = dialog.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void navigateToCart() {
        Intent intent = new Intent(HomeActivity.this, CartActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFoodItemClick(FoodItem foodItem) {
        Intent intent = new Intent(HomeActivity.this, FoodDetailActivity.class);
        intent.putExtra("name", foodItem.getName());
        intent.putExtra("price", foodItem.getPrice());
        intent.putExtra("image", foodItem.getImage());
        startActivity(intent);
    }
} 