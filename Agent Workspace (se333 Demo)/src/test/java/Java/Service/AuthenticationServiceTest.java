package Java.Service;

import Java.Model.account.AccountStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationServiceTest {

    @AfterEach
    void cleanup(){
        File f = new File("accounts.json");
        if(f.exists()) f.delete();
    }

    @Test
    void authenticateLogin_userNotExists_returnsFalse(){
        AccountStore store = new AccountStore();
        AuthenticationService svc = new AuthenticationService(store);

        assertFalse(svc.authenticateLogin("nope","pw"));
    }

    @Test
    void validateSignUp_and_authenticate_success(){
        AccountStore store = new AccountStore();
        AuthenticationService svc = new AuthenticationService(store);

        String res = svc.validateSignUp("testUser","AbcdEf1!");
        assertEquals("Success", res);
        assertTrue(store.exists("testUser"));
        assertTrue(svc.authenticateLogin("testUser","AbcdEf1!"));
    }
}
