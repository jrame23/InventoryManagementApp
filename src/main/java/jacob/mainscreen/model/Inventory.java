package jacob.mainscreen.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** The Inventory class manages the collection of parts and products in the application. */
public class Inventory {

    /** The observable list of all parts in the application. */
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();


    /** The observable list of all products in the application. */
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Deletes a part from the display Parts table.
     *
     * @param selectedPart The part to be deleted.
     */
    public static void deletePart(Part selectedPart){
        allParts.remove(selectedPart);
    }

    /**
     * Deletes a product from the display Products table.
     *
     * @param selectedProduct The part to be deleted.
     */
    public static void deleteProduct(Product selectedProduct){
        allProducts.remove(selectedProduct);
    }

    /**
     * Generates a unique ID for a new part.
     *
     * @return A unique ID for a new part.
     */
    public static int generateUniquePartId(){
        return getAllParts().size() +1;
    }

    /**
     * Generates a unique ID for a new product.
     *
     * @return A unique ID for a new product.
     */
    public static int generateUniqueProductId(){
        return getAllProducts().size() +1;
    }


    /**
     * Adds a new product to the inventory.
     *
     * @param newProduct The product to be added.
     */
    public static void addProduct(Product newProduct){

        newProduct.setId(generateUniqueProductId());
        allProducts.add(newProduct);
    }


    /**
     * Adds a new part to the inventory.
     *
     * @param newPart The product to be added.
     */
    public static void addPart(Part newPart){

        newPart.setId(generateUniquePartId());
        allParts.add(newPart);
    }
    /**
     * Updates a part in the observable list of all Parts.
     *
     * @param updatedPart The updated part.
     */
    public static void updatePart(Part updatedPart) {

        ObservableList<Part> allParts = getAllParts();
        int index = -1;

        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).getId() == updatedPart.getId()) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            allParts.set(index, updatedPart);
        }
    }

    /**
     * Updates a product in the observable list of all Products.
     *
     * @param updatedProduct The updated product.
     */
    public static void updateProduct(Product updatedProduct) {

        ObservableList<Product> allProducts = getAllProducts();
        int index = -1;

        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getId() == updatedProduct.getId()) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            allProducts.set(index, updatedProduct);
        }
    }

    /**
     * Gets a list of all parts in inventory.
     *
     * @return An ObservableList of all parts.
     */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }

    /**
     * Gets a list of all products in inventory.
     *
     * @return An ObservableList of all products.
     */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }


}
