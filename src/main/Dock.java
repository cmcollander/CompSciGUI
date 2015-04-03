/*
 Chris Collander
 Abdul Rafey Khan
 Clint Wetzel

 CSE 1325-002
 Semester Project
 */
package main;

/**
 * The Dock class is used to create Dock objects
 */
public class Dock {

    protected String name;
    protected char section;
    protected int dockNumber;
    protected double depth;
    protected double length;
    protected double width;
    protected Position position;

    /**
     * Constructor for the Dock class
     */
    public Dock() {
        this.name = "Rudolf's Dock";
        this.section = 'N';
        this.dockNumber = 100;
        this.depth = 15;
        this.length = 100;
        this.width = 6;
        double longitude = -2.977838;
        double latitude = 53.410777;
        position = new Position(latitude, longitude);
    }

    /**
     * String Constructor for the Dock class
     *
     * @param line String input
     */
    public Dock(String line) {
        String[] parts = line.split(",");
        this.name = parts[0].trim();
        this.section = parts[1].trim().charAt(0);
        this.dockNumber = Integer.parseInt(parts[2].trim());
        this.length = Double.parseDouble(parts[3].trim());
        this.width = Double.parseDouble(parts[4].trim());
        this.depth = Double.parseDouble(parts[5].trim());
        double longitude = Double.parseDouble(parts[6].trim());
        double latitude = Double.parseDouble(parts[7].trim());
        position = new Position(latitude, longitude);
    }

    /**
     * turn the Dock object into a string
     *
     * @return the string representation of the dock
     */
    @Override
    public String toString() {
        String ret = new String();
        ret += name + ",";
        ret += getSection() + ",";
        ret += dockNumber + ",";
        ret += length + ",";
        ret += width + ",";
        ret += depth + ",";
        ret += this.getLongitude() + ",";
        ret += this.getLatitude();

        return ret;
    }

    /**
     * Display information for the Dock object
     *
     * @return
     */
    public String display() {
        String ret = "";
        ret += "Name: " + this.name + "\n";
        ret += "Dock Number: " + this.getSection() + this.dockNumber + "\n";
        ret += "Size: " + this.length + "x" + this.depth + "x" + this.width + " metres\n";
        ret += "Location (" + String.format("%.6f", this.getLongitude()) + "," + String.format("%.6f", this.getLatitude()) + ")\n";
        ret += "\n\n";
        return ret;
    }

    /**
     * Get method for the Dock's number
     *
     * @return the dockNumber
     */
    public int getDockNumber() {
        return dockNumber;
    }

    /**
     * Set method for the Dock's number
     *
     * @param dockNumber the dockNumber to set
     */
    public void setDockNumber(int dockNumber) {
        this.dockNumber = dockNumber;
    }

    /**
     * Get method for the Dock's depth
     *
     * @return the depth
     */
    public double getDepth() {
        return depth;
    }

    /**
     * Set method for the Dock's depth
     *
     * @param depth the depth to set
     */
    public void setDepth(double depth) {
        this.depth = depth;
    }

    /**
     * Get method for the Dock's length
     *
     * @return the length
     */
    public double getLength() {
        return length;
    }

    /**
     * Set method for the Dock's length
     *
     * @param length the length to set
     */
    public void setLength(double length) {
        this.length = length;
    }

    /**
     * Get method for the Dock's width
     *
     * @return the width
     */
    public double getWidth() {
        return width;
    }

    /**
     * Set method for the Dock's width
     *
     * @param width the width to set
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Get method for the Dock's Longitude
     *
     * @return the longitude
     */
    public double getLongitude() {
        return getPosition().getLongitude();
    }

    /**
     * Set method for the Dock's Longitude
     *
     * @param longitude the longitude to set
     */
    public void setLongitude(double longitude) {
        getPosition().setLongitude(longitude);
    }

    /**
     * Get method for the Dock's Latitude
     *
     * @return the latitude
     */
    public double getLatitude() {
        return getPosition().getLatitude();
    }

    /**
     * Set method for the Dock's Latitude
     *
     * @param latitude the latitude to set
     */
    public void setLatitude(double latitude) {
        getPosition().setLatitude(latitude);
    }

    /**
     * Get method for the Dock's Name
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set method for the Dock's Name
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get method for the Dock's Section
     *
     * @return the section
     */
    public char getSection() {
        return section;
    }

    /**
     * Set method for the Dock's Section
     *
     * @param section the section to set
     */
    public void setSection(char section) {
        this.section = section;
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

}
