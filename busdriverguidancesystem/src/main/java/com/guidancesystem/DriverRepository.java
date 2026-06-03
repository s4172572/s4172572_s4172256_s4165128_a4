package com.guidancesystem;
import java.io.*;
import java.util.*;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class DriverRepository {
    private static final String FILE_PATH = "data/drivers.json";
    private Map<String, Driver> drivers;
    private Gson gson; // Instantiate Gson

    public DriverRepository() {
        drivers = new HashMap<>();
        // GsonBuilder with pretty printing makes the JSON file readable
        gson = new GsonBuilder().setPrettyPrinting().create(); 
        loadFromFile();
    }

    // ADD is to add new drivers (D1: Unique ID)
    public void add(Driver driver) {
        if (drivers.containsKey(driver.getDriverID())) {
            throw new IllegalArgumentException("Driver ID must be unique. Duplicate found: " + driver.getDriverID());
        }
        if (!driver.isValid()) {
            throw new IllegalArgumentException("Driver fails validation rules.");
        }
        drivers.put(driver.getDriverID(), driver);
        saveToFile();
    }

    // RETRIEVE is to retrieve existing drivers
    public Driver retrieve(String driverID) {
        return drivers.get(driverID);
    }

    // UPDATE: Update existing drivers' details
    // D5: Immutable Fields: DriverID and name cannot be updated
    public void update(String driverID, int newExperienceYears, String newLicenseType, String newAddress, String newBirthdate) {
        Driver existing = drivers.get(driverID);
        if (existing == null) {
            throw new IllegalArgumentException("Driver not found.");
        }

        // D4: License Update Restriction
        if (existing.getExperienceYears() > 10 && !existing.getLicenseType().equals(newLicenseType)) {
            throw new IllegalArgumentException("Cannot change license type for drivers with more than 10 years of experience.");
        }

        existing.setExperienceYears(newExperienceYears);
        existing.setLicenseType(newLicenseType);
        existing.setAddress(newAddress);
        existing.setBirthdate(newBirthdate);

        if (!existing.isValid()) {
            throw new IllegalArgumentException("Updated driver data is invalid.");
        }

        saveToFile();
    }

    // COUNT is to return the number of stored drivers
    public int count() {
        return drivers.size();
    }

    // --- File Storage Methods ---

    private void saveToFile() {
        // Ensure the directory exists before saving
        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs(); 

        try (Writer writer = new FileWriter(file)) {
            // Convert the map's values (the Driver objects) into a JSON array and save
            gson.toJson(drivers.values(), writer);
        } catch (IOException e) {
            System.err.println("Error saving drivers: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (Reader reader = new FileReader(file)) {
            // Tell Gson what type of data to expect (a List of Driver objects)
            Type driverListType = new TypeToken<List<Driver>>(){}.getType();
            List<Driver> driverList = gson.fromJson(reader, driverListType);

            // Populate the HashMap from the loaded List
            if (driverList != null) {
                for (Driver d : driverList) {
                    drivers.put(d.getDriverID(), d);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading drivers: " + e.getMessage());
        }
    }
}