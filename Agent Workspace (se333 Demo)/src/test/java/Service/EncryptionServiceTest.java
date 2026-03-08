package Service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EncryptionServiceTest {

    @Test
    public void testEncryptionServiceInstantiation() {
        // Test that the class can be instantiated (for 100% coverage)
        EncryptionService service = new EncryptionService();
        assertNotNull(service);
    }

    @Test
    public void testHashPasswordCreatesNonNullHash() {
        String plainText = "mySecurePassword123";
        String hashed = EncryptionService.hashPassword(plainText);
        assertNotNull(hashed);
    }

    @Test
    public void testHashPasswordCreatesNonEmptyHash() {
        String plainText = "myPassword";
        String hashed = EncryptionService.hashPassword(plainText);
        assertFalse(hashed.isEmpty());
    }

    @Test
    public void testHashPasswordCreatesDifferentHashesForSamePassword() {
        String plainText = "samePassword";
        String hash1 = EncryptionService.hashPassword(plainText);
        String hash2 = EncryptionService.hashPassword(plainText);
        
        // BCrypt generates different salts, so hashes should be different
        assertNotEquals(hash1, hash2);
    }

    @Test
    public void testHashPasswordWithEmptyString() {
        String plainText = "";
        String hashed = EncryptionService.hashPassword(plainText);
        assertNotNull(hashed);
    }

    @Test
    public void testHashPasswordWithSpecialCharacters() {
        String plainText = "P@ssw0rd!#$%";
        String hashed = EncryptionService.hashPassword(plainText);
        assertNotNull(hashed);
        assertFalse(hashed.isEmpty());
    }

    @Test
    public void testHashPasswordWithLongString() {
        String plainText = "ThisIsAVeryLongPasswordThatShouldStillBeHashedCorrectly12345678901234567890";
        String hashed = EncryptionService.hashPassword(plainText);
        assertNotNull(hashed);
    }

    @Test
    public void testVerifyPasswordWithCorrectPassword() {
        String plainText = "correctPassword123";
        String hashed = EncryptionService.hashPassword(plainText);
        
        assertTrue(EncryptionService.verifyPassword(plainText, hashed));
    }

    @Test
    public void testVerifyPasswordWithIncorrectPassword() {
        String plainText = "correctPassword";
        String wrongPassword = "incorrectPassword";
        String hashed = EncryptionService.hashPassword(plainText);
        
        assertFalse(EncryptionService.verifyPassword(wrongPassword, hashed));
    }

    @Test
    public void testVerifyPasswordWithEmptyPlainText() {
        String plainText = "password";
        String hashed = EncryptionService.hashPassword(plainText);
        
        assertFalse(EncryptionService.verifyPassword("", hashed));
    }

    @Test
    public void testVerifyPasswordCaseSensitive() {
        String plainText = "Password";
        String hashed = EncryptionService.hashPassword(plainText);
        
        assertFalse(EncryptionService.verifyPassword("password", hashed));
        assertTrue(EncryptionService.verifyPassword("Password", hashed));
    }

    @Test
    public void testVerifyPasswordWithSpecialCharacters() {
        String plainText = "P@ss!#$%123";
        String hashed = EncryptionService.hashPassword(plainText);
        
        assertTrue(EncryptionService.verifyPassword(plainText, hashed));
    }

    @Test
    public void testVerifyPasswordWithWhitespace() {
        String plainText = "pass word with spaces";
        String hashed = EncryptionService.hashPassword(plainText);
        
        assertTrue(EncryptionService.verifyPassword(plainText, hashed));
        assertFalse(EncryptionService.verifyPassword("password with spaces", hashed));
    }

    @Test
    public void testHashAndVerifyIntegration() {
        String[] passwords = {
            "simplePass",
            "Complex123!@#",
            "with spaces",
            "UPPERCASE",
            "lowercase",
            "12345678"
        };
        
        for (String password : passwords) {
            String hashed = EncryptionService.hashPassword(password);
            assertTrue(EncryptionService.verifyPassword(password, hashed),
                    "Password verification failed for: " + password);
        }
    }
}
