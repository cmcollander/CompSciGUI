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
    
    public void step() {
        // Place an individual step for Predator/Prey here!
        
        /*  SUMMARY OF STEPS
         1. Loop through each monster
         2. Find the nearest ship to the monster
         3. Move one step towards the ship, unless something is in the way (COMPLICATED)
        */
    }
    
}
