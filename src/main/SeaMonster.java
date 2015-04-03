/*
 Chris Collander
 Abdul Rafey Khan
 Clint Wetzel

 CSE 1325-002
 Semester Project
 */
package main;

//The seamonster class is used as an abstract class for creating different seamonsters.
public abstract class SeaMonster {

    protected Position position;
    protected String type;

    public abstract String battleCry();

    //Basic constructor
    public SeaMonster() {
        this.position = new Position();
        this.type = "Sea Monster";
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the position
     */
    public Position getPosition() {
        return position;
    }
}
