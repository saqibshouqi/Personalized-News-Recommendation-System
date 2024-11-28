package com.example.personalizednewsrecommendationsystem;

public class Article {
    private int id;
    private String title;
    private String content;
    private String category;

    public Article(int id, String title, String content, String category) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Category: " + category;
    }

}
