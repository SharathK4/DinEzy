package com.example.dinezyfinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FoodDetailActivity extends AppCompatActivity {

    private ImageButton backButton, favoriteButton;
    private Button decreaseButton, increaseButton, addToCartButton;
    private TextView quantityText, priceText, descriptionText;
    private ImageView foodImage;
    
    private int quantity = 1;
    private String foodName, foodPrice, imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        // Get data from intent
        foodName = getIntent().getStringExtra("name");
        foodPrice = getIntent().getStringExtra("price");
        imageName = getIntent().getStringExtra("image");

        initViews();
        setupListeners();
        displayFoodDetails();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        favoriteButton = findViewById(R.id.favoriteButton);
        decreaseButton = findViewById(R.id.decreaseButton);
        increaseButton = findViewById(R.id.increaseButton);
        addToCartButton = findViewById(R.id.addToCartButton);
        quantityText = findViewById(R.id.quantityText);
        priceText = findViewById(R.id.priceText);
        descriptionText = findViewById(R.id.descriptionText);
        foodImage = findViewById(R.id.foodImage);
    }

    private void setupListeners() {
        backButton.setOnClickListener(v -> finish());

        favoriteButton.setOnClickListener(v -> {
            Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
        });

        decreaseButton.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                updateQuantity();
            }
        });

        increaseButton.setOnClickListener(v -> {
            quantity++;
            updateQuantity();
        });

        addToCartButton.setOnClickListener(v -> {
            Toast.makeText(this, quantity + " " + foodName + " added to cart", Toast.LENGTH_SHORT).show();
            navigateToCart();
        });
    }

    private void displayFoodDetails() {
        // In a real app, you would load the image from a resource or URL
        // Example: loading image based on the image name received from intent
        if (imageName != null) {
            try {
                int resourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                if (resourceId != 0) {
                    foodImage.setImageResource(resourceId);
                }
            } catch (Exception e) {
                // If the resource doesn't exist, use a default image or handle the exception
                foodImage.setImageResource(R.drawable.burger);
            }
        }
        
        priceText.setText(foodPrice);
        
        // In a real app, you would get this description from a database or API
        descriptionText.setText("Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when.");
    }

    private void updateQuantity() {
        quantityText.setText(String.valueOf(quantity));
    }

    private void navigateToCart() {
        Intent intent = new Intent(FoodDetailActivity.this, CartActivity.class);
        startActivity(intent);
    }
} 