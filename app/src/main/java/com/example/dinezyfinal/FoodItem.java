package com.example.dinezyfinal;

public class FoodItem {
    private final String name;
    private final String price;
    private final String image;

    public FoodItem(String name, String price, String image) {
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }
} 