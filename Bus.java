public class Bus {
    private String busID;
    private int capacity;
    private double fuelLevel;
    private String fuelType; // Diesel, Hybrid, Electricity

    // constructor
    public Bus(String busID, int capacity, double fuelLevel, String fuelType) {
        this.busID = busID;
        this.capacity = capacity;
        this.fuelLevel = fuelLevel;
        this.fuelType = fuelType;
    }

    // getters
    public String getBusID() {
        return busID;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getFuelLevel() {
        return fuelLevel;
    }

    public String getFuelType() {
        return fuelType;
    }

    // setters
    // assuming busID acts as a primary key, it should not be modified during update operations so no setter is created for busID

    // B2. capacity Update Restriction
    // busCapacity cannot increase during update operations but it can decrease.
    public void setCapacity(int capacity) {
        if (capacity <= this.capacity) {
            this.capacity = capacity;
        } else {
            // Depending on how your repository handles errors, you might want to throw an exception here
            throw new IllegalArgumentException("Bus capacity cannot increase during an update.");
        }
    }

    public void setFuelLevel(double fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    // B1. bus ID rules
    // busID validation
    public boolean isBusIdValid() {
        // The busID must be exactly 8 characters long
        if (busID == null || busID.length() != 8) {
            return false;
        }

        // all charactars must be digits
        return busID.matches("\\d{8}");
    }

    // fuel Type validation
    public boolean isFuelTypeValid() {
        if (fuelType == null) {
            return false;
        }
        
        // Checks if fuel type is one of the accepted formats
        return fuelType.equals("Diesel") || fuelType.equals("Hybrid") || fuelType.equals("Electricity");
    }

    // add full validation
    public boolean isValid() {
        return isBusIdValid()
            && isFuelTypeValid()
            && capacity > 0
            && fuelLevel >= 0.0;
    }
}
