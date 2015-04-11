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
