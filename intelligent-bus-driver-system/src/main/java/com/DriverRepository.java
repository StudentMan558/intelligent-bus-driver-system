package com;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Repository for managing driver data persistence
 * @author [Jake Mastandrea - S4176874]
 */
public class DriverRepository {
    private static final String DATA_FILE = "data/drivers.txt";
   
    // Adds driver to text file only if the driver ID is unique
    public void add(Driver driver) throws IOException {
        ensureFileExists();
        
        if (retrieve(driver.getDriverID()) != null) {
            throw new IllegalArgumentException("Driver ID already exists. Cannot add duplicate.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE, true))) {
            writer.write(formatDriver(driver));
            writer.newLine();
        }
    }

    // Retrieves driver by ID from text file, null returned if not found
    public Driver retrieve(String driverID) throws IOException {
        ensureFileExists();

        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] parts = line.split(",");
                if (parts[0].equals(driverID)) {
                    
                    return new Driver(parts[0], parts[1], Integer.parseInt(parts[2]), parts[3], parts[4], parts[5]);
                }
            }
        }
        return null;
    }

    // Updates an existing driver while enforcing conditions such as immutable fields and licence type update restrictions
    public void update(Driver updatedDriver) throws IOException {
        ensureFileExists();
        List<Driver> allDrivers = getAllDrivers();
        boolean found = false;

        for (int i = 0; i < allDrivers.size(); i++) {
            Driver existingDriver = allDrivers.get(i);
            
            if (existingDriver.getDriverID().equals(updatedDriver.getDriverID())) {
                
                // Immutable fields check
                if (!existingDriver.getName().equals(updatedDriver.getName())) {
                    throw new IllegalArgumentException("Condition D5: Driver name cannot be modified.");
                }

                // licence Type Update Restriction
                if (!existingDriver.getlicenceType().equals(updatedDriver.getlicenceType())) {
                    if (existingDriver.getExperienceYears() <= 10) {
                        throw new IllegalArgumentException("Condition D4: Driver must have more than 10 years of experience to change licence type.");
                    }
                }

                // Replace the existing driver with the updated driver if conditions are met
                allDrivers.set(i, updatedDriver);
                found = true;
                break;
            }
        }

        if (!found) {
            throw new IllegalArgumentException("Driver not found for update.");
        }

        // Overwrite the file with the updated list
        saveAllDrivers(allDrivers);
    }

    // Return count of drivers in the text file
    public int count() throws IOException {
        ensureFileExists();
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            while (reader.readLine() != null) {
                count++;
            }
        }
        return count;
    }
    // Helper methods
    private void ensureFileExists() throws IOException {
        File file = new File(DATA_FILE);
        file.getParentFile().mkdirs(); // Creates the 'data' folder if it doesn't exist
        file.createNewFile();          // Creates the file if it doesn't exist
    }

    private List<Driver> getAllDrivers() throws IOException {
        List<Driver> drivers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                drivers.add(new Driver(parts[0], parts[1], Integer.parseInt(parts[2]), parts[3], parts[4], parts[5]));
            }
        }
        return drivers;
    }

    private void saveAllDrivers(List<Driver> drivers) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE, false))) {
            for (Driver driver : drivers) {
                writer.write(formatDriver(driver));
                writer.newLine();
            }
        }
    }

    private String formatDriver(Driver driver) {
        return String.join(",", 
            driver.getDriverID(), 
            driver.getName(), 
            String.valueOf(driver.getExperienceYears()), 
            driver.getlicenceType(), 
            driver.getAddress(), 
            driver.getBirthdate()
        );
    }
}