package jacob.mainscreen.model;

/** The Outsourced class extends the Part class. The Outsourced class is used to create the Outsourced Part object, and it is an extension of the Part class. */
public class Outsourced extends Part {


    /** @param companyName is assigned to the Outsourced Part object when created. */
    private String companyName;

    /** The Outsourced Part constructor must have an id, name, price, stock, min, max, and companyName. */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }


    /** @return the company name. */
    public String getCompanyName() {
        return companyName;
    }
/** @param companyName the company name to set. */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}
