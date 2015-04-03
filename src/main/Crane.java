/*
 Chris Collander
 Abdul Rafey Khan
 Clint Wetzel

 CSE 1325-002
 Semester Project
 */
package main;

/**
 * The Crane class is a subclass of Dock
 */
public class Crane extends Dock {

    /**
     * Default Constructor
     */
    public Crane() {
        super();
    }

    /**
     * CSV String based constructor
     *
     * @param csv
     */
    public Crane(String csv) {
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
     * Convert object to a CSV based String
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
     * Display the Crane in a console based format
     */
    @Override
    public String display() {
        String ret = "";
        ret += "Name: " + this.name + "\n";
        ret += "Crane Number: " + this.getSection() + this.getDockNumber() + "\n";
        ret += "Size: " + this.length + "x" + this.depth + "x" + this.width + " metres\n";
        ret += "Location (" + String.format("%.6f", this.getLongitude()) + "," + String.format("%.6f", this.getLatitude()) + ")\n";
        ret += "\n\n";
        return ret;
    }
}
