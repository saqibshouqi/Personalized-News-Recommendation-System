package com.example.personalizednewsrecommendationsystem;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;


import java.sql.Connection;
import java.sql.PreparedStatement;

public class UpdateArticle {

    @FXML
    private TextField  titleField;

    @FXML
    private ChoiceBox<String> categoryChoiceBox;

    @FXML
    private TextArea contentField;

    private Article selectedArticle;



    public void setArticle(Article article) {
        this.selectedArticle = article;

        categoryChoiceBox.getItems().addAll(
                "BLACK-VOICES", "BUSINESS", "COMEDY", "ENTERTAINMENT", "FOOD&DRINK",
                "HEALTHY-LIVING", "HOME&LIVING", "PARENTING", "PARENTS", "POLITICS",
                "QUEER-VOICES", "SPORTS", "STYLE&BEAUTY", "TRAVEL", "WELLNESS", "OTHERS"
        );
        categoryChoiceBox.setValue(article.getCategory());

        titleField.setText(article.getTitle());
        contentField.setText(article.getContent());
    }





    @FXML
    private void onUpdateClick(ActionEvent event) {
        String category = categoryChoiceBox.getValue().trim();
        String title = titleField.getText().trim();
        String content = contentField.getText().trim();

        if (category.isEmpty() || title.isEmpty() || content.isEmpty()) {
            showAlert("Error", "All fields must be filled!", Alert.AlertType.ERROR);
            return;
        }

        try (Connection connection = DatabaseConnection.connect()) {
            String query = "UPDATE NewsArticles SET category = ?, title = ?, content = ? WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, category);
            stmt.setString(2, title);
            stmt.setString(3, content);
            stmt.setInt(4, selectedArticle.getId());

            stmt.executeUpdate();
            showAlert("Success", "Article updated successfully!", Alert.AlertType.INFORMATION);
            // Close the current window
            categoryChoiceBox.getScene().getWindow().hide();
        } catch (Exception e) {
            showAlert("Error", "Failed to update article: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }



    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
