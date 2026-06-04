package com;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for BusRepository.
 * Pre-existing data written by hand in buses.txt before tests run:
 *   Line 1: busID 11111111,capacity 50,fuelLevel 90.0,fuelType Diesel
 * Tests verify that valid buses are stored correctly,
 * invalid buses are rejected, updates are persisted correctly,
 * and record counts are updated correctly.
 * @author [Ned Santrac]
 */
public class BusIntegrationTest {

    private BusRepository busRepository = new BusRepository();

    @BeforeEach
    public void setUp() throws IOException {
        // Ensure data directory exists
        Files.createDirectories(Paths.get("data"));
        
        // Initialize buses.txt with the pre-existing line 1 entry before each test, additionally adds a second entry
        String testData = "busID 11111111,capacity 50,fuelLevel 90.0,fuelType Diesel";
        Files.write(Paths.get("data/buses.txt"), testData.getBytes());
    }

    // ─── ADD TESTS ───────────────────────────────────────────────────────────────

    /**
     * Test 1 - addBus success:
     * Verifies a new bus is written below the pre-existing line 1 entry
     * without affecting it.
     */
    @Test
    void testAddBusSuccess() throws IOException {
        // Add a new bus below the pre-existing entry
        Bus newBus = new Bus("12345678", 50, 60.0, "Diesel");
        busRepository.addBus(newBus);

        // Retrieve the new entry and confirm it was stored correctly
        Bus retrieved = busRepository.retrieveBus("12345678");
        assertEquals("12345678", retrieved.getBusID());
        assertEquals(50, retrieved.getCapacity());
        assertEquals(60.0, retrieved.getFuelLevel());
        assertEquals("Diesel", retrieved.getFuelType());

        // Confirm line 1 was not affected by the add
        Bus existing = busRepository.retrieveBus("11111111");
        assertEquals("11111111", existing.getBusID());
    }

    /**
     * Test 2 - addBus duplicate failure:
     * Verifies that adding a bus with the same ID as line 1
     * is rejected and throws an IllegalArgumentException.
     */
    @Test
    void testAddBusDuplicateIDFails() {
        // Attempt to add a bus with the same ID as the line 1 entry
        Bus duplicateBus = new Bus("11111111", 20, 40.0, "Diesel");

        assertThrows(IllegalArgumentException.class, () -> {
            busRepository.addBus(duplicateBus);
        });
    }

    // ─── RETRIEVE TESTS ──────────────────────────────────────────────────────────

    /**
     * Test 3 - retrieveBus success:
     * Verifies that the pre-existing line 1 entry is retrieved correctly.
     */
    @Test
    void testRetrieveBusSuccess() throws IOException {
        // Retrieve the line 1 entry and confirm the ID matches
        Bus retrieved = busRepository.retrieveBus("11111111");
        assertEquals("11111111", retrieved.getBusID());
    }

    /**
     * Test 4 - retrieveBus failure:
     * Verifies that retrieving a non-existent ID throws
     * an IllegalArgumentException.
     */
    @Test
    void testRetrieveBusNotFoundFails() {
        // Attempt to retrieve an ID that does not exist in the file
        assertThrows(IllegalArgumentException.class, () -> {
            busRepository.retrieveBus("00000000");
        });
    }

    // ─── UPDATE TESTS ────────────────────────────────────────────────────────────

    /**
     * Test 5 - updateBus success:
     * Verifies that updating the line 1 entry persists the new values
     * correctly to the file.
     */
    @Test
    void testUpdateBusSuccess() throws IOException {
        // Print line 1 before the update so the change is visible
        Bus before = busRepository.retrieveBus("11111111");
        System.out.println("BEFORE UPDATE -> busID: " + before.getBusID() +
                           ", capacity: " + before.getCapacity() +
                           ", fuelLevel: " + before.getFuelLevel() +
                           ", fuelType: " + before.getFuelType());

        // Update the line 1 entry with new values
        Bus updatedBus = new Bus("11111111", 30, 55.0, "Hybrid");
        busRepository.updateBus(updatedBus);

        // Print line 1 after the update so the change is visible
        Bus after = busRepository.retrieveBus("11111111");
        System.out.println("AFTER UPDATE  -> busID: " + after.getBusID() +
                           ", capacity: " + after.getCapacity() +
                           ", fuelLevel: " + after.getFuelLevel() +
                           ", fuelType: " + after.getFuelType());

        // Confirm the updated values were persisted correctly
        assertEquals(30, after.getCapacity());
        assertEquals(55.0, after.getFuelLevel());
        assertEquals("Hybrid", after.getFuelType());
    }

    /**
     * Test 6 - updateBus capacity increase failure (B2):
     * Verifies that attempting to increase the capacity of line 1
     * throws an IllegalArgumentException.
     */
    @Test
    void testUpdateBusCapacityIncreaseFails() {
        // Attempt to increase capacity of line 1 from 50 to 80 violating B2
        Bus updatedBus = new Bus("11111111", 80, 90.0, "Diesel");

        assertThrows(IllegalArgumentException.class, () -> {
            busRepository.updateBus(updatedBus);
        });
    }

    // ─── COUNT TESTS ─────────────────────────────────────────────────────────────

    /**
     * Test 7 - countBus success:
     * Verifies the count increases correctly after adding a new entry.
     */
    @Test
    void testCountBusSuccess() throws IOException {        
        // Verify initial count is 1 (only the pre-existing bus)

        // Setting InitialCount = 1
        int initialCount = busRepository.countBus();
        assertEquals(1, initialCount, "Initial count should be 1");

        // Add a new bus
        Bus newBus = new Bus("99999999", 30, 60.0, "Hybrid");
        busRepository.addBus(newBus);

        // Verify the count increased to 2
        int updatedCount = busRepository.countBus();
        assertEquals(2, updatedCount, "Count should increase to 2 after adding a bus");

        // Retrieve it to confirm it was added correctly
        Bus retrieved = busRepository.retrieveBus("99999999");
        assertEquals("99999999", retrieved.getBusID());
        assertEquals(30, retrieved.getCapacity());
        assertEquals(60.0, retrieved.getFuelLevel());
        assertEquals("Hybrid", retrieved.getFuelType());

    }

}