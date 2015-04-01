/*
 Chris Collander
 Abdul Rafey Khan
 Clint Wetzel

 CSE 1325-002
 Semester Project
 */
package main;

/**
 * The Box class is a type of Cargo
 */
public class Box extends Cargo {

    private int teus;

    /**
     * Default Constructor
     */
    public Box() {
        this.description = "Marble";
        this.teus = 10000;
    }

    /**
     * Parameter based constructor
     *
     * @param teus
     * @param description
     */
    public Box(int teus, String description) {
        this.teus = teus;
        this.description = description;
    }

    /**
     * CSV String based constructor
     *
     * @param csv CSV String
     */
    public Box(String csv) {
        String[] parts = csv.split(",");
        this.description = parts[0].trim();
        this.teus = Integer.parseInt(parts[1].trim());
    }

    /**
     * Display object in console format
     *
     * @return String to Display
     */
    @Override
    public String display() {
        return teus + " teus of " + description + "\n";
    }

    /**
     * Convert object into a CSV based String
     *
     * @return CSV String
     */
    @Override
    public String toString() {
        return description + "," + teus;
    }

    /**
     * @return the teus
     */
    public int getTeus() {
        return teus;
    }

    /**
     * @param teus the teus to set
     */
    public void setTeus(int teus) {
        this.teus = teus;
    }
}
