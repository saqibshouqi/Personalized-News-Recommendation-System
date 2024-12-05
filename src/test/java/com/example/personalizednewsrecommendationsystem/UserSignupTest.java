package com.example.personalizednewsrecommendationsystem;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserSignupTest {

    @Test
    public void testIsValidEmail() {
        UserSignup userSignup = new UserSignup();

        assertTrue(userSignup.isValidEmail("test@example.com"));
        assertTrue(userSignup.isValidEmail("user.name@domain.co"));
        assertFalse(userSignup.isValidEmail("invalid-email"));
        assertFalse(userSignup.isValidEmail("another@.missingdomain"));
    }
}
