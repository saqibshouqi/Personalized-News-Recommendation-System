package com.example.personalizednewsrecommendationsystem;

import java.util.ArrayList;
import java.util.List;

public class RecommendationEngine {
    public List<Article> recommendArticles(User user, List<Article> allArticles) {
        List<Article> recommendations = new ArrayList<>();
        Category favoriteCategory = user.getPreferences().getFavoriteCategory();

        for (Article article : allArticles) {
            if (Category.valueOf(article.getCategory().toUpperCase()) == favoriteCategory) {
                recommendations.add(article);
            }
        }

        return recommendations;
    }
}
