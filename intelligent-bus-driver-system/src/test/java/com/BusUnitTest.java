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
    public void testSetShortBusID() {

        Bus bus = new Bus("1234567", 50, 75.0, "Diesel");

        assertThrows(IllegalArgumentException.class, () -> {
            bus.setBusID("1234567");
        });
    }

    // Unit test Case 3: Test that a long 8-digit busID is not accepted (e.g., "123456789")
    @Test
    public void testLongBusID() {

        Bus bus = new Bus("123456789", 50, 75.0, "Diesel");

        assertThrows(IllegalArgumentException.class, () -> {
            bus.setBusID("123456789");
        });
    }

    // Unit test Case 4: Test that a invalid busID using letters is not accepted (e.g., "1ads1f43")
    @Test 
    public void testDigitBusID() {
        // Sets parameters for this test.
        Bus bus = new Bus("1ads1f43", 50, 75.0, "Diesel");  

        assertThrows(IllegalArgumentException.class, () -> {
            bus.setBusID("1ads1f43");
    });
}

    // Unit test Case 5: Test that duplicate busID will be rejected and not allowed (e.g., if '12345678' is already used, will not allow '12345678' again.)
    @Test
    public void testDuplicateBusID() {
        Bus bus = new Bus("11111111", 50, 75.0, "Diesel");  
        
        assertThrows(IllegalArgumentException.class, () -> {
            bus.setBusID("12345678");  
    });
}
    // Condition B2: busCapacity cannot increase during update operations. However, it can decrease
    // Unit test Case 6: Capacity can decrease
    @Test
    public void testCapacityCanDecrease() {
        Bus bus = new Bus("87654321", 100, 75.0, "Diesel");
        Driver driver = new Driver("23aq!-dfIU", "John Doe", 10, "Heavy", "1  | Latrobe St | Melbourne | Victoria | Australia", "01-01-2000");
        
        // Decrease from 100 to 50 - should succeed
        bus.setCapacity(50, driver);
        assertEquals(50, bus.getCapacity());
    }

    // Unit test Case 7: Capacity can stay same
    @Test
    public void testCapacityCanStaySame() {
        Bus bus = new Bus("76543210", 50, 75.0, "Diesel");
        Driver driver = new Driver("23aq!-dfIU", "John Doe", 10, "Heavy", "1  | Latrobe St | Melbourne | Victoria | Australia", "01-01-2000");
        
        // Keep at 50 - should succeed
        bus.setCapacity(50, driver);
        assertEquals(50, bus.getCapacity());
    }

    // Unit test Case 8: Capacity cannot increase
    @Test
    public void testCapacityCannotIncrease() {
        Bus bus = new Bus("65432109", 50, 75.0, "Diesel");
        Driver driver = new Driver("23aq!-dfIU", "John Doe", 10, "Heavy", "1  | Latrobe St | Melbourne | Victoria | Australia", "01-01-1970");
        
        // Try to increase from 50 to 100 - should throw
        assertThrows(IllegalArgumentException.class, () -> {
            bus.setCapacity(100, driver);
        });
    }

    // Condition B3: Driver age must be less then 50 to drive buses with a capacity of 50 or more.
    // Unit test Case 9: Driver Under 50 Can Drive Large Bus
    @Test
    public void testDriverUnder50() {
        Bus bus = new Bus("11111111", 50, 75.0, "Diesel");  
        Driver driver = new Driver("23aq!-dfIU", "John Doe", 10, "Light", "1  | Latrobe St | Melbourne | Victoria | Australia", "01-01-2000");
        
        bus.setCapacity(50, driver);

        assertEquals(50, bus.getCapacity());  
};

    // Unit test Case 10: Driver Over 50 Cannot Drive Large Bus
    @Test
    public void testDriverOver50() {
        Bus bus = new Bus("11111111", 50, 75.0, "Diesel");  
        Driver driver = new Driver("23aq!-dfIU", "John Doe", 10, "Light", "1  | Latrobe St | Melbourne | Victoria | Australia", "01-01-1950");

        assertThrows(IllegalArgumentException.class, () -> {
            bus.setCapacity(51, driver);  
    });
}


    // Unit test Case 11: Driver Over 50 Can Drive Small Bus 
    @Test
    public void testDriverOver50Small() {
        Bus bus = new Bus("11111111", 49, 75.0, "Diesel");  
        Driver driver = new Driver("23aq!-dfIU", "John Doe", 10, "Light", "1  | Latrobe St | Melbourne | Victoria | Australia", "01-01-1950");
        
        bus.setCapacity(49, driver);

        assertEquals(49, bus.getCapacity());  
    };


    // Condition B4: Only drivers with at least 5 years of experience can drive electric buses (fuelType = "Electricity")
    // Unit test Case 12: Experienced Driver Can Drive Electric
    @Test
    public void testExperiencedDriverElectric() {
        Bus bus = new Bus("11111111", 50, 75.0, "Electricity");  
        Driver driver = new Driver("23aq!-dfIU", "John Doe", 10, "Heavy", "1  | Latrobe St | Melbourne | Victoria | Australia", "01-01-1950");
        
        bus.setFuelType("Electricity", driver);

        assertEquals("Electricity", bus.getFuelType());  
    };

    // Unit test Case 13: Inexperienced Driver Cannot Drive Electric
    @Test
    public void testInexperiencedDriverElectric() {
        Bus bus = new Bus("11111111", 50, 75.0, "Electricity");  
        Driver driver = new Driver("23aq!-dfIU", "John Doe", 4, "Heavy", "1  | Latrobe St | Melbourne | Victoria | Australia", "01-01-1950");
        
        assertThrows(IllegalArgumentException.class, () -> {
            bus.setFuelType("Electricity", driver);  
    });  
}

    // Unit test Case 14: Edge Case - Exactly 5 Years Experience
    @Test
    public void testInexperiencedDriverElectricEdgeCase() {
        Bus bus = new Bus("11111111", 50, 75.0, "Electricity");  
        Driver driver = new Driver("23aq!-dfIU", "John Doe", 5, "Heavy", "1  | Latrobe St | Melbourne | Victoria | Australia", "01-01-1950");
        
        bus.setFuelType("Electricity", driver);

        assertEquals("Electricity", bus.getFuelType());    
    };


    // Condition B5: Only drivers holding a Heavy or Public Transport licence are permitted to operate electric and hybrid buses
    // Unit test Case 15: Heavy License Can Drive Electric
    @Test
    public void testHeavyLiscenseElectric() {
        Bus bus = new Bus("11111111", 50, 75.0, "Electricity");  
        Driver driver = new Driver("23aq!-dfIU", "John Doe", 5, "Heavy", "1  | Latrobe St | Melbourne | Victoria | Australia", "01-01-1950");
        
        bus.setFuelType("Electricity", driver);

        assertEquals("Electricity", bus.getFuelType());    
    };

    // Unit test Case 16: Light License Cannot Drive Electric
    @Test
    public void testLightLiscenseElectric() {
        Bus bus = new Bus("11111111", 50, 75.0, "Electricity");  
        Driver driver = new Driver("23aq!-dfIU", "John Doe", 5, "Light", "1  | Latrobe St | Melbourne | Victoria | Australia", "01-01-1950");

        assertThrows(IllegalArgumentException.class, () -> {
            bus.setFuelType("Electricity", driver);  
    });    
}

    // Unit test Case 17: License Doesn't Restrict Diesel Buses
    @Test
    public void testLiscenseDiesel() {
        Bus bus = new Bus("11111111", 50, 75.0, "Diesel");  
        Driver driver = new Driver("23aq!-dfIU", "John Doe", 5, "Heavy", "1  | Latrobe St | Melbourne | Victoria | Australia", "01-01-1950");
        
        bus.setFuelType("Diesel", driver);

        assertEquals("Diesel", bus.getFuelType());    
    };
}
