package com.example.personalizednewsrecommendationsystem;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SessionManagerTest {

    @Test
    public void testSessionManagement() {
        // Test setting and getting username
        SessionManager.setCurrentUsername("testUser");
        assertEquals("testUser", SessionManager.getCurrentUsername());

        // Test user login check
        assertTrue(SessionManager.isUserLoggedIn());

        // Test clearing session
        SessionManager.clearSession();
        assertNull(SessionManager.getCurrentUsername());
        assertFalse(SessionManager.isUserLoggedIn());
    }

    @Test
    public void testSelectedArticleTitle() {
        SessionManager.setSelectedArticleTitle("Breaking News");
        assertEquals("Breaking News", SessionManager.getSelectedArticleTitle());

        SessionManager.setSelectedArticleTitle(null);
        assertNull(SessionManager.getSelectedArticleTitle());
    }
}
