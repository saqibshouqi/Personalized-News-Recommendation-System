package com.example.personalizednewsrecommendationsystem;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class NavigationHelper {

    public static void loadScene(String fxmlFile, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(NavigationHelper.class.getResource(fxmlFile));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();


        } catch (IOException e) {
            System.out.println("Error loading scene: " + e.getMessage());
        }
    }
}
