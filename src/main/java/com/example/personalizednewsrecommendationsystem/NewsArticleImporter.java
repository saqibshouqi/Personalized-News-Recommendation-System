package com.example.personalizednewsrecommendationsystem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class NewsArticleImporter {

    public static void main(String[] args) {
        String csvFile = "src/main/resources/com/example/personalizednewsrecommendationsystem/News Article Dataset.csv"; // Update the path
        String line;
        String delimiter = ",";

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3307/personalizednewsrecommendationsystem", "root", "2004")) {

            String query = "INSERT INTO NewsArticles (category, title, content) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(delimiter, 3); // Split into category, title, and content
                    preparedStatement.setString(1, values[0]); // category
                    preparedStatement.setString(2, values[1]); // title
                    preparedStatement.setString(3, values[2]); // content
                    preparedStatement.executeUpdate();
                }
                System.out.println("Data inserted successfully.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
