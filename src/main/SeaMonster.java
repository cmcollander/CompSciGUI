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

    public int getRow() {
        return position.getRow();
    }

    public int getCol() {
        return position.getCol();
    }

    public void setRow(int row) {
        position.setRow(row);
    }

    public void setCol(int col) {
        position.setCol(col);
    }

    public double getLongitude() {
        return position.getLongitude();
    }

    public double getLatitude() {
        return position.getLatitude();
    }

    public void setLongitude(double lon) {
        position.setLongitude(lon);
    }

    public void setLatitude(double lat) {
        position.setLatitude(lat);
    }

    public String display() {
        String ret = "";
        ret += "Monster Type: " + type + "\n";
        ret += "Location: (" + String.format("%.6f", getLongitude()) + "," + String.format("%.6f", getLatitude()) + ")\n";
        ret += "\n\n";

        return ret;
    }

    public String toString() {
        return type + "," + getLongitude() + "," + getLatitude();
    }
}
