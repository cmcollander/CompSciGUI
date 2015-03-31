/*
    Chris Collander
    1001101078
    Homework #3
    03/23/2015
*/

package main;

import java.util.ArrayList;
import java.io.File;
import java.util.Formatter;
import java.util.Scanner;
import java.io.FileNotFoundException;

/**
 * Static class to handle various file saving and loading
 * @author Chris Collander 1001101078
 */
public class FileHandler {
    
    /**
     * Load a file of Ships
     * @param fileName The filename to load
     * @return ArrayList of ships
     */
    public static ArrayList<CargoShip> getShipFile(String fileName) {
        ArrayList<CargoShip> ships = new ArrayList<>();
        
        Scanner scanner;
        
        try {
            scanner = new Scanner(new File(fileName));
            
            while(scanner.hasNextLine())
                ships.add(new CargoShip(scanner.nextLine()));
        
            scanner.close();
        } catch(FileNotFoundException e) {
            System.out.println(fileName+" file not found exception.\n");
        }catch(Exception e) {
            e.printStackTrace();
        } finally {
            return ships;
        }
    }
    
    /**
     *  Set a file of Ships
     * @param fileName The filename to set
     * @param ships an ArrayList of Ships to save
     */
    public static void setShipFile(String fileName, ArrayList<CargoShip> ships) {
        Formatter formatter = null;
        try {
            formatter = new Formatter(new File(fileName));
            for(CargoShip ship : ships)
                formatter.format(ship.toString() + "\n");
        
            formatter.flush();
            formatter.close();
        } catch(FileNotFoundException e) {
            System.out.println(fileName+" file not found exception.\n");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Load a file of Cargos
     * @param fileName The filename to load
     * @return Arraylist of Cargos
     */
    public static ArrayList<Cargo> getCargoFile(String fileName) {
        ArrayList<Cargo> cargos = new ArrayList<>();
        
        Scanner scanner;
        
        try {
            scanner = new Scanner(new File(fileName));
            while(scanner.hasNextLine())
                cargos.add(new Cargo(scanner.nextLine()));
        
            scanner.close();
        } catch(FileNotFoundException e) {
            System.out.println(fileName+" file not found exception.\n");
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            return cargos;
        }
    }
    
    /**
     * Save a Cargo file
     * @param fileName The filename to save 
     * @param cargos ArrayList of cargos to save
     */
    public static void setCargoFile(String fileName, ArrayList<Cargo> cargos) {
        Formatter formatter = null;
        
        try {
            formatter = new Formatter(new File(fileName));
            for(Cargo cargo : cargos) {
                formatter.format(cargo.toString() + "\n");
            }
        
            formatter.flush();
            formatter.close();
        } catch(FileNotFoundException e) {
            System.out.println(fileName+" file not found exception.\n");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Load a port file
     * @param fileName The filename to load
     * @return A Port object
     */
    public static Port getPortFile(String fileName) {
        Port port = new Port();
        ArrayList<Dock> docks = new ArrayList<>();
        ArrayList<Cargo> cargos = new ArrayList<>();
        
        Scanner scanner;
        String line;
        String[] parts;
        int lcv, numDocks, numCranes, numPiers, numCargos;
        
        try {
            scanner = new Scanner(new File(fileName));
            
            line = scanner.nextLine();
            parts = line.split(",");
        
            port.setName(parts[0].trim());
            numDocks = Integer.parseInt(parts[1].trim());
            numCranes = Integer.parseInt(parts[2].trim());
            numPiers = Integer.parseInt(parts[3].trim());
            numCargos = Integer.parseInt(parts[4].trim());
        
            for(lcv=0;lcv<numDocks;lcv++)
                docks.add(new Dock(scanner.nextLine()));
            for(lcv=0;lcv<numCranes;lcv++)
                docks.add(new Crane(scanner.nextLine()));
            for(lcv=0;lcv<numPiers;lcv++)
                docks.add(new Pier(scanner.nextLine()));
            for(lcv=0;lcv<numCargos;lcv++)
                cargos.add(new Cargo(scanner.nextLine()));
        
            port.setDocks(docks);
            port.setCargos(cargos);
        
            scanner.close();
        } catch(FileNotFoundException e) {
            System.out.println(fileName+" file not found exception.\n");
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            return port;
        }
    }
    
    /**
     * Save a Port file
     * @param fileName The filename to save
     * @param port The Port to save
     */
    public static void setPortFile(String fileName, Port port) {
        Formatter formatter = null;
        
        try {
            formatter = new Formatter(new File(fileName));
            
            formatter.format("%s, %d, %d\n",port.getName(),port.getDocks().size(),port.getCargos().size());
            for(Dock dock : port.getDocks()) {
                formatter.format("%s\n",dock.toString());
            }
            for(Cargo cargo : port.getCargos()) {
                formatter.format("%s\n",cargo.toString());
            }
        
            formatter.flush();
            formatter.close();
        } catch(FileNotFoundException e) {
            System.out.println(fileName+" file not found exception.\n");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Load a Map file
     * @param fileName The filename to load
     * @return A Map object
     */
    public static Map getMapFile(String fileName) {
        File file;
        Scanner scanner;
        
        Map map = new Map();
        
        try {
            file = new File(fileName);
            scanner = new Scanner(file);
            
            String[] parts;
                
            char[][] matrix = new char[36][54];
        
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                parts = line.split(",");
            
                int row = Integer.parseInt(parts[1].trim());
                int col = Integer.parseInt(parts[0].trim()); //TODO! Change based on Map.***_symbol varibles
                char c = parts[2].trim().charAt(0);
            
                matrix[row][col] = c;
            }
        
            map.setMatrix(matrix);
        
            scanner.close();
        } catch(FileNotFoundException e) {
            System.out.println(fileName+" file not found exception.\n");
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            return map;
        }
    }
    
    /**
     * Save a Map file
     * @param fileName The filename to save
     * @param map The Map to save
     */
    public static void SetMapFile(String fileName, Map map) {
        File file;
        Formatter formatter = null;
        
        try {
            file = new File(fileName);
            formatter = new Formatter(file);
            
            int row, col;
            for(row=0;row<map.getMatrix().length;row++) {
                for(col=0;col<map.getMatrix()[0].length;col++) {
                    formatter.format("%d,%d,%d\n",row,col,map.getMatrix()[row][col]);
                }
            }
        
            formatter.flush();
            formatter.close();
        } catch(FileNotFoundException e) {
            System.out.println(fileName+" file not found exception.\n");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
