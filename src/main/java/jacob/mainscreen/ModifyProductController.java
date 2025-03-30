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



import static jacob.mainscreen.model.Inventory.getAllParts;
import static jacob.mainscreen.model.Inventory.updateProduct;

/** The ModifyProductController class is the controller class for the modify Product screen. This method is responsible for modifying existing Products. */
public class ModifyProductController implements Initializable {

    public Button CancelModifyProductButton;
    public TextField ProductId;
    public TextField ProductName;
    public TextField ProductInventory;
    public TextField ProductPrice;
    public TextField ProductMin;
    public TextField ProductMax;
    public TableView<Part> displayAssociatedPartsTable;
    public TableView<Part> displayPartsTable;
    public TextField SearchPartTextField;
    public TableColumn partIdColumn;
    public TableColumn partNameColumn;
    public TableColumn partInventoryLevelColumn;
    public TableColumn partPriceColumn;
    public TableColumn associatedPartIdColumn;
    public TableColumn associatedPartNameColumn;
    public TableColumn associatedPartInventoryColumn;
    public TableColumn associatedPartPriceColumn;
    private Product product;
    public Product selectedProduct;

    /** The ModifyProductController is used to pass the selected product from the main menu screen so that the data can be pre-populated in the text fields. */
    public ModifyProductController(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }



    /** The initialize method called here is responsible for calling the initializePartsTable method below. */
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        initializePartsTable();

    }

    /** The initializePartsTable method is used to initialize the data from the observable lists and populate the tables with it. */
    private void initializePartsTable() {
        displayPartsTable.setItems(getAllParts());

        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        displayAssociatedPartsTable.setItems(selectedProduct.getAllAssociatedParts());
        associatedPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /** The setProduct method is directly responsible for passing the data from the main menu screen controller to the modify Product screen. It pre-populates the data into the text fields for editing by the user.
     *
     * @param selectedProduct the product selected on the main menu screen being passed to the modify product controller.
     */
    public void setProduct(Product selectedProduct) {

        this.product = selectedProduct;

        ProductName.setText(product.getName());
        ProductInventory.setText(String.valueOf(product.getStock()));
        ProductPrice.setText(String.valueOf(product.getPrice()));
        ProductMin.setText(String.valueOf(product.getMin()));
        ProductMax.setText(String.valueOf(product.getMax()));
        displayAssociatedPartsTable.setItems(selectedProduct.getAllAssociatedParts());

        initializePartsTable();


    }

    /** The OnCancelModifyProductButtonPress method cancels the addition of an updated product and returns the user to the main menu screen. */
    public void OnCancelModifyProductButtonPress(ActionEvent actionEvent) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1200, 700);
        stage.setTitle("MainMenu");
        stage.setScene(scene);
        stage.show();
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

    /** The searchByPartName method is used to search the display parts table for the part using even a partial match to the part name.
     *
     * @param partialPartName the partial or complete string provided by the user for searching the part names.
     */
    private ObservableList<Part> searchByPartName(String partialPartName) {
        ObservableList<Part> namedParts = FXCollections.observableArrayList();

        ObservableList<Part> AllParts = getAllParts();

        for (Part partBeingSearched : AllParts) {
            if (partBeingSearched.getName().contains(partialPartName)) {
                namedParts.add(partBeingSearched);
            }
        }

        return namedParts;
    }

    /** The searchPartByID method is used to search the display parts table for the part by its ID.
     *
     * @param partIdNumber the ID number of the part that is being searched.
     */
    private Part searchPartByID(int partIdNumber) {
        ObservableList<Part> AllParts = getAllParts();

        for (Part partBeingSearched : AllParts) {
            if (partBeingSearched.getId() == partIdNumber) {
                return partBeingSearched;
            }
        }
        return null;

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

    /** The testForValidStringInput method is used to ensure that Product names are correctly input.
     * The method checks for both integer and double values and if it detects a number value it triggers the show error dialog.
     *
     * @param value the input value being tested.
     */
    private boolean testForValidStringInput(String value) {
        try {
            // Try parsing the value as an integer first
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e1) {
            try {
                // If parsing as an integer fails, try parsing as a double
                Double.parseDouble(value);
                return true;
            } catch (NumberFormatException e2) {
                return false;
            }
        }
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


    /** The OnSaveModifiedProductButtonPress method is used to save a modified product to the display products table.
     * This method is responsible for updating existing Product objects and validating their data inputs.
     */
    public void OnSaveModifiedProductButtonPress(ActionEvent actionEvent) throws IOException {
        if (selectedProduct != null) {

            String name = ProductName.getText();
            if (testForValidStringInput(name)) {
                showErrorDialog("Invalid name input. Name cannot be a number!");
                return;
            }
            selectedProduct.setName(name);

            int stock;
            try{
                stock = Integer.parseInt(ProductInventory.getText());
            } catch (NumberFormatException e) {
                showErrorDialog("Inventory must be a valid number!");
                return;
            }
            selectedProduct.setStock(stock);

            double price;
            try{
                price = Double.parseDouble(ProductPrice.getText());
            } catch (NumberFormatException e) {
                showErrorDialog("Price must be a valid number!");
                return;
            }
            selectedProduct.setPrice(price);

            int min;
            try{
                min = Integer.parseInt(ProductMin.getText());
            } catch (NumberFormatException e) {
                showErrorDialog("Min must be a valid number!");
                return;
            }
            selectedProduct.setMin(min);

            int max;
            try{
                max = Integer.parseInt(ProductMax.getText());
            } catch (NumberFormatException e) {
                showErrorDialog("Max must be a valid number!");
                return;
            }
            selectedProduct.setMax(max);


            ObservableList<Part> associatedPartsListCopy = FXCollections.observableArrayList(displayAssociatedPartsTable.getItems());

            selectedProduct.getAllAssociatedParts().clear();

            selectedProduct.getAllAssociatedParts().addAll(associatedPartsListCopy);


            if (min > max || stock > max || stock < min){
                showErrorDialog("Please ensure that the Max, Min, and Inventory fields are correctly input!");
                return;
            }

            boolean userConfirmed = showConfirmationDialog("Are you sure you would like to save these changes?");
            if (!userConfirmed) {
                return;
            }

            updateProduct(selectedProduct);

            Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1200, 700);
            stage.setTitle("MainMenu");
            stage.setScene(scene);
            stage.show();
        }
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
            selectedProduct.deleteAssociatedPart(selectedPart);
        }
    }

    /** The OnClickAddAssociatedPartButton method is used to add an associated part to the new product object being created by adding it to the associated parts list. */
    public void OnAddAssociatedPartButtonPress(ActionEvent actionEvent) {
        Part selectedPart = displayPartsTable.getSelectionModel().getSelectedItem();

            // Add the selected part to the associated parts of the newProduct
            selectedProduct.addAssociatedPart(selectedPart);
            // Update the associated parts table
            displayAssociatedPartsTable.setItems(selectedProduct.getAllAssociatedParts());
        }
    }

