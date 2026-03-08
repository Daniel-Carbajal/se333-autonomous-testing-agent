package Model.user;

import Model.currentMarket.CurrentMarketSide;
import Model.currentMarket.InvalidVolumeException;
import Model.price.InvalidPriceException;
import Model.price.Price;
import Model.price.PriceFactory;
import Model.productbook.BookSide;
import Model.tradable.TradableDTO;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testUserCreationWithValidUserId() throws InvalidUserException, InvalidPriceException {
        User user = new User("TestUser");
        assertNotNull(user);
        assertEquals("TestUser", user.getUserId());
    }

    @Test
    public void testUserCreationInitializesWalletToZero() throws InvalidUserException, InvalidPriceException {
        User user = new User("TestUser");
        Price expectedBalance = PriceFactory.makePrice(0);
        assertEquals(expectedBalance, user.getWalletBalance());
    }

    @Test
    public void testUserCreationWithNullUserIdThrowsException() {
        assertThrows(InvalidUserException.class, () -> {
            new User(null);
        });
    }

    @Test
    public void testUserCreationWithEmptyUserIdThrowsException() {
        assertThrows(InvalidUserException.class, () -> {
            new User("");
        });
    }

    @Test
    public void testUserCreationWithWhitespaceOnlyThrowsException() {
        assertThrows(InvalidUserException.class, () -> {
            new User("   ");
        });
    }

    @Test
    public void testUserCreationTrimsWhitespace() throws InvalidUserException, InvalidPriceException {
        User user = new User("  TestUser  ");
        assertEquals("TestUser", user.getUserId());
    }

    @Test
    public void testGetUserId() throws InvalidUserException, InvalidPriceException {
        User user = new User("Alice");
        assertEquals("Alice", user.getUserId());
    }

    @Test
    public void testAddToWallet() throws InvalidUserException, InvalidPriceException {
        User user = new User("Bob");
        user.addToWallet(10000); // Add $100.00
        
        Price expectedBalance = PriceFactory.makePrice(10000);
        assertEquals(expectedBalance, user.getWalletBalance());
    }

    @Test
    public void testAddToWalletMultipleTimes() throws InvalidUserException, InvalidPriceException {
        User user = new User("Charlie");
        user.addToWallet(5000);  // +$50.00
        user.addToWallet(3000);  // +$30.00
        user.addToWallet(2000);  // +$20.00
        
        Price expectedBalance = PriceFactory.makePrice(10000); // $100.00
        assertEquals(expectedBalance, user.getWalletBalance());
    }

    @Test
    public void testAddToWalletWithInvalidAmountThrowsException() throws InvalidUserException, InvalidPriceException {
        User user = new User("Dave");
        assertThrows(InvalidPriceException.class, () -> {
            user.addToWallet(0); // Invalid: zero
        });
    }

    @Test
    public void testSubtractFromWallet() throws InvalidUserException, InvalidPriceException {
        User user = new User("Eve");
        user.addToWallet(10000); // Add $100.00
        user.subtractFromWallet(3000); // Subtract $30.00
        
        Price expectedBalance = PriceFactory.makePrice(7000); // $70.00
        assertEquals(expectedBalance, user.getWalletBalance());
    }

    @Test
    public void testSubtractFromWalletMultipleTimes() throws InvalidUserException, InvalidPriceException {
        User user = new User("Frank");
        user.addToWallet(10000);
        user.subtractFromWallet(2000);
        user.subtractFromWallet(1000);
        user.subtractFromWallet(3000);
        
        Price expectedBalance = PriceFactory.makePrice(4000);
        assertEquals(expectedBalance, user.getWalletBalance());
    }

    @Test
    public void testSubtractFromWalletWithInsufficientFundsThrowsException() throws InvalidUserException, InvalidPriceException {
        User user = new User("Grace");
        user.addToWallet(5000);
        
        assertThrows(InvalidPriceException.class, () -> {
            user.subtractFromWallet(10000); // Try to subtract more than balance
        });
    }

    @Test
    public void testGetWalletBalance() throws InvalidUserException, InvalidPriceException {
        User user = new User("Henry");
        Price initialBalance = user.getWalletBalance();
        assertEquals(PriceFactory.makePrice(0), initialBalance);
        
        user.addToWallet(15000);
        Price newBalance = user.getWalletBalance();
        assertEquals(PriceFactory.makePrice(15000), newBalance);
    }

    @Test
    public void testGetUserMapInitiallyEmpty() throws InvalidUserException, InvalidPriceException {
        User user = new User("Ivy");
        HashMap<String, TradableDTO> userMap = user.getUserMap();
        assertNotNull(userMap);
        assertTrue(userMap.isEmpty());
    }

    @Test
    public void testGetUserMapReturnsCopy() throws InvalidUserException, InvalidPriceException {
        User user = new User("Jack");
        HashMap<String, TradableDTO> map1 = user.getUserMap();
        HashMap<String, TradableDTO> map2 = user.getUserMap();
        
        // Should be different instances (defensive copy)
        assertNotSame(map1, map2);
    }

    @Test
    public void testGetCurrentMarketsInitiallyEmpty() throws InvalidUserException, InvalidPriceException {
        User user = new User("Kate");
        HashMap<String, CurrentMarketSide[]> markets = user.getCurrentMarkets();
        assertNotNull(markets);
        assertTrue(markets.isEmpty());
    }

    @Test
    public void testGetCurrentMarketsReturnsCopy() throws InvalidUserException, InvalidPriceException {
        User user = new User("Leo");
        HashMap<String, CurrentMarketSide[]> map1 = user.getCurrentMarkets();
        HashMap<String, CurrentMarketSide[]> map2 = user.getCurrentMarkets();
        
        // Should be different instances (defensive copy)
        assertNotSame(map1, map2);
    }

    @Test
    public void testUpdateTradableWithValidDTO() throws InvalidUserException, InvalidPriceException {
        User user = new User("Mike");
        Price price = PriceFactory.makePrice(5000);
        TradableDTO dto = new TradableDTO("PRODUCT1", "Mike", price, 100, 80, 10, 10, BookSide.BUY, "ID123");
        
        user.updateTradable(dto);
        HashMap<String, TradableDTO> userMap = user.getUserMap();
        assertTrue(userMap.containsKey("ID123"));
        assertEquals(dto, userMap.get("ID123"));
    }

    @Test
    public void testUpdateTradableReplacesExistingDTO() throws InvalidUserException, InvalidPriceException {
        User user = new User("Nina");
        Price price1 = PriceFactory.makePrice(5000);
        Price price2 = PriceFactory.makePrice(6000);
        
        TradableDTO dto1 = new TradableDTO("PRODUCT1", "Nina", price1, 100, 100, 0, 0, BookSide.BUY, "ID123");
        TradableDTO dto2 = new TradableDTO("PRODUCT1", "Nina", price2, 100, 80, 10, 10, BookSide.BUY, "ID123");
        
        user.updateTradable(dto1);
        user.updateTradable(dto2);
        
        HashMap<String, TradableDTO> userMap = user.getUserMap();
        assertEquals(dto2, userMap.get("ID123"));
        assertEquals(price2, userMap.get("ID123").price());
    }

    @Test
    public void testUpdateTradableWithNullIgnored() throws InvalidUserException, InvalidPriceException {
        User user = new User("Oscar");
        user.updateTradable(null);
        
        // Should not throw exception, just ignore null
        HashMap<String, TradableDTO> userMap = user.getUserMap();
        assertTrue(userMap.isEmpty());
    }

    @Test
    public void testUpdateCurrentMarket() throws InvalidUserException, InvalidPriceException, InvalidVolumeException {
        User user = new User("Paul");
        Price buyPrice = PriceFactory.makePrice(5000);
        Price sellPrice = PriceFactory.makePrice(5100);
        
        CurrentMarketSide buySide = new CurrentMarketSide(buyPrice, 100);
        CurrentMarketSide sellSide = new CurrentMarketSide(sellPrice, 50);
        
        user.updateCurrentMarket("AAPL", buySide, sellSide);
        
        HashMap<String, CurrentMarketSide[]> markets = user.getCurrentMarkets();
        assertTrue(markets.containsKey("AAPL"));
        assertEquals(buySide, markets.get("AAPL")[0]);
        assertEquals(sellSide, markets.get("AAPL")[1]);
    }

    @Test
    public void testUpdateCurrentMarketMultipleSymbols() throws InvalidUserException, InvalidPriceException, InvalidVolumeException {
        User user = new User("Quinn");
        Price price1 = PriceFactory.makePrice(5000);
        Price price2 = PriceFactory.makePrice(10000);
        
        CurrentMarketSide buy1 = new CurrentMarketSide(price1, 100);
        CurrentMarketSide sell1 = new CurrentMarketSide(price1, 50);
        CurrentMarketSide buy2 = new CurrentMarketSide(price2, 200);
        CurrentMarketSide sell2 = new CurrentMarketSide(price2, 150);
        
        user.updateCurrentMarket("AAPL", buy1, sell1);
        user.updateCurrentMarket("GOOGL", buy2, sell2);
        
        HashMap<String, CurrentMarketSide[]> markets = user.getCurrentMarkets();
        assertEquals(2, markets.size());
        assertTrue(markets.containsKey("AAPL"));
        assertTrue(markets.containsKey("GOOGL"));
    }

    @Test
    public void testUpdateCurrentMarketReplacesExisting() throws InvalidUserException, InvalidPriceException, InvalidVolumeException {
        User user = new User("Rachel");
        Price price1 = PriceFactory.makePrice(5000);
        Price price2 = PriceFactory.makePrice(5500);
        
        CurrentMarketSide buy1 = new CurrentMarketSide(price1, 100);
        CurrentMarketSide sell1 = new CurrentMarketSide(price1, 50);
        CurrentMarketSide buy2 = new CurrentMarketSide(price2, 200);
        CurrentMarketSide sell2 = new CurrentMarketSide(price2, 75);
        
        user.updateCurrentMarket("AAPL", buy1, sell1);
        user.updateCurrentMarket("AAPL", buy2, sell2);
        
        HashMap<String, CurrentMarketSide[]> markets = user.getCurrentMarkets();
        assertEquals(1, markets.size());
        assertEquals(buy2, markets.get("AAPL")[0]);
        assertEquals(sell2, markets.get("AAPL")[1]);
    }

    @Test
    public void testGetCurrentMarketsStringWithNoMarkets() throws InvalidUserException, InvalidPriceException {
        User user = new User("Sam");
        String result = user.getCurrentMarketsString();
        assertEquals("", result);
    }

    @Test
    public void testGetCurrentMarketsStringWithOneMarket() throws InvalidUserException, InvalidPriceException, InvalidVolumeException {
        User user = new User("Tina");
        Price buyPrice = PriceFactory.makePrice(5000);
        Price sellPrice = PriceFactory.makePrice(5100);
        
        CurrentMarketSide buySide = new CurrentMarketSide(buyPrice, 100);
        CurrentMarketSide sellSide = new CurrentMarketSide(sellPrice, 50);
        
        user.updateCurrentMarket("AAPL", buySide, sellSide);
        
        String result = user.getCurrentMarketsString();
        assertNotNull(result);
        assertTrue(result.contains("AAPL"));
        assertTrue(result.contains(buySide.toString()));
        assertTrue(result.contains(sellSide.toString()));
    }

    @Test
    public void testGetCurrentMarketsStringWithMultipleMarkets() throws InvalidUserException, InvalidPriceException, InvalidVolumeException {
        User user = new User("Uma");
        Price price1 = PriceFactory.makePrice(5000);
        Price price2 = PriceFactory.makePrice(10000);
        
        CurrentMarketSide buy1 = new CurrentMarketSide(price1, 100);
        CurrentMarketSide sell1 = new CurrentMarketSide(price1, 50);
        CurrentMarketSide buy2 = new CurrentMarketSide(price2, 200);
        CurrentMarketSide sell2 = new CurrentMarketSide(price2, 150);
        
        user.updateCurrentMarket("AAPL", buy1, sell1);
        user.updateCurrentMarket("GOOGL", buy2, sell2);
        
        String result = user.getCurrentMarketsString();
        assertTrue(result.contains("AAPL"));
        assertTrue(result.contains("GOOGL"));
    }

    @Test
    public void testToStringWithNoTradables() throws InvalidUserException, InvalidPriceException {
        User user = new User("Victor");
        String result = user.toString();
        assertNotNull(result);
        assertTrue(result.contains("User Id: Victor"));
    }

    @Test
    public void testToStringWithTradables() throws InvalidUserException, InvalidPriceException {
        User user = new User("Wendy");
        Price price = PriceFactory.makePrice(5000);
        TradableDTO dto = new TradableDTO("PRODUCT1", "Wendy", price, 100, 80, 10, 10, BookSide.BUY, "ID123");
        
        user.updateTradable(dto);
        
        String result = user.toString();
        assertTrue(result.contains("User Id: Wendy"));
        assertTrue(result.contains("PRODUCT1"));
        assertTrue(result.contains("ID123"));
    }

    @Test
    public void testWalletOperationsIntegration() throws InvalidUserException, InvalidPriceException {
        User user = new User("Xavier");
        
        // Start with 0
        assertEquals(PriceFactory.makePrice(0), user.getWalletBalance());
        
        // Add money
        user.addToWallet(10000);
        assertEquals(PriceFactory.makePrice(10000), user.getWalletBalance());
        
        // Subtract money
        user.subtractFromWallet(3000);
        assertEquals(PriceFactory.makePrice(7000), user.getWalletBalance());
        
        // Add more
        user.addToWallet(5000);
        assertEquals(PriceFactory.makePrice(12000), user.getWalletBalance());
    }

    @Test
    public void testUserWithComplexScenario() throws InvalidUserException, InvalidPriceException, InvalidVolumeException {
        User user = new User("Zoe");
        
        // Setup wallet
        user.addToWallet(50000);
        
        // Setup tradables
        Price price = PriceFactory.makePrice(5000);
        TradableDTO dto = new TradableDTO("STOCK1", "Zoe", price, 100, 100, 0, 0, BookSide.BUY, "ID1");
        user.updateTradable(dto);
        
        // Setup market
        CurrentMarketSide buy = new CurrentMarketSide(price, 100);
        CurrentMarketSide sell = new CurrentMarketSide(price, 50);
        user.updateCurrentMarket("STOCK1", buy, sell);
        
        // Verify all state
        assertEquals(PriceFactory.makePrice(50000), user.getWalletBalance());
        assertEquals(1, user.getUserMap().size());
        assertEquals(1, user.getCurrentMarkets().size());
        assertNotNull(user.toString());
    }
}
