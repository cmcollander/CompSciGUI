/*
    Chris Collander
    1001101078
    Homework #3
    03/23/2015
*/

package main;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Formatter;
import java.util.InputMismatchException;

/**
 * The Main class contains the main function for this project as well as
 * functions for the menus and member variables for input/output and the map.
 * 
 * @author Chris Collander 1001101078
 */
public class Main {

    private static Scanner input;
    private static Formatter output;

    private static Map map;

    /**
     * The main function, which initializes the main menu, the IO, and the map
     * @param args
     */
    public static void main(String[] args) {
        input = new Scanner(System.in);
        output = new Formatter(System.out);
        
        map = new Map();
        try {
            mainMenu();
        } catch(NullPointerException e) {
            output.format("Null Pointer Exception encountered, exiting program\n\n");
        }
    }

    /**
     * The main menu
     */
    public static void mainMenu() throws NullPointerException{
        int inputValue = -1;
        do {
            switch (inputValue) {
                case 1:
                    showStudentID();
                    break;
                case 2:
                    loadSystem();
                    break;
                case 3:
                    shipMenu();
                    break;
                case 4:
                    portMenu();
                    break;
                case 5:
                    showMap();
                    break;
                case 6:
                    displayReport();
                    break;
                case 8:
                    return;
            }
            output.format("     Main Menu\n");
            output.format("------------------\n");
            output.format("1. Show Student ID\n");
            output.format("2. Load System\n");
            output.format("3. Ship Menu\n");
            output.format("4. Port Menu\n");
            output.format("5. Show Map\n");
            output.format("6. Display Report\n\n");
            output.format("8. Quit\n");
            output.format("------------------\n");
            output.format("::>");
            try {
                inputValue = input.nextInt();
                input.nextLine(); // Consume \n
            } catch(InputMismatchException e) {
                input.nextLine();
                inputValue = -1;
            } catch(Exception e) {
                e.printStackTrace();
            }
        } while (inputValue != 8);
    }

    /**
     * Display the student's name, ID number, Class/Section, and Date of program
     */
    public static void showStudentID() {
        output.format("Name: Chris Collander\n");
        output.format("ID: 1001101078\n");
        output.format("CSE 1325-002\n");
        output.format("March 23, 2015\n");
        output.format("------------------\n");
    }

    /**
     * Display the ship menu
     */
    public static void shipMenu() throws NullPointerException {
        int inputValue = -1;
        do {
            switch (inputValue) {
                case 1:
                    // Generate Ships
                    int numShips = -1;
                    while(numShips<1 || numShips>10) {
                        output.format("Please enter the number of ships between 1 and 10\n");
                        output.format("::>");
                        
                        numShips = input.nextInt();
                        input.nextLine(); // Consume \n
                    }
                    map.generateShips(numShips);
                    
                    break;
                case 2:
                    // Update CargoShip
                    shipPropertiesMenu();                 
                    break;
                case 3:
                    // Remove Ships
                    map.getShips().clear();
                    break;
                case 4:
                    // Display Ships
                    for(CargoShip ship : map.getShips()) {
                        ship.display();
                    }
                    break;
            }
            output.format("Ship Menu\n");
            output.format("------------------\n");
            output.format("1. Generate Ships\n");
            output.format("2. Update Ship\n");
            output.format("3. Remove All Ships\n");
            output.format("4. Display Ships\n");
            output.format("5. Previous Menu\n");
            output.format("------------------\n");
            output.format("::>");
            try {
                inputValue = input.nextInt();
                input.nextLine(); // Consume \n
            } catch(InputMismatchException e) {
                inputValue = -1;
            }catch(Exception e) {
                e.printStackTrace();
            }
        } while (inputValue != 5);
    }

