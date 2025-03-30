package jacob.mainscreen.model;

/**
 * Supplied class Part.java
 */

/**
 *
 * @author Jacob Ramey
 */

/** This is an abstract class containing the Part parameters. This class is extended by the In House and Outsourced classes. */
public abstract class Part {
    /** @param id the ID of the Part. */
    private int id;
    /** @param name the name of the Part. */
    private String name;
    /** @param price the price of the Part. */
    private double price;
    /** @param stock the current inventory level of the Part. */
    private int stock;
    /** @param min the minimum inventory level of the Part. */
    private int min;
    /** @param max the maximum inventory level of the Part. */
    private int max;
    /** This is the constructor for the Part Objects. The Part object must have an id, name, price, stock, min, and max value. */
    public Part(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

}
