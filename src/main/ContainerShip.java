/*
    Chris Collander
    1001101078
    Homework #3
    03/23/2015
*/

package main;

/**
 * ContainerShip is a type of CargoShip
 * @author Chris
 */
public class ContainerShip extends CargoShip{
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
            this.longitude = Double.parseDouble(parts[7].trim());
            this.latitude = Double.parseDouble(parts[8].trim());
            
            Box newBox = new Box();
            
            if(parts.length > 9) {
                newBox.setDescription(parts[9].trim());
                newBox.setTeus(Integer.parseInt(parts[10].trim()));
            }
            this.cargo = newBox;
            
            this.shipSymbol = 'B';
    }
    
    /**
     * Convert object to a csv based string
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
        str += Double.toString(this.longitude) + ",";
        str += Double.toString(this.latitude);
        if(this.cargo != null)
            str += "," + this.cargo.toString();
        return str;
    }
    
    /**
     * Display the ContainerShip in a console based format
     */
    @Override
    public void display() {
        System.out.println("Container Ship: "+this.name);
        System.out.println("Country of Origin: "+this.countryOfRegistration);
        System.out.println("Transponder: "+this.transponderNumber);
        System.out.println("Length: "+this.length+" metres");
        System.out.println("Beam: "+this.beam+" metres");
        System.out.println("Draft: "+this.draft+" metres");
        System.out.println("Capacity: "+this.cargoCapacity+" tons");
        System.out.println("Location: ("+this.longitude+","+this.latitude+")");
        if(this.cargo != null) {
            System.out.print("Cargo: ");
            ((Box)this.cargo).display();
        }
        System.out.println();
        
    }
}