    /**
     * Display the menu to update the ship's properties
     */
    public static void shipPropertiesMenu() throws NullPointerException{
        int lcv, shipNum = -1;
        do {
            output.format("Ship Selection Menu\n");
            output.format("------------------\n");
            for(lcv=0;lcv<map.getShips().size();lcv++)
                output.format(lcv+": "+map.getShips().get(lcv).getName()+"\n");
            output.format(lcv+": Previous menu\n\n");
            output.format("------------------\n");
            output.format("Please choose a ship\n");
            output.format("::>");
            try {
                shipNum = input.nextInt();
                input.nextLine(); // Consume \n
            } catch(InputMismatchException e) {
                shipNum = -1;
            }
                catch(Exception e) {
                e.printStackTrace();
            }
            
        } while(shipNum<0 || shipNum>map.getShips().size());
        
        if(shipNum == map.getShips().size())
            return;
        
        int inputValue = -1;
        do {
            switch (inputValue) {
                case 1:
                    output.format("What is the ship's name: ");
                    map.getShips().get(shipNum).setName(input.nextLine());
                    break;
                case 2:
                    output.format("What is the ship's country of registration: ");
                    map.getShips().get(shipNum).setCountryOfRegistration(input.nextLine());
                    break;
                case 3:
                    output.format("What is the ship's transponder number: ");
                    map.getShips().get(shipNum).setTransponderNumber(input.nextLong());
                    input.nextLine(); // Consume \n
                    break;
                case 4:
                    output.format("What is the ship's cargo capacity: ");
                    map.getShips().get(shipNum).setCargoCapacity(input.nextDouble());
                    input.nextLine(); // Consume \n
                    break;
                case 5:
                    output.format("What is the ship's length: ");
                    map.getShips().get(shipNum).setLength(input.nextDouble());
                    input.nextLine(); // Consume \n
                    break;
                case 6:
                    output.format("What is the ship's beam: ");
                    map.getShips().get(shipNum).setBeam(input.nextDouble());
                    input.nextLine(); // Consume \n
                    break;
                case 7:
                    output.format("What is the ship's draft: ");
                    map.getShips().get(shipNum).setDraft(input.nextDouble());
                    input.nextLine(); // Consume \n
                    break;
                case 8:
                    output.format("What is the ship's longitude: ");
                    double newLong = input.nextDouble();
                    input.nextLine(); // Consume \n
                    output.format("What is the ship's latitude: ");
                    double newLat = input.nextDouble();
                    input.nextLine(); // Consume \n
                    
                    // Assign new Long and Lat to the ship
                    map.getShips().get(shipNum).setLongitude(newLong);
                    map.getShips().get(shipNum).setLatitude(newLat);
                    break;
                case 9:
                    output.format("What is the ship's row: ");
                    int newRow2 = input.nextInt();
                    input.nextLine(); // Consume \n
                    output.format("What is the ship's column: ");
                    int newCol2 = input.nextInt();
                    input.nextLine(); // Consume \n
                    
                    // Assign new Row and Col to the ship
                    map.getShips().get(shipNum).setLatitude(MapConverter.row2lat(newRow2));
                    map.getShips().get(shipNum).setLongitude(MapConverter.col2lon(newCol2));
                    break;
                case 10:
                    cargoPropertiesMenu(shipNum);
                    break;
                case 11:
                    map.getShips().get(shipNum).display();
                    break;
            }
            output.format("Ship Properties Menu\n");
            output.format("--------------------------------\n");
            output.format("1. Update Name\n");
            output.format("2. Update Registration\n");
            output.format("3. Update Transponder\n");
            output.format("4. Update Capacity\n");
            output.format("5. Update Length\n");
            output.format("6. Update Beam\n");
            output.format("7. Update Draft\n");
            output.format("8. Update Longitude and Latitude\n");
            output.format("9. Update Row and Column\n");
            output.format("10. Update Cargo\n");
            output.format("11. Display the Ship\n");
            output.format("12. Previous Menu\n\n");
            output.format("--------------------------------\n");
            output.format("::>");

            try {
                inputValue = input.nextInt();
                input.nextLine(); // Consume \n
            } catch(InputMismatchException e) {
                inputValue = -1;
            } catch(Exception e) {
                e.printStackTrace();
            }
        } while (inputValue != 12);
    }

