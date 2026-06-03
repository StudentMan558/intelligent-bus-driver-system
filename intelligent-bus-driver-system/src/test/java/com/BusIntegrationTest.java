package com;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Integration tests for BusRepository.
 * Tests verify that valid buses are stored correctly,
 * invalid buses are rejected, updates are persisted correctly,
 * and record counts are updated correctly.
 * @author [Ned Santrac]
 */
public class BusIntegrationTest {

    // Fields declared at class level so all methods can access them
    private BusRepository busRepository;
    private int originalCount;

    /**
     * Creates a fresh BusRepository, ensures the data folder exists,
     * records the original entry count, and seeds one known bus entry
     * before each test.
     */
    @BeforeEach
    void setUp() throws IOException {
        busRepository = new BusRepository();

        // Create the data folder if it does not already exist
        new File("data").mkdirs();

        // Record how many entries already exist before we add test data
        originalCount = busRepository.countBus();

        // Seed the file with a known bus entry to test against
        Bus seedBus = new Bus("12345678", 40, 85.5, "Diesel");
        busRepository.addBus(seedBus);
    }

    /**
     * Reads all current entries, deletes the file, then rewrites
     * only the original entries to remove any test data added.
     */
    @AfterEach
    void tearDown() throws IOException {
        // Read all current lines before deleting
        List<Bus> allBuses = busRepository.retrieveAllBus();

        // Delete the file
        new File("data\\buses.txt").delete();

        // Rewrite only up to the original count
        for (int i = 0; i < originalCount; i++) {
            busRepository.addBus(allBuses.get(i));
        }
    }

    /**
     * Test 1 - addBus success:
     * Verifies that a valid bus is written to the file correctly
     * by adding it and then retrieving it to confirm the data matches.
     */
    @Test
    void testAddBusSuccess() throws IOException {
        // Add a new unique bus
        Bus newBus = new Bus("87654321", 30, 60.0, "Hybrid");
        busRepository.addBus(newBus);

        // Retrieve it and confirm the data was stored correctly
        Bus retrieved = busRepository.retrieveBus("87654321");
        assertEquals("87654321", retrieved.getBusID());
        assertEquals(30, retrieved.getCapacity());
        assertEquals(60.0, retrieved.getFuelLevel());
        assertEquals("Hybrid", retrieved.getFuelType());
    }

    /**
     * Test 2 - addBus duplicate failure:
     * Verifies that adding a bus with a duplicate ID is rejected
     * and throws an IllegalArgumentException.
     */
    @Test
    void testAddBusDuplicateIDFails() {
        // Attempt to add a bus with the same ID as the seeded entry
        Bus duplicateBus = new Bus("12345678", 20, 40.0, "Electricity");

        assertThrows(IllegalArgumentException.class, () -> {
            busRepository.addBus(duplicateBus);
        });
    }

    /**
     * Test 3 - retrieveBus success:
     * Verifies that an existing bus is retrieved correctly by ID.
     */
    @Test
    void testRetrieveBusSuccess() throws IOException {
        // Retrieve the seeded bus and confirm the data matches
        Bus retrieved = busRepository.retrieveBus("12345678");
        assertEquals("12345678", retrieved.getBusID());
        assertEquals(40, retrieved.getCapacity());
        assertEquals(85.5, retrieved.getFuelLevel());
        assertEquals("Diesel", retrieved.getFuelType());
    }

    /**
     * Test 4 - retrieveBus failure:
     * Verifies that retrieving a non-existent ID throws
     * an IllegalArgumentException.
     */
    @Test
    void testRetrieveBusNotFoundFails() {
        // Attempt to retrieve a bus ID that does not exist in the file
        assertThrows(IllegalArgumentException.class, () -> {
            busRepository.retrieveBus("00000000");
        });
    }

    /**
     * Test 5 - updateBus success:
     * Verifies that updating an existing bus persists the new values
     * correctly to the file.
     */
    @Test
    void testUpdateBusSuccess() throws IOException {
        // Update the seeded bus with a lower capacity and new fuel level
        Bus updatedBus = new Bus("12345678", 30, 50.0, "Diesel");
        busRepository.updateBus(updatedBus);

        // Retrieve and confirm the updated values were persisted
        Bus retrieved = busRepository.retrieveBus("12345678");
        assertEquals(30, retrieved.getCapacity());
        assertEquals(50.0, retrieved.getFuelLevel());
    }

    /**
     * Test 6 - updateBus capacity increase failure (B2):
     * Verifies that attempting to increase bus capacity during
     * an update throws an IllegalArgumentException.
     */
    @Test
    void testUpdateBusCapacityIncreaseFails() {
        // Attempt to increase capacity from 40 to 60 which violates B2
        Bus updatedBus = new Bus("12345678", 60, 85.5, "Diesel");

        assertThrows(IllegalArgumentException.class, () -> {
            busRepository.updateBus(updatedBus);
        });
    }

    /**
     * Test 7 - countBus success:
     * Verifies that the count reflects the correct number of entries
     * after adding a second bus to the seeded file.
     */
    @Test
    void testCountBusSuccess() throws IOException {
        // Add a second bus so there are now two entries on top of originals
        Bus secondBus = new Bus("87654321", 30, 60.0, "Hybrid");
        busRepository.addBus(secondBus);

        // Count should now return originalCount + 2 (seed + second)
        assertEquals(originalCount + 2, busRepository.countBus());
    }
}