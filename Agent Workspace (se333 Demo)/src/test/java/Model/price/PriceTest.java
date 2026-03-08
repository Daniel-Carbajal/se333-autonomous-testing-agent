package Model.price;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PriceTest {

    // Test isNegative method
    @Test
    public void testIsNegativeWithNegativePrice() {
        Price negativePrice = PriceFactory.makePrice(-500);
        assertTrue(negativePrice.isNegative());
    }

    @Test
    public void testIsNegativeWithPositivePrice() {
        Price positivePrice = PriceFactory.makePrice(500);
        assertFalse(positivePrice.isNegative());
    }

    @Test
    public void testIsNegativeWithZeroPrice() {
        Price zeroPrice = PriceFactory.makePrice(0);
        assertFalse(zeroPrice.isNegative());
    }

    // Test greaterThan method
    @Test
    public void testGreaterThanWithLargerPrice() throws InvalidPriceException {
        Price price1 = PriceFactory.makePrice(1000);
        Price price2 = PriceFactory.makePrice(500);
        assertTrue(price1.greaterThan(price2));
    }

    @Test
    public void testGreaterThanWithSmallerPrice() throws InvalidPriceException {
        Price price1 = PriceFactory.makePrice(500);
        Price price2 = PriceFactory.makePrice(1000);
        assertFalse(price1.greaterThan(price2));
    }

    @Test
    public void testGreaterThanWithEqualPrice() throws InvalidPriceException {
        Price price1 = PriceFactory.makePrice(500);
        Price price2 = PriceFactory.makePrice(500);
        assertFalse(price1.greaterThan(price2));
    }

    @Test
    public void testGreaterThanWithNullPrice() {
        Price price = PriceFactory.makePrice(500);
        assertThrows(InvalidPriceException.class, () -> {
            price.greaterThan(null);
        });
    }

    // Test lessThan method
    @Test
    public void testLessThanWithSmallerPrice() throws InvalidPriceException {
        Price price1 = PriceFactory.makePrice(500);
        Price price2 = PriceFactory.makePrice(1000);
        assertTrue(price1.lessThan(price2));
    }

    @Test
    public void testLessThanWithLargerPrice() throws InvalidPriceException {
        Price price1 = PriceFactory.makePrice(1000);
        Price price2 = PriceFactory.makePrice(500);
        assertFalse(price1.lessThan(price2));
    }

    @Test
    public void testLessThanWithEqualPrice() throws InvalidPriceException {
        Price price1 = PriceFactory.makePrice(500);
        Price price2 = PriceFactory.makePrice(500);
        assertFalse(price1.lessThan(price2));
    }

    @Test
    public void testLessThanWithNullPrice() {
        Price price = PriceFactory.makePrice(500);
        assertThrows(InvalidPriceException.class, () -> {
            price.lessThan(null);
        });
    }

    // Test greaterOrEqual method
    @Test
    public void testGreaterOrEqualWithLargerPrice() throws InvalidPriceException {
        Price price1 = PriceFactory.makePrice(1000);
        Price price2 = PriceFactory.makePrice(500);
        assertTrue(price1.greaterOrEqual(price2));
    }

    @Test
    public void testGreaterOrEqualWithEqualPrice() throws InvalidPriceException {
        Price price1 = PriceFactory.makePrice(500);
        Price price2 = PriceFactory.makePrice(500);
        assertTrue(price1.greaterOrEqual(price2));
    }

    @Test
    public void testGreaterOrEqualWithSmallerPrice() throws InvalidPriceException {
        Price price1 = PriceFactory.makePrice(500);
        Price price2 = PriceFactory.makePrice(1000);
        assertFalse(price1.greaterOrEqual(price2));
    }

    @Test
    public void testGreaterOrEqualWithNullPrice() {
        Price price = PriceFactory.makePrice(500);
        assertThrows(InvalidPriceException.class, () -> {
            price.greaterOrEqual(null);
        });
    }

    // Test lessOrEqual method
    @Test
    public void testLessOrEqualWithSmallerPrice() throws InvalidPriceException {
        Price price1 = PriceFactory.makePrice(500);
        Price price2 = PriceFactory.makePrice(1000);
        assertTrue(price1.lessOrEqual(price2));
    }

    @Test
    public void testLessOrEqualWithEqualPrice() throws InvalidPriceException {
        Price price1 = PriceFactory.makePrice(500);
        Price price2 = PriceFactory.makePrice(500);
        assertTrue(price1.lessOrEqual(price2));
    }

    @Test
    public void testLessOrEqualWithLargerPrice() throws InvalidPriceException {
        Price price1 = PriceFactory.makePrice(1000);
        Price price2 = PriceFactory.makePrice(500);
        assertFalse(price1.lessOrEqual(price2));
    }

    @Test
    public void testLessOrEqualWithNullPrice() {
        Price price = PriceFactory.makePrice(500);
        assertThrows(InvalidPriceException.class, () -> {
            price.lessOrEqual(null);
        });
    }

    // Test add method
    @Test
    public void testAddPositivePrices() throws InvalidPriceException {
        Price price1 = PriceFactory.makePrice(500);
        Price price2 = PriceFactory.makePrice(300);
        Price result = price1.add(price2);
        assertEquals(PriceFactory.makePrice(800), result);
    }

    @Test
    public void testAddNegativePrice() throws InvalidPriceException {
        Price price1 = PriceFactory.makePrice(500);
        Price price2 = PriceFactory.makePrice(-300);
        Price result = price1.add(price2);
        assertEquals(PriceFactory.makePrice(200), result);
    }

    @Test
    public void testAddWithNullPrice() {
        Price price = PriceFactory.makePrice(500);
        assertThrows(InvalidPriceException.class, () -> {
            price.add(null);
        });
    }

    // Test subtract method
    @Test
    public void testSubtractPositivePrices() throws InvalidPriceException {
        Price price1 = PriceFactory.makePrice(1000);
        Price price2 = PriceFactory.makePrice(300);
        Price result = price1.subtract(price2);
        assertEquals(PriceFactory.makePrice(700), result);
    }

    @Test
    public void testSubtractResultingInNegative() throws InvalidPriceException {
        Price price1 = PriceFactory.makePrice(500);
        Price price2 = PriceFactory.makePrice(800);
        Price result = price1.subtract(price2);
        assertTrue(result.isNegative());
        assertEquals(PriceFactory.makePrice(-300), result);
    }

    @Test
    public void testSubtractWithNullPrice() {
        Price price = PriceFactory.makePrice(500);
        assertThrows(InvalidPriceException.class, () -> {
            price.subtract(null);
        });
    }

    // Test multiply method
    @Test
    public void testMultiplyByPositiveNumber() {
        Price price = PriceFactory.makePrice(100);
        Price result = price.multiply(5);
        assertEquals(PriceFactory.makePrice(500), result);
    }

    @Test
    public void testMultiplyByZero() {
        Price price = PriceFactory.makePrice(100);
        Price result = price.multiply(0);
        assertEquals(PriceFactory.makePrice(0), result);
    }

    @Test
    public void testMultiplyByNegativeNumber() {
        Price price = PriceFactory.makePrice(100);
        Price result = price.multiply(-3);
        assertEquals(PriceFactory.makePrice(-300), result);
    }

    // Test equals method
    @Test
    public void testEqualsWithSamePrice() {
        Price price1 = PriceFactory.makePrice(500);
        Price price2 = PriceFactory.makePrice(500);
        assertEquals(price1, price2);
    }

    @Test
    public void testEqualsWithDifferentPrice() {
        Price price1 = PriceFactory.makePrice(500);
        Price price2 = PriceFactory.makePrice(600);
        assertNotEquals(price1, price2);
    }

    @Test
    public void testEqualsWithNull() {
        Price price = PriceFactory.makePrice(500);
        assertNotEquals(null, price);
    }

    @Test
    public void testEqualsWithDifferentClass() {
        Price price = PriceFactory.makePrice(500);
        String notAPrice = "500";
        assertNotEquals(price, notAPrice);
    }

    // Test hashCode method
    @Test
    public void testHashCodeConsistency() {
        Price price1 = PriceFactory.makePrice(500);
        Price price2 = PriceFactory.makePrice(500);
        assertEquals(price1.hashCode(), price2.hashCode());
    }

    @Test
    public void testHashCodeDifferent() {
        Price price1 = PriceFactory.makePrice(500);
        Price price2 = PriceFactory.makePrice(600);
        assertNotEquals(price1.hashCode(), price2.hashCode());
    }

    // Test compareTo method
    @Test
    public void testCompareToWithSmallerPrice() {
        Price price1 = PriceFactory.makePrice(1000);
        Price price2 = PriceFactory.makePrice(500);
        assertTrue(price1.compareTo(price2) > 0);
    }

    @Test
    public void testCompareToWithLargerPrice() {
        Price price1 = PriceFactory.makePrice(500);
        Price price2 = PriceFactory.makePrice(1000);
        assertTrue(price1.compareTo(price2) < 0);
    }

    @Test
    public void testCompareToWithEqualPrice() {
        Price price1 = PriceFactory.makePrice(500);
        Price price2 = PriceFactory.makePrice(500);
        assertEquals(0, price1.compareTo(price2));
    }

    @Test
    public void testCompareToWithNull() {
        Price price = PriceFactory.makePrice(500);
        assertTrue(price.compareTo(null) < 0);
    }

    // Test toString method
    @Test
    public void testToStringWithPositivePrice() {
        Price price = PriceFactory.makePrice(12345);
        assertEquals("$123.45", price.toString());
    }

    @Test
    public void testToStringWithNegativePrice() {
        Price price = PriceFactory.makePrice(-12345);
        assertEquals("$-123.45", price.toString());
    }

    @Test
    public void testToStringWithZeroPrice() {
        Price price = PriceFactory.makePrice(0);
        assertEquals("$0.00", price.toString());
    }

    @Test
    public void testToStringWithLargePrice() {
        Price price = PriceFactory.makePrice(123456789);
        assertEquals("$1,234,567.89", price.toString());
    }

    @Test
    public void testToStringWithSmallPrice() {
        Price price = PriceFactory.makePrice(5);
        assertEquals("$0.05", price.toString());
    }
}
