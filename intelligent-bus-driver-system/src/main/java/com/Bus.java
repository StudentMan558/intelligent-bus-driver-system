package com;

// For formatting driver age
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

/**
 * Represents a bus in the system
 * @author Adam Dimkovski (s4168373)
 */

public class Bus {
    private String busID;
    private int capacity;
    private double fuelLevel;
    private String fuelType; // Diesel, Hybrid, Electricity

    // Basic Constructor (Variable names subject to change)
    public Bus(String busID, int capacity, double fuelLevel, String fuelType) {
        this.busID = busID;
        this.capacity = capacity;
        this.fuelLevel = fuelLevel;
        this.fuelType = fuelType;
    }

    // Getter: Returns busID
    public String getBusID() {
        return this.busID;
    }

    // B1.1: All BusID must be unique, No busID can be Duplicates
    // B1.2: The busID must be exactly 8 characters long, all characters must be digits
    // Setter: Sets/Updates BusID
    public void setBusID(String busString) {
        // First, validate the format (8 digits)
        if (busString == null || busString.length() != 8 || !busString.matches("\\d+")) {
            throw new IllegalArgumentException("BusID Invalid: must be exactly 8 digits");
        }
        
        // Check if this busID already exists in the file (only if it's different from current busID)
        if (!busString.equals(this.busID) && busIDExists(busString)) {
            throw new IllegalArgumentException("BusID already exists: " + busString);
        }
        else {
        // If all validations pass, set the busID
        this.busID = busString;
        }
    }

    // Helper method to check if a busID already exists in the file (B1.1)
    private boolean busIDExists(String busString) {

    String busesData = "data/buses.txt";

    try (Stream<String> stream = Files.lines(Paths.get(busesData))) {

        return stream.anyMatch(line -> {

            line = line.trim();
            System.out.println("Checking line: " + line);

            // Check line starts correctly
            if (!line.startsWith("busID")) {
                return false;
            }

            // Split by commas
            String[] parts = line.split(",");

            // Extract first section
            String firstPart = parts[0].trim();

            // Remove "busID"
            String existingBusID =
                firstPart.replace("busID", "").trim();

                System.out.println("Extracted ID: " + existingBusID);
                System.out.println("Input ID: " + busString);

            return existingBusID.equals(busString);
        });

    } catch (IOException e) {

        throw new IllegalArgumentException(
            "buses.txt file not found"
        );
    }
}

    // Getter: Returns capacity
    public int getCapacity() {
        return this.capacity;
    }

    // Setter: Sets/Updates Capacity
    public void setCapacity(int busSpace, Driver driver) {

        // Condition B3: Driver age must be less then 50 to drive buses with a capacity of 50 or more. 
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate driverBirthDate = LocalDate.parse(driver.getBirthdate(), formatter);
        LocalDate currentDate = LocalDate.now();

        int driverAge = Period.between(driverBirthDate, currentDate).getYears();
        
        // Checks to see if capacity is greater than 50
        if(busSpace >= 50) {

            // Capacity >= 50 and checks if drivers age is less then 50 (B3)
            if(driverAge < 50 && driverAge > 0) {
                this.capacity = busSpace;
            }
            else {
                throw new IllegalArgumentException("Driver Age must be 50 and Valid");  
            }
        }
        else {
            // Passes capacity through if not greater then 50 all drivers can operate
            this.capacity = busSpace;
        }
    }

    // Getter: Returns fuelLevel
    public double getFuelLevel() {
        return this.fuelLevel;
    }

    // Setter: Sets/Updates fuelLevel
    public void setFuelLevel(double fuelAmount) {
        this.fuelLevel = fuelAmount;
    }

    // Getter: Returns fuelType
    public String getFuelType() {
        return this.fuelType;
    }

    // Setter: Sets/Updates FuelType 
    public void setFuelType(String TypeOfFuel, Driver driver) {

        // Condition B5: Only drivers holding a Heavy or Public Transport licence are permitted to operate electric and hybrid buses
        // Checks for appropriate fuel types
        if(TypeOfFuel.equalsIgnoreCase("Diesel")) {
        this.fuelType = TypeOfFuel;
        }

        // Checks to see if Drivers are able to operate buses
        else if (TypeOfFuel.equalsIgnoreCase("Hybrid")) {
            
            // Checks to see if driver has correct liscense type (B5)
            if (driver.getlicenceType().equals("Heavy") || driver.getlicenceType().equals("Public Transport")) {
            this.fuelType = TypeOfFuel;    
            }

            // If driver does not have right liscence type
            else {
            throw new IllegalArgumentException("Driver Liscense Invalid");    
            }
        }
        // Condition B4: Only drivers with at least 5 years of experience can drive electric buses (fuelType = "Electricity")
        else if (TypeOfFuel.equalsIgnoreCase("Electricity")) {

            // Checks to see if driver has correct liscense type (B5)
            if (driver.getlicenceType().equals("Heavy") || driver.getlicenceType().equals("Public Transport")) {

                // Checks if driver has adequete experience for electric bus (B4)
                if (driver.getExperienceYears() >= 5) {
                    this.fuelType = TypeOfFuel;
                }
                else {
                throw new IllegalArgumentException("Driver needs 5 or more years experience to operate electric buses");    
                }
            }
            else {
            // Throw Error if not Heavy or Public Transport license
            throw new IllegalArgumentException("Drivers Liscense must be Heavy or Public Transport");
            }
            
        }
        // If driver does not have right liscence type
        else { throw new IllegalArgumentException("Driver Liscense Invalid"); }
    }
}

    // TO DO:
    // Condition B2: busCapacity cannot increase during update operations. However, it can decrease





