package jacob.mainscreen;

import jacob.mainscreen.model.Inventory;
import jacob.mainscreen.model.Part;
import jacob.mainscreen.model.Product;
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


/** The AddProductController class is the controller class for the add Product screen. This method is responsible for adding new Products and their associated parts to the Product table. */
public class AddProductController implements Initializable {
    public TableView <Part> displayPartsTable;
    public TableColumn partIdColumn;
    public TableColumn partNameColumn;
    public TableColumn partInventoryLevelColumn;
    public TableColumn partPriceColumn;
    public TextField SearchPartTextField;
    public TableView <Part> displayAssociatedPartsTable;
    public TableColumn associatedPartIdColumn;
    public TableColumn associatedPartNameColumn;
    public TableColumn associatedPartInventoryColumn;
    public TableColumn associatedPartPriceColumn;
    public TextField ProductId;
    public TextField ProductName;
    public TextField ProductInventory;
    public TextField ProductPrice;
    public TextField ProductMin;
    public TextField ProductMax;

    private Product newProduct;



    /** The OnCancelAddProductButtonPress method cancels the addition of a new product and returns the user to the main menu screen. */
    public void OnCancelAddProductButtonPress(ActionEvent actionEvent) throws
            IOException {

        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1200, 700);
        stage.setTitle("MainMenu");
        stage.setScene(scene);
        stage.show();
    }

    /** The searchPartByID method is used to search the display parts table for the part by its ID.
     *
     * @param partIdNumber the ID number of the part that is being searched.
     */
    private Part searchPartByID(int partIdNumber) {

        ObservableList<Part> AllParts = Inventory.getAllParts();

        for (Part partBeingSearched : AllParts) {
            if (partBeingSearched.getId() == partIdNumber) {
                return partBeingSearched;
            }
        }
        return null;
    }

    /** The searchByPartName method is used to search the display parts table for the part using even a partial match to the part name.
     *
     * @param partialPartName the partial or complete string provided by the user for searching the part names.
     */
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


    /** The initialize method is used to initialize the Parts table on the Add Product Screen. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializePartsTable();
    }


    /** The OnClickSearchPartButton is used to parse the input in the text field and use it to search the list of Parts.
     * It uses both the search by name and search by id in tandem to ensure the correct Part is located.
     */
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

    /** The OnClickAddAssociatedPartButton method is used to add an associated part to the new product object being created by adding it to the associated parts list. */
    public void OnClickAddAssociatedPartButton(ActionEvent actionEvent) {
        Part selectedPart = displayPartsTable.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            if (newProduct == null) {
                int id = generateUniqueProductId();
                String name = ProductName.getText();
                int inventory = Integer.parseInt(ProductInventory.getText());
                double price = Double.parseDouble(ProductPrice.getText());
                int min = Integer.parseInt(ProductMin.getText());
                int max = Integer.parseInt(ProductMax.getText());

                newProduct = new Product(id, name, price, inventory, min, max);
            }

            newProduct.addAssociatedPart(selectedPart);

            displayAssociatedPartsTable.setItems(newProduct.getAllAssociatedParts());
        }
    }


    /** The OnSaveNewProductButtonPress method is used to save a new product to the display products table.
     * This method is responsible for creating new Product objects and validating their data inputs.
     */
    public void OnSaveNewProductButtonPress(ActionEvent actionEvent) throws IOException {
        int id = generateUniqueProductId();
        String name = ProductName.getText();

        int inventory;
        try {
            inventory = Integer.parseInt(ProductInventory.getText());
        } catch (NumberFormatException e) {
            showErrorDialog("Invalid inventory value. Please enter a valid integer.");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(ProductPrice.getText());
        } catch (NumberFormatException e) {
            showErrorDialog("Invalid price value. Please enter a valid number.");
            return;
        }

        int min;
        try {
            min = Integer.parseInt(ProductMin.getText());
        } catch (NumberFormatException e) {
            showErrorDialog("Invalid min value. Please enter a valid integer.");
            return;
        }

        int max;
        try {
            max = Integer.parseInt(ProductMax.getText());
        } catch (NumberFormatException e) {
            showErrorDialog("Invalid max value. Please enter a valid integer.");
            return;
        }

        if (testForValidStringInput(name)) {
            showErrorDialog("Invalid name input. Name cannot be a number!");
            return;
        }

        if (min > max || inventory > max || inventory < min){
            showErrorDialog("Please ensure that the Max, Min, and Inventory fields are correctly input!");
            return;
        }

        newProduct = new Product(id, name, price, inventory, min, max);

        ObservableList<Part> associatedParts = displayAssociatedPartsTable.getItems();
        for (Part part : associatedParts) {
            newProduct.addAssociatedPart(part);
        }

        boolean userConfirmed = showConfirmationDialog("Are you sure you would like to save this Product?");
        if (!userConfirmed) {
            return;
        }
        addProduct(newProduct);

        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,  1200,700);
        stage.setTitle("MainMenu");
        stage.setScene(scene);
        stage.show();

    }

    /** The initializePartsTable method is used to initialize the data from the observable lists and populate the tables with it. */
    private void initializePartsTable() {
        displayPartsTable.setItems(getAllParts());

        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        // If newProduct is not null, set its associated parts
        if (newProduct != null) {
            displayAssociatedPartsTable.setItems(newProduct.getAllAssociatedParts());

        } else {
            // If newProduct is null, clear the associated parts table
            displayAssociatedPartsTable.setItems(FXCollections.observableArrayList());
        }

        associatedPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /** The OnRemoveAssociatedPartButtonPress method is used to remove an associated part from a Product object.
     * The method takes in the users input as a selection and deletes the part from the associated parts observable list.
     */
    public void OnRemoveAssociatedPartButtonPress(ActionEvent actionEvent) {
        Part selectedPart = displayAssociatedPartsTable.getSelectionModel().getSelectedItem();
        boolean userConfirmed = showConfirmationDialog("Are you sure you would like to delete this Associated Part?");
        if (!userConfirmed) {
            return;
        }
        if (selectedPart != null) {
            newProduct.deleteAssociatedPart(selectedPart);
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
        alert.setHeaderText("Invalid Input Option");
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
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /** The testForValidStringInput method is used to ensure that Product names are correctly input.
     * The method checks for both integer and double values and if it detects a number value it triggers the show error dialog.
     *
     * @param value the input value being tested.
     */
    private boolean testForValidStringInput(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e1) {
            try {
                Double.parseDouble(value);
                return true;
            } catch (NumberFormatException e2) {
                return false;
            }
        }
    }
}

