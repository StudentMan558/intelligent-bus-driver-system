package com;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Repository for managing bus data persistence
 * @author [Team Member Names]
 */
public class BusRepository {

    private static final String FILE_PATH = "buses.txt";

    // create a bus object with only placeholder values
    private Bus bus;

    // TODO: Implement Add(), Update(), Retrieve(), Count() methods

    
    // collect variables for bus object from data/buses.txt
    while ((line = reader.readLine()) != null) {
        try (FileInputStream input = new FileInputStream("data\\buses.txt")) {
            
        }
    }

    //the following updates the bus objects parameters with the correct types
    setBusID();
    setCapacity();
    setFuelLevel();
    setFuelType();

    //the following returns the number of stored busses 
    public int getStoredBusAmt() {
        // read from file and set to variable
        try (FileInputStream input = new FileInputStream("filename.txt")) {

        }
        int busAmt;
        int this.busAmt = // test data from tests
        return busAmt;  
    }

}