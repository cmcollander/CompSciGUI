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
    protected double longitude;
    protected double latitude;
    protected char dockSymbol;

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
        this.longitude = -2.977838;
        this.latitude = 53.410777;
        this.dockSymbol = 'D';
    }
    
    /**
     * String Constructor for the Dock class
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
        this.longitude = Double.parseDouble(parts[6].trim());
        this.latitude = Double.parseDouble(parts[7].trim());
        this.dockSymbol = 'D';
    }
    
    /**
     * turn the Dock object into a string
     * @return the string representation of the dock
     */
    @Override
    public String toString() {
        String ret = new String();
        ret += name + ",";
        ret += section + ",";
        ret += dockNumber + ",";
        ret += length + ",";
        ret += width + ",";
        ret += depth + ",";
        ret += longitude + ",";
        ret += latitude;
        
        return ret;
    }
    
    /**
     * Display information for the Dock object
     */
    public void display() {
        System.out.println("Name: "+this.name);
        System.out.println("Dock Number: "+this.section+this.dockNumber);
        System.out.println("Size: "+this.length+"x"+this.depth+"x"+this.width+" metres");
        System.out.println("Location ("+this.longitude+","+this.latitude+")");
    }
    
    /**
     * Get method for the Dock's number
     * @return the dockNumber
     */
    public int getDockNumber() {
        return dockNumber;
    }

    /**
     * Set method for the Dock's number
     * @param dockNumber the dockNumber to set
     */
    public void setDockNumber(int dockNumber) {
        this.dockNumber = dockNumber;
    }

    /**
     * Get method for the Dock's depth
     * @return the depth
     */
    public double getDepth() {
        return depth;
    }

    /**
     * Set method for the Dock's depth
     * @param depth the depth to set
     */
    public void setDepth(double depth) {
        this.depth = depth;
    }

    /**
     * Get method for the Dock's length
     * @return the length
     */
    public double getLength() {
        return length;
    }

    /**
     * Set method for the Dock's length
     * @param length the length to set
     */
    public void setLength(double length) {
        this.length = length;
    }

    /**
     * Get method for the Dock's width
     * @return the width
     */
    public double getWidth() {
        return width;
    }

    /**
     * Set method for the Dock's width
     * @param width the width to set
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Get method for the Dock's Longitude
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Set method for the Dock's Longitude
     * @param longitude the longitude to set
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Get method for the Dock's Latitude
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Set method for the Dock's Latitude
     * @param latitude the latitude to set
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Get method for the Dock's Name
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set method for the Dock's Name
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get method for the Dock's Section
     * @return the section
     */
    public char getSection() {
        return section;
    }

    /**
     * Set method for the Dock's Section
     * @param section the section to set
     */
    public void setSection(char section) {
        this.section = section;
    }

    /**
     * @return the dockSymbol
     */
    public char getDockSymbol() {
        return dockSymbol;
    }

    /**
     * @param dockSymbol the dockSymbol to set
     */
    public void setDockSymbol(char dockSymbol) {
        this.dockSymbol = dockSymbol;
    }
    
}
