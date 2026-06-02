import java.io.*;
import java.util.*;

public class BusRepository {
    private static final String FILE_PATH = "data/buses.json";
    private Map<String, Bus> buses;

    public BusRepository() {
        buses = new HashMap<>();
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

    // --- File Storage Methods ---

    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Bus b : buses.values()) {
                writer.println(b.getBusID() + ";" + b.getCapacity() + ";" + 
                               b.getFuelLevel() + ";" + b.getFuelType());
            }
        } catch (IOException e) {
            System.err.println("Error saving buses: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 4) {
                    Bus b = new Bus(parts[0], Integer.parseInt(parts[1]), 
                                    Double.parseDouble(parts[2]), parts[3]);
                    buses.put(b.getBusID(), b);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading buses: " + e.getMessage());
        }
    }
}