package jacob.mainscreen.model;
/**
 * The InHouse class extends the Part class. The InHouse class is used to create the InHouse Part object, and is an extension of the Part class.
 */
public class InHouse extends Part {

/** @param machineId the machine ID is assigned to the InHouse Part Object when created. */
    private int machineId;

/** The InHouse constructor must have an id, name, price, stock, min, max, and machineId. */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    //methods

    /**
     * @return the machine ID.
     */
    public int getMachineId() {
        return machineId;
    }

    /**
     * @param machineId the machineId to set.
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

}
