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
        this.dockSymbol = 'C';
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
        this.longitude = Double.parseDouble(parts[6].trim());
        this.latitude = Double.parseDouble(parts[7].trim());
        this.dockSymbol = 'C';
    }

    /**
     * Convert object to a CSV based String
     *
     * @return CSV String
     */
    public String toString() {
        String ret = new String();
        ret += name + ",";
        ret += section + ",";
        ret += getDockNumber() + ",";
        ret += length + ",";
        ret += width + ",";
        ret += depth + ",";
        ret += longitude + ",";
        ret += latitude;

        return ret;
    }

    /**
     * Display the Crane in a console based format
     */
    @Override
    public void display() {
        System.out.println("Name: " + this.name);
        System.out.println("Crane Number: " + this.section + this.getDockNumber());
        System.out.println("Size: " + this.length + "x" + this.depth + "x" + this.width + " metres");
        System.out.println("Location (" + this.longitude + "," + this.latitude + ")");
    }
}
