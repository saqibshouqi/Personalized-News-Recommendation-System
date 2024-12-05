//package com.example.personalizednewsrecommendationsystem;
//
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Scene;
//import javafx.scene.control.Alert;
//import javafx.scene.control.ListView;
//import javafx.stage.Stage;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.util.*;
//
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class RecommendedNews {
//
//    @FXML
//    private ListView<String> recommendedListView;
//
//    private String currentUsername;
//
//    private final ExecutorService executor = Executors.newCachedThreadPool(); // Dynamically scalable thread pool
//
//    public void initialize() {
//        currentUsername = SessionManager.getCurrentUsername(); // Assume this method exists
//        loadRecommendedNews();
//    }
//
//    private void loadRecommendedNews() {
//        Map<String, Integer> categoryPoints = new HashMap<>();
//
//        try (Connection connection = DatabaseConnection.connect()) {
//            String query = "SELECT Category, Points FROM UserPoints WHERE UserName = ?";
//            PreparedStatement stmt = connection.prepareStatement(query);
//            stmt.setString(1, SessionManager.getCurrentUsername()); // Use SessionManager to get the username
//            ResultSet rs = stmt.executeQuery();
//
//            while (rs.next()) {
//                categoryPoints.put(rs.getString("Category"), rs.getInt("Points"));
//            }
//
//            List<String> topCategories = categoryPoints.entrySet().stream()
//                    .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
//                    .limit(3)
//                    .map(Map.Entry::getKey)
//                    .toList();
//
//            if (topCategories.isEmpty()) {
//                showAlert("Info", "No recommendations available. Like or dislike articles to get recommendations.", Alert.AlertType.INFORMATION);
//                return;
//            }
//
//            for (int i = 0; i < topCategories.size(); i++) {
//                String category = topCategories.get(i);
//                int limit = i == 0 ? 5 : i == 1 ? 3 : 1;
//
//                // Dynamically build query with the LIMIT value
//                query = "SELECT title FROM NewsArticles WHERE category = ? LIMIT " + limit;
//                stmt = connection.prepareStatement(query);
//                stmt.setString(1, category);
//
//                rs = stmt.executeQuery();
//                while (rs.next()) {
//                    recommendedListView.getItems().add(rs.getString("title"));
//                }
//            }
//        } catch (Exception e) {
//            showAlert("Error", "Failed to load recommendations: " + e.getMessage(), Alert.AlertType.ERROR);
//        }
//    }
//
//
//
//
//
//
//
//    @FXML
//    private void onViewDetailsClick(ActionEvent actionEvent) {
//        String selectedTitle = recommendedListView.getSelectionModel().getSelectedItem();
//        if (selectedTitle == null) {
//            showAlert("Warning", "Please select a news article to view details.", Alert.AlertType.WARNING);
//            return;
//        }
//
//        try (Connection connection = DatabaseConnection.connect()) {
//            String query = "SELECT * FROM NewsArticles WHERE title = ?";
//            PreparedStatement stmt = connection.prepareStatement(query);
//            stmt.setString(1, selectedTitle);
//            ResultSet rs = stmt.executeQuery();
//
//            if (rs.next()) {
//                Article article = new Article(
//                        rs.getInt("id"),
//                        rs.getString("title"),
//                        rs.getString("content"),
//                        rs.getString("category")
//                );
//
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("view-selected-article.fxml"));
//                Stage stage = new Stage();
//                stage.setScene(new Scene(loader.load()));
//                stage.setTitle("View Article");
//
//                ViewSelectedArticle controller = loader.getController();
//                controller.setArticle(article);
//
//                stage.show();
//            }
//        } catch (Exception e) {
//            showAlert("Error", "Failed to load article details: " + e.getMessage(), Alert.AlertType.ERROR);
//        }
//    }
//
//    private void showAlert(String title, String content, Alert.AlertType type) {
//        Alert alert = new Alert(type);
//        alert.setTitle(title);
//        alert.setContentText(content);
//        alert.showAndWait();
//    }
//}





package com.example.personalizednewsrecommendationsystem;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecommendedNews {

    @FXML
    private ListView<String> recommendedListView;

    private String currentUsername;

    private final ExecutorService executor = Executors.newCachedThreadPool(); // Dynamically scalable thread pool

    public void initialize() {
        currentUsername = SessionManager.getCurrentUsername();
        loadRecommendedNews();
    }

    void loadRecommendedNews() {
        executor.execute(() -> {
            Map<String, Integer> categoryPoints = new HashMap<>();

            try (Connection connection = DatabaseConnection.connect()) {
                String query = "SELECT Category, Points FROM UserPoints WHERE UserName = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, currentUsername);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    categoryPoints.put(rs.getString("Category"), rs.getInt("Points"));
                }

                List<String> topCategories = categoryPoints.entrySet().stream()
                        .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                        .limit(3)
                        .map(Map.Entry::getKey)
                        .toList();

                if (topCategories.isEmpty()) {
                    Platform.runLater(() -> showAlert("Info", "No recommendations available. Like or dislike articles to get recommendations.", Alert.AlertType.INFORMATION));
                    return;
                }

                List<String> recommendations = new ArrayList<>();

                for (int i = 0; i < topCategories.size(); i++) {
                    String category = topCategories.get(i);
                    int limit = i == 0 ? 5 : i == 1 ? 3 : 1;

                    query = "SELECT title FROM NewsArticles WHERE category = ? LIMIT " + limit;
                    stmt = connection.prepareStatement(query);
                    stmt.setString(1, category);

                    rs = stmt.executeQuery();
                    while (rs.next()) {
                        recommendations.add(rs.getString("title"));
                    }
                }

                Platform.runLater(() -> recommendedListView.getItems().addAll(recommendations));
            } catch (Exception e) {
                Platform.runLater(() -> showAlert("Error", "Failed to load recommendations: " + e.getMessage(), Alert.AlertType.ERROR));
            }
        });
    }

    @FXML
    private void onViewDetailsClick(ActionEvent actionEvent) {
        String selectedTitle = recommendedListView.getSelectionModel().getSelectedItem();
        if (selectedTitle == null) {
            showAlert("Warning", "Please select a news article to view details.", Alert.AlertType.WARNING);
            return;
        }

        executor.execute(() -> {
            try (Connection connection = DatabaseConnection.connect()) {
                String query = "SELECT * FROM NewsArticles WHERE title = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, selectedTitle);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    Article article = new Article(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("content"),
                            rs.getString("category")
                    );

                    Platform.runLater(() -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("view-selected-article.fxml"));
                            Stage stage = new Stage();
                            stage.setScene(new Scene(loader.load()));
                            stage.setTitle("View Article");

                            ViewSelectedArticle controller = loader.getController();
                            controller.setArticle(article);

                            stage.show();
                        } catch (Exception e) {
                            showAlert("Error", "Failed to load article details: " + e.getMessage(), Alert.AlertType.ERROR);
                        }
                    });
                }
            } catch (Exception e) {
                Platform.runLater(() -> showAlert("Error", "Failed to load article details: " + e.getMessage(), Alert.AlertType.ERROR));
            }
        });
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Ensure the executor is properly shut down when no longer needed
    public void shutdownExecutor() {
        executor.shutdown();
    }
}
