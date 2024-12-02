package com.example.personalizednewsrecommendationsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminDashboard {

    @FXML
    private TableView<Article> newsTable;
    @FXML
    private TableColumn<Article, Integer> colId;
    @FXML
    private TableColumn<Article, String> colCategory;
    @FXML
    private TableColumn<Article, String> colTitle;
    @FXML
    private TableColumn<Article, String> colContent;

    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, String> colUsername;

    @FXML
    private TableColumn<User, String> colEmail;



    private ObservableList<Article> articlesList = FXCollections.observableArrayList();
    private ObservableList<User> usersList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colContent.setCellValueFactory(new PropertyValueFactory<>("content"));

        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));


        loadArticles();
        loadUsers();
    }

    private void loadArticles() {
        articlesList.clear();
        try (Connection connection = DatabaseConnection.connect()) {
            String query = "SELECT * FROM NewsArticles";
            ResultSet rs = connection.createStatement().executeQuery(query);
            while (rs.next()) {
                articlesList.add(new Article(rs.getInt("id"), rs.getString("title"), rs.getString("content"), rs.getString("category")));
            }
            newsTable.setItems(articlesList);
        } catch (Exception e) {
            showAlert("Error", "Failed to load articles: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    private void loadUsers() {
        usersList.clear();
        try (Connection connection = DatabaseConnection.connect()) {
            String query = "SELECT UserName, userEmail FROM UserCredentials"; // Include userEmail
            ResultSet rs = connection.createStatement().executeQuery(query);
            while (rs.next()) {
                usersList.add(new User(rs.getString("UserName"), "", rs.getString("userEmail"))); // Pass email
            }
            usersTable.setItems(usersList);
        } catch (Exception e) {
            showAlert("Error", "Failed to load users: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    @FXML
    private void onAddArticleClick(ActionEvent event) {
        // Add logic to open a new dialog to add an article.
        NavigationHelper.loadScene("add-article.fxml", "Add Article");
    }





    @FXML
    private void onUpdateArticleClick(ActionEvent event) {
        Article selectedArticle = newsTable.getSelectionModel().getSelectedItem();
        if (selectedArticle != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("update-article.fxml"));
            try {
                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));
                UpdateArticle controller = loader.getController();
                controller.setArticle(selectedArticle);
                stage.setTitle("Update Article");
                stage.show();
            } catch (IOException e) {
                showAlert("Error", "Failed to open update window: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Error", "Please select an article to update!", Alert.AlertType.ERROR);
        }
    }


    @FXML
    private void onDeleteArticleClick(ActionEvent event) {
        Article selectedArticle = newsTable.getSelectionModel().getSelectedItem();
        if (selectedArticle != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Delete Confirmation");
            confirmationAlert.setHeaderText("Are you sure you want to delete this article?");
            confirmationAlert.setContentText("Title: " + selectedArticle.getTitle());

            // Wait for user response
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try (Connection connection = DatabaseConnection.connect()) {
                        String query = "DELETE FROM NewsArticles WHERE id = ?";
                        PreparedStatement stmt = connection.prepareStatement(query);
                        stmt.setInt(1, selectedArticle.getId());
                        stmt.executeUpdate();
                        showAlert("Success", "Article deleted successfully!", Alert.AlertType.INFORMATION);
                        loadArticles();
                    } catch (Exception e) {
                        showAlert("Error", "Failed to delete article: " + e.getMessage(), Alert.AlertType.ERROR);
                    }
                }
            });
        } else {
            showAlert("Error", "Please select an article to delete!", Alert.AlertType.ERROR);
        }
    }










    @FXML
    private void onAddUserClick(ActionEvent event) {
        NavigationHelper.loadScene("user-signup.fxml", "Add User");
    }

    @FXML
    private void onDeleteUserClick(ActionEvent event) {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try (Connection connection = DatabaseConnection.connect()) {
                String query = "DELETE FROM UserCredentials WHERE UserName = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, selectedUser.getUsername());
                stmt.executeUpdate();
                showAlert("Success", "User deleted successfully!", Alert.AlertType.INFORMATION);
                loadUsers();
            } catch (Exception e) {
                showAlert("Error", "Failed to delete user: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Error", "Please select a user to delete!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onLogoutClick(ActionEvent event) {
        // Get the current stage from the logout button
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        // Call the modified NavigationHelper method to open the login window and close the current dashboard
        NavigationHelper.loadScene("role-selector.fxml", "Login", currentStage);
    }


    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void onRefreshClick(ActionEvent event) {
        loadArticles();
        loadUsers();
        showAlert("Info", "Dashboard refreshed successfully!", Alert.AlertType.INFORMATION);
    }

}
