package jacob.mainscreen;

import jacob.mainscreen.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static jacob.mainscreen.model.Inventory.*;

/** The HelloController class is the controller for the main screen of the application.
 * The main screen is responsible for enabling switching to the other screens of the application, as well as editing the Part and Product Tables.*/
public class HelloController implements Initializable {

    public TableColumn<Part, Integer> PartIDColumn;
    public TableColumn<Part, String> PartNameColumn;
    public TableColumn<Part, Integer> PartInventoryLevelColumn;
    public TableColumn<Part, Double> PartPriceColumn;
    public Button AddPartButton;
    public Button ModifyPartButton;
    public Button DeletePartButton;
    public Button SearchPartButton;
    public TableColumn ProductIDColumn;
    public TableColumn ProductNameColumn;
    public TableColumn ProductInventoryLevelColumn;
    public TableColumn ProductPriceColumn;
    public Button DeleteProductButton;
    public Button ModifyProductButton;
    public Button AddProductButton;
    public Button SearchProductButton;
    public Button ExitButton;
    public TableView<Part> displayPartsTable;
    public TableView<Product> displayProductsTable;
    public TextField SearchPartTextField;
    public TextField SearchProductTextField;

    /**
     * Initializes the controller and sets all the Part and Product objects to their tables.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        displayPartsTable.setItems(getAllParts());

        PartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        PartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        PartInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        PartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));


        displayProductsTable.setItems(getAllProducts());
        ProductIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        ProductNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ProductInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        ProductPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    /** The OnClickAddPartButton method is used to switch to the add part screen when the button is clicked. */
    public void OnClickAddPartButton(ActionEvent actionEvent) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("AddPartMenu.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 700, 700);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();

    }

