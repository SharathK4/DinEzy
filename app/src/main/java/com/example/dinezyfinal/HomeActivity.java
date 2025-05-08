package com.example.dinezyfinal;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.appbar.AppBarLayout;
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
    
    private static final long BANNER_SLIDE_DELAY = 4000; // 4 seconds delay
    private static final long BANNER_ANIMATION_TIME = 500; // 500ms for animation
    private Handler bannerSlideHandler;
    private Runnable bannerSlideRunnable;
    private static final int LOCATION_REQUEST_CODE = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        setupBanner();
        setupPopularFoods();
        setupListeners();
        setupAppBarScrollListener();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        startBannerAutoSlide();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        stopBannerAutoSlide();
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
        
        // Set offscreen page limit to avoid recreation
        bannerViewPager.setOffscreenPageLimit(3);
        
        // Reduce page transformer sensitivity
        bannerViewPager.setPageTransformer(new CompositePageTransformer());
        
        // Register page change callback to update dots
        bannerViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateDots(position);
            }
        });
        
        // Initialize banner auto-slide handler and runnable
        bannerSlideHandler = new Handler(Looper.getMainLooper());
        bannerSlideRunnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = bannerViewPager.getCurrentItem();
                int totalItems = bannerAdapter.getItemCount();
                int nextItem = (currentItem + 1) % totalItems;
                
                // Smooth scroll to next item with controlled animation time
                bannerViewPager.setCurrentItem(nextItem, true);
                
                bannerSlideHandler.postDelayed(this, BANNER_SLIDE_DELAY);
            }
        };
    }
    
    private void startBannerAutoSlide() {
        bannerSlideHandler.postDelayed(bannerSlideRunnable, BANNER_SLIDE_DELAY);
    }
    
    private void stopBannerAutoSlide() {
        bannerSlideHandler.removeCallbacks(bannerSlideRunnable);
    }

    private void setupPopularFoods() {
        // Create popular food items list
        List<FoodItem> popularFoods = new ArrayList<>();
        popularFoods.add(new FoodItem("Pepperoni Pizza", "$14.50", "pizza"));
        popularFoods.add(new FoodItem("Cheese Burger", "$12.99", "burger"));
        popularFoods.add(new FoodItem("French Fries", "$5.99", "fries"));
        popularFoods.add(new FoodItem("Caesar Salad", "$8.50", "salad"));
        popularFoods.add(new FoodItem("Pasta Carbonara", "$13.99", "pasta"));

        // Set up RecyclerView with grid layout (2 columns)
        PopularFoodAdapter adapter = new PopularFoodAdapter(popularFoods, this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        popularRecyclerView.setLayoutManager(layoutManager);
        popularRecyclerView.setAdapter(adapter);
    }

    private void setupListeners() {
        locationIcon.setOnClickListener(v -> showLocationDialog());
        
        profileIcon.setOnClickListener(v -> navigateToProfile());
        
        TextView viewAllText = findViewById(R.id.viewAllText);
        viewAllText.setOnClickListener(v -> {
            Toast.makeText(this, "Viewing all popular foods", Toast.LENGTH_SHORT).show();
            // In a real app, you would navigate to a view with all popular foods
        });
        
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                // Already on home, do nothing
                return true;
            } else if (itemId == R.id.navigation_search) {
                // Navigate to search
                navigateToSearch();
                return true;
            } else if (itemId == R.id.navigation_cart) {
                // Navigate to cart
                navigateToCart();
                return true;
            } else if (itemId == R.id.navigation_profile) {
                // Navigate to profile
                navigateToProfile();
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
        Intent intent = new Intent(HomeActivity.this, LocationActivity.class);
        startActivityForResult(intent, LOCATION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == LOCATION_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Get the selected location data
            double latitude = data.getDoubleExtra("latitude", 0);
            double longitude = data.getDoubleExtra("longitude", 0);
            String address = data.getStringExtra("address");
            
            // Save the location data (e.g., using SharedPreferences)
            saveLocationToPreferences(latitude, longitude, address);
            
            // Show a confirmation toast
            Toast.makeText(this, "Delivery location updated", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void saveLocationToPreferences(double latitude, double longitude, String address) {
        // Save location data to SharedPreferences
        getSharedPreferences("user_prefs", MODE_PRIVATE)
                .edit()
                .putFloat("latitude", (float) latitude)
                .putFloat("longitude", (float) longitude)
                .putString("address", address)
                .apply();
    }

    private void navigateToCart() {
        Intent intent = new Intent(HomeActivity.this, CartActivity.class);
        startActivity(intent);
    }

    private void navigateToSearch() {
        Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
        startActivity(intent);
    }
    
    private void navigateToProfile() {
        Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    private void setupAppBarScrollListener() {
        AppBarLayout appBarLayout = findViewById(R.id.appBarLayout);
        NestedScrollView nestedScrollView = findViewById(R.id.nestedScrollView);
        
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                // Calculate scroll percentage (0 to 1)
                float scrollPercentage = Math.abs(verticalOffset) / (float) appBarLayout.getTotalScrollRange();
                
                // When scrolled more than 75%, stop the banner auto-slide to save resources
                if (scrollPercentage > 0.75) {
                    stopBannerAutoSlide();
                } else if (scrollPercentage < 0.25 && bannerSlideHandler != null) {
                    // Resume when back at the top
                    startBannerAutoSlide();
                }
            }
        });
        
        // Find the Popular Foods header view
        TextView popularTitle = findViewById(R.id.popularTitle);
        
        if (popularTitle != null) {
            // When Popular Foods title is clicked, scroll to show popular foods
            popularTitle.setOnClickListener(v -> {
                appBarLayout.setExpanded(false, true);
            });
        }
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