import java.io.*;
import java.util.*;

public class DriverRepository {
    private static final String FILE_PATH = "data/drivers.json";
    private Map<String, Driver> drivers;

    public DriverRepository() {
        drivers = new HashMap<>();
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

    // Saving to file

    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Driver d : drivers.values()) {
                // Using ';' as delimiter since address uses '|'
                writer.println(d.getDriverID() + ";" + d.getName() + ";" + 
                               d.getExperienceYears() + ";" + d.getLicenseType() + ";" + 
                               d.getAddress() + ";" + d.getBirthdate());
            }
        } catch (IOException e) {
            System.err.println("Error saving drivers: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 6) {
                    Driver d = new Driver(parts[0], parts[1], Integer.parseInt(parts[2]), parts[3], parts[4], parts[5]);
                    drivers.put(d.getDriverID(), d);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading drivers: " + e.getMessage());
        }
    }
}