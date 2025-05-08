package com.example.dinezyfinal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FoodItem {
    private final String name;
    private final String price;
    private final String image;
    private final List<String> tags;
    private final String description;

    public FoodItem(String name, String price, String image) {
        this(name, price, image, "", null);
    }

    public FoodItem(String name, String price, String image, String description, List<String> tags) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.description = description != null ? description : "";
        this.tags = tags != null ? tags : generateDefaultTags(name);
    }

    private List<String> generateDefaultTags(String name) {
        // Generate default tags based on the name
        List<String> defaultTags = new ArrayList<>();
        defaultTags.add(name.toLowerCase());
        
        // Extract words from name and add as individual tags
        String[] words = name.toLowerCase().split("\\s+");
        defaultTags.addAll(Arrays.asList(words));
        
        // Add food category based on name
        if (name.toLowerCase().contains("pizza")) {
            defaultTags.add("pizza");
            defaultTags.add("italian");
        } else if (name.toLowerCase().contains("burger")) {
            defaultTags.add("burger");
            defaultTags.add("fast food");
        } else if (name.toLowerCase().contains("salad")) {
            defaultTags.add("salad");
            defaultTags.add("healthy");
            defaultTags.add("vegetarian");
        } else if (name.toLowerCase().contains("pasta")) {
            defaultTags.add("pasta");
            defaultTags.add("italian");
        } else if (name.toLowerCase().contains("fries")) {
            defaultTags.add("fries");
            defaultTags.add("sides");
            defaultTags.add("fast food");
        }
        
        return defaultTags;
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
    
    public List<String> getTags() {
        return tags;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean matchesQuery(String query) {
        if (query == null || query.trim().isEmpty()) {
            return true;
        }
        
        String lowercaseQuery = query.toLowerCase().trim();
        
        // Check if the query matches name
        if (name.toLowerCase().contains(lowercaseQuery)) {
            return true;
        }
        
        // Check if the query matches description
        if (description.toLowerCase().contains(lowercaseQuery)) {
            return true;
        }
        
        // Check if the query matches any tag
        for (String tag : tags) {
            if (tag.contains(lowercaseQuery)) {
                return true;
            }
        }
        
        return false;
    }
} 