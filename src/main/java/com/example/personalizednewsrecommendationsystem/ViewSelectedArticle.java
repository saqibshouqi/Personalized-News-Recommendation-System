//package com.example.personalizednewsrecommendationsystem;
//
//import javafx.fxml.FXML;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextArea;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//
//public class ViewSelectedArticle {
//
//    @FXML
//    private Label titleLabel;
//
//    @FXML
//    private Label categoryLabel;
//
//    @FXML
//    private TextArea contentTextArea;
//
//    private Article selectedArticle;
//
//    public void initialize() {
//        // This method will run after FXML is loaded.
//    }
//
//    public void setArticle(Article article) {
//        this.selectedArticle = article;
//        titleLabel.setText(article.getTitle());
//        categoryLabel.setText(article.getCategory());
//        contentTextArea.setText(article.getContent());
//    }
//
//    @FXML
//    private void onLikeClick() {
//        updateCategoryPoints(1);
//    }
//
//    @FXML
//    private void onDislikeClick() {
//        updateCategoryPoints(-1);
//    }
//
//    @FXML
//    private void onSaveClick() {
//        try (Connection connection = DatabaseConnection.connect()) {
//            String query = "INSERT INTO SavedArticles (UserName, ArticleID) VALUES (?, ?)";
//            PreparedStatement stmt = connection.prepareStatement(query);
//            stmt.setString(1, SessionManager.getCurrentUsername());
//            stmt.setInt(2, selectedArticle.getId());
//            stmt.executeUpdate();
//            showAlert("Success", "Article saved successfully!", Alert.AlertType.INFORMATION);
//        } catch (Exception e) {
//            showAlert("Error", "Failed to save article: " + e.getMessage(), Alert.AlertType.ERROR);
//        }
//    }
//
//    private void updateCategoryPoints(int points) {
//        try (Connection connection = DatabaseConnection.connect()) {
//            String query = "UPDATE UserPoints SET Points = Points + ? WHERE UserName = ? AND Category = ?";
//            PreparedStatement stmt = connection.prepareStatement(query);
//            stmt.setInt(1, points);
//            stmt.setString(2, SessionManager.getCurrentUsername());
//            stmt.setString(3, selectedArticle.getCategory());
//            stmt.executeUpdate();
//
//            String message = points > 0 ? "Liked" : "Disliked";
//            showAlert("Success", "Article " + message + " successfully!", Alert.AlertType.INFORMATION);
//        } catch (Exception e) {
//            showAlert("Error", "Failed to update points: " + e.getMessage(), Alert.AlertType.ERROR);
//        }
//    }
//
//
//
//
//
//
//
//
//    private void showAlert(String title, String content, Alert.AlertType type) {
//        Alert alert = new Alert(type);
//        alert.setTitle(title);
//        alert.setContentText(content);
//        alert.showAndWait();
//    }
//}
//
//
//
//
//





package com.example.personalizednewsrecommendationsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ViewSelectedArticle {
    @FXML
    private Label articleTitleLabel, categoryLabel;

    @FXML
    private TextArea contentArea;

    @FXML
    private Button likeButton, dislikeButton, saveButton;

    private Article currentArticle;
    private String currentUsername;

    public void initialize() {
        currentUsername = SessionManager.getCurrentUsername(); // Assume this method works correctly
    }

    public void setArticle(Article article) {
        this.currentArticle = article;
        articleTitleLabel.setText(article.getTitle());
        categoryLabel.setText("Category: " + article.getCategory());
        contentArea.setText(article.getContent());
    }

    @FXML
    private void onLikeClick(ActionEvent event) {
        updateCategoryPoints(1);
    }

    @FXML
    private void onDislikeClick(ActionEvent event) {
        updateCategoryPoints(-1);
    }

    @FXML
    private void onSaveClick(ActionEvent event) {
        try (Connection connection = DatabaseConnection.connect()) {
            String query = "INSERT INTO SavedArticles (UserName, ArticleID) VALUES (?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, currentUsername);
            stmt.setInt(2, currentArticle.getId());
            stmt.executeUpdate();

            showAlert("Success", "Article saved successfully.", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Error", "Failed to save article: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void updateCategoryPoints(int delta) {
        try (Connection connection = DatabaseConnection.connect()) {
            // First, check if a record exists for the current user and category
            String checkQuery = "SELECT * FROM UserPoints WHERE UserName = ? AND Category = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setString(1, currentUsername);
            checkStmt.setString(2, currentArticle.getCategory());
            var resultSet = checkStmt.executeQuery();

            if (!resultSet.next()) {
                // Insert a new record if none exists
                String insertQuery = "INSERT INTO UserPoints (UserName, Category, Points) VALUES (?, ?, 0)";
                PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
                insertStmt.setString(1, currentUsername);
                insertStmt.setString(2, currentArticle.getCategory());
                insertStmt.executeUpdate();
            }

            // Now update the points
            String updateQuery = "UPDATE UserPoints SET Points = Points + ? WHERE UserName = ? AND Category = ?";
            PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
            updateStmt.setInt(1, delta);
            updateStmt.setString(2, currentUsername);
            updateStmt.setString(3, currentArticle.getCategory());
            updateStmt.executeUpdate();

            String action = delta > 0 ? "liked" : "disliked";
            showAlert("Success", "You have " + action + " the article.", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Error", "Failed to update points: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