    /**
     * Display the menu to update the ship's cargo
     * @param shipNum CargoShip Number to Edit Cargo of
     */
    public static void cargoPropertiesMenu(int shipNum) {
        int inputValue = -1;
        
        // If there is no cargo, create some!
        if(map.getShips().get(shipNum).getCargo() == null) {
            CargoShip ship = map.getShips().get(shipNum);
            if(ship instanceof OilTanker)
                ship.setCargo(new Oil());
            else if(ship instanceof ContainerShip)
                ship.setCargo(new Box());
            else
                ship.setCargo(new Cargo());
        }
        
        do {
            switch (inputValue) {
                case 1:
                    output.format("What is the cargo's description: ");
                    map.getShips().get(shipNum).getCargo().setDescription(input.nextLine());
                    break;
                case 2:
                    Cargo cargo = map.getShips().get(shipNum).getCargo();
                    if(cargo instanceof Box) {
                        output.format("How many TEUs on the ContainerShip: ");
                        ((Box)map.getShips().get(shipNum).getCargo()).setTeus(input.nextInt());
                    }
                    else if(cargo instanceof Oil) {
                        output.format("How much oil is on the Tanker: ");
                        ((Oil)map.getShips().get(shipNum).getCargo()).setBarrels(input.nextInt());
                    }
                    else { // Just Cargo, no subclass
                        output.format("What is the cargo's weight: ");
                        map.getShips().get(shipNum).getCargo().setTonnage(input.nextDouble());
                        input.nextLine(); // Consume \n
                        break;
                    }
                case 3:
                    map.getShips().get(shipNum).getCargo().display();
                    break;
            }
            output.format("Cargo Properties Menu\n");
            output.format("---------------------\n");
            output.format("1. Update Description\n");
            output.format("2. Update Weight/Amount\n");
            output.format("3. Display Cargo\n");
            output.format("4. Previous Menu\n\n");
            output.format("---------------------\n");
            output.format("::>");
            
            try {
                inputValue = input.nextInt();
                input.nextLine(); // Consume \n
            } catch(InputMismatchException e) {
                inputValue = -1;
            } catch(Exception e) {
                e.printStackTrace();
            }
        } while (inputValue != 4);
    }

    /**
     * Display the Port menu
     */
    public static void portMenu() throws NullPointerException {
        int inputValue = -1;
        do {
            switch(inputValue) {
                case 1:
                    // Update Dock
                   updateDock();
                    break;
                case 2:
                    // Unload CargoShip
                    unloadShip();
                    break;
                case 3:
                    // Display All Cargos
                    displayCargos();
                    break;
                case 4:
                    // Display All Docks
                    displayDocks();
                    break;
            }
            output.format("Port Menu\n");
            output.format("--------------------------\n");
            output.format("1. Update Dock\n");
            output.format("2. Unload Ship\n");
            output.format("3. Display All Cargos\n");
            output.format("4. Display All Docks\n");
            output.format("5. Return to Previous Menu\n\n");
            output.format("--------------------------\n");
            output.format("::>");
            
            try {
                inputValue = input.nextInt();
                input.nextLine(); // Consume \n
            } catch(InputMismatchException e) {
                inputValue = -1;
            } catch(Exception e) {
                e.printStackTrace();
            }
        } while(inputValue != 5);
    }
    
