/*
    Chris Collander
    Abdul Rafey Khan
    Clint Wetzel

    CSE 1325-002
    Semester Project
*/

package main;

/**
 * The Oil class is a subclass of Cargo
 */

public class Oil extends Cargo {
   private int barrels;
   
    /**
     * Default Constructor
     */
    public Oil() {
        this.description = "Light Crude";
        this.barrels = 700000;
    }
    
    /**
     * Parameter based constructor
     * @param barrels The number of barrels of oil
     * @param description The type of oil
     */
    public Oil(int barrels, String description) {
        this.barrels = barrels;
        this.description = description;
    }
    
    /**
     * CSV based String constructor
     * @param csv CSV based String
     */
    public Oil(String csv) {
        String[] parts = csv.split(",");
        this.description = parts[0].trim();
        this.barrels = Integer.parseInt(parts[1].trim());
    }
    
    /**
     * Display the object in a console format
     */
   @Override
    public void display() {
        System.out.println(barrels+ " barrels of " + description);
    }
    
    /**
     * Return a CSV string of the object
     * @return CSV String
     */
   @Override
    public String toString() {
        return description+","+barrels;
    }

    /**
     * @return the barrels
     */
    public int getBarrels() {
        return barrels;
    }

    /**
     * @param barrels the barrels to set
     */
    public void setBarrels(int barrels) {
        this.barrels = barrels;
    }
}
