package Java.Model.user;

import Java.Model.price.PriceFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void userWalletOperations() throws Exception {
        User u = new User("alice");
        assertEquals("alice", u.getUserId());
        assertEquals(PriceFactory.makePrice(0), u.getWalletBalance());

        u.addToWallet(100);
        assertEquals(PriceFactory.makePrice(100), u.getWalletBalance());

        u.subtractFromWallet(50);
        assertEquals(PriceFactory.makePrice(50), u.getWalletBalance());
    }
}
