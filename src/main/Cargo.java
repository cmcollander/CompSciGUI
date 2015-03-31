/*
    Chris Collander
    1001101078
    Homework #3
    03/23/2015
*/

package main;

/**
 * The Cargo class is used to create Cargo objects consisting of a tonnage
 * and a description
 * @author Chris Collander 1001101078
 */
public class Cargo {
    protected double tonnage;
    protected String description;

    /**
     * Constructor of the Cargo class
     */
    public Cargo() {
        this.tonnage = 10.0;
        this.description = "Bananas!";
    }
    
    /**
     * Custom Constructor of the Cargo class
     * @param tonnage the tonnage to set
     * @param description the description to set
     */
    public Cargo(double tonnage, String description) {
        this.tonnage = tonnage;
        this.description = description;
    }
    
    /**
     * String Constructor of the Cargo class
     * @param line String input
     */
    public Cargo(String line) {
        String[] parts = line.split(",");
        this.description = parts[0].trim();
        this.tonnage = Double.parseDouble(parts[1].trim());
    }
    
    /**
     * toString function for Cargo
     * @return String
     */
    @Override
    public String toString() {
        String str = new String();
        str += this.description + ",";
        str += Double.toString(this.tonnage);
        return str;
    }
    
    /**
     * Get method for the Cargo's tonnage 
     * @return the tonnage
     */
    public double getTonnage() {
        return tonnage;
    }

    /**
     * Set method for the Cargo's tonnage
     * @param tonnage the tonnage to set
     */
    public void setTonnage(double tonnage) {
        this.tonnage = tonnage;
    }

    /**
     * Get method for the Cargo's description
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set method for the Cargo's description
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Display the tonnage and description of the cargo
     */
    public void display() {
        System.out.println(tonnage+" tons of "+description);
    }
}
