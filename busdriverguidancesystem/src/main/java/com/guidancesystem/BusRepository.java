package com.guidancesystem;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class BusRepository {
    private static final String FILE_PATH = "src/main/resources/data/buses.json";
    private Map<String, Bus> buses;
    private Gson gson; // Instantiate Gson

    public BusRepository() {
        buses = new HashMap<>();
        // GsonBuilder with pretty printing makes the JSON file readable
        gson = new GsonBuilder().setPrettyPrinting().create(); 
        loadFromFile();
    }

    // ADD: Add new buses (Enforces B1 - Unique ID)
    public void add(Bus bus) {
        if (buses.containsKey(bus.getBusID())) {
            throw new IllegalArgumentException("Bus ID must be unique. Duplicate found: " + bus.getBusID());
        }
        if (!bus.isValid()) {
            throw new IllegalArgumentException("Bus fails validation rules.");
        }
        buses.put(bus.getBusID(), bus);
        saveToFile();
    }

    // RETRIEVE: Retrieve existing buses
    public Bus retrieve(String busID) {
        return buses.get(busID);
    }

    // UPDATE: Update existing buses' details
    public void update(String busID, int newCapacity, double newFuelLevel, String newFuelType) {
        Bus existing = buses.get(busID);
        if (existing == null) {
            throw new IllegalArgumentException("Bus not found.");
        }

        // B2. Capacity Update Restriction (Handled by the Bus setter, but guarded here as well)
        existing.setCapacity(newCapacity); 
        existing.setFuelLevel(newFuelLevel);
        existing.setFuelType(newFuelType);

        if (!existing.isValid()) {
            throw new IllegalArgumentException("Updated bus data is invalid.");
        }

        saveToFile();
    }

    // COUNT: Return the number of stored buses
    public int count() {
        return buses.size();
    }

    // File Storage Methods
private void saveToFile() {
        // Ensure the directory exists before saving
        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs(); 

        try (Writer writer = new FileWriter(file)) {
            // Convert the map's values (the Bus objects) into a JSON array and save
            gson.toJson(buses.values(), writer);
        } catch (IOException e) {
            System.err.println("Error saving buses: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (Reader reader = new FileReader(file)) {
            // Tell Gson what type of data to expect (a List of Bus objects)
            Type busListType = new TypeToken<List<Bus>>(){}.getType();
            List<Bus> busList = gson.fromJson(reader, busListType);

            // Populate the HashMap from the loaded List
            if (busList != null) {
                for (Bus b : busList) {
                    buses.put(b.getBusID(), b);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading buses: " + e.getMessage());
        }
    }
}