package com.example.personalizednewsrecommendationsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AdminDashboard {

    @FXML
    private ListView<String> categoryListView;

    private ObservableList<String> categories = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadCategories();
    }

    @FXML
    private void onAddCategoryButtonClick(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Category");
        dialog.setHeaderText("Add a New Category");
        dialog.setContentText("Enter category name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(category -> {
            if (addCategory(category)) {
                showAlert("Success", "Category added successfully.", Alert.AlertType.INFORMATION);
                loadCategories();
            } else {
                showAlert("Error", "Failed to add category. It might already exist.", Alert.AlertType.ERROR);
            }
        });
    }

    @FXML
    private void onEditCategoryButtonClick(ActionEvent event) {
        String selectedCategory = categoryListView.getSelectionModel().getSelectedItem();
        if (selectedCategory == null) {
            showAlert("Warning", "Please select a category to edit.", Alert.AlertType.WARNING);
            return;
        }

        TextInputDialog dialog = new TextInputDialog(selectedCategory);
        dialog.setTitle("Edit Category");
        dialog.setHeaderText("Edit Selected Category");
        dialog.setContentText("Enter new category name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newCategory -> {
            if (editCategory(selectedCategory, newCategory)) {
                showAlert("Success", "Category updated successfully.", Alert.AlertType.INFORMATION);
                loadCategories();
            } else {
                showAlert("Error", "Failed to update category. It might already exist.", Alert.AlertType.ERROR);
            }
        });
    }

    @FXML
    private void onDeleteCategoryButtonClick(ActionEvent event) {
        String selectedCategory = categoryListView.getSelectionModel().getSelectedItem();
        if (selectedCategory == null) {
            showAlert("Warning", "Please select a category to delete.", Alert.AlertType.WARNING);
            return;
        }

        if (deleteCategory(selectedCategory)) {
            showAlert("Success", "Category deleted successfully.", Alert.AlertType.INFORMATION);
            loadCategories();
        } else {
            showAlert("Error", "Failed to delete category.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onViewUsersButtonClick(ActionEvent event) {
        NavigationHelper.loadScene("view-users.fxml", "View Users");
    }

    @FXML
    private void onLogoutButtonClick(ActionEvent event) {
        NavigationHelper.loadScene("role-selector.fxml", "Login");
    }

    private void loadCategories() {
        categories.clear();
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement stmt = connection.prepareStatement("SELECT name FROM Categories");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categories.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            showAlert("Error", "Failed to load categories: " + e.getMessage(), Alert.AlertType.ERROR);
        }
        categoryListView.setItems(categories);
    }

    private boolean addCategory(String category) {
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement stmt = connection.prepareStatement("INSERT INTO Categories (name) VALUES (?)")) {

            stmt.setString(1, category);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private boolean editCategory(String oldCategory, String newCategory) {
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement stmt = connection.prepareStatement("UPDATE Categories SET name = ? WHERE name = ?")) {

            stmt.setString(1, newCategory);
            stmt.setString(2, oldCategory);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private boolean deleteCategory(String category) {
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement stmt = connection.prepareStatement("DELETE FROM Categories WHERE name = ?")) {

            stmt.setString(1, category);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
