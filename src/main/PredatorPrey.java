package main;

public class PredatorPrey implements Runnable{
    private final Map map;
    private final int DELAY = 500; // In Milliseconds
    
    public PredatorPrey(Map map) {
        this.map = map;
    }
    
    @Override
    public void run() {
        // Keep repeating until there are no more ships
        while(!map.getShips().isEmpty()) {
            step();
            try {
                Thread.sleep(DELAY);
            } catch (Exception ex) {
                System.out.println("This platform does not support Thread.sleep()\n");
            }
        }
    }
    
    public double distance(SeaMonster monster, CargoShip ship) {
        int y = monster.getRow() - ship.getRow();
        int x = monster.getCol() - ship.getCol();
        return Math.sqrt(y*y + x*x);
    }
    
    public void step() {
        // Place an individual step for Predator/Prey here!
        
        /*  SUMMARY OF STEPS
         1. Loop through each monster
         2. Find the nearest ship to the monster
         3. Move one step towards the ship, unless something is in the way (COMPLICATED)
        */
        
        for(SeaMonster m : map.getMonsters()) {
            CargoShip nearestShip = map.getShips().get(0);
            for(CargoShip s : map.getShips()) {
                if(distance(m,s) > distance(m,nearestShip))
                    nearestShip = s;
            }
            // Move m closer to s
            int dx, dy;
            if(m.getRow()==nearestShip.getRow())
                dy=0;
            else if(m.getRow() < nearestShip.getRow())
                dy=1;
            else
                dy=-1;
            
            if(m.getCol()==nearestShip.getCol())
                dx=0;
            else if(m.getCol() < nearestShip.getCol())
                dx=1;
            else
                dx=-1;
            
            
            m.setRow(m.getRow()+dy);
            m.setCol(m.getCol()+dx);
            
        }
        
        // Place Ship movement here
    }
    
}