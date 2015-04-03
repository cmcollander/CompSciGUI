/*
 Chris Collander
 Abdul Rafey Khan
 Clint Wetzel

 CSE 1325-002
 Semester Project
 */
package main;

/**
 * The Pier class is a subclass of Dock
 */
public class Pier extends Dock {

    /**
     * Default Constructor
     */
    public Pier() {
        super();
    }

    /**
     * CSV String based Constructor
     *
     * @param csv CSV String
     */
    public Pier(String csv) {
        String[] parts = csv.split(",");
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
     * Return CSV String of the object
     *
     * @return CSV String
     */
    public String toString() {
        String ret = new String();
        ret += name + ",";
        ret += getSection() + ",";
        ret += getDockNumber() + ",";
        ret += length + ",";
        ret += width + ",";
        ret += depth + ",";
        ret += this.getLongitude() + ",";
        ret += this.getLatitude();

        return ret;
    }

    /**
     * Display the object in a console format
     */
    public String display() {
        String ret = "";
        ret += "Name: " + this.name + "\n";
        ret += "Pier Number: " + this.getSection() + this.getDockNumber() + "\n";
        ret += "Size: " + this.length + "x" + this.depth + "x" + this.width + " metres\n";
        ret += "Location (" + String.format("%.6f", this.getLongitude()) + "," + String.format("%.6f", this.getLatitude()) + ")\n";
        ret += "\n\n";
        return ret;
    }
}
