package Model.user;

import Model.price.InvalidPriceException;
import Model.price.Price;
import Model.price.PriceFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WalletTest {

    @Test
    public void testWalletCreationWithValidPrice() throws InvalidPriceException {
        Price initialValue = PriceFactory.makePrice(10000); // $100.00
        Wallet wallet = new Wallet(initialValue);
        assertNotNull(wallet);
        assertEquals(initialValue, wallet.getWalletValue());
    }

    @Test
    public void testWalletCreationWithZeroPrice() throws InvalidPriceException {
        Price zeroPrice = PriceFactory.makePrice(0);
        Wallet wallet = new Wallet(zeroPrice);
        assertEquals(zeroPrice, wallet.getWalletValue());
    }

    @Test
    public void testGetWalletValue() throws InvalidPriceException {
        Price price = PriceFactory.makePrice(50000); // $500.00
        Wallet wallet = new Wallet(price);
        assertEquals(price, wallet.getWalletValue());
    }

    @Test
    public void testAddMoneyWithPositiveValue() throws InvalidPriceException {
        Price initialPrice = PriceFactory.makePrice(10000); // $100.00
        Wallet wallet = new Wallet(initialPrice);
        
        wallet.addMoney(5000); // Add $50.00
        
        Price expectedValue = PriceFactory.makePrice(15000); // $150.00
        assertEquals(expectedValue, wallet.getWalletValue());
    }

    @Test
    public void testAddMoneyMultipleTimes() throws InvalidPriceException {
        Price initialPrice = PriceFactory.makePrice(10000);
        Wallet wallet = new Wallet(initialPrice);
        
        wallet.addMoney(1000); // +$10.00
        wallet.addMoney(2000); // +$20.00
        wallet.addMoney(500);  // +$5.00
        
        Price expectedValue = PriceFactory.makePrice(13500); // $135.00
        assertEquals(expectedValue, wallet.getWalletValue());
    }

    @Test
    public void testAddMoneyWithZeroThrowsException() throws InvalidPriceException {
        Price initialPrice = PriceFactory.makePrice(10000);
        Wallet wallet = new Wallet(initialPrice);
        
        InvalidPriceException exception = assertThrows(InvalidPriceException.class, () -> {
            wallet.addMoney(0);
        });
        
        assertEquals("Can't add negative price", exception.getMessage());
    }

    @Test
    public void testAddMoneyWithNegativeValueThrowsException() throws InvalidPriceException {
        Price initialPrice = PriceFactory.makePrice(10000);
        Wallet wallet = new Wallet(initialPrice);
        
        InvalidPriceException exception = assertThrows(InvalidPriceException.class, () -> {
            wallet.addMoney(-5000);
        });
        
        assertEquals("Can't add negative price", exception.getMessage());
    }

    @Test
    public void testSubtractMoneyWithValidAmount() throws InvalidPriceException {
        Price initialPrice = PriceFactory.makePrice(10000); // $100.00
        Wallet wallet = new Wallet(initialPrice);
        
        wallet.subtractMoney(3000); // Subtract $30.00
        
        Price expectedValue = PriceFactory.makePrice(7000); // $70.00
        assertEquals(expectedValue, wallet.getWalletValue());
    }

    @Test
    public void testSubtractMoneyMultipleTimes() throws InvalidPriceException {
        Price initialPrice = PriceFactory.makePrice(10000);
        Wallet wallet = new Wallet(initialPrice);
        
        wallet.subtractMoney(1000); // -$10.00
        wallet.subtractMoney(2000); // -$20.00
        wallet.subtractMoney(500);  // -$5.00
        
        Price expectedValue = PriceFactory.makePrice(6500); // $65.00
        assertEquals(expectedValue, wallet.getWalletValue());
    }

    @Test
    public void testSubtractExactWalletAmount() throws InvalidPriceException {
        Price initialPrice = PriceFactory.makePrice(10000);
        Wallet wallet = new Wallet(initialPrice);
        
        wallet.subtractMoney(10000); // Subtract all
        
        Price expectedValue = PriceFactory.makePrice(0);
        assertEquals(expectedValue, wallet.getWalletValue());
    }

    @Test
    public void testSubtractMoneyWithZeroThrowsException() throws InvalidPriceException {
        Price initialPrice = PriceFactory.makePrice(10000);
        Wallet wallet = new Wallet(initialPrice);
        
        InvalidPriceException exception = assertThrows(InvalidPriceException.class, () -> {
            wallet.subtractMoney(0);
        });
        
        assertEquals("value to deduct cant be negative or 0", exception.getMessage());
    }

    @Test
    public void testSubtractMoneyWithNegativeValueThrowsException() throws InvalidPriceException {
        Price initialPrice = PriceFactory.makePrice(10000);
        Wallet wallet = new Wallet(initialPrice);
        
        InvalidPriceException exception = assertThrows(InvalidPriceException.class, () -> {
            wallet.subtractMoney(-1000);
        });
        
        assertEquals("value to deduct cant be negative or 0", exception.getMessage());
    }

    @Test
    public void testSubtractMoreThanWalletValueThrowsException() throws InvalidPriceException {
        Price initialPrice = PriceFactory.makePrice(10000); // $100.00
        Wallet wallet = new Wallet(initialPrice);
        
        InvalidPriceException exception = assertThrows(InvalidPriceException.class, () -> {
            wallet.subtractMoney(15000); // Try to subtract $150.00
        });
        
        assertEquals("value to deduct can not be greater than wallet's value", exception.getMessage());
    }

    @Test
    public void testSubtractFromEmptyWalletThrowsException() throws InvalidPriceException {
        Price zeroPrice = PriceFactory.makePrice(0);
        Wallet wallet = new Wallet(zeroPrice);
        
        assertThrows(InvalidPriceException.class, () -> {
            wallet.subtractMoney(100);
        });
    }

    @Test
    public void testAddAndSubtractCombination() throws InvalidPriceException {
        Price initialPrice = PriceFactory.makePrice(10000); // $100.00
        Wallet wallet = new Wallet(initialPrice);
        
        wallet.addMoney(5000);     // +$50.00 = $150.00
        wallet.subtractMoney(3000); // -$30.00 = $120.00
        wallet.addMoney(2000);     // +$20.00 = $140.00
        wallet.subtractMoney(4000); // -$40.00 = $100.00
        
        Price expectedValue = PriceFactory.makePrice(10000);
        assertEquals(expectedValue, wallet.getWalletValue());
    }

    @Test
    public void testWalletValueRemainsConsistent() throws InvalidPriceException {
        Price initialPrice = PriceFactory.makePrice(10000);
        Wallet wallet = new Wallet(initialPrice);
        
        Price value1 = wallet.getWalletValue();
        Price value2 = wallet.getWalletValue();
        
        assertEquals(value1, value2);
    }

    @Test
    public void testLargeAmountsInWallet() throws InvalidPriceException {
        Price largePrice = PriceFactory.makePrice(100000000); // $1,000,000.00
        Wallet wallet = new Wallet(largePrice);
        
        wallet.addMoney(50000000); // +$500,000.00
        
        Price expectedValue = PriceFactory.makePrice(150000000);
        assertEquals(expectedValue, wallet.getWalletValue());
    }
}
