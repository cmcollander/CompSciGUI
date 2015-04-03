/*
 Chris Collander
 Abdul Rafey Khan
 Clint Wetzel

 CSE 1325-002
 Semester Project
 */
package main;

/**
 * OilTanker is a type of CargoShip
 */
public class OilTanker extends CargoShip {

    /**
     * Default Constructor
     */
    public OilTanker() {
        super();
        this.cargo = new Oil();
    }

    /**
     * String Constructor
     *
     * @param line
     */
    public OilTanker(String line) {
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

        Oil newOil = null;

        if (parts.length > 9) {
            newOil = new Oil();
            newOil.setDescription(parts[9].trim());
            newOil.setBarrels(Integer.parseInt(parts[10].trim()));
        }
        this.cargo = newOil;
    }

    /**
     * Convert data to csv based string
     *
     * @return string of OilTanker
     */
    @Override
    public String toString() {
        String str = new String();
        str += "Tanker,";
        str += this.name + ",";
        str += this.countryOfRegistration + ",";
        str += Long.toString(this.transponderNumber) + ",";
        str += Double.toString(this.cargoCapacity) + ",";
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
     * Display the OilTanker in a console format
     */
    @Override
    public String display() {
        String ret = "";
        ret += "Tanker: " + this.name + "\n";
        ret += "Country of Origin: " + this.countryOfRegistration + "\n";
        ret += "Transponder: " + this.transponderNumber + "\n";
        ret += "Length: " + this.length + " metres\n";
        ret += "Beam: " + this.beam + " metres\n";
        ret += "Draft: " + this.draft + " metres\n";
        ret += "Location: (" + String.format("%.6f", this.getLongitude()) + "," + String.format("%.6f", this.getLatitude()) + ")\n";
        if (this.cargo != null) {
            ret += "Cargo: ";
            ret += cargo.display();
        }
        ret += "\n\n";
        return ret;

    }
}
