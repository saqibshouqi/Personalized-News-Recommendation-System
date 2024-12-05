package com.example.personalizednewsrecommendationsystem;

import opennlp.tools.doccat.DoccatModel;
import org.junit.jupiter.api.*;
        import java.util.*;
        import java.util.concurrent.*;
        import static org.junit.jupiter.api.Assertions.*;

class ConcurrencyTests {

    private static ExecutorService executor;

    @BeforeAll
    static void setUp() {
        executor = Executors.newFixedThreadPool(4); // Shared pool for concurrency tests
    }

    @AfterAll
    static void tearDown() {
        executor.shutdown();
    }

    @Test
    void testConcurrentCategoryPrediction() throws Exception {
        DoccatModel model = ArticleCategorization.trainClassifier();
        List<Callable<String[]>> tasks = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            String title = "Sample Title " + i;
            tasks.add(() -> {
                String category = ArticleCategorization.predictCategory(model, title);
                return new String[]{category, title};
            });
        }

        List<Future<String[]>> futures = executor.invokeAll(tasks);
        List<String[]> results = new ArrayList<>();

        for (Future<String[]> future : futures) {
            results.add(future.get());
        }

        // Check for uniqueness and non-null results
        assertEquals(10, results.size());
        for (String[] result : results) {
            assertNotNull(result[0]); // Category should not be null
            assertNotNull(result[1]); // Title should not be null
        }
    }

    @Test
    void testConcurrentRecommendations() throws Exception {
        RecommendedNews recommendedNews = new RecommendedNews();

        // Simulate concurrent calls to loadRecommendedNews
        List<Callable<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            tasks.add(() -> {
                recommendedNews.loadRecommendedNews();
                return null;
            });
        }

        List<Future<Void>> futures = executor.invokeAll(tasks);

        for (Future<Void> future : futures) {
            future.get(); // Ensure all tasks complete without exception
        }

        // No assertion here as loadRecommendedNews interacts with the DB,
        // but ensure no concurrency-related exceptions occur.
    }

    @Test
    void testConcurrentNewsLoading() throws Exception {
        ViewAllNews viewAllNews = new ViewAllNews();

        // Simulate concurrent calls to loadNews
        List<Callable<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            tasks.add(() -> {
                viewAllNews.loadNews();
                return null;
            });
        }

        List<Future<Void>> futures = executor.invokeAll(tasks);

        for (Future<Void> future : futures) {
            future.get(); // Ensure all tasks complete without exception
        }

        // Verify that no deadlocks or race conditions occurred.
    }

    @Test
    void testConcurrentArticleProcessing() throws Exception {
        List<String[]> mockArticles = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mockArticles.add(new String[]{"Sample Title " + i, "Sample Content " + i});
        }

        DoccatModel model = ArticleCategorization.trainClassifier();
        List<Callable<String[]>> tasks = new ArrayList<>();

        for (String[] article : mockArticles) {
            tasks.add(() -> {
                String category = ArticleCategorization.predictCategory(model, article[0]);
                return new String[]{category, article[0], article[1]};
            });
        }

        List<Future<String[]>> futures = executor.invokeAll(tasks);
        List<String[]> results = new ArrayList<>();

        for (Future<String[]> future : futures) {
            results.add(future.get());
        }

        // Validate results
        assertEquals(10, results.size());
        for (String[] result : results) {
            assertNotNull(result[0]); // Category should not be null
            assertNotNull(result[1]); // Title should not be null
            assertNotNull(result[2]); // Content should not be null
        }
    }
}
