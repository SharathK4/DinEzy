package com.example.dinezyfinal;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

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
        setupFoodItems();
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
        // In a real app, you would set up a banner adapter with real data
        bannerViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateDots(position);
            }
        });
    }

    private void setupFoodItems() {
        // Create mock food data
        List<FoodItem> foodItems = new ArrayList<>();
        foodItems.add(new FoodItem("French Fries", "$14.50", "fries"));
        foodItems.add(new FoodItem("Pizza", "$14.50", "pizza"));
        foodItems.add(new FoodItem("Salad", "$14.50", "salad"));
        foodItems.add(new FoodItem("Pasta", "$14.50", "pasta"));

        // Set up RecyclerView
        FoodAdapter adapter = new FoodAdapter(foodItems);
        popularRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
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

    private void navigateToFoodDetail(FoodItem foodItem) {
        Intent intent = new Intent(HomeActivity.this, FoodDetailActivity.class);
        intent.putExtra("name", foodItem.getName());
        intent.putExtra("price", foodItem.getPrice());
        intent.putExtra("image", foodItem.getImage());
        startActivity(intent);
    }

    // Model class for food items
    static class FoodItem {
        private final String name;
        private final String price;
        private final String image;

        FoodItem(String name, String price, String image) {
            this.name = name;
            this.price = price;
            this.image = image;
        }

        String getName() {
            return name;
        }

        String getPrice() {
            return price;
        }

        String getImage() {
            return image;
        }
    }

    // Adapter for food items
    class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

        private final List<FoodItem> foodItems;

        FoodAdapter(List<FoodItem> foodItems) {
            this.foodItems = foodItems;
        }

        @NonNull
        @Override
        public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_food, parent, false);
            return new FoodViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
            FoodItem foodItem = foodItems.get(position);
            holder.foodName.setText(foodItem.getName());
            holder.foodPrice.setText(foodItem.getPrice());
            
            // In a real app, you would load images from resources or URLs
            // For simplicity, we're not handling images here

            holder.itemView.setOnClickListener(v -> navigateToFoodDetail(foodItem));
        }

        @Override
        public int getItemCount() {
            return foodItems.size();
        }

        class FoodViewHolder extends RecyclerView.ViewHolder {
            TextView foodName, foodPrice;
            ImageView foodImage;

            FoodViewHolder(@NonNull View itemView) {
                super(itemView);
                foodName = itemView.findViewById(R.id.foodName);
                foodPrice = itemView.findViewById(R.id.foodPrice);
                foodImage = itemView.findViewById(R.id.foodImage);
            }
        }
    }
} 