package com.example.personalizednewsrecommendationsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewAllNews {

    @FXML
    private ListView<String> newsListView;

    public void initialize() {
        loadNews();
    }

    private void loadNews() {
        try (Connection connection = DatabaseConnection.connect()) {
            String query = "SELECT title FROM NewsArticles";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                newsListView.getItems().add(rs.getString("title"));
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load news: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onViewDetailsClick(ActionEvent actionEvent) {
        String selectedNews = newsListView.getSelectionModel().getSelectedItem();
        if (selectedNews == null) {
            showAlert("Warning", "Please select a news article to view details.", Alert.AlertType.WARNING);
        } else {
            try (Connection connection = DatabaseConnection.connect()) {
                String query = "SELECT * FROM NewsArticles WHERE title = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, selectedNews);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    Article article = new Article(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("content"),
                            rs.getString("category")
                    );

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("view-selected-article.fxml"));
                    Parent root = loader.load();
                    ViewSelectedArticle controller = loader.getController();
                    controller.setArticle(article);

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("View Selected Article");
                    stage.show();
                }
            } catch (Exception e) {
                showAlert("Error", "Failed to load article details: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }



    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}


