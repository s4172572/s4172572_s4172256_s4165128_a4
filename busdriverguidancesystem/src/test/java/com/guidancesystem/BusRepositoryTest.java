package com.guidancesystem;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BusRepositoryTest {

    private BusRepository repository;
    private final String FILE_PATH = "src/main/resources/data/buses.json";

    @BeforeEach
    public void setUp() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
        repository = new BusRepository();
    }

    @AfterEach
    public void tearDown() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testAddNewBus() {
        Bus bus = new Bus("10000002", 60, 85.5, "Electricity");
        repository.add(bus);

        assertEquals(1, repository.count());
        Bus retrieved = repository.retrieve("10000002");
        assertNotNull(retrieved);
        assertEquals("Electricity", retrieved.getFuelType());
    }

    @Test
    public void testAddBusWhereIdIsExisting() {
        Bus bus1 = new Bus("10000001", 60, 85.5, "Electricity");
        repository.add(bus1);

        Bus duplicateBus = new Bus("10000001", 40, 50.0, "Diesel");
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            repository.add(duplicateBus); 
        });
        assertEquals("Bus ID must be unique. Duplicate found: 10000001", exception.getMessage());
    }

    @Test
    public void testUpdateBusDetailsWhereIdIsExisting() {
        Bus bus = new Bus("10000001", 60, 85.5, "Electricity");
        repository.add(bus);

        repository.update("10000001", 60, 85.5, "Diesel");

        Bus updatedBus = repository.retrieve("10000001");
        assertEquals("Diesel", updatedBus.getFuelType());
    }

    @Test
    public void testUpdateBusDetailsWhereIdIsNotExisting() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            repository.update("10000005", 60, 85.5, "Electricity");
        });
        assertNotNull(exception.getMessage());
    }

    @Test
    public void testUpdateBusCountAfterBusHasBeenCreated() {
        assertEquals(0, repository.count());

        Bus bus1 = new Bus("10000001", 60, 85.5, "Electricity");
        repository.add(bus1);
        assertEquals(1, repository.count());

        Bus bus2 = new Bus("10000003", 60, 85.5, "Electricity");
        repository.add(bus2);
        assertEquals(2, repository.count());
    }
}