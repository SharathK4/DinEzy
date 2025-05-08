package com.example.dinezyfinal;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements PopularFoodAdapter.OnFoodItemClickListener {

    private EditText searchEditText;
    private ImageView backButton;
    private RecyclerView searchResultsRecyclerView;
    private TextView noResultsText;
    private List<FoodItem> allFoodItems;
    private PopularFoodAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViews();
        setupData();
        setupListeners();
    }

    private void initViews() {
        searchEditText = findViewById(R.id.searchEditText);
        backButton = findViewById(R.id.backButton);
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);
        noResultsText = findViewById(R.id.noResultsText);
        
        // Setup RecyclerView with grid layout (2 columns)
        searchResultsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private void setupData() {
        // Initialize with sample data
        allFoodItems = createSampleFoodItems();
        
        // Setup adapter with all items initially
        adapter = new PopularFoodAdapter(allFoodItems, this);
        searchResultsRecyclerView.setAdapter(adapter);
    }

    private void setupListeners() {
        // Back button click listener
        backButton.setOnClickListener(v -> finish());
        
        // Search text changed listener
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filter items based on search query
                filterItems(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });
    }

    private void filterItems(String query) {
        List<FoodItem> filteredList = new ArrayList<>();
        
        for (FoodItem item : allFoodItems) {
            if (item.matchesQuery(query)) {
                filteredList.add(item);
            }
        }
        
        // Update adapter with filtered items
        adapter = new PopularFoodAdapter(filteredList, this);
        searchResultsRecyclerView.setAdapter(adapter);
        
        // Show/hide no results text
        if (filteredList.isEmpty()) {
            noResultsText.setVisibility(View.VISIBLE);
            searchResultsRecyclerView.setVisibility(View.GONE);
        } else {
            noResultsText.setVisibility(View.GONE);
            searchResultsRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private List<FoodItem> createSampleFoodItems() {
        List<FoodItem> items = new ArrayList<>();
        
        // Add sample food items with tags
        items.add(new FoodItem(
                "Pepperoni Pizza",
                "$14.50",
                "pizza",
                "Classic pepperoni pizza with mozzarella cheese and tomato sauce",
                Arrays.asList("pizza", "pepperoni", "cheese", "italian", "popular", "spicy")
        ));
        
        items.add(new FoodItem(
                "Cheese Burger",
                "$12.99",
                "burger",
                "Juicy beef patty with cheese, lettuce, tomato, and special sauce",
                Arrays.asList("burger", "beef", "cheese", "fast food", "popular")
        ));
        
        items.add(new FoodItem(
                "French Fries",
                "$5.99",
                "fries",
                "Crispy golden fries with sea salt",
                Arrays.asList("fries", "potatoes", "sides", "vegetarian", "vegan")
        ));
        
        items.add(new FoodItem(
                "Caesar Salad",
                "$8.50",
                "salad",
                "Fresh romaine lettuce with Caesar dressing, croutons, and parmesan cheese",
                Arrays.asList("salad", "healthy", "vegetarian", "light")
        ));
        
        items.add(new FoodItem(
                "Pasta Carbonara",
                "$13.99",
                "pasta",
                "Creamy pasta with bacon, eggs, and parmesan cheese",
                Arrays.asList("pasta", "italian", "creamy", "bacon", "dinner")
        ));
        
        items.add(new FoodItem(
                "Vegetable Stir Fry",
                "$10.99",
                "stirfry",
                "Mixed vegetables stir-fried in a savory sauce",
                Arrays.asList("vegetarian", "vegan", "healthy", "asian", "vegetables")
        ));
        
        items.add(new FoodItem(
                "Chicken Wings",
                "$11.50",
                "wings",
                "Crispy chicken wings with choice of sauce",
                Arrays.asList("chicken", "wings", "spicy", "appetizer", "finger food")
        ));
        
        items.add(new FoodItem(
                "Margherita Pizza",
                "$13.99",
                "pizza",
                "Classic pizza with tomato, mozzarella, and basil",
                Arrays.asList("pizza", "vegetarian", "italian", "cheese", "classic")
        ));
        
        return items;
    }

    @Override
    public void onFoodItemClick(FoodItem foodItem) {
        // Handle food item click (same as in HomeActivity)
        // You can navigate to FoodDetailActivity or show details dialog
    }
} 