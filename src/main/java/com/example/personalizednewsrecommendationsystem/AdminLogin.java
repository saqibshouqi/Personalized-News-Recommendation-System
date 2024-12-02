package com.example.personalizednewsrecommendationsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminLogin {

    private static final String ADMIN_PASSWORD = "admin2024";

    @FXML
    private TextField adminPassword;

    @FXML
    private Button loginAsAdmin;

    @FXML
    protected void onLoginAsAdminButtonClick(ActionEvent actionEvent) {
        String enteredPassword = adminPassword.getText().trim();

        if (enteredPassword.isEmpty()) {
            showAlert("Error", "Admin Password cannot be empty!", Alert.AlertType.ERROR);
            return;
        }

        if (ADMIN_PASSWORD.equals(enteredPassword)) {
            showAlert("Success", "Welcome, Admin!", Alert.AlertType.INFORMATION);

            // Get the current stage and pass it to NavigationHelper
            Stage currentStage = (Stage) loginAsAdmin.getScene().getWindow();
            NavigationHelper.loadScene("admin-dashboard.fxml", "Admin Dashboard", currentStage);
        } else {
            showAlert("Error", "Incorrect Admin Password!", Alert.AlertType.ERROR);
        }
    }


    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
