package com.example.personalizednewsrecommendationsystem;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import opennlp.tools.doccat.DoccatModel;

import java.io.IOException;

public class ArticleCategorizationTest {

    @Test
    public void testPredictCategory() throws IOException {
        DoccatModel model = ArticleCategorization.trainClassifier();  // Assuming a local model path works
        String category = ArticleCategorization.predictCategory(model, "Business growth and stock market updates");

        assertNotNull(category);
        assertTrue(category.length() > 0);  // Ensures prediction isn't empty
    }
}
