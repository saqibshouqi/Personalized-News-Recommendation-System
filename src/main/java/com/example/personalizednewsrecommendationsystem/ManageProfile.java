package com.example.personalizednewsrecommendationsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ManageProfile {

    @FXML
    private ListView<String> savedArticlesList;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField newPasswordField, confirmPasswordField;

    private String currentUsername;

    public void initialize() {
        currentUsername = SessionManager.getCurrentUsername(); // Assume this method exists to get the current username.
        usernameField.setText(currentUsername);
        loadSavedArticles();
    }

    private void loadSavedArticles() {
        try (Connection connection = DatabaseConnection.connect()) {
            String query = "SELECT NewsArticles.title FROM SavedArticles " +
                    "JOIN NewsArticles ON SavedArticles.ArticleID = NewsArticles.id " +
                    "WHERE SavedArticles.UserName = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, currentUsername);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                savedArticlesList.getItems().add(rs.getString("title"));
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load saved articles: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onUpdatePasswordClick(ActionEvent actionEvent) {
        String newPassword = newPasswordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        if (newPassword.length() < 5 || newPassword.length() > 20) {
            showAlert("Error", "Password must be between 5 and 20 characters.", Alert.AlertType.ERROR);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match!", Alert.AlertType.ERROR);
            return;
        }

        try (Connection connection = DatabaseConnection.connect()) {
            String updateQuery = "UPDATE UserCredentials SET UserPassword = ? WHERE UserName = ?";
            PreparedStatement stmt = connection.prepareStatement(updateQuery);
            stmt.setString(1, newPassword);
            stmt.setString(2, currentUsername);
            stmt.executeUpdate();

            showAlert("Success", "Password updated successfully!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Error", "Failed to update password: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }



//    @FXML
//    private void onViewArticleClick(ActionEvent event) {
//        String selectedTitle = savedArticlesList.getSelectionModel().getSelectedItem();
//
//        if (selectedTitle == null) {
//            showAlert("Warning", "Please select an article to view.", Alert.AlertType.WARNING);
//            return;
//        }
//
//        try (Connection connection = DatabaseConnection.connect()) {
//            String query = "SELECT id, category, title, content FROM NewsArticles WHERE title = ?";
//            PreparedStatement stmt = connection.prepareStatement(query);
//            stmt.setString(1, selectedTitle);
//            ResultSet rs = stmt.executeQuery();
//
//            if (rs.next()) {
//                Article article = new Article(
//                        rs.getInt("id"),
//                        rs.getString("category"),
//                        rs.getString("title"),
//                        rs.getString("content")
//                );
//
//                // Load and set article in ViewSelectedArticle
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("view-selected-article.fxml"));
//                Parent viewArticleRoot = loader.load();
//
//                ViewSelectedArticle controller = loader.getController();
//                controller.setArticle(article);
//
//                Stage stage = (Stage) savedArticlesList.getScene().getWindow();
//                stage.setScene(new Scene(viewArticleRoot));
//            } else {
//                showAlert("Error", "Article not found.", Alert.AlertType.ERROR);
//            }
//        } catch (Exception e) {
//            showAlert("Error", "Failed to load the article: " + e.getMessage(), Alert.AlertType.ERROR);
//        }
//    }


    @FXML
    private void onViewArticleClick(ActionEvent event) {
        String selectedTitle = savedArticlesList.getSelectionModel().getSelectedItem();

        if (selectedTitle == null) {
            showAlert("Warning", "Please select an article to view.", Alert.AlertType.WARNING);
            return;
        }

        try (Connection connection = DatabaseConnection.connect()) {
            String query = "SELECT id, category, title, content FROM NewsArticles WHERE title = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, selectedTitle);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Article article = new Article(rs.getInt("id"), rs.getString("title"), rs.getString("content"), rs.getString("category"));
                SessionManager.setSelectedArticleTitle(selectedTitle);

                // Load view-selected-article.fxml with NavigationHelper
                FXMLLoader loader = new FXMLLoader(getClass().getResource("view-selected-article.fxml"));
                Parent root = loader.load();

                // Pass article to ViewSelectedArticle controller
                ViewSelectedArticle controller = loader.getController();
                controller.setArticle(article);

                Stage stage = (Stage) savedArticlesList.getScene().getWindow();
                stage.setScene(new Scene(root));
            } else {
                showAlert("Error", "Article not found.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load the article: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }



    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }



}

