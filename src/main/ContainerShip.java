/*
 Chris Collander
 Abdul Rafey Khan
 Clint Wetzel

 CSE 1325-002
 Semester Project
 */
package main;

/**
 * ContainerShip is a type of CargoShip
 */
public class ContainerShip extends CargoShip {

    /**
     * Default Constructor
     */
    public ContainerShip() {
        super();
        this.cargo = new Box();
        this.shipSymbol = 'B';
    }

    /**
     * csv String based constructor
     *
     * @param line
     */
    public ContainerShip(String line) {
        String[] parts = line.split(",");
        this.name = parts[0].trim();
        this.countryOfRegistration = parts[1].trim();
        this.transponderNumber = Long.parseLong(parts[2].trim());
        this.cargoCapacity = Double.parseDouble(parts[3].trim());
        this.length = Double.parseDouble(parts[4].trim());
        this.beam = Double.parseDouble(parts[5].trim());
        this.draft = Double.parseDouble(parts[6].trim());
        double longitude = Double.parseDouble(parts[7].trim());
        double latitude = Double.parseDouble(parts[8].trim());
        
        position = new Position(latitude, longitude);
        
        Box newBox = new Box();

        if (parts.length > 9) {
            newBox.setDescription(parts[9].trim());
            newBox.setTeus(Integer.parseInt(parts[10].trim()));
        }
        this.cargo = newBox;

        this.shipSymbol = 'B';
    }

    /**
     * Convert object to a csv based string
     *
     * @return csv based string
     */
    @Override
    public String toString() {
        String str = new String();
        str += "Container Ship,";
        str += this.name + ",";
        str += this.countryOfRegistration + ",";
        str += Long.toString(this.transponderNumber) + ",";
        str += Double.toString(this.length) + ",";
        str += Double.toString(this.beam) + ",";
        str += Double.toString(this.draft) + ",";
        str += Double.toString(this.getLongitude()) + ",";
        str += Double.toString(this.getLatitude());
        if (this.cargo != null) {
            str += "," + this.cargo.toString();
        }
        return str;
    }

    /**
     * Display the ContainerShip in a console based format
     */
    @Override
    public String display() {
        String ret = "";
        ret += "Container Ship: " + this.name + "\n";
        ret += "Country of Origin: " + this.countryOfRegistration + "\n";
        ret += "Transponder: " + this.transponderNumber + "\n";
        ret += "Length: " + this.length + " metres\n";
        ret += "Beam: " + this.beam + " metres\n";
        ret += "Draft: " + this.draft + " metres\n";
        ret += "Capacity: " + this.cargoCapacity + " tons\n";
        ret += "Location: (" + this.getLongitude() + "," + this.getLatitude() + ")\n";
        if (this.cargo != null) {
            ret += "Cargo: ";
            ret += this.cargo.display();
        }
        ret += "\n\n";
        return ret;
    }
}
