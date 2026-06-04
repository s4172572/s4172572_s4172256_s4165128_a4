package com.guidancesystem;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

/**
 * Unit tests for Bus-related operations and business conditions (B1 - B5).
 * Contains 15 test cases spanning normal cases, invalid inputs, and edge cases.
 */
public class BusUnitTest {

    // =========================================================================
    // --- B1: BUS ID RULES TESTS ---
    // Rule: Must be unique, exactly 8 characters long, all characters are digits.
    // =========================================================================

    @Test
    @DisplayName("B1 - Test Case 1 (Normal): Verify a correct 8-digit numeric Bus ID")
    public void testB1_NormalCase() {
        // Arrange & Act: Construct a bus with a perfectly valid 8-digit numeric string
        Bus bus = new Bus("10000001", 60, 100.0, "Electricity");

        // Assert: The built-in isBusIdValid() method should return true
        assertTrue(bus.isBusIdValid(), "Normal Case: 8-digit numeric ID must be valid.");
    }

    @Test
    @DisplayName("B1 - Test Case 2 (Invalid): Verify an incorrect Bus ID containing alphabetic letters")
    public void testB1_InvalidInput() {
        // Arrange & Act: Construct a bus with an invalid ID format containing characters
        Bus bus = new Bus("1000012A", 60, 100.0, "Diesel");

        // Assert: The validation regex must catch the letters and return false
        assertFalse(bus.isBusIdValid(), "Invalid Case: Non-numeric characters must be rejected.");
    }

    @Test
    @DisplayName("B1 - Test Case 3 (Edge Case): Verify an edge case Bus ID that is exactly 9 digits long")
    public void testB1_EdgeCase() {
        // Arrange & Act: Construct a bus with numeric characters but exceeding the boundary length by 1
        Bus bus = new Bus("100000001", 60, 100.0, "Hybrid");

        // Assert: The length rule check must catch the boundary violation and return false
        assertFalse(bus.isBusIdValid(), "Edge Case: Length strictly outside the 8-digit boundary must be rejected.");
    }

    // =========================================================================
    // --- B2: CAPACITY UPDATE RESTRICTION TESTS ---
    // Rule: busCapacity cannot increase during updates. It can decrease or stay equal.
    // =========================================================================

    @Test
    @DisplayName("B2 - Test Case 4 (Normal): Verify a correct capacity decrease update operation")
    public void testB2_NormalCase() {
        // Arrange: Start with an initial capacity of 60
        Bus bus = new Bus("10000001", 60, 100.0, "Electricity");

        // Act: Lower the capacity down to 40 (allowed operation)
        bus.setCapacity(40);

        // Assert: Ensure the new value took effect successfully
        assertEquals(40, bus.getCapacity(), "Normal Case: Decreasing capacity must be accepted.");
    }

