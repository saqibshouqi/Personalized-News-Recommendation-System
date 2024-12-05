package com.example.personalizednewsrecommendationsystem;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ViewAllNews {

    @FXML
    private ListView<Article> newsListView;

    public void initialize() {
        newsListView.setCellFactory(param -> new ListCell<Article>() {
            @Override
            protected void updateItem(Article article, boolean empty) {
                super.updateItem(article, empty);
                if (empty || article == null) {
                    setText(null);
                } else {
                    setText(article.getCategory() + " - " + article.getTitle());
                }
            }
        });

        loadNews();
    }

    void loadNews() {
        Main.getExecutorService().execute(() -> {
            List<Article> articles = new ArrayList<>();
            try (Connection connection = DatabaseConnection.connect()) {
                String query = "SELECT title, category FROM NewsArticles";
                PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    articles.add(new Article(
                            0, // ID is not needed here
                            rs.getString("title"),
                            "", // Content is not needed here
                            rs.getString("category")
                    ));
                }

                Platform.runLater(() -> newsListView.getItems().addAll(articles));
            } catch (Exception e) {
                Platform.runLater(() -> showAlert("Error", "Failed to load news: " + e.getMessage(), Alert.AlertType.ERROR));
            }
        });
    }

    @FXML
    private void onViewDetailsClick(ActionEvent actionEvent) {
        Article selectedNews = newsListView.getSelectionModel().getSelectedItem();
        if (selectedNews == null) {
            showAlert("Warning", "Please select a news article to view details.", Alert.AlertType.WARNING);
        } else {
            Main.getExecutorService().execute(() -> {
                try (Connection connection = DatabaseConnection.connect()) {
                    String query = "SELECT * FROM NewsArticles WHERE title = ?";
                    PreparedStatement stmt = connection.prepareStatement(query);
                    stmt.setString(1, selectedNews.getTitle());
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        Article article = new Article(
                                rs.getInt("id"),
                                rs.getString("title"),
                                rs.getString("content"),
                                rs.getString("category")
                        );

                        Platform.runLater(() -> openArticleView(article));
                    }
                } catch (Exception e) {
                    Platform.runLater(() -> showAlert("Error", "Failed to load article details: " + e.getMessage(), Alert.AlertType.ERROR));
                }
            });
        }
    }

    private void openArticleView(Article article) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view-selected-article.fxml"));
            Parent root = loader.load();
            ViewSelectedArticle controller = loader.getController();
            controller.setArticle(article);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("View Selected Article");
            stage.show();
        } catch (Exception e) {
            showAlert("Error", "Failed to open article view: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
