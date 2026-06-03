package com;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.IOException;

/**
 * Driver Unit Tests for validating the Driver class and its interaction with the DriverRepository
 * @author [Jake Mastandrea - S4176874]
 */
public class DriverUnitTest {

    // ==============================================================================
    // CONDITION D1: DRIVER ID VALIDATION
    // Rule: Exactly 10 characters. First 2 must be digits (2-9). Last 2 must be 
    // uppercase letters (A-Z). Middle 6 must contain at least 2 special characters.
    // ==============================================================================

    /**
     * Test Case 1: Valid Driver ID
     * Objective: Verify that a perfectly formatted Driver ID successfully instantiates a Driver object.
     * Input Breakdown: "55" (Valid 2-9 start), "!@abcd" (Contains exactly 2 special chars), "XY" (Valid A-Z end).
     * Expected Result: The getter returns the exact ID provided without throwing an exception.
     */
    @Test
    public void testValidDriverID() {
        Driver driver = new Driver("55!@abcdXY", "John Doe", 5, "Light", "1|Main St|Melbourne|VIC|Australia", "01-09-2004");
        assertEquals("55!@abcdXY", driver.getDriverID(), "Driver ID should exactly match the valid 10-character input.");
    }

    /**
     * Test Case 2: Invalid Driver ID Length (Too Short)
     * Objective: Ensure the system rejects an ID that fails the strict 10-character boundary limit.
     * Input: A 9-character string ("55!@abcXY").
     * Expected Result: IllegalArgumentException is thrown due to invalid length.
     */
    @Test
    public void testDriverIDInvalidLength() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Driver("55!@abcXY", "John Doe", 5, "Light", "1|Main St|Melbourne|VIC|Australia", "01-09-2004");
        }, "Should throw exception because Driver ID is not exactly 10 characters.");
    }

    /**
     * Test Case 3: Invalid Starting Characters
     * Objective: Validate that the first two characters must be strictly between 2 and 9.
     * Input: ID starting with "11" (which falls outside the 2-9 requirement).
     * Expected Result: IllegalArgumentException is thrown by the validation logic.
     */
    @Test
    public void testDriverIDInvalidStartDigits() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Driver("11!@abcdXY", "John Doe", 5, "Light", "1|Main St|Melbourne|VIC|Australia", "01-09-2004");
        }, "Should throw exception because first two characters must be digits 2-9.");
    }

    /**
     * Test Case 4: Invalid Ending Characters
     * Objective: Validate that the final two characters must be uppercase letters.
     * Input: ID ending with lowercase "xy".
     * Expected Result: IllegalArgumentException is thrown.
     */
    @Test
    public void testDriverIDInvalidEndLetters() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Driver("55!@abcdxy", "John Doe", 5, "Light", "1|Main St|Melbourne|VIC|Australia", "01-09-2004");
        }, "Should throw exception because last two characters must be uppercase A-Z.");
    }

    /**
     * Test Case 5: Insufficient Special Characters
     * Objective: Enforce the rule requiring a minimum of two special characters in the middle section.
     * Input: Middle section contains only one special character ('!').
     * Expected Result: IllegalArgumentException is thrown.
     */
    @Test
    public void testDriverIDNotEnoughSpecialChars() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Driver("55!aabcdXY", "John Doe", 5, "Light", "1|Main St|Melbourne|VIC|Australia", "01-09-2004");
        }, "Should throw exception because the middle 6 characters must contain at least two special characters.");
    }


    // ==============================================================================
    // CONDITION D2: ADDRESS FORMAT VALIDATION
    // Rule: Must be perfectly formatted as: Street Number | Street Name | City | State | Country
    // ==============================================================================

    /**
     * Test Case 6: Valid Address Formatting
     * Objective: Verify the system accepts an address split perfectly into 5 parts by the pipe '|' delimiter.
     * Expected Result: The address is stored and retrieved accurately.
     */
    @Test
    public void testValidAddress() {
        Driver driver = new Driver("55!@abcdXY", "John Doe", 5, "Light", "1|Main St|Melbourne|VIC|Australia", "01-09-2004");
        assertEquals("1|Main St|Melbourne|VIC|Australia", driver.getAddress(), "Address should be accepted when properly formatted with 5 pipe-separated parts.");
    }

    /**
     * Test Case 7: Invalid Address (Missing Segments)
     * Objective: Ensure the system rejects addresses missing required data fields.
     * Input: A 4-part address (omitting the country segment).
     * Expected Result: IllegalArgumentException is thrown.
     */
    @Test
    public void testAddressMissingParts() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Driver("55!@abcdXY", "John Doe", 5, "Light", "1|Main St|Melbourne|VIC", "01-09-2004");
        }, "Should throw exception because address is missing the 5th required segment.");
    }

    /**
     * Test Case 8: Invalid Address (Excess Segments)
     * Objective: Prevent invalid, overly long address structures from being saved.
     * Input: A 6-part address.
     * Expected Result: IllegalArgumentException is thrown.
     */
    @Test
    public void testAddressTooManyParts() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Driver("55!@abcdXY", "John Doe", 5, "Light", "1|Main St|Melbourne|VIC|Australia|Earth", "01-09-2004");
        }, "Should throw exception because address has more than 5 segments.");
    }


    // ==============================================================================
    // CONDITION D3: BIRTHDATE VALIDATION
    // Rule: Must stringently follow the exact DD-MM-YYYY format
    // ==============================================================================

    /**
     * Test Case 9: Valid Birthdate Formatting
     * Objective: Confirm dates matching the DD-MM-YYYY regex pattern are successfully processed.
     * Expected Result: Getter returns the exact date string.
     */
    @Test
    public void testValidBirthdate() {
        Driver driver = new Driver("55!@abcdXY", "John Doe", 5, "Light", "1|Main St|Melbourne|VIC|Australia", "01-09-2004");
        assertEquals("01-09-2004", driver.getBirthdate(), "Birthdate should be accepted in DD-MM-YYYY format.");
    }

    /**
     * Test Case 10: Invalid Date Separator
     * Objective: Catch formatting errors where users utilize slashes instead of hyphens.
     * Input: Date separated by slashes (01/09/2004).
     * Expected Result: IllegalArgumentException is thrown to enforce data consistency.
     */
    @Test
    public void testInvalidBirthdateWrongSeparator() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Driver("55!@abcdXY", "John Doe", 5, "Light", "1|Main St|Melbourne|VIC|Australia", "01/09/2004");
        }, "Should throw exception because birthdate uses slashes instead of dashes.");
    }

    /**
     * Test Case 11: Invalid Date Characters
     * Objective: Prevent alphabetical characters from corrupting the birthdate field.
     * Input: Literal "DD-MM-YYYY" alphabetical string.
     * Expected Result: IllegalArgumentException is thrown.
     */
    @Test
    public void testInvalidBirthdateLetters() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Driver("55!@abcdXY", "John Doe", 5, "Light", "1|Main St|Melbourne|VIC|Australia", "DD-MM-YYYY");
        }, "Should throw exception because birthdate contains alphabetical characters.");
    }


    // ==============================================================================
    // CONDITION D4: LICENSE UPDATE RESTRICTIONS
    // Rule: Drivers must possess strictly greater than 10 years experience to upgrade license.
    // ==============================================================================

    /**
     * Test Case 12: Block License Update (Insufficient Experience)
     * Objective: Ensure a driver with 10 years or less experience is blocked from license upgrades.
     * Setup: Original driver in repo has 5 years experience and a "Light" license.
     * Action: Attempt to update the driver's license to "Medium".
     * Expected Result: The repository's update method throws an IllegalArgumentException.
     */
    @Test
    public void testLicenseUpdateFailsInsufficientExperience() throws IOException {
        DriverRepository repo = new DriverRepository();
        
        Driver original = new Driver("88#$efghZZ", "Jane Doe", 5, "Light", "5|High St|Altona|VIC|Australia", "10-10-2000");
        try { repo.add(original); } catch (Exception e) { /* Ignore if already exists for test isolation */ }
        
        Driver updated = new Driver("88#$efghZZ", "Jane Doe", 5, "Medium", "5|High St|Altona|VIC|Australia", "10-10-2000");

        assertThrows(IllegalArgumentException.class, () -> {
            repo.update(updated);
        }, "Condition D4: Should throw exception because driver has 10 or fewer years of experience.");
    }

    /**
     * Test Case 13: Allow License Update (Sufficient Experience)
     * Objective: Ensure drivers exceeding 10 years experience can successfully process upgrades.
     * Setup: Driver has 12 years experience and a "Medium" license.
     * Action: Attempt to update license to "Heavy".
     * Expected Result: The update processes smoothly without throwing exceptions.
     */
    @Test
    public void testLicenseUpdateSucceedsSufficientExperience() throws IOException {
        DriverRepository repo = new DriverRepository();
        
        Driver original = new Driver("99%^ijklWW", "David Joe", 12, "Medium", "5|High St|Altona|VIC|Australia", "10-10-1970");
        try { repo.add(original); } catch (Exception e) { /* Ignore if already exists */ }
        
        Driver updated = new Driver("99%^ijklWW", "David Joe", 12, "Heavy", "5|High St|Altona|VIC|Australia", "10-10-1970");

        assertDoesNotThrow(() -> {
            repo.update(updated);
        }, "Condition D4: Should NOT throw exception because driver has more than 10 years of experience.");
    }


    // ==============================================================================
    // CONDITION D5: IMMUTABLE FIELDS
    // Rule: Driver Name and Driver ID cannot be modified during update operations.
    // ==============================================================================

    /**
     * Test Case 14: Prevent Modification of Driver Name
     * Objective: Ensure the system blocks any updates where the provided driver name differs from the stored name.
     * Action: Push an update where "Thomas Smith" is changed to "Joe Smith".
     * Expected Result: IllegalArgumentException is thrown enforcing immutability.
     */
    @Test
    public void testImmutableDriverNameFailsUpdate() throws IOException {
        DriverRepository repo = new DriverRepository();
        
        Driver original = new Driver("77&*mnopQQ", "Thomas Smith", 15, "Light", "1|Main St|Melbourne|VIC|Australia", "05-05-1975");
        try { repo.add(original); } catch (Exception e) { /* Ignore if already exists */ }
        
        Driver updated = new Driver("77&*mnopQQ", "Joe Smith", 15, "Light", "1|Main St|Melbourne|VIC|Australia", "05-05-1975");

        assertThrows(IllegalArgumentException.class, () -> {
            repo.update(updated);
        }, "Condition D5: Should throw exception because driver name cannot be modified.");
    }

    /**
     * Test Case 15: Prevent Modification of Driver ID
     * Objective: The Driver ID is the primary key. Modifying it means the repository 
     * should fail to locate the original record, throwing an explicit 'not found' exception.
     * Action: Push an update containing a completely fabricated, unregistered ID ("99()failZZ").
     * Expected Result: IllegalArgumentException is thrown since the system cannot locate the ID to overwrite.
     */
    @Test
    public void testImmutableDriverIDIsEnforced() throws IOException {
        DriverRepository repo = new DriverRepository();
        
        Driver original = new Driver("66()testEE", "John Doe", 8, "Light", "1|Main St|Melbourne|VIC|Australia", "01-09-2004");
        try { repo.add(original); } catch (Exception e) { /* Ignore if already exists */ }
        
        Driver updated = new Driver("99()failZZ", "John Doe", 8, "Light", "1|Main St|Melbourne|VIC|Australia", "01-09-2004");

        assertThrows(IllegalArgumentException.class, () -> {
            repo.update(updated);
        }, "Condition D5: Changing the driver ID means the repository will not find the original record to update.");
    }
}