package com.example.personalizednewsrecommendationsystem;

import java.util.HashMap;
import java.util.Map;

public class UserPreferences {
    private Map<Category, Integer> categoryScores;

    public UserPreferences() {
        categoryScores = new HashMap<>();
        for (Category category : Category.values()) {
            categoryScores.put(category, 0);
        }
    }

    public void incrementCategoryScore(Category category) {
        categoryScores.put(category, categoryScores.getOrDefault(category, 0) + 1);
    }

    public Category getFavoriteCategory() {
        return categoryScores.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(() -> new IllegalStateException("No preferences found"))
                .getKey();
    }
}
