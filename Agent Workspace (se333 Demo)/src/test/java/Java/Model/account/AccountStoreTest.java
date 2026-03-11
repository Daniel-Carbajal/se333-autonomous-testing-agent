package Java.Model.account;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class AccountStoreTest {

    @AfterEach
    void cleanup(){
        File f = new File("accounts.json");
        if(f.exists()) f.delete();
    }

    @Test
    void addExistsGetPasswordHash(){
        AccountStore s = new AccountStore();
        s.add("u","h");
        assertTrue(s.exists("u"));
        assertEquals("h", s.getPasswordHash("u"));
    }
}
