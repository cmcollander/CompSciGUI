package main;

import java.util.Random;

public class PredatorPrey {

    public static double distance(SeaMonster m, CargoShip s) {
        return Math.sqrt((m.getRow() - s.getRow()) * (m.getRow() - s.getRow()) + (m.getCol() - s.getCol()) * (m.getCol() - s.getCol()));
    }

    //same concept as above but for ships and docks
    public static double distance(CargoShip s, Dock d) {
        return Math.sqrt((s.getRow() - d.getRow()) * (s.getRow() - d.getRow()) + (s.getCol() - d.getCol()) * (s.getCol() - d.getCol()));
    }

    public static void delay() {
        try {
            Thread.sleep(100);
        } catch (Exception ex) {
            System.out.println("Doesn't support Thread.sleep");
        }
    }

    public static void step(Map map) {
        for (SeaMonster monster : map.getMonsters()) {
            CargoShip closestShip = map.getShips().get(0); // Assume the first ship is the closest
            for (CargoShip ship : map.getShips()) {
                double currDistance = distance(monster, ship);
                if (currDistance < distance(monster, closestShip)) {
                    closestShip = ship;
                }
            }

            int dx, dy;
            if (monster.getCol() < closestShip.getCol()) {
                dx = 1;
            } else if (monster.getCol() > closestShip.getCol()) {
                dx = -1;
            } else {
                dx = 0;

            }

            if (monster.getRow() < closestShip.getRow()) {
                dy = 1;
            } else if (monster.getRow() > closestShip.getRow()) {
                dy = -1;
            } else {
                dy = 0;

            }

            // Lets add a step of randomness here!
            int newRow = constrain(monster.getRow() + randStep(dy), 0, 35);
            int newCol = constrain(monster.getCol() + randStep(dx), 0, 53);

            // If the monster will land on water or a dock, move
            if (!map.isMonster(new Position(newRow, newCol))) {
                if (map.getMatrix()[newRow][newCol] == '.' || map.isDock(newRow, newCol) || monster instanceof Godzilla) {
                    monster.setCol(newCol);
                    monster.setRow(newRow);
                }
            }

            // Adjust the monster's Y position based on land or water
            monster.getModel().setTranslateY(monster instanceof Godzilla ? ((map.getMatrix()[monster.getRow()][monster.getCol()] == '*') ? 2 : 0) : 0); // Update Monster Height
        }

        // Start ship processing here
        for (CargoShip ship : map.getShips()) {
            Dock closestDock = map.getPort().getDocks().get(0);
            for (Dock dock : map.getPort().getDocks()) {
                double currDistance = distance(ship, dock);
                if (currDistance < distance(ship, closestDock)) {
                    closestDock = dock;
                }
            }

            int dx = 0, dy = 0;
            if (ship.getCol() < closestDock.getCol()) {
                dx = 1;
            } else if (ship.getCol() > closestDock.getCol()) {
                dx = -1;
            } else {
                dx = 0;
            }

            if (ship.getRow() < closestDock.getRow()) {
                dy = 1;
            } else if (ship.getRow() > closestDock.getRow()) {
                dy = -1;
            } else {
                dy = 0;
            }

            // Lets add a step of randomness here!
            int newRow = constrain(ship.getRow() + randStep(dy), 0, 35);
            int newCol = constrain(ship.getCol() + randStep(dx), 0, 53);

            // If the monster will land on water or a dock, move
            if (!map.isShip(newRow, newCol)) {
                if (map.getMatrix()[newRow][newCol] == '.' || map.isDock(newRow, newCol)) {
                    ship.setCol(newCol);
                    ship.setRow(newRow);
                }
            }
        }
    }

    private static int constrain(int val, int min, int max) {
        if (val < min) {
            return min;
        }
        if (val > max) {
            return max;
        }
        return val;
    }

    public static int randStep(int d) {
        Random rand = new Random();
        // 25% chance to change direction
        if (rand.nextInt(100) < 25) {
            int num = rand.nextInt(3);
            d = 1 - num;
        }
        // 15% chance to stop
        if (rand.nextInt(100) < 15) {
            d = 0;
        }
        // 15% chance to double speed
        if (rand.nextInt(100) < 15) {
            d *= 2;
        }

        return d;
    }
}
