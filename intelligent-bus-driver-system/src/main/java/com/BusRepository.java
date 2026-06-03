package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles Add, Retrieve, RetrieveAll, Update, and Count for buses.txt
 * @author [Ned Santrac]
 */
public class BusRepository {

    private static final String FILE_PATH = "data\\buses.txt";

    public BusRepository() {}

    /**
     * Takes a Bus object breaks it down into its variables,
     * checks for duplicate IDs and writes it to buses.txt if unique.
     * Throws an exception if a duplicate ID is detected.
     */
    public boolean addBus(Bus bus) throws IOException {
        File file = new File(FILE_PATH);

        // Check for duplicate ID by reading through existing entries
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
                String line;

                // Loop through every entry in the file
                while ((line = reader.readLine()) != null) {
                    if (line.isBlank()) continue;

                    // Extract just the ID from this line for comparison
                    String existingID = line.split(",")[0].split(" ")[1].trim();

                    // If a duplicate ID is found, deny the add and throw
                    if (existingID.equals(bus.getBusID())) {
                        throw new IllegalArgumentException(
                            "Duplicate bus ID detected, entry denied: " + bus.getBusID());
                    }
                }
            }
        }

        // No duplicate found break the bus object down into its variables
        String busID     = bus.getBusID();
        int capacity     = bus.getCapacity();
        double fuelLevel = bus.getFuelLevel();
        String fuelType  = bus.getFuelType();

        // Write the variables to the file in the expected format
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write("busID " + busID +
                         ",capacity " + capacity +
                         ",fuelLevel " + fuelLevel +
                         ",fuelType " + fuelType);
            writer.newLine();
        }

        return true;
    }

    /**
     * Reads buses.txt line by line and returns the single Bus object
     * whose ID matches the given parameter.
     * Throws an exception if the file is not found or no match exists.
     */
    public Bus retrieveBus(String busID) throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            throw new IllegalArgumentException("buses.txt file not found.");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            // Read every line until the file is empty
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                // Split the line by comma to get each field
                String[] parts = line.split(",");

                // Extract the value after the space for each field
                String id        = parts[0].split(" ")[1].trim();
                int capacity     = Integer.parseInt(parts[1].split(" ")[1].trim());
                double fuelLevel = Double.parseDouble(parts[2].split(" ")[1].trim());
                String fuelType  = parts[3].split(" ")[1].trim();

                // Check if this line matches the requested ID
                if (id.equals(busID)) {
                    return new Bus(id, capacity, fuelLevel, fuelType);
                }
            }
        }

        // No matching bus found after reading the entire file
        throw new IllegalArgumentException("No bus found with ID: " + busID);
    }

    /**
     * Reads every entry in buses.txt and returns them all
     * as a list of Bus objects.
     * Returns an empty list if the file does not exist.
     */
    public List<Bus> retrieveAllBus() throws IOException {
        List<Bus> buses = new ArrayList<>();
        File file = new File(FILE_PATH);

        // If file doesn't exist yet return empty list instead of throwing
        if (!file.exists()) return buses;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            // Read every line until the file is empty
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                // Split the line by comma to get each field
                String[] parts   = line.split(",");
                String id        = parts[0].split(" ")[1].trim();
                int capacity     = Integer.parseInt(parts[1].split(" ")[1].trim());
                double fuelLevel = Double.parseDouble(parts[2].split(" ")[1].trim());
                String fuelType  = parts[3].split(" ")[1].trim();

                // Create a fresh Bus object for this entry and add to list
                buses.add(new Bus(id, capacity, fuelLevel, fuelType));
            }
        }

        // Return the full list of Bus objects
        return buses;
    }

    /**
     * Reads through buses.txt and returns the total number
     * of entries as an integer.
     * Returns 0 if the file does not exist.
     */
    public int countBus() throws IOException {
        File file = new File(FILE_PATH);

        // If file doesn't exist yet there are simply no entries
        if (!file.exists()) return 0;

        // Counter to track the number of entries
        int count = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            // Increment the counter for every non blank line
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) count++;
            }
        }

        // Return the total count
        return count;
    }

    /**
     * Takes a Bus object finds the matching entry in buses.txt by ID
     * and overwrites all other fields with the new values.
     * Throws an exception if no matching ID is found.
     */
    public boolean updateBus(Bus updated) throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            throw new IllegalArgumentException("buses.txt file not found.");
        }

        // List to hold all lines as we rewrite the file
        List<String> allLines = new ArrayList<>();
        boolean matchFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            // Read every line and check if it matches the target ID
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                // Extract the ID from this line for comparison
                String[] parts    = line.split(",");
                String existingID = parts[0].split(" ")[1].trim();
                int existingCap   = Integer.parseInt(parts[1].split(" ")[1].trim());

                if (existingID.equals(updated.getBusID())) {
                    // Match found enforce B2 before updating
                    if (updated.getCapacity() > existingCap) {
                        throw new IllegalArgumentException(
                            "Bus capacity cannot be increased during an update.");
                    }

                    // Replace the old line with the updated values
                    allLines.add("busID " + updated.getBusID() +
                                 ",capacity " + updated.getCapacity() +
                                 ",fuelLevel " + updated.getFuelLevel() +
                                 ",fuelType " + updated.getFuelType());
                    matchFound = true;
                } else {
                    // No match so keep the existing line unchanged
                    allLines.add(line);
                }
            }
        }

        // If no matching ID was found throw an exception
        if (!matchFound) {
            throw new IllegalArgumentException("No bus found with ID: " + updated.getBusID());
        }

        // Rewrite the entire file with the updated lines
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (String l : allLines) {
                writer.write(l);
                writer.newLine();
            }
        }

        return true;
    }
}