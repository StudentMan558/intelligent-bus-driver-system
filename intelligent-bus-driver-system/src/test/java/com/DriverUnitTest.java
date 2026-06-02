package com;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.IOException;
/**
 * Driver Unit Tests for validating the Driver class and its interaction with the DriverRepository
 * @author [Jake Mastandrea - S4176874]
 */
public class DriverUnitTest {

    // --- Condition D1: Driver ID Validation ---
    // Rule: Exactly 10 chars, starts with 2-9 (2 chars), ends with A-Z (2 chars), middle has >= 2 special chars.

    @Test
    public void testValidDriverID() {
        // '55' (Valid Start), '!@abcd' (Contains 2 special chars), 'XY' (Valid End)
        Driver driver = new Driver("55!@abcdXY", "John Doe", 5, "Light", "1|Main St|Melbourne|VIC|Australia", "01-09-2004");
        assertEquals("55!@abcdXY", driver.getDriverID(), "Driver ID should exactly match the valid 10-character input.");
    }

    @Test
    public void testDriverIDInvalidLength() {
        assertThrows(IllegalArgumentException.class, () -> {
            // 9 characters (Too short)
            new Driver("55!@abcXY", "John Doe", 5, "Light", "1|Main St|Melbourne|VIC|Australia", "01-09-2004");
        }, "Should throw exception because Driver ID is not exactly 10 characters.");
    }

    @Test
    public void testDriverIDInvalidStartDigits() {
        assertThrows(IllegalArgumentException.class, () -> {
            // Starts with '11' (Rule requires digits between 2 and 9)
            new Driver("11!@abcdXY", "John Doe", 5, "Light", "1|Main St|Melbourne|VIC|Australia", "01-09-2004");
        }, "Should throw exception because first two characters must be digits 2-9.");
    }

    @Test
    public void testDriverIDInvalidEndLetters() {
        assertThrows(IllegalArgumentException.class, () -> {
            // Ends with 'xy' (Lowercase instead of uppercase)
            new Driver("55!@abcdxy", "John Doe", 5, "Light", "1|Main St|Melbourne|VIC|Australia", "01-09-2004");
        }, "Should throw exception because last two characters must be uppercase A-Z.");
    }

    @Test
    public void testDriverIDNotEnoughSpecialChars() {
        assertThrows(IllegalArgumentException.class, () -> {
            // Middle section only has one special character ('!')
            new Driver("55!aabcdXY", "John Doe", 5, "Light", "1|Main St|Melbourne|VIC|Australia", "01-09-2004");
        }, "Should throw exception because the middle 6 characters must contain at least two special characters.");
    }


    // --- Condition D2: Address Format Validation ---
    // Rule: Must be in format: Street Number | Street Name | City | State | Country

    @Test
    public void testValidAddress() {
        Driver driver = new Driver("55!@abcdXY", "John Doe", 5, "Light", "1|Main St|Melbourne|VIC|Australia", "01-09-2004");
        assertEquals("1|Main St|Melbourne|VIC|Australia", driver.getAddress(), "Address should be accepted when properly formatted with 5 pipe-separated parts.");
    }

    @Test
    public void testAddressMissingParts() {
        assertThrows(IllegalArgumentException.class, () -> {
            // Only 4 parts (Missing country)
            new Driver("55!@abcdXY", "John Doe", 5, "Light", "1|Main St|Melbourne|VIC", "01-09-2004");
        }, "Should throw exception because address is missing the 5th required segment.");
    }

    @Test
    public void testAddressTooManyParts() {
        assertThrows(IllegalArgumentException.class, () -> {
            // 6 parts
            new Driver("55!@abcdXY", "John Doe", 5, "Light", "1|Main St|Melbourne|VIC|Australia|Earth", "01-09-2004");
        }, "Should throw exception because address has more than 5 segments.");
    }


    // --- Condition D3: Birthdate Validation ---
    // Rule: DD-MM-YYYY format

    @Test
    public void testValidBirthdate() {
        Driver driver = new Driver("55!@abcdXY", "John Doe", 5, "Light", "1|Main St|Melbourne|VIC|Australia", "01-09-2004");
        assertEquals("01-09-2004", driver.getBirthdate(), "Birthdate should be accepted in DD-MM-YYYY format.");
    }

