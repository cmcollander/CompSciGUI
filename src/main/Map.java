/*
 Chris Collander
 Abdul Rafey Khan
 Clint Wetzel

 CSE 1325-002
 Semester Project
 */
package main;

import java.util.ArrayList;
import java.util.Random;

/**
 * The Map class is used to create Map objects
 */
public class Map {

    private char[][] matrix;

    private ArrayList<CargoShip> ships;
    private Port port;

    /**
     * Default constructor for Map
     */
    public Map() {
        matrix = new char[0][0];

        ships = new ArrayList<>();
        port = new Port();
    }

    /**
     * Display the Map's Components
     */
    public void display() {
        port.display();
        for (CargoShip ship : ships) {
            ship.display();
            System.out.println("Safety: " + isShipSafe(MapConverter.lat2row(ship.getLatitude()), MapConverter.lon2col(ship.getLongitude())) + "\n");
        }
    }

    /**
     * Display the Map in Visual form
     */
    public void showMap() {
        /*
         Start out by rounding all the ships to the correct rows, fixing any incompatible values.
         */
        for (CargoShip ship : ships) {
            ship.setLatitude(MapConverter.row2lat(MapConverter.lat2row(ship.getLatitude())));
            ship.setLongitude(MapConverter.col2lon(MapConverter.lon2col(ship.getLongitude())));
        }

        int row, col;
        for (row = 0; row < matrix.length; row++) {
            for (col = 0; col < matrix[0].length; col++) {
                char currentChar = matrix[row][col];
                if (isShip(row, col)) {
                    if (!isShipSafe(row, col)) {
                        currentChar = 'X';
                    } else if (isDock(row, col)) {
                        currentChar = '$';
                    } else {
                        currentChar = getShipAt(row, col).getShipSymbol();
                    }
                } else {
                    if (isDock(row, col)) {
                        currentChar = getDockAt(row, col).getDockSymbol();
                    }
                }
                System.out.print(currentChar);
            }
            System.out.println();
        }
    }

