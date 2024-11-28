package com.example.personalizednewsrecommendationsystem;

public class SessionManager {
    private static String currentUsername;

    // Private constructor to prevent instantiation
    private SessionManager() {}

    // Sets the current username after a successful login
    public static void setCurrentUsername(String username) {
        currentUsername = username;
    }

    // Retrieves the current username for session management
    public static String getCurrentUsername() {
        return currentUsername;
    }

    // Clears the session when user logs out
    public static void clearSession() {
        currentUsername = null;
    }

    // Checks if a user is logged in
    public static boolean isUserLoggedIn() {
        return currentUsername != null;
    }




    private static String selectedArticleTitle;

    public static void setSelectedArticleTitle(String title) {
        selectedArticleTitle = title;
    }

    public static String getSelectedArticleTitle() {
        return selectedArticleTitle;
    }


}
