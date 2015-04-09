package main;


public class OilSpill {
    private Position position;
    
    public OilSpill() {
        position = new Position();
    }
    
    public OilSpill(Position position) {
        this.position = position;
    }
    
    public static OilSpill newOilSpill(Position position) {
        OilSpill ret = new OilSpill(position);
        return ret;
    }

    /**
     * @return the position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Position position) {
        this.position = position;
    }
    
    
}
