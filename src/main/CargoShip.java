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
    protected Position position;
    protected Cargo cargo;
    protected int direction;
    protected Xform model;

    /**
     * Default Constructor for the Ship class
     */
    public CargoShip() {
        this.direction = 0;
        this.name = "Zenda";
        this.countryOfRegistration = "Ruritania";
        this.transponderNumber = 0;
        this.cargoCapacity = 10;
        this.length = 90;
        this.beam = 10;
        this.draft = 5;
        position = new Position(53.410777, -2.977838);
        cargo = new Cargo();
        model = new Xform();
    }

    public void updateXform() {
        model.setTranslateX(5 + getCol() * 10);
        model.setTranslateZ(5 + getRow() * 10);
    }

    public void removeModel() {
        model.setTranslateY(10000);
    }

    /**
     * String Constructor for the ship class
     *
     * @param line the string to parse for the new Ship
     */
    public CargoShip(String line) {
        this.direction = 0;
        String[] parts = line.split(",");
        this.name = parts[0].trim();
        this.countryOfRegistration = parts[1].trim();
        this.transponderNumber = Long.parseLong(parts[2].trim());
        this.cargoCapacity = Double.parseDouble(parts[3].trim());
        this.length = Double.parseDouble(parts[4].trim());
        this.beam = Double.parseDouble(parts[5].trim());
        this.draft = Double.parseDouble(parts[6].trim());

        double longitude = Double.parseDouble(parts[7].trim());
        double latitude = Double.parseDouble(parts[8].trim());
        position = new Position(latitude, longitude);

        model = new Xform();

        Cargo newCargo = null;

        if (parts.length > 9) {
            newCargo = new Cargo();
            newCargo.setDescription(parts[9].trim());
            newCargo.setTonnage(Double.parseDouble(parts[10].trim()));
        }
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
        str += Double.toString(this.getLongitude()) + ",";
        str += Double.toString(this.getLatitude());
        if (this.cargo != null) {
            str += "," + this.cargo.toString();
        }
        return str;
    }

    /**
     * Display information for the CargoShip
     */
    public String display() {
        String ret = "";
        ret += "Name: " + this.name + "\n";
        ret += "Country of Origin: " + this.countryOfRegistration + "\n";
        ret += "Transponder: " + this.transponderNumber + "\n";
        ret += "Length: " + this.length + " metres\n";
        ret += "Beam: " + this.beam + " metres\n";
        ret += "Draft: " + this.draft + " metres\n";
        ret += "Capacity: " + this.cargoCapacity + " tons\n";
        ret += "Location: (" + String.format("%.6f", this.getLongitude()) + "," + String.format("%.6f", this.getLatitude()) + ")\n";
        if (this.cargo != null) {
            ret += "Cargo: ";
            ret += cargo.display();
        }
        ret += "\n\n";

        return ret;
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
        return getPosition().getLongitude();
    }

    /**
     * Set method for the CargoShip's longitude
     *
     * @param longitude the longitude to set
     */
    public void setLongitude(double longitude) {
        getPosition().setLongitude(longitude);
    }

    /**
     * Get method for the CargoShip's latitude
     *
     * @return the latitude
     */
    public double getLatitude() {
        return getPosition().getLatitude();
    }

    /**
     * Set method for the CargoShip's latitude
     *
     * @param latitude the latitude to set
     */
    public void setLatitude(double latitude) {
        getPosition().setLatitude(latitude);
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

    public void setRow(int row) {
        getPosition().setRow(row);
    }

    public void setCol(int col) {
        getPosition().setCol(col);
    }

    public int getRow() {
        return getPosition().getRow();
    }

    public int getCol() {
        return getPosition().getCol();
    }

    /**
     * @return the position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * @return the direction
     */
    public int getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * @return the model
     */
    public Xform getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(Xform model) {
        this.model = model;
    }
}
