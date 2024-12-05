package com.example.personalizednewsrecommendationsystem;

import com.example.personalizednewsrecommendationsystem.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import opennlp.tools.doccat.DoccatModel;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddArticle {

    @FXML
    private TextField  titleField;
    @FXML
    private TextArea contentField;


    @FXML
    private void onSubmitClick(ActionEvent event) {
        String title = titleField.getText().trim();
        String content = contentField.getText().trim();

        if (title.isEmpty() || content.isEmpty()) {
            showAlert("Error", "Title and content must be filled!", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Predict category
            DoccatModel model = ArticleCategorization.loadTrainedModel(); // Add a method to load model in ArticleCategorization
            String predictedCategory = ArticleCategorization.predictCategory(model, title);
            showAlert("Success", "Article category predicted!", Alert.AlertType.INFORMATION);

            // Insert article into the database
            try (Connection connection = DatabaseConnection.connect()) {
                String query = "INSERT INTO NewsArticles (category, title, content) VALUES (?, ?, ?)";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, predictedCategory);
                stmt.setString(2, title);
                stmt.setString(3, content);

                stmt.executeUpdate();
                showAlert("Success", "Article added successfully!", Alert.AlertType.INFORMATION);
                titleField.getScene().getWindow().hide();
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to add article: " + e.getMessage(), Alert.AlertType.ERROR);
            System.out.println(e.getMessage());
        }
    }






    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
