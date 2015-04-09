package main;

public class PredatorPrey {

    public static double distance(SeaMonster m, CargoShip s) {
        return Math.sqrt((m.getRow() - s.getRow()) * (m.getRow() - s.getRow()) + (m.getCol() - s.getCol()) * (m.getCol() - s.getCol()));
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

            if (!map.isMonster(new Position(monster.getRow() + dy, monster.getCol() + dx))) {
                monster.setCol(monster.getCol() + dx);
                monster.setRow(monster.getRow() + dy);
            }

        }

    }

}