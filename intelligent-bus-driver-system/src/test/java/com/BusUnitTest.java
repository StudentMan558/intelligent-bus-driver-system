package com;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class BusUnitTest {
    
    @Test
    public void testBusConstructorAndGetters() {
        // Test that constructor properly initializes all fields
        Bus bus = new Bus("12345678", 50, 75.0, "Diesel");
        
        assertEquals("12345678", bus.getBusID(),
                "Bus ID should match the constructor value");
        assertEquals(50, bus.getCapacity(),
                "Capacity should match the constructor value");
        assertEquals(75.0, bus.getFuelLevel(), 0.001,
                "Fuel level should match the constructor value");
        assertEquals("Diesel", bus.getFuelType(),
                "Fuel type should match the constructor value");
    }
    
    @Test
    public void testSetBusIDValid() {
        // Test setting a valid bus ID
        Bus bus = new Bus("12345678", 50, 75.0, "Diesel");
        bus.setBusID("87654321");
        
        assertEquals("87654321", bus.getBusID(),
                "Bus ID should be updated to the new valid value");
    }
}