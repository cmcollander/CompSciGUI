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
        // Highest Predator First
        stepEnterprise(map);
        stepMonsters(map);
        stepShips(map);
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

    public static void stepMonsters(Map map) {
        for (SeaMonster monster : map.getMonsters()) {
            // Godzilla code here!
            if (monster instanceof Godzilla) {
                // TODO!! Pretty much same as monster code except will go towards monster if a monster is closer than a ship!
                // Adjust godzilla's Y position based on land or water
                monster.getModel().setTranslateY(monster instanceof Godzilla ? ((map.getMatrix()[monster.getRow()][monster.getCol()] == '*') ? 2 : 0) : 0);
            } // Other monsters here!
            else {
                if (!map.getShips().isEmpty()) {
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

                    // Change the monster's actual direction based on dx and dy
                    if (dx != 0 || dy != 0) {
                        monster.setDirection(Math.toDegrees(Math.atan(-(double) dy / (double) dx)) / 90.0);
                    }

                    // If the monster will land on water or a dock, move
                    if (!map.isMonster(new Position(newRow, newCol))) {
                        if (map.getMatrix()[newRow][newCol] == '.' || map.isDock(newRow, newCol) || monster instanceof Godzilla) {
                            monster.setCol(newCol);
                            monster.setRow(newRow);
                        }
                    }
                }
            }
        }
    }

    public static void stepShips(Map map) {
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

            // Change the ship's actual direction based on dx and dy
            if (dx != 0 || dy != 0) {
                ship.setDirection(Math.toDegrees(Math.atan(-(double) dy / (double) dx)) / 90.0);
            }

            // If the monster will land on water or a dock, move
            if (!map.isShip(newRow, newCol)) {
                if (map.getMatrix()[newRow][newCol] == '.' || map.isDock(newRow, newCol)) {
                    ship.setCol(newCol);
                    ship.setRow(newRow);
                }
            }
        }
    }

    public static void stepEnterprise(Map map) {
        // Only need to step Enterprise if the map has both a godzilla and an enterprise
        if (!map.hasEnterprise() || !map.hasGodzilla()) {
            return;
        }

        Enterprise e = map.getEnterprise();
        Godzilla g = map.getGodzilla();

        int dx = 0, dy = 0;

        if (g.getPosition().getCol() > e.getPosition().getCol()) {
            dx = 1;
        } else if (g.getPosition().getCol() < e.getPosition().getCol()) {
            dx = -1;
        } else {
            dx = 0;
        }

        if (g.getPosition().getRow() > e.getPosition().getRow()) {
            dy = 1;
        } else if (g.getPosition().getRow() < e.getPosition().getRow()) {
            dy = -1;
        } else {
            dy = 0;
        }

        // Enterprise goes twice as fast as any other entity on the map. GODZILLA CANT ESCAPE!!!
        dx *= 2;
        dy *= 2;

        // Lets add a step of randomness here!
        int newRow = constrain(e.getPosition().getRow() + randStep(dy), 0, 35);
        int newCol = constrain(e.getPosition().getCol() + randStep(dx), 0, 53);

        // Change the ship's actual direction based on dx and dy
        if (dx != 0 || dy != 0) {
            e.setDirection(Math.toDegrees(Math.atan(-(double) dy / (double) dx)) / 90.0);
        }

        e.getPosition().setRow(newRow);
        e.getPosition().setCol(newCol);

        if (distance(e, g) < 4) {
            g.getModel().setTranslateY(10000);
            g.setModel(null);
            map.getMonsters().remove(g);
            g = null;
        }

    }

    private static int distance(Enterprise e, Godzilla g) {
        return (int) Math.sqrt((e.getPosition().getCol() - g.getPosition().getCol()) * (e.getPosition().getCol() - g.getPosition().getCol()) + (e.getPosition().getRow() - g.getPosition().getRow()) * (e.getPosition().getRow() - g.getPosition().getRow()));
    }
}
