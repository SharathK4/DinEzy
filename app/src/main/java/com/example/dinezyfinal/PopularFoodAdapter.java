package com.example.dinezyfinal;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class PopularFoodAdapter extends RecyclerView.Adapter<PopularFoodAdapter.FoodViewHolder> {

    private final List<FoodItem> foodItems;
    private final OnFoodItemClickListener listener;
    private final List<String> backgroundColors = Arrays.asList(
            "#FFF6E5", "#F2FCE4", "#ECFBFF", "#FEE5E7", "#E9F9F0"
    );

    public interface OnFoodItemClickListener {
        void onFoodItemClick(FoodItem foodItem);
    }

    public PopularFoodAdapter(List<FoodItem> foodItems, OnFoodItemClickListener listener) {
        this.foodItems = foodItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_popular_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem foodItem = foodItems.get(position);
        holder.foodName.setText(foodItem.getName());
        holder.foodPrice.setText(foodItem.getPrice());
        
        // Set the food image
        Context context = holder.itemView.getContext();
        int imageResourceId = context.getResources().getIdentifier(
                foodItem.getImage(), "drawable", context.getPackageName());
        if (imageResourceId != 0) {
            holder.foodImage.setImageResource(imageResourceId);
        }
        
        // Set card background color
        String colorHex = backgroundColors.get(position % backgroundColors.size());
        holder.cardView.setCardBackgroundColor(Color.parseColor(colorHex));
        
        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFoodItemClick(foodItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, foodPrice;
        ImageView foodImage;
        CardView cardView;

        FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            foodImage = itemView.findViewById(R.id.foodImage);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
} 