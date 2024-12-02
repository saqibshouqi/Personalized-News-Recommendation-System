package com.example.personalizednewsrecommendationsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class UserDashboard {

    @FXML
    private Button manageProfile, logout, viewAllNews, viewRecommendedNews;

    @FXML
    private void onManageProfileButtonClick(ActionEvent actionEvent) {
        NavigationHelper.loadScene("manage-profile.fxml", "Manage Profile");
    }

    @FXML
    private void onLogoutButtonClick(ActionEvent actionEvent) {
        // Get the current stage from the logout button
        Stage currentStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

        // Call NavigationHelper to load the login window and close the current dashboard
        NavigationHelper.loadScene("user-login.fxml", "Login", currentStage);
    }


    @FXML
    private void onViewAllNewsButtonClick(ActionEvent actionEvent) {
        NavigationHelper.loadScene("view-all-news.fxml", "View All News");
    }

    @FXML
    private void onViewRecommendedNewsButtonClick(ActionEvent actionEvent) {
        NavigationHelper.loadScene("recommended-news.fxml", "Recommended News");
    }

}
