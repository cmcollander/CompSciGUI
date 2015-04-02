/*
 Chris Collander
 Abdul Rafey Khan
 Clint Wetzel

 CSE 1325-002
 Semester Project
 */
package main;

public class Position {
    private int row;
    private int col;
    
    public Position(double lat, double lon) {
        setLatitude(lat);
        setLongitude(lon);
    }
    
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    public Position() {
        this.row = 0;
        this.col = 0;
    }

    /**
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * @param row the row to set
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * @return the col
     */
    public int getCol() {
        return col;
    }

    /**
     * @param col the col to set
     */
    public void setCol(int col) {
        this.col = col;
    }
    
    public double getLatitude() {
        return MapConverter.row2lat(this.row);
    }
    
    public double getLongitude() {
        return MapConverter.col2lon(this.col);
    }
    
    public void setLatitude(double lat) {
        this.row = MapConverter.lat2row(lat);
    }
    
    public void setLongitude(double lon) {
        this.col = MapConverter.lon2col(lon);
    }
}
