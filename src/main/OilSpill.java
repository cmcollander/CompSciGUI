package main;

import java.util.Random;

public class OilSpill {

    private Position position;
    private int direction;
    private Xform model;

    public OilSpill() {
        position = new Position();
        direction = new Random().nextInt(4);
        model = new Xform();
    }

    public OilSpill(Position position) {
        this.position = position;
        direction = new Random().nextInt(4);
        model = new Xform();
    }

    public void updateXform() {
        model.setTranslateX(5 + position.getCol() * 10);
        model.setTranslateZ(5 + position.getRow() * 10);
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

    /**
     * @return the direction
     */
    public int getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * @return the model
     */
    public Xform getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(Xform model) {
        this.model = model;
    }

}