    @Test
    public void testInvalidBirthdateWrongSeparator() {
        assertThrows(IllegalArgumentException.class, () -> {
            // Using slashes instead of dashes
            new Driver("55!@abcdXY", "John Doe", 5, "Light", "1|Main St|Melbourne|VIC|Australia", "01/09/2004");
        }, "Should throw exception because birthdate uses slashes instead of dashes.");
    }

    @Test
    public void testInvalidBirthdateLetters() {
        assertThrows(IllegalArgumentException.class, () -> {
            // Using alphabetical characters
            new Driver("55!@abcdXY", "John Doe", 5, "Light", "1|Main St|Melbourne|VIC|Australia", "DD-MM-YYYY");
        }, "Should throw exception because birthdate contains alphabetical characters.");
    }


    // --- Condition D4: License Update Restrictions ---
    // Rule: Must have > 10 years experience to change license type

    @Test
    public void testLicenseUpdateFailsInsufficientExperience() throws IOException {
        DriverRepository repo = new DriverRepository();
        // 5 years of experience (<= 10)
        Driver original = new Driver("88#$efghZZ", "Jane Doe", 5, "Light", "5|High St|Altona|VIC|Australia", "10-10-2000");
        try { repo.add(original); } catch (Exception e) { /* Ignore if already exists for test isolation */ }
        
        // Attempting to change license from "Light" to "Medium"
        Driver updated = new Driver("88#$efghZZ", "Jane Doe", 5, "Medium", "5|High St|Altona|VIC|Australia", "10-10-2000");

        assertThrows(IllegalArgumentException.class, () -> {
            repo.update(updated);
        }, "Condition D4: Should throw exception because driver has 10 or fewer years of experience.");
    }

    @Test
    public void testLicenseUpdateSucceedsSufficientExperience() throws IOException {
        DriverRepository repo = new DriverRepository();
        // 12 years of experience (> 10)
        Driver original = new Driver("99%^ijklWW", "David Joe", 12, "Medium", "5|High St|Altona|VIC|Australia", "10-10-1970");
        try { repo.add(original); } catch (Exception e) { /* Ignore if already exists */ }
        
        // Attempting to change license from "Medium" to "Heavy"
        Driver updated = new Driver("99%^ijklWW", "David Joe", 12, "Heavy", "5|High St|Altona|VIC|Australia", "10-10-1970");

        assertDoesNotThrow(() -> {
            repo.update(updated);
        }, "Condition D4: Should NOT throw exception because driver has more than 10 years of experience.");
    }


    // --- Condition D5: Immutable Fields ---
    // Rule: Driver Name and ID cannot be modified during update

    @Test
    public void testImmutableDriverNameFailsUpdate() throws IOException {
        DriverRepository repo = new DriverRepository();
        Driver original = new Driver("77&*mnopQQ", "Thomas Smith", 15, "Light", "1|Main St|Melbourne|VIC|Australia", "05-05-1975");
        try { repo.add(original); } catch (Exception e) { /* Ignore if already exists */ }
        
        // Attempting to change the name
        Driver updated = new Driver("77&*mnopQQ", "Joe Smith", 15, "Light", "1|Main St|Melbourne|VIC|Australia", "05-05-1975");

        assertThrows(IllegalArgumentException.class, () -> {
            repo.update(updated);
        }, "Condition D5: Should throw exception because driver name cannot be modified.");
    }

    @Test
    public void testImmutableDriverIDIsEnforced() throws IOException {
        DriverRepository repo = new DriverRepository();
        
        Driver original = new Driver("66()testEE", "John Doe", 8, "Light", "1|Main St|Melbourne|VIC|Australia", "01-09-2004");
        try { repo.add(original); } catch (Exception e) { /* Ignore if already exists */ }
        
        // ID here that we guarantee is nowhere in drivers.txt
        Driver updated = new Driver("99()failZZ", "John Doe", 8, "Light", "1|Main St|Melbourne|VIC|Australia", "01-09-2004");

        assertThrows(IllegalArgumentException.class, () -> {
            repo.update(updated);
        }, "Condition D5: Changing the driver ID means the repository will not find the original record to update.");
    }
}