    /**
     * Display the update dock menu
     */
    public static void updateDock() {
        int lcv, dockNum = -1;
        do {
            output.format("Dock Selection Menu\n");
            output.format("------------------\n");
            for(lcv=0;lcv<map.getPort().getDocks().size();lcv++)
                output.format(lcv+": "+map.getPort().getDocks().get(lcv).getName()+"\n");
            output.format(lcv+": Previous menu\n\n");
            output.format("--------------------------\n");
            output.format("Please choose a dock\n");
            output.format("::>");
            
            try {
                dockNum = input.nextInt();
                input.nextLine(); // Consume \n
            } catch(InputMismatchException e) {
                dockNum = -1;
            } catch(Exception e) {
                e.printStackTrace();
            }
            
        } while(dockNum<0 || dockNum>map.getPort().getDocks().size());
        
        if(dockNum == map.getPort().getDocks().size())
            return;
        
        int inputValue = -1;
        do {
            switch (inputValue) {
                case 1:
                    output.format("What is the dock number: ");
                    map.getPort().getDocks().get(dockNum).setDockNumber(input.nextInt());
                    input.nextLine(); // Consume \n
                    break;
                case 2:
                    output.format("What is the length: ");
                    map.getPort().getDocks().get(dockNum).setLength(input.nextDouble());
                    input.nextLine(); // Consume \n
                    break;
                case 3:
                    output.format("What is the width: ");
                    map.getPort().getDocks().get(dockNum).setWidth(input.nextDouble());
                    input.nextLine(); // Consume \n
                    break;
                case 4:
                    output.format("What is the depth: ");
                    map.getPort().getDocks().get(dockNum).setDepth(input.nextDouble());
                    input.nextLine(); // Consume \n
                    break;
                case 5:
                    output.format("What is the dock's longitude: ");
                    map.getPort().getDocks().get(dockNum).setLongitude(input.nextDouble());
                    input.nextLine(); // Consume \n
                    output.format("What is the dock's latitude: ");
                    map.getPort().getDocks().get(dockNum).setLatitude(input.nextDouble());
                    input.nextLine(); // Consume \n
                    break;
            }
            output.format("Dock Properties Menu\n");
            output.format("-----------------------------\n");
            output.format("1. Set the number\n");
            output.format("2. Set the length\n");
            output.format("3. Set the width\n");
            output.format("4. Set the depth\n");
            output.format("5. Set longitude and latitude\n");
            output.format("6. Previous Menu\n\n");
            output.format("-----------------------------\n");
            output.format("::>");
            
            try {
                inputValue = input.nextInt();
                input.nextLine(); // Consume \n
            } catch(InputMismatchException e) {
                inputValue = -1;
            } catch(Exception e) {
                e.printStackTrace();
            }
        } while (inputValue != 6);
    }

    /**
     * Unload CargoShip Menu
     */
    public static void unloadShip() {
        ArrayList<CargoShip> safeShips = new ArrayList<>();
        
        // Get a list of all the safe ships at docks, that have cargo, and are the correct ship and cargotype
        for(Dock dock : map.getPort().getDocks()) {
            int dockRow = MapConverter.lat2row(dock.getLatitude());
            int dockCol = MapConverter.lon2col(dock.getLongitude());
             
            // If there is a ship here
            CargoShip ship = map.getShipAt(dockRow,dockCol);
            if(ship==null)
                continue;
            
            // If the ship fits
            if((dock.getDepth() <= ship.getDraft() || dock.getLength() <= ship.getLength()) || dock.getWidth() <= ship.getBeam())
                continue;
            
            // If the ship has cargo and is correct type
            boolean correctDockAndShip = false;
            if(dock instanceof Pier && ship.getCargo() instanceof Oil)
                correctDockAndShip = true;
            if(dock instanceof Crane && ship.getCargo() instanceof Box)
                correctDockAndShip = true;
            if(!(dock instanceof Pier) && !(dock instanceof Crane) && !(ship.getCargo() instanceof Box) && !(ship.getCargo() instanceof Oil))
                correctDockAndShip = true;
            
            if(!correctDockAndShip)
                continue;
            
            safeShips.add(ship);
        }
        if(safeShips.isEmpty()) {
            output.format("\nThere are no ships safely at dock to unload\n\n");
            return;
        }
        
        int shipNum = -1, lcv;
        do {
            output.format("Ship Selection Menu\n");
            output.format("------------------\n");
            for(lcv=0;lcv<safeShips.size();lcv++)
                output.format("%d: %s\n", lcv, safeShips.get(lcv).getName());
            output.format("%d: Previous menu\n",lcv);
            output.format("-----------------------------\n");
            output.format("What ship do you want to unload\n");
            output.format("::>");
            
            try {
                shipNum = input.nextInt();
                input.nextLine(); // Consume \n
            } catch(InputMismatchException e) {
                output.format("Invalid input, returning to previous menu.\n\n");
            } catch(Exception e) {
                e.printStackTrace();
            }
            
        } while(shipNum<0 || shipNum>map.getPort().getDocks().size()+1);
        
        if(shipNum == safeShips.size())
            return;
        map.getPort().unloadShip(safeShips.get(shipNum));
    }
    
