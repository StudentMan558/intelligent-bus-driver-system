package com;

/**
 * Represents a bus in the system
 * @author Adam Dimkovski (s4168373)
 */


public class Bus {
    private String busID;
    private int capacity;
    private double fuelLevel;
    private String fuelType; // Diesel, Hybrid, Electricity

    // TODO: Add constructors, getters, setters, and validation methods

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

    // Setter: Sets/Updates BusID
    public void setBusID(String busString) {

        // Checks that busString contains 8 characters and is fully digits.
        if (busString.length() == 8 && busString.matches("\\d+")) {
            this.busID = busString; }

        // Handles Invalid BusID
        else { System.out.println("BusID Invalid"); }
    }

    // Getter: Returns capacity
    public int getCapacity() {
        return this.capacity;
    }

    // Setter: Sets/Updates Capacity
    public void setCapacity(int busSpace) {
        this.capacity = busSpace;
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
    public void setFuelType(String TypeOfFuel) {

        // Checks for appropriate fuel types
        if(TypeOfFuel.equalsIgnoreCase("Diesel") 
        || TypeOfFuel.equalsIgnoreCase("Hybrid")
        || TypeOfFuel.equalsIgnoreCase("Electricity")) {
            this.fuelType = TypeOfFuel;
        }

        // Handles Invalid Fuel Types
        else { System.out.println("Fuel Type Invalid"); }
    }

}


