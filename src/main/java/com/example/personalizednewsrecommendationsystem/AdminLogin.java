//package com.example.personalizednewsrecommendationsystem;
//
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Scene;
//import javafx.scene.control.TextField;
//import javafx.stage.Stage;
//
//import java.awt.*;
//import java.io.IOException;
//
//public class AdminLogin {
//
//    @FXML
//    private TextField adminPassword;
//    @FXML
//    private javafx.scene.control.Button loginAsAdmin;
//
//
//
//    @FXML
//    protected void onLoginAsAdminButtonClick(ActionEvent actionEvent) throws IOException {
//        loadScene("admin-dashboard.fxml", "Admin Dashboard");
//    }
//
//    private void loadScene(String fxmlFile, String title) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
//        Scene scene = new Scene(fxmlLoader.load());
//        Stage stage = new Stage();
//        stage.setTitle(title);
//        stage.setScene(scene);
//        stage.show();
//    }
//
//}





package com.example.personalizednewsrecommendationsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

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
            NavigationHelper.loadScene("admin-dashboard.fxml", "Admin Dashboard");
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
