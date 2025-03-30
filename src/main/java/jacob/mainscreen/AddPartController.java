package jacob.mainscreen;

import jacob.mainscreen.model.InHouse;
import jacob.mainscreen.model.Outsourced;
import jacob.mainscreen.model.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

import static jacob.mainscreen.model.Inventory.addPart;
import static jacob.mainscreen.model.Inventory.generateUniquePartId;

/** The AddPartController class is the controller class for the add Part screen. This method is responsible for adding new parts to the Parts table. */
public class AddPartController {
    public ToggleGroup addPartTGroup;
    public TextField PartID;
    public TextField PartName;
    public TextField PartInventory;
    public TextField PartPrice;
    public TextField PartMin;
    public TextField PartMax;
    public Label changingLabel;
    public TextField changingTextField;
    public RadioButton inHouseRadioButton;
    public RadioButton outsourcedRadioButton;


    /** The OnCancelAddPartButtonPress method is responsible for cancelling the addition of the new Part and returning the user to the main menu screen. */
    public void OnCancelAddPartButtonPress(ActionEvent actionEvent) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,  1200,700);
        stage.setTitle("MainMenu");
        stage.setScene(scene);
        stage.show();
    }

    /** The OnInHouseRadioButtonPress is used to switch the changing labels text to Machine ID. */
    public void OnInHouseRadioButtonPress(ActionEvent actionEvent) {

        changingLabel.setText("Machine ID");
    }

    /** The OnOutsourcedRadioButtonPress is used to switch the changing labels text to Company Name. */
    public void OnOutsourcedRadioButtonPress(ActionEvent actionEvent) {

        changingLabel.setText("Company Name");
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
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * This method is used to save a new part to the display parts table. This method is responsible for creating new Part objects and validating their data inputs.
     */
    public void OnSaveNewPart(ActionEvent actionEvent) throws IOException {

        int id = generateUniquePartId();
        String name = PartName.getText();

        int inventory;
        try {
            inventory = Integer.parseInt(PartInventory.getText());
        } catch (NumberFormatException e){
            showErrorDialog("Inventory must be a valid number!");
            return;
        }


        double price;
        try{
            price = Double.parseDouble(PartPrice.getText());
        } catch (NumberFormatException e){
            showErrorDialog("Price must be a valid number!");
            return;
        }

        int min;
        try {
            min = Integer.parseInt(PartMin.getText());
        } catch (NumberFormatException e){
            showErrorDialog("Min must be a valid number!");
            return;
        }

        int max;
        try{
            max = Integer.parseInt(PartMax.getText());
        } catch (NumberFormatException e){
            showErrorDialog("Max must be a valid number!");
            return;
        }

        if (min > max || inventory > max || inventory < min){
            showErrorDialog("Please ensure that the Max, Min, and Inventory fields are correctly input!");
            return;
        }

        Part part;

        if(inHouseRadioButton.isSelected()){
            changingTextField.setId("machineID");
            int machineId;
            try{
                machineId = Integer.parseInt(changingTextField.getText());
            } catch (NumberFormatException e){
                showErrorDialog("Machine ID must be a valid number!");
                return;
            }

            part = new InHouse(id, name, price, inventory, min, max, machineId);

        } else if(outsourcedRadioButton.isSelected()) {
            changingTextField.setId("companyName");
            String companyName = changingTextField.getText();

            part = new Outsourced(id, name, price, inventory, min, max, companyName);

        } else {
            return;
        }

        boolean userConfirmed = showConfirmationDialog("Are you sure you would like to save this new Part?");
        if (!userConfirmed) {
            return;
        }

        addPart(part);

        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,  1200,700);
        stage.setTitle("MainMenu");
        stage.setScene(scene);
        stage.show();

    }
}
