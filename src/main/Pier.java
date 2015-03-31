/*
    Chris Collander
    1001101078
    Homework #3
    03/23/2015
*/

package main;

/**
 * The Pier class is a subclass of Dock
 * @author Chris Collander
 */

public class Pier extends Dock{
    /**
     * Default Constructor
     */
    public Pier() {
        super();
        this.dockSymbol = 'P';
    }
    
    /**
     * CSV String based Constructor
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
    public void display() {
        System.out.println("Name: "+this.name);
        System.out.println("Pier Number: "+this.section+this.getDockNumber());
        System.out.println("Size: "+this.length+"x"+this.depth+"x"+this.width+" metres");
        System.out.println("Location ("+this.longitude+","+this.latitude+")");        
    }
}