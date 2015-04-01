/*
 Chris Collander
 Abdul Rafey Khan
 Clint Wetzel

 CSE 1325-002
 Semester Project
 */
package main;

import java.util.ArrayList;

/**
 * The Port class represents a port containing multiple docks and a cargo supply
 */
public class Port {

    private String name;
    private ArrayList<Dock> docks;
    private ArrayList<Cargo> cargos;

    /**
     * Constructor for Port
     */
    public Port() {
        this.name = "Liverpool";
        this.docks = new ArrayList<>();
        this.cargos = new ArrayList<>();
    }

    /**
     * Unload a CargoShip into a dock Check for safety before running this
     * method!
     *
     * @param ship The CargoShip to unload
     */
    public void unloadShip(CargoShip ship) throws NullPointerException {
        // If there is no cargo on the ship, return without doing anything else.
        if (ship.getCargo() == null) {
            return;
        }

        // Find the corresponding Dock for the ship (if no dock found, will just return)
        for (Dock dock : docks) {
            if (MapConverter.lat2row(ship.getLatitude()) == MapConverter.lat2row(dock.getLatitude()) && MapConverter.lon2col(ship.getLongitude()) == MapConverter.lon2col(dock.getLongitude())) {
                // Add the ship's cargo to the port's cargo array, remove from ship.
                cargos.add(ship.getCargo());
                ship.setCargo(null);
                return;
            }
        }
    }

    /**
     * Display all docks and cargos in this port
     */
    public void display() {
        for (Dock dock : this.docks) {
            dock.display();
        }
        for (Cargo cargo : this.cargos) {
            cargo.display();
        }
    }

    /**
     * Get method for the port's name
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set method for the port's name
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get method for the port's docks
     *
     * @return the docks
     */
    public ArrayList<Dock> getDocks() {
        return docks;
    }

    /**
     * Set method for the port's docks
     *
     * @param docks the docks to set
     */
    public void setDocks(ArrayList<Dock> docks) {
        this.docks = docks;
    }

    /**
     * Get method for the port's cargos
     *
     * @return the cargos
     */
    public ArrayList<Cargo> getCargos() {
        return cargos;
    }

    /**
     * Set method for the port's cargos
     *
     * @param cargos the cargos to set
     */
    public void setCargos(ArrayList<Cargo> cargos) {
        this.cargos = cargos;
    }
}
