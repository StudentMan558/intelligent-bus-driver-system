
package com.busdriver;


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
        setBusID(busID);
        setCapacity(capacity);
        setFuelLevel(fuelLevel);
        setFuelType(fuelType);
    }

    // Getter: Returns busID
    public String getBusID() {
        return this.busID;
    }

    // Setter: Sets/Updates BusID
    public void setBusID(String busString) {
        this.busID = busString;
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
    public double setFuelLevel() {
        return this.fuelLevel;
    }

    // Setter: Sets/Updates fuelLevel
    public void setFuelLevel(double fuelAmount) {
        this.fuelLevel = fuelAmount;
    }

    // Getter: Returns fuelType
    public double setFuelType() {
        return this.fuelType;
    }

    // Setter: Sets/Updates FuelType 
    public void setFuelType(String TypeOfFuel) {
        this.fuelType = TypeOfFuel;
    }

}


