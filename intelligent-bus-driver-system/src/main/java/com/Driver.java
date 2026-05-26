package com;

/**
 * Represents a bus driver in the system
 * @author [Jake Mastandrea - S4176874]
 */
public class Driver {
    private String driverID;
    private String name;
    private int experienceYears;
    private String licenceType; // Light, Medium, Heavy, PublicTransport
    private String address;
    private String birthdate;
    
    // Driver constructor with validation
    public Driver(String driverID, String name, int experienceYears, String licenceType, String address, String birthdate) {
        setDriverID(driverID);
        setName(name);
        setExperienceYears(experienceYears);
        setlicenceType(licenceType);
        setAddress(address);
        setBirthdate(birthdate);
    }

    // Getters
    public String getDriverID() { return driverID; }
    public String getName() { return name; }
    public int getExperienceYears() { return experienceYears; }
    public String getlicenceType() { return licenceType; }
    public String getAddress() { return address; }
    public String getBirthdate() { return birthdate; }

    // Setters and logic for validation

    // Rules for Driver ID such as exactly 10 characters, starts with two digits between 2-9, ends with two uppercase letters, and contains at least two special characters in the middle
    public void setDriverID(String driverID) {
        if (driverID == null || driverID.length() != 10) {
            throw new IllegalArgumentException("Driver ID must be exactly 10 characters long.");
        }

        boolean validStart = driverID.substring(0, 2).matches("^[2-9]{2}$");
        boolean validEnd = driverID.substring(8, 10).matches("^[A-Z]{2}$");
        
        String middle = driverID.substring(2, 8);
        long specialCount = middle.chars().filter(ch -> !Character.isLetterOrDigit(ch)).count();

        if (validStart && validEnd && specialCount >= 2) {
            this.driverID = driverID;
        } else {
            throw new IllegalArgumentException("Driver ID violates character formatting rules.");
        }
    }

    // No constraints for name but ensure that it isn't empty
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        this.name = name;
    }

    // Ensure years of experience is a non-negative int
    public void setExperienceYears(int experienceYears) {
        if (experienceYears < 0) {
            throw new IllegalArgumentException("Experience years cannot be negative.");
        }
        this.experienceYears = experienceYears;
    }

    // Licence type must be light, medium, heavy, or public transport
    public void setlicenceType(String licenceType) {
        if (licenceType.equals("Light") || licenceType.equals("Medium") || 
            licenceType.equals("Heavy") || licenceType.equals("Public Transport")) {
            this.licenceType = licenceType;
        } else {
            throw new IllegalArgumentException("Invalid licence Type. Must be Light, Medium, Heavy, or Public Transport.");
        }
    }

    // Address must be in the following format: Street Number | Street Name | City | State | Country
    public void setAddress(String address) {
        if (address != null && address.split("\\|").length == 5) {
            this.address = address;
        } else {
            throw new IllegalArgumentException("Address must be in format: Street Number | Street Name | City | State | Country");
        }
    }

    // Birthdate must be formatted as DD-MM-YYYY
    public void setBirthdate(String birthdate) {
        if (birthdate != null && birthdate.matches("^\\d{2}-\\d{2}-\\d{4}$")) {
            this.birthdate = birthdate;
        } else {
            throw new IllegalArgumentException("Birthdate must follow the format: DD-MM-YYYY");
        }
    }
}