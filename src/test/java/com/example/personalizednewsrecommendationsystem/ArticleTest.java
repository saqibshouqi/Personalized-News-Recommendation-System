package com.example.personalizednewsrecommendationsystem;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArticleTest {

    @Test
    public void testArticleCreation() {
        Article article = new Article(1, "Test Title", "Test Content", "Test Category");

        assertEquals(1, article.getId());
        assertEquals("Test Title", article.getTitle());
        assertEquals("Test Content", article.getContent());
        assertEquals("Test Category", article.getCategory());
    }

    @Test
    public void testToString() {
        Article article = new Article(2, "Another Title", "Content here", "Category");
        String expected = "Title: Another Title, Category: Category";

        assertEquals(expected, article.toString());
    }
}