    /**
     * Method to find if there is a dock at a specific grid position
     *
     * @param row The grid row
     * @param col The grid column
     * @return true if there is a dock at row/col, false otherwise
     */
    private boolean isDock(int row, int col) {
        double lat = MapConverter.row2lat(row);
        double lon = MapConverter.col2lon(col);

        if (port.getDocks().isEmpty()) {
            return false;
        }

        for (Dock dock : port.getDocks()) {
            if (row == MapConverter.lat2row(dock.getLatitude()) && col == MapConverter.lon2col(dock.getLongitude())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to find if there is a ship at a specific grid position
     *
     * @param row The grid row
     * @param col The grid column
     * @return true if there is a ship at row/col, false otherwise
     */
    private boolean isShip(int row, int col) {

        if (ships.isEmpty()) {
            return false;
        }

        for (CargoShip ship : ships) {
            if (row == MapConverter.lat2row(ship.getLatitude()) && col == MapConverter.lon2col(ship.getLongitude())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Is the ship at location row, col safe?
     *
     * @param row The grid row
     * @param col The grid col
     * @return True if ship is safe, False otherwise
     */
    public boolean isShipSafe(int row, int col) throws NullPointerException {
        // If there is no ship at this location, default to FALSE
        if (!isShip(row, col)) {
            return false;
        }

        // Get a count of the number of ships at this location. If more than one, return FALSE
        int count = 0;
        for (CargoShip ship : ships) {
            if (MapConverter.lat2row(ship.getLatitude()) == row && MapConverter.lon2col(ship.getLongitude()) == col) {
                count++;
            }
        }
        if (count > 1) {
            return false;
        }

        // If is at dock
        if (isDock(row, col)) {
            Dock dock = getDockAt(row, col);
            CargoShip ship = getShipAt(row, col);

            // If the ship has cargo and is correct type
            boolean correctDockAndShip = false;
            if (dock instanceof Pier && ship instanceof OilTanker) {
                correctDockAndShip = true;
            }
            if (dock instanceof Crane && ship instanceof ContainerShip) {
                correctDockAndShip = true;
            }
            if (!(dock instanceof Pier) && !(dock instanceof Crane) && !(ship instanceof ContainerShip) && !(ship instanceof OilTanker)) {
                correctDockAndShip = true;
            }

            if (!correctDockAndShip) {
                return false;
            }
        }

        return true;
    }

    /**
     * Obtain a CargoShip object at position row/col.
     *
     * @param row The grid row
     * @param col The grid row
     * @return The ship at the requested location
     */
    public CargoShip getShipAt(int row, int col) {
        double lat = MapConverter.row2lat(row);
        double lon = MapConverter.col2lon(col);

        for (CargoShip ship : ships) {
            if (row == MapConverter.lat2row(ship.getLatitude()) && col == MapConverter.lon2col(ship.getLongitude())) {
                return ship;
            }
        }
        return null;
    }

    /**
     * Obtain a Dock object at position row/col.
     *
     * @param row The grid row
     * @param col The grid row
     * @return The dock at the requested location
     */
    private Dock getDockAt(int row, int col) {
        for (Dock dock : port.getDocks()) {
            if (row == MapConverter.lat2row(dock.getLatitude()) && col == MapConverter.lon2col(dock.getLongitude())) {
                return dock;
            }
        }
        return null;
    }

    /**
     * @return the matrix Get method for the matrix
     */
    public char[][] getMatrix() {
        return matrix;
    }

    /**
     * @param matrix the matrix to set Set method for the matrix
     */
    public void setMatrix(char[][] matrix) {
        this.matrix = matrix;
    }

    /**
     * @return the ships Ships get method
     */
    public ArrayList<CargoShip> getShips() {
        return ships;
    }

    /**
     * @param ships the ships to set Ships set method
     */
    public void setShips(ArrayList<CargoShip> ships) {
        this.ships = ships;
    }

    /**
     * @return the port Get method for the port
     */
    public Port getPort() {
        return port;
    }

    /**
     * @param port the port to set Set method for the port
     */
    public void setPort(Port port) {
        this.port = port;
    }

    /**
     * Generate a number of ships between 1 and 10, randomly, and placing them
     * in ArrayList Ships
     *
     * @param numShips The number of ships to generate
     */
    public void generateShips(int numShips) throws NullPointerException {

        int lcv;

        for (lcv = 0; lcv < numShips; lcv++) {

            CargoShip currShip;// = new CargoShip();

            Random randomGenerator = new Random();

            // determine ship type
            int shipType = randomGenerator.nextInt(3);
            switch (shipType) {
                case 0:
                    currShip = new CargoShip();
                    break;
                case 1:
                    currShip = new ContainerShip();
                    break;
                default:
                    currShip = new OilTanker();
                    break;
            }

            boolean validLocation = false;

            int row, col;
            double randLat = 0, randLong = 0;

            //Make sure the matrix is initialized
            if (matrix.length == 0 || matrix[0].length == 0) {
                return;
            }

            // Keep generating locations until a valid location is found
            while (!validLocation) {
                row = randomGenerator.nextInt(matrix.length);
                randLat = MapConverter.row2lat(row);
                col = randomGenerator.nextInt(matrix[0].length);
                randLong = MapConverter.col2lon(col);

                if (!isShip(row, col) && (matrix[row][col] == '.' || isDock(row, col))) {
                    validLocation = true;
                }
            }

            currShip.setLatitude(randLat);
            currShip.setLongitude(randLong);

            // Randomly generate a first and last name for the ship
            String firstName;
            String lastName;
            int firstNum = randomGenerator.nextInt(10);
            int lastNum = randomGenerator.nextInt(10);

            switch (firstNum) {
                case 0:
                    firstName = "Red";
                    break;
                case 1:
                    firstName = "Green";
                    break;
                case 2:
                    firstName = "Dark";
                    break;
                case 3:
                    firstName = "Light";
                    break;
                case 4:
                    firstName = "Day";
                    break;
                case 5:
                    firstName = "Night";
                    break;
                case 6:
                    firstName = "Savanah";
                    break;
                case 7:
                    firstName = "Mountain";
                    break;
                case 8:
                    firstName = "Captain's";
                    break;
                case 9:
                    firstName = "Admiral's";
                    break;
                default:
                    firstName = "ERROR";
            }

            switch (lastNum) {
                case 0:
                    lastName = "Buffalo";
                    break;
                case 1:
                    lastName = "Pastures";
                    break;
                case 2:
                    lastName = "Knight";
                    break;
                case 3:
                    lastName = "Wave";
                    break;
                case 4:
                    lastName = "Star";
                    break;
                case 5:
                    lastName = "Moon";
                    break;
                case 6:
                    lastName = "Lion";
                    break;
                case 7:
                    lastName = "Goat";
                    break;
                case 8:
                    lastName = "Pride";
                    break;
                case 9:
                    lastName = "Joy";
                    break;
                default:
                    lastName = "ERROR";
                    break;
            }

            currShip.setName(firstName + " " + lastName);

            long minLong = 1000000L;
            long maxLong = 10000000L;
            long randomLong = minLong + ((long) (randomGenerator.nextDouble() * (maxLong - minLong)));
            currShip.setTransponderNumber(randomLong);

            ships.add(currShip);
        }
    }
}
