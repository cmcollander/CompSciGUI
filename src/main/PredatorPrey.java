package main;

import java.util.Random;

public class PredatorPrey {
    
    public static double distance(Position a, Position b) {
        int dr = b.getRow() - a.getRow();
        int dc = b.getCol() - a.getCol();
        return Math.hypot(dr, dc);
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

                // If there are no ships and no monsters but godzilla, return
                if (map.getShips().isEmpty() && map.getMonsters().size() == 1) {
                    return;
                }
                
                Position closestPosition = new Position(500, 500);
                for (CargoShip ship : map.getShips()) {
                    if (distance(monster.getPosition(), ship.getPosition()) < distance(monster.getPosition(), closestPosition)) {
                        closestPosition = ship.getPosition();
                    }
                }
                for (SeaMonster monster2 : map.getMonsters()) {
                    if (monster.equals(monster2)) // Skip Godzilla
                    {
                        continue;
                    }
                    if (distance(monster.getPosition(), monster2.getPosition()) < distance(monster.getPosition(), closestPosition)) {
                        closestPosition = monster2.getPosition();
                    }
                }
                
                int dx = (int) Math.signum(closestPosition.getCol() - monster.getCol());
                int dy = (int) Math.signum(closestPosition.getRow() - monster.getRow());

                // Lets add a step of randomness here!
                int newRow = constrain(monster.getRow() + randStep(dy), 0, 35);
                int newCol = constrain(monster.getCol() + randStep(dx), 0, 53);

                // Change the monster's actual direction based on dx and dy
                if (dx != 0 || dy != 0) {
                    monster.setDirection(Math.toDegrees(Math.atan2(-(double) dy, (double) dx)) / 90.0);
                }
                
                monster.setRow(newRow);
                monster.setCol(newCol);

                // Adjust godzilla's Y position based on land or water
                monster.getModel().setTranslateY(monster instanceof Godzilla ? ((map.getMatrix()[monster.getRow()][monster.getCol()] == '*') ? 2 : 0) : 0);
            } // Other monsters here!
            else {
                if (!map.getShips().isEmpty()) {
                    CargoShip closestShip = map.getShips().get(0); // Assume the first ship is the closest
                    for (CargoShip ship : map.getShips()) {
                        double currDistance = distance(monster.getPosition(), ship.getPosition());
                        if (currDistance < distance(monster.getPosition(), closestShip.getPosition())) {
                            closestShip = ship;
                        }
                    }
                    
                    int dx = (int) Math.signum(closestShip.getCol() - monster.getCol());
                    int dy = (int) Math.signum(closestShip.getRow() - monster.getRow());

                    // Lets add a step of randomness here!
                    int newRow = constrain(monster.getRow() + randStep(dy), 0, 35);
                    int newCol = constrain(monster.getCol() + randStep(dx), 0, 53);

                    // Change the monster's actual direction based on dx and dy
                    if (dx != 0 || dy != 0) {
                        monster.setDirection(Math.toDegrees(Math.atan2(-(double) dy, (double) dx)) / 90.0);
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
                double currDistance = distance(ship.getPosition(), dock.getPosition());
                if (currDistance < distance(ship.getPosition(), closestDock.getPosition())) {
                    closestDock = dock;
                }
            }
            
            int dx = (int) Math.signum(closestDock.getCol() - ship.getCol());
            int dy = (int) Math.signum(closestDock.getRow() - ship.getRow());

            // Lets add a step of randomness here!
            int newRow = constrain(ship.getRow() + randStep(dy), 0, 35);
            int newCol = constrain(ship.getCol() + randStep(dx), 0, 53);

            // Change the ship's actual direction based on dx and dy
            if (dx != 0 || dy != 0) {
                ship.setDirection(Math.toDegrees(Math.atan2(-(double) dy, (double) dx)) / 90.0);
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
        
        int dx = (int) Math.signum(g.getCol() - e.getPosition().getCol());
        int dy = (int) Math.signum(g.getRow() - e.getPosition().getRow());

        // Enterprise goes twice as fast as any other entity on the map. GODZILLA CANT ESCAPE!!!
        dx *= 2;
        dy *= 2;

        // Lets add a step of randomness here!
        int newRow = constrain(e.getPosition().getRow() + randStep(dy), 0, 35);
        int newCol = constrain(e.getPosition().getCol() + randStep(dx), 0, 53);

        // Change the ship's actual direction based on dx and dy
        if (dx != 0 || dy != 0) {
            e.setDirection(Math.toDegrees(Math.atan2(-(double) dy, (double) dx)) / 90.0);
        }
        
        e.getPosition().setRow(newRow);
        e.getPosition().setCol(newCol);

        // Moving all actual collision detection to main collisions method
        /*if (distance(e.getPosition(), g.getPosition()) < 4) {
         g.getModel().setTranslateY(10000);
         g.setModel(null);
         map.getMonsters().remove(g);
         g = null;
         }*/
    }
}
