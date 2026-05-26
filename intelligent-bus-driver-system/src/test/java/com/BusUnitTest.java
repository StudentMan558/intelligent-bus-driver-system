package com;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class BusUnitTest {
    
    // Condition B1: busID must be unique. Duplicate bus IDs are not allowed and the busID must be exactly 8 characters long (all characters are digits)
    // Unit test Case 1: Test that a valid 8-digit busID is accepted (e.g., "12345678")
    @Test 
    public void testValidBusID() {
        // Sets parameters for this test.
        Bus bus = new Bus("12345678", 50, 75.0, "Diesel");  

        assertEquals("12345678", bus.getBusID(),
                "Bus ID should match the 8-digit value");
    }
    // Unit test Case 2: Test that a short 8-digit busID is not accepted (e.g., "1234567")
    @Test
    public void testShortBusID() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Bus("1234567", 50, 75.0, "Diesel");
        });
    }
    // Unit test Case 3: Test that a long 8-digit busID is not accepted (e.g., "123456789")
    @Test
    public void testLongBusID() {
        assertThrows(IllegalArgumentException.class, () -> {
             new Bus("123456789", 50, 75.0, "Diesel");
        });
    }
}