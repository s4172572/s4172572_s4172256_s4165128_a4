public class Driver {
    private String driverID;
    private String name;
    private int experienceYears;
    private String licenseType; // Light, Medium, Heavy, PublicTransport
    private String address;
    private String birthdate;

    // constructor
    public Driver(String driverID, String name, int experienceYears, String licenseType, String address, String birthdate) {
        this.driverID = driverID;
        this.name = name;
        this.experienceYears = experienceYears;
        this.licenseType = licenseType;
        this.address = address;
        this.birthdate = birthdate;
    }

    // getters
    public String getDriverID() {
        return driverID;
    }

    public String getName() {
        return name;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public String getAddress() {
        return address;
    }

    public String getBirthdate() {
        return birthdate;
    }

    // setters
    // D5. The driverID and name cannot be modified during update operations
    // setters are not created for these operations
    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }



    // D1. Driver ID Rules
    // driverID validation
    public boolean isDriverIdValid() {

        // The driverID must be exactly 10 characters long
        if (driverID == null || driverID.length() != 10) {
            return false;
        }

        // The first two characters must be digits between 2 and 9
        if (!driverID.substring(0, 2).matches("[2-9]{2}")) {
            return false;
        }

        // There must be at least two special characters between characters 3 and 8.
        String middle = driverID.substring(2, 8);
        int specialCount = 0;

        for (char c : middle.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                specialCount++;
            }
        }

        if (specialCount < 2) {
            return false;
        }

        // The last two characters must be uppercase letters (A-Z)
        return driverID.substring(8, 10).matches("[A-Z]{2}");
    }



    // D2. Address format
    // address validation
    public boolean isAddressValid() {
        // The driver address must follow the format:
        //       Street Number|Street Name|City|State|Country

        if (address == null) {
            return false;
        }

        // checks if all parts of address are present and seperated using "|"
        String[] parts = address.split("\\|");
        if (parts.length != 5) {
            return false;
        }

        // checks for missing/omitted details
        for (String part : parts) {
            if (part.trim().isEmpty()) {
                return false;
            }
        }

        // checks if first part matches format for an address, including letters for housing units
        return parts[0].matches("\\d+[A-Za-z]?");
    }























}

