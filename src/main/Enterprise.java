package main;

import java.util.Random;

public class Enterprise {
    private Position position;
    private Xform model;
    private double direction;
    
    public Enterprise() {
        Random rand = new Random();
        position = new Position(rand.nextInt(36), rand.nextInt(54));
        model = new Xform();
        direction = rand.nextDouble()*4;
    }
    
    public void updateXform() {
        model.setScale(0.01);
        model.setTranslateY(30);
        model.setTranslateX(5 + position.getCol() * 10);
        model.setTranslateZ(5 + position.getRow() * 10);
        model.setRotateY(direction*90.0);
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

    /**
     * @return the direction
     */
    public double getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(double direction) {
        this.direction = direction;
    }
    
    
}
