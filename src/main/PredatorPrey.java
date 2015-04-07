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
    }
    
}
