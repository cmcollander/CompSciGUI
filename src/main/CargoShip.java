/*
 Chris Collander
 Abdul Rafey Khan
 Clint Wetzel

 CSE 1325-002
 Semester Project
 */
package main;

/**
 * The CargoShip class is used to create CargoShip objects
 */
public class CargoShip {

    protected String name;
    protected String countryOfRegistration;
    protected long transponderNumber;
    protected double cargoCapacity;
    protected double length;
    protected double beam;
    protected double draft;
    protected double longitude;
    protected double latitude;
    protected Cargo cargo;
    protected char shipSymbol;

    /**
     * Default Constructor for the Ship class
     */
    public CargoShip() {
        this.name = "Zenda";
        this.countryOfRegistration = "Ruritania";
        this.transponderNumber = 0;
        this.cargoCapacity = 10;
        this.length = 90;
        this.beam = 10;
        this.draft = 5;
        this.longitude = -2.977838;
        this.latitude = 53.410777;
        this.shipSymbol = 'S';
        cargo = new Cargo();
    }

    /**
     * String Constructor for the ship class
     *
     * @param line the string to parse for the new Ship
     */
    public CargoShip(String line) {
        String[] parts = line.split(",");
        this.name = parts[0].trim();
        this.countryOfRegistration = parts[1].trim();
        this.transponderNumber = Long.parseLong(parts[2].trim());
        this.cargoCapacity = Double.parseDouble(parts[3].trim());
        this.length = Double.parseDouble(parts[4].trim());
        this.beam = Double.parseDouble(parts[5].trim());
        this.draft = Double.parseDouble(parts[6].trim());
        this.longitude = Double.parseDouble(parts[7].trim());
        this.latitude = Double.parseDouble(parts[8].trim());

        Cargo newCargo = null;

        if (parts.length > 9) {
            newCargo = new Cargo();
            newCargo.setDescription(parts[9].trim());
            newCargo.setTonnage(Double.parseDouble(parts[10].trim()));
        }

        this.shipSymbol = 'S';
    }

    /**
     * toString function for CargoShip
     *
     * @return String
     */
    @Override
    public String toString() {
        String str = new String();
        str += "Cargo Ship,";
        str += this.name + ",";
        str += this.countryOfRegistration + ",";
        str += Long.toString(this.transponderNumber) + ",";
        str += Double.toString(this.cargoCapacity) + ",";
        str += Double.toString(this.length) + ",";
        str += Double.toString(this.beam) + ",";
        str += Double.toString(this.draft) + ",";
        str += Double.toString(this.longitude) + ",";
        str += Double.toString(this.latitude);
        if (this.cargo != null) {
            str += "," + this.cargo.toString();
        }
        return str;
    }

    /**
     * Display information for the CargoShip
     */
    public void display() {
        System.out.println("Name: " + this.name);
        System.out.println("Country of Origin: " + this.countryOfRegistration);
        System.out.println("Transponder: " + this.transponderNumber);
        System.out.println("Length: " + this.length + " metres");
        System.out.println("Beam: " + this.beam + " metres");
        System.out.println("Draft: " + this.draft + " metres");
        System.out.println("Capacity: " + this.cargoCapacity + " tons");
        System.out.println("Location: (" + this.longitude + "," + this.latitude + ")");
        if (this.cargo != null) {
            System.out.print("Cargo: ");
            cargo.display();
        }
        System.out.println();

    }

    /**
     * Get method for the CargoShip's name
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set method for the CargoShip's name
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get method for the CargoShip's country of registration
     *
     * @return the countryOfRegistration
     */
    public String getCountryOfRegistration() {
        return countryOfRegistration;
    }

    /**
     * Set method for the CargoShip's country of registration
     *
     * @param countryOfRegistration the countryOfRegistration to set
     */
    public void setCountryOfRegistration(String countryOfRegistration) {
        this.countryOfRegistration = countryOfRegistration;
    }

    /**
     * Get method for the CargoShip's transponder number
     *
     * @return the transponderNumber
     */
    public long getTransponderNumber() {
        return transponderNumber;
    }

    /**
     * Set method for the CargoShip's transponder number
     *
     * @param transponderNumber the transponderNumber to set
     */
    public void setTransponderNumber(long transponderNumber) {
        this.transponderNumber = transponderNumber;
    }

    /**
     * Get method for the CargoShip's cargo capacity
     *
     * @return the cargoCapacity
     */
    public double getCargoCapacity() {
        return cargoCapacity;
    }

    /**
     * Set method for the CargoShip's cargo capacity
     *
     * @param cargoCapacity the cargoCapacity to set
     */
    public void setCargoCapacity(double cargoCapacity) {
        this.cargoCapacity = cargoCapacity;
    }

    /**
     * Get method for the CargoShip's length
     *
     * @return the length
     */
    public double getLength() {
        return length;
    }

    /**
     * Set method for the CargoShip's length
     *
     * @param length the length to set
     */
    public void setLength(double length) {
        this.length = length;
    }

    /**
     * Get method for the CargoShip's beam
     *
     * @return the beam
     */
    public double getBeam() {
        return beam;
    }

    /**
     * Set method for the CargoShip's beam
     *
     * @param beam the beam to set
     */
    public void setBeam(double beam) {
        this.beam = beam;
    }

    /**
     * Get method for the CargoShip's draft
     *
     * @return the draft
     */
    public double getDraft() {
        return draft;
    }

    /**
     * Set method for the CargoShip's draft
     *
     * @param draft the draft to set
     */
    public void setDraft(double draft) {
        this.draft = draft;
    }

    /**
     * Get method for the CargoShip's longitude
     *
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Set method for the CargoShip's longitude
     *
     * @param longitude the longitude to set
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Get method for the CargoShip's latitude
     *
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Set method for the CargoShip's latitude
     *
     * @param latitude the latitude to set
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Get method for the CargoShip's Cargo
     *
     * @return the cargo
     */
    public Cargo getCargo() {
        return cargo;
    }

    /**
     * Set method for the CargoShip's Cargo
     *
     * @param cargo the cargo to set
     */
    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    /**
     * @return the shipSymbol
     */
    public char getShipSymbol() {
        return shipSymbol;
    }

    /**
     * @param shipSymbol the shipSymbol to set
     */
    public void setShipSymbol(char shipSymbol) {
        this.shipSymbol = shipSymbol;
    }
}
