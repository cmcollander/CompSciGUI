/*
 Chris Collander
 Abdul Rafey Khan
 Clint Wetzel

 CSE 1325-002
 Semester Project
 */
package main;

import java.util.ArrayList;
import java.io.File;
import java.util.Formatter;
import java.util.Scanner;
import java.io.FileOutputStream;

/**
 * Static class to handle loading Port and Map files
 */
public class FileHandler {

    /**
     * Load a port file
     *
     * @param fileName The filename to load
     * @return A Port object
     */
    public static Port getPortFile(String fileName) throws Exception {
        Port port = new Port();
        ArrayList<Dock> docks = new ArrayList<>();
        ArrayList<Cargo> cargos = new ArrayList<>();

        Scanner scanner;
        String line;
        String[] parts;
        int lcv, numDocks, numCranes, numPiers, numCargos;

        scanner = new Scanner(new File(fileName));

        line = scanner.nextLine();
        parts = line.split(",");

        port.setName(parts[0].trim());
        numDocks = Integer.parseInt(parts[1].trim());
        numCranes = Integer.parseInt(parts[2].trim());
        numPiers = Integer.parseInt(parts[3].trim());
        numCargos = Integer.parseInt(parts[4].trim());

        for (lcv = 0; lcv < numDocks; lcv++) {
            docks.add(new Dock(scanner.nextLine()));
        }
        for (lcv = 0; lcv < numCranes; lcv++) {
            docks.add(new Crane(scanner.nextLine()));
        }
        for (lcv = 0; lcv < numPiers; lcv++) {
            docks.add(new Pier(scanner.nextLine()));
        }
        for (lcv = 0; lcv < numCargos; lcv++) {
            cargos.add(new Cargo(scanner.nextLine()));
        }

        port.setDocks(docks);
        port.setCargos(cargos);

        scanner.close();

        return port;
    }

    /**
     * Load a Map file
     *
     * @param fileName The filename to load
     * @return A Map object
     */
    public static Map getMapFile(String fileName) throws Exception {
        File file;
        Scanner scanner;

        Map map = new Map();

        file = new File(fileName);
        scanner = new Scanner(file);

        String[] parts;

        char[][] matrix = new char[36][54];

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            parts = line.split(",");

            int row = Integer.parseInt(parts[1].trim());
            int col = Integer.parseInt(parts[0].trim()); //TODO! Change based on Map.***_symbol varibles
            char c = parts[2].trim().charAt(0);

            matrix[row][col] = c;
        }

        map.setMatrix(matrix);

        scanner.close();

        return map;
    }

    /**
     * Save the parameters of the Map to a CSV file
     *
     * @param file File to save to
     * @param map Map to load data from
     * @throws java.lang.Exception
     */
    public static void setSnapShot(File file, Map map) throws Exception {
        FileOutputStream fileOut = new FileOutputStream(file);
        Formatter out = new Formatter(fileOut);

        // Ships
        for (CargoShip ship : map.getShips()) {
            out.format("%s\n", ship.toString());
        }

        // Docks
        for (Dock dock : map.getPort().getDocks()) {
            out.format("%s\n", dock.toString());
        }

        // Sea Monsters
        // TODO: Sea Monsters
        // Cargos
        for (Cargo cargo : map.getPort().getCargos()) {
            out.format("%s\n", cargo.toString());
        }

        out.close();
        fileOut.close();
    }
}
