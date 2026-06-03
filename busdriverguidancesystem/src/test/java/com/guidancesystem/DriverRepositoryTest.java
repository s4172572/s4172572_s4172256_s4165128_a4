package com.guidancesystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

public class DriverRepositoryTest {

    private DriverRepository repository;
    private final String FILE_PATH = "data/drivers.json";

    @BeforeEach
    public void setUp() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
        repository = new DriverRepository();
    }

    @AfterEach
    public void tearDown() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testAddDriverWithExistingIdThrowsException() {
        Driver driver1 = new Driver("34abcd!@WX", "Test Name", 12, "Heavy", "435-437|Swanston Street|Melbourne|VIC|Australia", "14-06-1988");
        repository.add(driver1);

        Driver duplicateDriver = new Driver("34abcd!@WX", "Another Name", 5, "Light", "123|Fake Street", "01-01-1990");
        
        // JUnit 5 uses assertThrows with a lambda expression
        assertThrows(IllegalArgumentException.class, () -> {
            repository.add(duplicateDriver); 
        });
    }

    @Test
    public void testAddDriverWithUniqueId() {
        Driver driver = new Driver("34abcd!@WZ", "Test Name", 12, "Heavy", "435-437|Swanston Street|Melbourne|VIC|Australia", "14-06-1988");
        repository.add(driver);

        assertEquals(1, repository.count());
        Driver retrieved = repository.retrieve("34abcd!@WZ");
        assertNotNull(retrieved);
        assertEquals("Test Name", retrieved.getName());
    }

    @Test
    public void testUpdateLicenseTypeWithGreaterThan10YearsExperience() {
        Driver driver = new Driver("34abcd!@ZY", "Test Name", 12, "Light", "Address", "14-06-1988");
        repository.add(driver);

        assertThrows(IllegalArgumentException.class, () -> {
            repository.update("34abcd!@ZY", 11, "Heavy", "Address", "14-06-1988");
        });
    }

    @Test
    public void testUpdateDetailsWithNonExistingId() {
        assertThrows(IllegalArgumentException.class, () -> {
            repository.update("34abcd!@WY", 12, "Heavy", "Address", "14-06-1977");
        });
    }

    @Test
    public void testUpdateLicenseTypeWithLessThan10YearsExperience() {
        Driver driver = new Driver("34abcd!@WX", "Test Name", 9, "Light", "435-437|Swanston Street|Melbourne|VIC|Australia", "14-06-1977");
        repository.add(driver);

        repository.update("34abcd!@WX", 9, "Heavy", "435-437|Swanston Street|Melbourne|VIC|Australia", "14-06-1977");

        Driver updatedDriver = repository.retrieve("34abcd!@WX");
        assertEquals("Heavy", updatedDriver.getLicenseType());
    }

    @Test
    public void testUpdateDriverCountAfterDriverIsAdded() {
        assertEquals(0, repository.count());

        Driver driver1 = new Driver("34abcd!@YZ", "Test Name", 12, "Heavy", "Address", "14-06-1988");
        repository.add(driver1);
        assertEquals(1, repository.count());

        Driver driver2 = new Driver("99abcd!@AA", "Test Name 2", 5, "Light", "Address", "01-01-2000");
        repository.add(driver2);
        assertEquals(2, repository.count());
    }
}