package com.example.personalizednewsrecommendationsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;




public class RoleSelector {

    @FXML
    private Button user;

    @FXML
    private Button systemAdministrator;



    @FXML
    protected void onUserButtonClick(ActionEvent actionEvent) throws IOException{
        loadScene("user-login.fxml", "User Login");
    }


    @FXML
    protected void onSystemAdministratorButtonClick(ActionEvent actionEvent) throws IOException {
        loadScene("admin-login.fxml", "System Administrator Login");
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