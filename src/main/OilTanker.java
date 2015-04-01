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
        this.shipSymbol = 'T';
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
        this.longitude = Double.parseDouble(parts[7].trim());
        this.latitude = Double.parseDouble(parts[8].trim());

        Oil newOil = null;

        if (parts.length > 9) {
            newOil = new Oil();
            newOil.setDescription(parts[9].trim());
            newOil.setBarrels(Integer.parseInt(parts[10].trim()));
        }
        this.cargo = newOil;

        this.shipSymbol = 'T';
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
        str += Double.toString(this.longitude) + ",";
        str += Double.toString(this.latitude);
        if (this.cargo != null) {
            str += "," + this.cargo.toString();
        }
        return str;
    }

    /**
     * Display the OilTanker in a console format
     */
    @Override
    public void display() {
        System.out.println("Tanker: " + this.name);
        System.out.println("Country of Origin: " + this.countryOfRegistration);
        System.out.println("Transponder: " + this.transponderNumber);
        System.out.println("Length: " + this.length + " metres");
        System.out.println("Beam: " + this.beam + " metres");
        System.out.println("Draft: " + this.draft + " metres");
        System.out.println("Location: (" + this.longitude + "," + this.latitude + ")");
        if (this.cargo != null) {
            System.out.print("Cargo: ");
            cargo.display();
        }
        System.out.println();

    }
}
