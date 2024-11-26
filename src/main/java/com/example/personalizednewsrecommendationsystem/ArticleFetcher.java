package com.example.personalizednewsrecommendationsystem;

import java.util.ArrayList;
import java.util.List;

public class ArticleFetcher {
    public static List<Article> fetchArticles() {
        // This simulates fetching articles. Replace with DB/API logic.
        List<Article> articles = new ArrayList<>();
        articles.add(new Article("AI Revolution in 2024", "Content about AI", "AI"));
        articles.add(new Article("Health Benefits of Yoga", "Content about health", "HEALTH"));
        articles.add(new Article("Latest Sports Updates", "Content about sports", "SPORTS"));
        articles.add(new Article("Tech Trends to Watch", "Content about technology", "TECHNOLOGY"));
        return articles;
    }
}
