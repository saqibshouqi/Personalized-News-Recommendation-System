package com.example.personalizednewsrecommendationsystem;

import com.example.personalizednewsrecommendationsystem.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddArticle {

    @FXML
    private TextField categoryField, titleField;
    @FXML
    private TextArea contentField;

    @FXML
    private void onSubmitClick(ActionEvent event) {
        String category = categoryField.getText().trim();
        String title = titleField.getText().trim();
        String content = contentField.getText().trim();

        if (category.isEmpty() || title.isEmpty() || content.isEmpty()) {
            showAlert("Error", "All fields must be filled!", Alert.AlertType.ERROR);
            return;
        }

        try (Connection connection = DatabaseConnection.connect()) {
            String query = "INSERT INTO NewsArticles (category, title, content) VALUES (?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, category);
            stmt.setString(2, title);
            stmt.setString(3, content);

            stmt.executeUpdate();
            showAlert("Success", "Article added successfully!", Alert.AlertType.INFORMATION);
            // Close the current window
            categoryField.getScene().getWindow().hide();
        } catch (Exception e) {
            showAlert("Error", "Failed to add article: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
