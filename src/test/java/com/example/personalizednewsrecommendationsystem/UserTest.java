package com.example.personalizednewsrecommendationsystem;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testUserProperties() {
        User user = new User("testUser", "password123", "test@example.com");

        assertEquals("testUser", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("test@example.com", user.getEmail());

        // Test setters
        user.setUsername("newUser");
        assertEquals("newUser", user.getUsername());

        user.setPassword("newPass");
        assertEquals("newPass", user.getPassword());

        user.setEmail("new@example.com");
        assertEquals("new@example.com", user.getEmail());
    }
}
