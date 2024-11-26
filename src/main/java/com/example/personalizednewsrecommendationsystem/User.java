package com.example.personalizednewsrecommendationsystem;

public class User {
    private String username;
    private String password;
    private UserPreferences preferences;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.preferences = new UserPreferences();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserPreferences getPreferences() {
        return preferences;
    }
}
