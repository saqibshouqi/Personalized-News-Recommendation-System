package com.example.personalizednewsrecommendationsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserSignup {

    @FXML
    private TextField username,email,  password, confirmPassword;

    @FXML
    private Button confirm;


    @FXML
    protected void onConfirmButtonClick(ActionEvent actionEvent) {
        String user = username.getText().trim();
        String pass = password.getText().trim();
        String confirmPass = confirmPassword.getText().trim();
        String userEmail = email.getText().trim();

        if (user.isEmpty() || pass.isEmpty() || confirmPass.isEmpty() || userEmail.isEmpty()) {
            showAlert("Error", "All fields must be filled!", Alert.AlertType.ERROR);
            return;
        }

        if (user.length() < 5 || user.length() > 20 || pass.length() < 5 || pass.length() > 20) {
            showAlert("Error", "Username and Password must be between 5 and 20 characters.", Alert.AlertType.ERROR);
            return;
        }

        if (!pass.equals(confirmPass)) {
            showAlert("Error", "Passwords do not match!", Alert.AlertType.ERROR);
            return;
        }

        if (!isValidEmail(userEmail)) {
            showAlert("Error", "Invalid email format!", Alert.AlertType.ERROR);
            return;
        }

        try (Connection connection = DatabaseConnection.connect()) {
            String checkQuery = "SELECT * FROM UserCredentials WHERE UserName = ? OR userEmail = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setString(1, user);
            checkStmt.setString(2, userEmail);

            ResultSet resultSet = checkStmt.executeQuery();
            if (resultSet.next()) {
                showAlert("Error", "Username or Email already exists!", Alert.AlertType.ERROR);
                return;
            }

            String insertQuery = "INSERT INTO UserCredentials (UserName, UserPassword, userEmail) VALUES (?, ?, ?)";
            PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
            insertStmt.setString(1, user);
            insertStmt.setString(2, pass);
            insertStmt.setString(3, userEmail);

            insertStmt.executeUpdate();
            showAlert("Success", "Account created successfully!", Alert.AlertType.INFORMATION);
            NavigationHelper.loadScene("user-login.fxml", "User Login");
        } catch (Exception e) {
            showAlert("Error", "Database Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
        return email.matches(emailRegex);
    }


    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