    /** The OnClickModifyPartButton method is used to switch to the modify part screen when the button is clicked. */
    public void OnClickModifyPartButton(ActionEvent actionEvent) throws IOException {

        // The selected Product is being passed on through to the Modify Product screen to pre-populate the data into the text fields
        Part selectedPart = displayPartsTable.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Modify Part.fxml"));
            Parent root = loader.load();
            ModifyPartController modifyPartController = loader.getController();
            modifyPartController.setPart(selectedPart);

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 700, 700);
            stage.setTitle("Modify Part");
            stage.setScene(scene);
            stage.show();
        }
    }

    /** The OnClickModifyProductButton method is used to switch to the modify product screen when the button is clicked. */
    public void OnClickModifyProductButton(ActionEvent actionEvent) throws IOException {

        Product selectedProduct = displayProductsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Modify Product.fxml"));
            // Pass the selectedProduct to the ModifyProductController constructor
            loader.setController(new ModifyProductController(selectedProduct));
            Parent root = loader.load();
            ModifyProductController modifyProductController = loader.getController();
            modifyProductController.setProduct(selectedProduct);

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1100, 700);
            stage.setTitle("Modify Product");
            stage.setScene(scene);
            stage.show();
        }
    }

    /** The OnClickAddProductButton method is used to switch to the add product screen when the button is clicked.*/
    public void OnClickAddProductButton(ActionEvent actionEvent) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("AddProductMenu.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1100, 700);
        stage.setTitle("Add Product");
        stage.setScene(scene);
        stage.show();
    }

    /** The OnClickExitProgramButton method is used to close the application when the button is clicked.*/
    public void OnClickExitProgramButton(ActionEvent actionEvent) {
        System.exit(0);
    }

    /** The OnClickSearchPartButton is used to parse the input in the text field and use it to search the list of Parts.
     * It uses both the search by name and search by id in tandem to ensure the correct Part is located. */
    public void OnClickSearchPartButton(ActionEvent actionEvent) {
        String searchPartQuery = SearchPartTextField.getText();

        ObservableList<Part> filteredParts;

        try {
            int partIdNumber = Integer.parseInt(searchPartQuery);
            Part partBeingSearched = searchPartByID(partIdNumber);

            if (partBeingSearched != null) {
                filteredParts = FXCollections.observableArrayList(partBeingSearched);
            } else {
                filteredParts = FXCollections.observableArrayList();
            }
        } catch (NumberFormatException e) {
            filteredParts = searchByPartName(searchPartQuery);
        }
        if (filteredParts.isEmpty()) {
            showErrorDialog("No matching parts found!");
        } else {
            displayPartsTable.setItems(filteredParts);
        }

        SearchPartTextField.setText("");
    }

    /** The searchPartByID method is used to search the display parts table for the part by its ID.
     *
     * @param partIdNumber the Id number of the part that is being searched.
     * */
    private Part searchPartByID(int partIdNumber) {

        ObservableList<Part> AllParts = Inventory.getAllParts();

        for (Part partBeingSearched : AllParts) {
            if (partBeingSearched.getId() == partIdNumber) {
                return partBeingSearched;
            }
        }
        return null;
    }

    /**
     * The searchByPartName method is used to search the display parts table for the part using even a partial match to the part name.
     *
     *  @param partialPartName the partial or complete string provided by the user for searching the part names.
     *  */
    private ObservableList<Part> searchByPartName(String partialPartName) {
        ObservableList<Part> namedParts = FXCollections.observableArrayList();

        ObservableList<Part> AllParts = Inventory.getAllParts();

        for (Part partBeingSearched : AllParts) {
            if (partBeingSearched.getName().contains(partialPartName)) {
                namedParts.add(partBeingSearched);
            }
        }

        return namedParts;
    }

    /** The OnClickSearchProductButton is used to parse the input in the text field and use it to search the list of Products.
     * It uses both the search by name and search by id in tandem to ensure the correct Product is located.
     */
    public void OnClickSearchProductButton(ActionEvent actionEvent) {
        String searchProductQuery = SearchProductTextField.getText();

        ObservableList<Product> filteredProducts;

        try {
            int productIdNumber = Integer.parseInt(searchProductQuery);
            Product productBeingSearched = searchProductByID(productIdNumber);

            if (productBeingSearched != null) {
                filteredProducts = FXCollections.observableArrayList(productBeingSearched);
            } else {
                filteredProducts = FXCollections.observableArrayList();
            }
        } catch (NumberFormatException e) {
            filteredProducts = searchByProductName(searchProductQuery);
        }
        if (filteredProducts.isEmpty()) {
            showErrorDialog("No matching products found!");
        } else {
            displayProductsTable.setItems(filteredProducts);
        }

        SearchProductTextField.setText("");
    }


    /** The searchByProductName method is used to search the display products table for the product using even a partial match to the product name.
     *
     * @param partialProductName the partial or complete string provided by the user for searching the product names.
     */
    private ObservableList<Product> searchByProductName(String partialProductName) {
        ObservableList<Product> namedProducts = FXCollections.observableArrayList();

        ObservableList<Product> AllProducts = Inventory.getAllProducts();

        for (Product productBeingSearched : AllProducts) {
            if (productBeingSearched.getName().contains(partialProductName)) {
                namedProducts.add(productBeingSearched);
            }
        }

        return namedProducts;
    }

    /** The searchProductByID method is used to search the display products table for the product by its Id.
     *
     * @param productIdNumber the Id number of the part that is being searched.
     */
    private Product searchProductByID(int productIdNumber) {

        ObservableList<Product> AllProducts = Inventory.getAllProducts();

        for (Product productBeingSearched : AllProducts) {
            if (productBeingSearched.getId() == productIdNumber) {
                return productBeingSearched;
            }
        }
        return null;
    }

    /** Deletes a part from the displayPartsTable and inventory.
     *
     * @param actionEvent The ActionEvent associated with the delete part button click.
     */
    public void OnClickDeletePartButton(ActionEvent actionEvent) {
        Part selectedPart = displayPartsTable.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            showConfirmationDialog("Are you sure you want to delete this Part?");
            deletePart(selectedPart);
        }
    }

    /** The showErrorDialog method is used to create a pop up to alert the user of an invalid input parameter.
     * This method enables a custom method specific to each error to notify the user where the error is occurring; This method also blocks the invalid input from being added to an object in the table.
     *
     * @param message the message being displayed to the user, this message changes based on the error location.
     */
    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

/** The showConfirmationDialog method is used to ensure that the user intends to perform the given function.
 * This method acts as a stop gap and helps ensure that all input data is confirmed by the user.
 *
 * @param message the confirmation dialogue displayed to the user.
 */
    private boolean showConfirmationDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.YES;
    }

    /** The OnClickDeleteProductButton is used to delete the selected Product from the product table.
     * The deleteProduct will only run if there are no associated parts attached to the Product Object being selected and will ask the user to confirm the action before it is performed.
     */
    public void OnClickDeleteProductButton(ActionEvent actionEvent) {
        Product selectedProduct = displayProductsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null && selectedProduct.getAllAssociatedParts().isEmpty()) {
            showConfirmationDialog("Are you sure you want to delete this product?");
            deleteProduct(selectedProduct);
        } else {
            showErrorDialog("You must first remove any associated Parts prior to deleting!");
        }
    }
}
