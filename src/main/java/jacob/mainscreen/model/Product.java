package jacob.mainscreen.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** The Product class is a public class. The Product class is used to create Product objects and assign them associated Parts. */
public class Product {

/** @param associatedParts an observable list of the associated parts of the product object. */
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    /** @param id the ID of the product. */
    private int id;
    /** @param name the name of the product. */
    private String name;
    /** @param price the price of the product. */
    private double price;
    /** @param stock the stock of the product. */
    private int stock;
    /** @param min the minimum stock of the product. */
    private int min;
    /** @param max the maximum stock of the product*/
    private int max;

/** The Product constructor must have an id, name, price, stock, min, and max value. */
    public Product(int id, String name, double price, int stock, int min, int max){
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }
/** This empty Product constructor is used to create a default product object with base values in order to allow the add and modify product controllers to function correctly. */
    public Product(){
        this.id = 0;
        this.name = "";
        this.price = 0;
        this.stock = 0;
        this.min = 0;
        this.max = 0;

    }




/**
 * @return the id.
 */
    public int getId(){
        return id;
    }
 /**
  * @param id the id to set.
  */
    public void setId(int id){
        this.id = id;
    }
/**
 * @return the name.
 */
    public String getName(){
        return name;
    }
/**
 * @param name the name to set.
 */
    public void setName(String name){
        this.name = name;
    }
/**
 * @return the price.
 */
    public double getPrice() {
        return price;
    }
/**
 * @param price the price to set.
 */
    public void setPrice(double price) {
        this.price = price;
    }
/**
 * @return the stock.
 */
    public int getStock() {
        return stock;
    }
/**
 * @param stock the stock to set.
 */
    public void setStock(int stock) {
        this.stock = stock;
    }
/**
 * @return the min.
 */
    public int getMin() {
        return min;
    }
/**
 * @param min the min to set.
 */
    public void setMin(int min) {
        this.min = min;
    }
/**
 * @return the max.
 */
    public int getMax() {
        return max;
    }
/**
 * @param max the max to set.
 */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * @return The getAllAssociatedParts method returns the list of associated parts.
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
/**
 * The deleteAssociatedPart method is used to remove a selected part from the associated parts table and the Product object instance.
 *
 * @param selectedPart the associated part being selected by the user for deletion.
 */
    public void deleteAssociatedPart(Part selectedPart){
        associatedParts.remove(selectedPart);
    }
/**
 * The addAssociatedPart method is used to add a selected part from the display parts table to the associated parts table and the Product object instance.
 *
 * @param selectedPart the part being chosen by the user to be added to the associated parts table and Product object.
 */
    public void addAssociatedPart(Part selectedPart) {
        associatedParts.add(selectedPart);
    }


}









