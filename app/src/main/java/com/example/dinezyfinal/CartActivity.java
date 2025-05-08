package com.example.dinezyfinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;

public class CartActivity extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private Button paymentButton;
    private TextView totalPrice;
    private List<CartItem> cartItems;
    private CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initViews();
        setupCartItems();
        setupListeners();
    }

    private void initViews() {
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        paymentButton = findViewById(R.id.paymentButton);
        totalPrice = findViewById(R.id.totalPrice);
    }

    private void setupCartItems() {
        // Create mock cart data
        cartItems = new ArrayList<>();
        cartItems.add(new CartItem("French Fries", 4.50, 1, "fries"));
        cartItems.add(new CartItem("Pizza", 6.75, 1, "pizza"));
        cartItems.add(new CartItem("Salad", 3.25, 1, "salad"));

        // Set up RecyclerView
        adapter = new CartAdapter(cartItems);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(adapter);

        // Calculate total
        updateTotal();
    }

    private void setupListeners() {
        paymentButton.setOnClickListener(v -> navigateToOrderConfirmation());
    }

    private void updateTotal() {
        double total = 0.0;
        
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        
        DecimalFormat df = new DecimalFormat("0.00");
        totalPrice.setText("$ " + df.format(total));
    }

    private void navigateToOrderConfirmation() {
        Intent intent = new Intent(CartActivity.this, OrderConfirmationActivity.class);
        startActivity(intent);
    }

    // Model class for cart items
    static class CartItem {
        private final String name;
        private final double price;
        private int quantity;
        private final String image;

        CartItem(String name, double price, int quantity, String image) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
            this.image = image;
        }

        String getName() {
            return name;
        }

        String getFormattedPrice() {
            DecimalFormat df = new DecimalFormat("0.00");
            return "$ " + df.format(price);
        }
        
        double getPrice() {
            return price;
        }
        
        double getTotalPrice() {
            return price * quantity;
        }

        int getQuantity() {
            return quantity;
        }

        void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        String getImage() {
            return image;
        }
    }

    // Adapter for cart items
    class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

        private final List<CartItem> cartItems;

        CartAdapter(List<CartItem> cartItems) {
            this.cartItems = cartItems;
        }

        @NonNull
        @Override
        public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_cart, parent, false);
            return new CartViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
            CartItem cartItem = cartItems.get(position);
            holder.foodName.setText(cartItem.getName());
            holder.foodPrice.setText(cartItem.getFormattedPrice());
            holder.quantityText.setText(String.valueOf(cartItem.getQuantity()));
            
            // In a real app, you would load images from resources or URLs
            // For simplicity, we're not handling images here

            holder.decreaseButton.setOnClickListener(v -> {
                if (cartItem.getQuantity() > 1) {
                    cartItem.setQuantity(cartItem.getQuantity() - 1);
                    holder.quantityText.setText(String.valueOf(cartItem.getQuantity()));
                    updateTotal();
                }
            });

            holder.increaseButton.setOnClickListener(v -> {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                holder.quantityText.setText(String.valueOf(cartItem.getQuantity()));
                updateTotal();
            });
        }

        @Override
        public int getItemCount() {
            return cartItems.size();
        }

        class CartViewHolder extends RecyclerView.ViewHolder {
            TextView foodName, foodPrice, quantityText;
            ImageView foodImage;
            ImageButton decreaseButton, increaseButton;

            CartViewHolder(@NonNull View itemView) {
                super(itemView);
                foodName = itemView.findViewById(R.id.foodName);
                foodPrice = itemView.findViewById(R.id.foodPrice);
                quantityText = itemView.findViewById(R.id.quantityText);
                foodImage = itemView.findViewById(R.id.foodImage);
                decreaseButton = itemView.findViewById(R.id.decreaseButton);
                increaseButton = itemView.findViewById(R.id.increaseButton);
            }
        }
    }
} 