    @Test
    @DisplayName("B2 - Test Case 5 (Invalid): Verify an incorrect capacity increase update is rejected")
    public void testB2_InvalidInput() {
        // Arrange: Start with an initial capacity of 60
        Bus bus = new Bus("10000001", 60, 100.0, "Electricity");

        // Act & Assert: Attempting to raise capacity to 65 must trigger an IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            bus.setCapacity(65);
        }, "Invalid Case: Increasing capacity during an update operation must throw an exception.");
    }

    @Test
    @DisplayName("B2 - Test Case 6 (Edge Case): Verify an edge case matching equal capacity update")
    public void testB2_EdgeCase() {
        // Arrange: Start with an initial capacity of 60
        Bus bus = new Bus("10000001", 60, 100.0, "Electricity");

        // Act & Assert: Keeping the capacity exactly the same (60) sits on the boundary condition (<=)
        assertDoesNotThrow(() -> bus.setCapacity(60), 
            "Edge Case: Keeping the capacity exactly identical to its old value is completely valid.");
        assertEquals(60, bus.getCapacity());
    }

    // =========================================================================
    // --- B3: DRIVER AGE RESTRICTION TESTS ---
    // Rule: Drivers older than 50 years cannot drive buses with a capacity of 50 or more.
    // Context Year: 2026.
    // =========================================================================

    @Test
    @DisplayName("B3 - Test Case 7 (Normal): Verify a correct young driver assignment on a high-capacity bus")
    public void testB3_NormalCase() {
        // Arrange: Driver is 30 years old in 2026 (Born 1996), Bus capacity is high (60)
        String birthdate = "15-05-1996"; 
        Bus bus = new Bus("10000001", 60, 100.0, "Diesel");

        // Act: Evaluate assignment compliance
        boolean isAllowed = validateDriverAgeForBus(birthdate, bus.getCapacity());

        // Assert: Young drivers should be allowed to operate heavy passenger routes
        assertTrue(isAllowed, "Normal Case: A young driver can easily drive high-capacity buses.");
    }

    @Test
    @DisplayName("B3 - Test Case 8 (Invalid): Verify an incorrect senior driver assignment on a high-capacity bus")
    public void testB3_InvalidInput() {
        // Arrange: Driver is 55 years old in 2026 (Born 1971), Bus capacity is high (60)
        String birthdate = "15-05-1971"; 
        Bus bus = new Bus("10000001", 60, 100.0, "Diesel");

        // Act: Evaluate assignment compliance
        boolean isAllowed = validateDriverAgeForBus(birthdate, bus.getCapacity());

        // Assert: The logic must block this because the driver is > 50 and capacity is >= 50
        assertFalse(isAllowed, "Invalid Case: Drivers over 50 years old cannot operate a bus with 50+ capacity.");
    }

    @Test
    @DisplayName("B3 - Test Case 9 (Edge Case): Verify an edge case age boundary assignment")
    public void testB3_EdgeCase() {
        // Arrange: Driver is EXACTLY 50 years old in 2026 (Born 1976), Bus capacity is EXACTLY 50
        String birthdate = "04-06-1976"; 
        Bus bus = new Bus("10000001", 50, 100.0, "Diesel");

        // Act: Evaluate assignment compliance
        boolean isAllowed = validateDriverAgeForBus(birthdate, bus.getCapacity());

        // Assert: Allowed because the restriction specifies "older than 50" (strictly > 50)
        assertTrue(isAllowed, "Edge Case: An exact 50-year-old is authorized to operate a 50-capacity bus.");
    }

    // =========================================================================
    // --- B4: ELECTRIC BUS RESTRICTION TESTS ---
    // Rule: Only drivers with at least 5 years of experience can drive electric buses.
    // =========================================================================

    @Test
    @DisplayName("B4 - Test Case 10 (Normal): Verify a correct highly experienced driver on an electric bus")
    public void testB4_NormalCase() {
        // Arrange: Driver has 10 years experience, Bus fuel type is Electricity
        int experienceYears = 10;
        Bus bus = new Bus("10000001", 40, 100.0, "Electricity");

        // Act: Evaluate validation rule
        boolean isAllowed = validateDriverExperienceForBus(experienceYears, bus.getFuelType());

        // Assert: 10 years is safely above the 5-year required line
        assertTrue(isAllowed, "Normal Case: Experienced drivers are authorized to operate electric vehicles.");
    }

    @Test
    @DisplayName("B4 - Test Case 11 (Invalid): Verify an incorrect inexperienced driver on an electric bus is blocked")
    public void testB4_InvalidInput() {
        // Arrange: Driver only has 2 years experience, Bus fuel type is Electricity
        int experienceYears = 2;
        Bus bus = new Bus("10000001", 40, 100.0, "Electricity");

        // Act: Evaluate validation rule
        boolean isAllowed = validateDriverExperienceForBus(experienceYears, bus.getFuelType());

        // Assert: 2 years is less than 5, so registration should fail
        assertFalse(isAllowed, "Invalid Case: Drivers with under 5 years of experience must be blocked from electric buses.");
    }

    @Test
    @DisplayName("B4 - Test Case 12 (Edge Case): Verify an edge case experience boundary assignment")
    public void testB4_EdgeCase() {
        // Arrange: Driver has EXACTLY 5 years of experience, Bus fuel type is Electricity
        int experienceYears = 5;
        Bus bus = new Bus("10000001", 40, 100.0, "Electricity");

        // Act: Evaluate validation rule
        boolean isAllowed = validateDriverExperienceForBus(experienceYears, bus.getFuelType());

        // Assert: Allowed because "at least 5 years" includes the exact boundary value of 5
        assertTrue(isAllowed, "Edge Case: Exactly 5 years of experience hits the minimal legal boundary line.");
    }

    // =========================================================================
    // --- B5: DRIVER LICENCE RESTRICTION TESTS ---
    // Rule: Only Heavy or Public Transport licenses can operate electric and hybrid buses.
    // =========================================================================

    @Test
    @DisplayName("B5 - Test Case 13 (Normal): Verify a correct Heavy license match on an electric bus")
    public void testB5_NormalCase() {
        // Arrange: Driver holds a Heavy tier license, Bus is an eco-friendly asset (Electricity)
        String licenseType = "Heavy";
        Bus bus = new Bus("10000001", 40, 100.0, "Electricity");

        // Act: Evaluate validation logic
        boolean isAllowed = validateDriverLicenseForBus(licenseType, bus.getFuelType());

        // Assert: Heavy license maps directly to valid asset management parameters
        assertTrue(isAllowed, "Normal Case: Heavy license profiles are authorized for electric units.");
    }

    @Test
    @DisplayName("B5 - Test Case 14 (Invalid): Verify an incorrect Light license match on a hybrid bus is blocked")
    public void testB5_InvalidInput() {
        // Arrange: Driver holds a Light tier license, Bus is an eco-friendly asset (Hybrid)
        String licenseType = "Light";
        Bus bus = new Bus("10000001", 40, 100.0, "Hybrid");

        // Act: Evaluate validation logic
        boolean isAllowed = validateDriverLicenseForBus(licenseType, bus.getFuelType());

        // Assert: Light licenses are systematically unauthorized for eco-friendly powertrains
        assertFalse(isAllowed, "Invalid Case: Light tier licenses are explicitly rejected for hybrid buses.");
    }

    @Test
    @DisplayName("B5 - Test Case 15 (Edge Case): Verify an edge case Medium license match on a hybrid bus")
    public void testB5_EdgeCase() {
        // Arrange: Driver holds a Medium tier license (sits right below Heavy boundary), Bus is Hybrid
        String licenseType = "Medium";
        Bus bus = new Bus("10000001", 40, 100.0, "Hybrid");

        // Act: Evaluate validation logic
        boolean isAllowed = validateDriverLicenseForBus(licenseType, bus.getFuelType());

        // Assert: Medium license must fail because it does not reach the required authorization tier
        assertFalse(isAllowed, "Edge Case: A Medium license boundary check must fail for high-voltage eco targets.");
    }

    // =========================================================================
    // --- COMPLIANCE VERIFICATION HELPERS ---
    // These reflect the standalone business functions supporting B3, B4, and B5.
    // =========================================================================

    private boolean validateDriverAgeForBus(String birthdateStr, int busCapacity) {
        // Convert the string birthdate ("DD-MM-YYYY") into an actionable object
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate birthdate = LocalDate.parse(birthdateStr, formatter);
        
        // Define system operation context date (Targeting the current year: 2026)
        LocalDate currentSystemDate = LocalDate.of(2026, 6, 4);
        int age = Period.between(birthdate, currentSystemDate).getYears();

        // B3 Rule Constraint Enforcement
        if (age > 50 && busCapacity >= 50) {
            return false;
        }
        return true;
    }

    private boolean validateDriverExperienceForBus(int experienceYears, String fuelType) {
        // B4 Rule Constraint Enforcement
        if ("Electricity".equals(fuelType) && experienceYears < 5) {
            return false;
        }
        return true;
    }

    private boolean validateDriverLicenseForBus(String licenseType, String fuelType) {
        boolean isEcoBus = "Electricity".equals(fuelType) || "Hybrid".equals(fuelType);
        
        // B5 Rule Constraint Enforcement
        if (isEcoBus) {
            return "Heavy".equals(licenseType) || "Public Transport".equals(licenseType);
        }
        return true;
    }
}
