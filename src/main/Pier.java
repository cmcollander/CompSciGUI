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
        this.dockSymbol = 'P';
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
        this.longitude = Double.parseDouble(parts[6].trim());
        this.latitude = Double.parseDouble(parts[7].trim());
        this.dockSymbol = 'P';
    }

    /**
     * Return CSV String of the object
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
     * Display the object in a console format
     */
    public String display() {
        String ret = "";
        ret += "Name: " + this.name + "\n";
        ret += "Pier Number: " + this.section + this.getDockNumber() + "\n";
        ret += "Size: " + this.length + "x" + this.depth + "x" + this.width + " metres\n";
        ret += "Location (" + this.longitude + "," + this.latitude + ")\n";
        ret += "\n\n";
        return ret;
    }
}
