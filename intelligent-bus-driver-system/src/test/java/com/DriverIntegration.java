package com;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
/**
 * Driver Integration Tests to ensure that the Driver class and DriverRepository work together correctly, especially with file persistence
 * @author [Jake Mastandrea - S4176874]
 */
public class DriverIntegration {

    // This runs before all tests to ensure clean slate, comment out for video to preserve dynamic entry
    @BeforeEach
    public void setup() {
        try (PrintWriter writer = new PrintWriter("data/drivers.txt")) {
            writer.print(""); // Empties the file
        } catch (Exception e) {
            // Ignore if the file doesn't exist, repo will create
        }
    }

    @Test
    public void testAddAndRetrieveDriverLifecycle() throws IOException {
        DriverRepository repo = new DriverRepository();
        
        // 1. Create a valid driver
        Driver newDriver = new Driver("33#$efghAB", "John Doe", 6, "Light", "10|Test St|Melbourne|VIC|Australia", "01-09-2004");
        
        // 2. Add to repo
        repo.add(newDriver);
        
        // 3. Verify count is updated
        assertEquals(1, repo.count(), "Repository count should be exactly 1 after adding a driver.");

        // 4. Retrieve from file and verify the integrity
        Driver retrievedDriver = repo.retrieve("33#$efghAB");
        assertNotNull(retrievedDriver, "Driver should be successfully retrieved from the text file.");
        assertEquals("John Doe", retrievedDriver.getName(), "Retrieved driver name should match.");
        assertEquals("Light", retrievedDriver.getlicenceType(), "Retrieved license type should match.");
    }

    @Test
    public void testUpdateDriverPersistence() throws IOException {
        DriverRepository repo = new DriverRepository();
        
        // 1. Add initial driver (Experience > 10 allows license change)
        Driver driver = new Driver("44%^ijklCD", "Jane Doe", 12, "Medium", "20|Old St|Altona|VIC|Australia", "10-10-2000");
        repo.add(driver);

        // 2. Create an updated driver object (Same ID/Name, but new Address and License)
        Driver updatedDriver = new Driver("44%^ijklCD", "Jane Doe", 13, "Heavy", "99|New St|Altona|VIC|Australia", "10-10-2000");
        
        // 3. Push update to the file
        repo.update(updatedDriver);

        // 4. Retrieve from file to confirm changes were physically saved
        Driver retrieved = repo.retrieve("44%^ijklCD");
        assertEquals("Heavy", retrieved.getlicenceType(), "License type should be successfully updated in the file.");
        assertEquals("99|New St|Altona|VIC|Australia", retrieved.getAddress(), "Address should be successfully updated in the file.");
    }

    @Test
    public void testPreventDuplicateDriverIDs() throws IOException {
        DriverRepository repo = new DriverRepository();
        
        // Two distinct drivers, but they share the same ID
        Driver driver1 = new Driver("55&*mnopEF", "David Joe", 10, "Light", "1|A St|Melbourne|VIC|AU", "01-01-1970");
        Driver driver2 = new Driver("55&*mnopEF", "Duplicate Name", 5, "Medium", "2|B St|Melbourne|VIC|AU", "02-02-1980");
        
        repo.add(driver1); // First one should succeed

        // Second one should fail because the repository checks the file for duplicates
        assertThrows(IllegalArgumentException.class, () -> {
            repo.add(driver2);
        }, "Repository should reject adding a driver with an ID that already exists in the file.");
    }

    @Test
    public void testRepositoryIndependentInstances() throws IOException {
        // This test proves that the data is actually reading and writing from the text file,
        // rather than just being temporarily stored in the repository's local memory.
        
        DriverRepository repo1 = new DriverRepository();
        Driver driver = new Driver("66()qrstGH", "Thomas Smith", 15, "Public Transport", "1|X St|Melbourne|VIC|AU", "05-05-1975");
        repo1.add(driver); // Save using the first instance

        // Create a completely new, disconnected repository instance
        DriverRepository repo2 = new DriverRepository();
        
        // Attempt to retrieve the driver using the new instance
        Driver retrieved = repo2.retrieve("66()qrstGH");
        
        assertNotNull(retrieved, "A new repository instance should be able to read data saved by a previous instance.");
        assertEquals("Thomas Smith", retrieved.getName(), "Data consistency should be maintained across repository instances.");
    }
    
    @Test
    public void testDynamicVideoUpdate() throws IOException {
        DriverRepository repo = new DriverRepository();
        String videoDriverID = "88!@videoXX"; // Our dedicated pre-existing driver

        // 1. ENSURE PRE-EXISTING ENTRY EXISTS
        // If the file is empty, this adds the "past entry" so there is something to edit.
        if (repo.retrieve(videoDriverID) == null) {
            Driver pastEntry = new Driver(videoDriverID, "Demo Driver", 15, "Light", "1|Old St|Altona|VIC|Australia", "01-01-2000");
            repo.add(pastEntry);
        }

        // 2. CREATE A DYNAMIC UPDATE USING THE CLOCK
        // This grabs your computer's live time (e.g., "10:51:23")
        String liveTime = java.time.LocalTime.now().withNano(0).toString(); 
        
        // We inject the live time into the new address
        String dynamicAddress = "1|Updated At " + liveTime + "|Altona|VIC|Australia";
        Driver updatedEntry = new Driver(videoDriverID, "Demo Driver", 15, "Heavy", dynamicAddress, "01-01-2000");

        // 3. EDIT THE PAST ENTRY
        repo.update(updatedEntry);

        // 4. VERIFY
        Driver retrieved = repo.retrieve(videoDriverID);
        assertEquals(dynamicAddress, retrieved.getAddress(), "The file should now contain the live time stamp!");
    }
}