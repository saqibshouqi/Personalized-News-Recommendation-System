package com.example.personalizednewsrecommendationsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class UserDashboard {

    @FXML
    private Button manageProfile, logout, viewAllNews, viewRecommendedNews;

    @FXML
    private void onManageProfileButtonClick(ActionEvent actionEvent) {
        NavigationHelper.loadScene("manage-profile.fxml", "Manage Profile");
    }

    @FXML
    private void onLogoutButtonClick(ActionEvent actionEvent) {
        // Handle logout logic, e.g., close session, redirect to login.
        // Clear session and redirect to login
        NavigationHelper.loadScene("user-login.fxml", "Login");
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
