package com.example.personalizednewsrecommendationsystem;

import org.junit.jupiter.api.Test;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseConnectionTest {

    @Test
    public void testDatabaseConnection() {
        Connection connection = DatabaseConnection.connect();
        assertNotNull(connection, "Connection should not be null");
        try {
            connection.close();
        } catch (Exception e) {
            fail("Failed to close connection: " + e.getMessage());
        }
    }
}
