package com.example.personalizednewsrecommendationsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;

public class Main extends Application {

    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static ExecutorService getExecutorService() {
        return executor;
    }

    @Override
    public void stop() {
        executor.shutdown(); // Shutdown the executor on app close
        Platform.exit();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("role-selector.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Personalized News Recommendation System");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }

}