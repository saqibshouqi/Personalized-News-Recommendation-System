//package com.example.personalizednewsrecommendationsystem;
//
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.scene.control.ListView;
//import javafx.scene.control.PasswordField;
//import javafx.scene.control.TextField;
//
//public class ManageProfile {
//
//    @FXML
//    private ListView savedArticlesList;
//
//    @FXML
//    private TextField usernameField, newPasswordField, confirmPasswordField;
//
//    @FXML
//    private void onUpdatePasswordClick(ActionEvent actionEvent) {
//    }
//}
//
//
//



package com.example.personalizednewsrecommendationsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

