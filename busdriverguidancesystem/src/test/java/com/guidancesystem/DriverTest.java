package com.guidancesystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DriverTest {

    private Driver test;

    @BeforeEach
    public void setUp() {
        test = new Driver(
                "23abcd!!EF",
                "Test Name",
                12,
                "Heavy",
                "435|Swanston Street|Melbourne|VIC|Australia",
                "14-06-1988"
        );
    }

// D1. Driver ID Rules
            // o driverID must be unique. Duplicate driver IDs are not allowed.
            // o The driverID must be exactly 10 characters long
            // § The first two characters must be digits between 2 and 9
            // § There must be at least two special characters between characters 3 and
            // 8.
            // § The last two characters must be uppercase letters (A-Z)

    @Test
    public void testValidDriverIdPasses() {
        assertTrue(test.isDriverIdValid());
    }

    @Test
    public void testDriverIdWithWrongLengthFails() {
        Driver driver = new Driver(
                "23abcd!!E",
                "Test Name",
                12,
                "Heavy",
                "435|Swanston Street|Melbourne|VIC|Australia",
                "14-06-1988"
        );

        assertFalse(driver.isDriverIdValid());
    }

    @Test
    public void testDriverIdWithInvalidFirstTwoCharactersFails() {
        Driver driver = new Driver(
                "13abcd!!E",
                "Test Name",
                12,
                "Heavy",
                "435|Swanston Street|Melbourne|VIC|Australia",
                "14-06-1988"
        );

        assertFalse(driver.isDriverIdValid());
    }

    @Test
    public void testDriverIdWithLessThanTwoSpecialCharactersFails() {
        Driver driver = new Driver(
                "23abcd!EFG",
                "Test Name",
                12,
                "Heavy",
                "435|Swanston Street|Melbourne|VIC|Australia",
                "14-06-1988"
        );

        assertFalse(driver.isDriverIdValid());
    }

    @Test
    public void testDriverIdWithoutUppercaseEndingFails() {
        Driver driver = new Driver(
                "23abcd!!ef",
                "Test Name",
                12,
                "Heavy",
                "435|Swanston Street|Melbourne|VIC|Australia",
                "14-06-1988"
        );

        assertFalse(driver.isDriverIdValid());
    }

// D2. Address Format
        // o The driver address must follow the format:
        // § Street Number|Street Name|City|State|Country

    @Test
    public void testValidAddressPasses() {
        assertTrue(test.isAddressValid());
    }

    @Test
    public void testAddressWithMissingCountryFails() {
        Driver driver = new Driver(
                "23abcd!!EF",
                "Test Name",
                12,
                "Heavy",
                "435|Swanston Street|Melbourne|VIC",
                "14-06-1988"
        );

        assertFalse(driver.isAddressValid());
    }

    @Test
    public void testAddressWithEmptyStreetNameFails() {
        Driver driver = new Driver(
                "23abcd!!EF",
                "Test Name",
                12,
                "Heavy",
                "435||Melbourne|VIC|Australia",
                "14-06-1988"
        );

        assertFalse(driver.isAddressValid());
    }

    @Test
    public void testAddressWithInvalidStreetNumberFails() {
        Driver driver = new Driver(
                "23abcd!!EF",
                "Test Name",
                12,
                "Heavy",
                "435-437|Swanston Street|Melbourne|VIC|Australia",
                "14-06-1988"
        );

        assertFalse(driver.isAddressValid());
    }

//  D3. Birthday Format
        // o The birthdate must follow the format: DD-MM-YYYY

    @Test
    public void testValidBirthdayPasses() {
        assertTrue(test.isBirthdayValid());
    }

    @Test
    public void testBirthdayUsingSlashesFails() {
        Driver driver = new Driver(
                "23abcd!!EF",
                "Test Name",
                12,
                "Heavy",
                "435|Swanston Street|Melbourne|VIC|Australia",
                "14/06/1988"
        );

        assertFalse(driver.isBirthdayValid());
    }

    @Test
    public void testBirthdayWithMissingYearFails() {
        Driver driver = new Driver(
                "323abcd!!EF",
                "Test Name",
                12,
                "Heavy",
                "435|Swanston Street|Melbourne|VIC|Australia",
                "14-06"
        );

        assertFalse(driver.isBirthdayValid());
    }

    @Test
    public void testBirthdayWithSingleDigitDayFails() {
        Driver driver = new Driver(
                "23abcd!!EF",
                "JAMES MITSILIAS",
                12,
                "Heavy",
                "435|Swanston Street|Melbourne|VIC|Australia",
                "4-06-1988"
        );

        assertFalse(driver.isBirthdayValid());
    }

// D4. License Update Restriction
        // o If a driver has more than 10 years of experience, their licenseType cannot be
        // changed during update operations.

    @Test
    public void testFullyValidDriverPasses() {
        assertTrue(test.isValid());
    }

    @Test
    public void testDriverWithNegativeExperienceFails() {
        Driver driver = new Driver(
                "23abcd!!EF",
                "Test Name",
                -1,
                "Heavy",
                "435|Swanston Street|Melbourne|VIC|Australia",
                "14-06-1988"
        );

        assertFalse(driver.isValid());
    }

    @Test
    public void testDriverWithEmptyLicenseTypeFails() {
        Driver driver = new Driver(
                "23abcd!!EF",
                "Test Name",
                12,
                "",
                "435|Swanston Street|Melbourne|VIC|Australia",
                "14-06-1988"
        );

        assertFalse(driver.isValid());
    }
}