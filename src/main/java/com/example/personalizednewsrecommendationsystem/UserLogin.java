package com.example.personalizednewsrecommendationsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class UserLogin {

    @FXML
    private Button login;

    @FXML
    private Button signUp;

    @FXML
    protected void onLoginButtonClick(ActionEvent actionEvent) throws IOException {
    }

    @FXML
    protected void onSignUpButtonClick(ActionEvent actionEvent) throws IOException{
        loadScene("user-signup.fxml", "User Sign Up");
    }

    private void loadScene(String fxmlFile, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}
