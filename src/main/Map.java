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
    private ArrayList<SeaMonster> monsters;
    private Port port;

    /**
     * Default constructor for Map
     */
    public Map() {
        matrix = new char[0][0];

        ships = new ArrayList<>();
        monsters = new ArrayList<>();
        port = new Port();
    }

    /*
     * Method to determin if there is a monster at a specific grid position
     *
     *@param position the current position
     *@return true if there is a monster at position, otherwise false
     *SHOULD implement position checking correctly
     */
    public boolean isMonster(Position position) {
        if (getMonsters().isEmpty()) {
            return false;
        }

        for (SeaMonster monster : getMonsters()) {
            if (monster.getPosition().equals(position)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Method to find if there is a dock at a specific grid position
     *
     * @param row The grid row
     * @param col The grid column
     * @return true if there is a dock at row/col, false otherwise
     */
    public boolean isDock(int row, int col) {

        if (port.getDocks().isEmpty()) {
            return false;
        }

        for (Dock dock : port.getDocks()) {
            if (row == dock.getRow() && col == dock.getCol()) {
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
    public boolean isShip(int row, int col) {

        if (ships.isEmpty()) {
            return false;
        }

        for (CargoShip ship : ships) {
            if (row == ship.getRow() && col == ship.getCol()) {
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

        // Is the ship on land
        if (matrix[row][col] == '*' && !isDock(row, col)) {
            return false;
        }

        // Get a count of the number of ships at this location. If more than one, return FALSE
        int count = 0;
        for (CargoShip ship : ships) {
            if (ship.getRow() == row && ship.getCol() == col) {
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
        for (CargoShip ship : ships) {
            if (row == ship.getRow() && col == ship.getCol()) {
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
    public Dock getDockAt(int row, int col) {
        for (Dock dock : port.getDocks()) {
            if (row == dock.getRow() && col == dock.getCol()) {
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
     * Generate a number of ships randomly, and placing them in ArrayList Ships
     *
     * @param numShips The number of ships to generate
     */
    public void generateShips(int numShips) throws NullPointerException {

        // First things first, make sure we don't overload our names!
        if ((numShips + ships.size()) > 100) {
            numShips = 100 - ships.size();
        }

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

            int row = 0, col = 0;
            double randLat = 0, randLong = 0;

            //Make sure the matrix is initialized
            if (matrix.length == 0 || matrix[0].length == 0) {
                return;
            }

            // Keep generating locations until a valid location is found
            while (!validLocation) {
                row = randomGenerator.nextInt(matrix.length);
                col = randomGenerator.nextInt(matrix[0].length);

                if (!isShip(row, col) && (matrix[row][col] == '.' || isDock(row, col))) {
                    if(isDock(row, col)) {
				Dock dock = getDockAt(row,col);
                                CargoShip ship = getShipAt(row, col);
				if(dock instanceof Pier && ship instanceof OilTanker)
					validLocation = true;
				if(dock instanceof Crane && ship instanceof ContainerShip)
					validLocation = true;
				if( !(dock instanceof Crane) && !(dock instanceof Pier) && !(ship instanceof ContainerShip) && !(ship instanceof OilTanker))
					validLocation = true;
			}
			else
				validLocation = true;
                }
            }

            currShip.setRow(row);
            currShip.setCol(col);

            // Randomly generate a first and last name for the ship
            String firstName = "ERROR";
            String lastName = "ERROR";
            boolean nameUsed = true;

            while (nameUsed) {
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
                String shipName = firstName + " " + lastName;
                boolean found = false;
                for (CargoShip ship : ships) {
                    if (shipName.equalsIgnoreCase(ship.getName())) {
                        found = true;
                    }
                }
                if (!found) {
                    nameUsed = false;
                }
            }

            currShip.setName(firstName + " " + lastName);

            long minLong = 1000000L;
            long maxLong = 10000000L;
            long randomLong = minLong + ((long) (randomGenerator.nextDouble() * (maxLong - minLong)));
            currShip.setTransponderNumber(randomLong);

            ships.add(currShip);
        }
    }

    /**
     * Generate a number of monsters between 1 and 10, randomly, and placing
     * them in ArrayList monsters
     *
     * @param numMonsters The number of monsters to generate
     */
    public void generateMonsters(int numMonsters) {
        int numK = 0;
        int numL = 0;
        int numS = 0;

        // Count how many we have of what types so far
        for (SeaMonster monster : monsters) {
            if (monster instanceof Kraken) {
                numK++;
            }
            if (monster instanceof Leviathan) {
                numL++;
            }
            if (monster instanceof SeaSerpent) {
                numS++;
            }
        }

        int index;

        for (index = 0; index < numMonsters; index++) {
            SeaMonster currMonster;
            Position currPosition = new Position();
            Random randomGenerator = new Random();

            int monsterType = randomGenerator.nextInt(3);
            switch (monsterType) {
                case 0:
                    currMonster = new Kraken();
                    numK++;
                    currMonster.setType(currMonster.getType() + " " + numK);
                    break;
                case 1:
                    currMonster = new Leviathan();
                    numL++;
                    currMonster.setType(currMonster.getType() + " " + numL);
                    break;
                default:
                    currMonster = new SeaSerpent();
                    numS++;
                    currMonster.setType(currMonster.getType() + " " + numS);
                    break;
            }

            boolean validLocation = false;

            // Keep generating locations until a valid location is found
            while (!validLocation) {
                currPosition = new Position(randomGenerator.nextInt(matrix.length), randomGenerator.nextInt(matrix[0].length));

                if (!isMonster(currMonster.getPosition()) && matrix[currPosition.getRow()][currPosition.getCol()] == '.') {
                    validLocation = true;
                }
            }

            currMonster.setPosition(currPosition);
            getMonsters().add(currMonster);
        }
    }

    /**
     * @return the monsters
     */
    public ArrayList<SeaMonster> getMonsters() {
        return monsters;
    }

    /**
     * @param monsters the monsters to set
     */
    public void setMonsters(ArrayList<SeaMonster> monsters) {
        this.monsters = monsters;
    }

    public SeaMonster getMonsterAt(int row, int col) {
        for (SeaMonster monster : monsters) {
            if (monster.getRow() == row && monster.getCol() == col) {
                return monster;
            }
        }
        return null;
    }
}
