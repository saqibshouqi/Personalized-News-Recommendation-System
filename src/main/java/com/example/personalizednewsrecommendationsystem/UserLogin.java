package com.example.personalizednewsrecommendationsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserLogin {

    @FXML
    private TextField username, userPassword;

    @FXML
    private Button login, signUp;


    @FXML
    protected void onLoginButtonClick(ActionEvent actionEvent) {
        String user = username.getText().trim();
        String pass = userPassword.getText().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            showAlert("Error", "Username or Password cannot be empty", Alert.AlertType.ERROR);
            return;
        }

        try (Connection connection = DatabaseConnection.connect()) {
            String query = "SELECT * FROM UserCredentials WHERE UserName = ? AND UserPassword = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, pass);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                SessionManager.setCurrentUsername(user);

                showAlert("Success", "Login Successful!", Alert.AlertType.INFORMATION);

                // Get current stage and pass it to NavigationHelper
                Stage currentStage = (Stage) username.getScene().getWindow();
                NavigationHelper.loadScene("user-dashboard.fxml", "User Dashboard", currentStage);
            } else {
                showAlert("Error", "Invalid Username or Password", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Error", "Database Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    @FXML
    protected void onSignUpButtonClick(ActionEvent actionEvent) {
        // Get current stage and pass it to NavigationHelper
        Stage currentStage = (Stage) signUp.getScene().getWindow();
        NavigationHelper.loadScene("user-signup.fxml", "User Sign Up", currentStage);
    }


    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