    /**
     * Display all Cargos in the port
     */
    public static void displayCargos() {
        int lcv;
        
        if(map.getPort().getCargos() == null || map.getPort().getCargos().size()==0) {
            output.format("There are currently no cargos in the port\n");
            return;
        }
        
        for(lcv=0;lcv<map.getPort().getCargos().size();lcv++) {
            output.format("%d: ",lcv);
            map.getPort().getCargos().get(lcv).display();
        }
    }
    
    /**
     * Display all Docks in the port
     */
    public static void displayDocks() {
        int lcv;
        for(lcv=0;lcv<map.getPort().getDocks().size();lcv++) {
            output.format("%d: ",lcv);
            map.getPort().getDocks().get(lcv).display();
            output.format("\n");
        }
    }
    
    /**
     * Display the Map
     */
    public static void showMap() {
        map.showMap();
    }
    
    /**
     * Display the Report
     */
    public static void displayReport() {
        output.format("Status Report\n");
        output.format("--------------------------------\n");
        for(int lcv=0;lcv<map.getShips().size();lcv++) {
            output.format("Ship #%d\n",lcv);
            CargoShip ship = map.getShips().get(lcv);
            
            // Display the ship
            ship.display();
            
            // Get the Row/Col
            int row = MapConverter.lat2row(ship.getLatitude());
            int col = MapConverter.lon2col(ship.getLongitude());
            
            // At sea/dock
            boolean dockFound = false;
            for(Dock dock : map.getPort().getDocks()) {
                int dockRow = MapConverter.lat2row(dock.getLatitude());
                int dockCol = MapConverter.lon2col(dock.getLongitude());
                if(row == dockRow && col == dockCol) {
                    dockFound = true;
                    output.format("Ship is at dock %s\n",dock.getName());
                }
            }
            if(!dockFound)
                output.format("Ship is at sea\n");
            
            // Safety Report
            if(map.isShipSafe(row, col)) {
                output.format("Ship is safe\n");
            } else {
                output.format("Ship is unsafe\n");
            }
            output.format("-------------------------------\n");
        }
        // Docks
        output.format("\nDocks\n");
        output.format("---------------------------------\n");
        for(Dock dock : map.getPort().getDocks()) {
            dock.display();
            output.format("\n");
        }
        // Carogs
        output.format("\nCargos\n");
        output.format("---------------------------------\n");
        for(Cargo cargo : map.getPort().getCargos()) {
            cargo.display();
            output.format("\n");
        }
        output.format("---------------------------------\n");
    }
    
    /**
     * saveMap menu
     */
    public static void saveMap() {
        String fileName;
        output.format("What are you going to call this Map file? ");
        try {
            fileName = input.nextLine().trim();
            output.format("Saving %s...\n",fileName+".map.txt");
            FileHandler.SetMapFile(fileName+".map.txt", map);
            output.format("Saving %s...\n",fileName+".ship.txt");
            FileHandler.setShipFile(fileName+".ship.txt",map.getShips());
            output.format("Saving %s...\n",fileName+".port.txt");
            FileHandler.setPortFile(fileName+".port.txt",map.getPort());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
        
    /**
     * loadMap menu
     */
    public static void loadMap() {
        String fileName;
        output.format("What is the name of the Map file to open? ");
        try {
            fileName = input.nextLine().trim();
            output.format("Loading %s...\n",fileName+".map.txt");
            map = FileHandler.getMapFile(fileName+".map.txt");
            output.format("Loading %s...\n",fileName+".ship.txt");
            map.setShips(FileHandler.getShipFile(fileName+".ship.txt"));
            output.format("Loading %s...\n",fileName+".port.txt");
            map.setPort(FileHandler.getPortFile(fileName+".port.txt"));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void loadSystem() {
        String fileName;
        output.format("Please enter a tag for your load files: ");
        try {
            fileName = input.nextLine().trim();
            map = FileHandler.getMapFile(fileName+".map.txt");
            map.setPort(FileHandler.getPortFile(fileName+".port.txt"));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}