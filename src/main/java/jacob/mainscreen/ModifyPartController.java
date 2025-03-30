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

import static jacob.mainscreen.model.Inventory.updatePart;

/** The ModifyPartController class is the controller for the modify part screen. This Method is responsible for modifying the existing parts in the table. */
public class ModifyPartController {

    public RadioButton inHouseRadioButton;
    public ToggleGroup modifyPartGroup;
    public RadioButton outsourcedRadioButton;
    public Label changingLabel;
    public TextField changingTextField;
    public Button CancelModifyPartButton;
    public TextField PartID;
    public TextField PartName;
    public TextField PartInventory;
    public TextField PartPrice;
    public TextField PartMin;
    public TextField PartMax;

    private Part part;

    /** The OnCancelModifyPartButtonPress is used to cancel the modification of a selected part and return to the main menu. */
    public void OnCancelModifyPartButtonPress(ActionEvent actionEvent) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1200, 700);
        stage.setTitle("MainMenu");
        stage.setScene(scene);
        stage.show();
    }

    /** The OnInHouseButtonPress is used to switch the changing labels text to Machine ID. */
    public void OnInHouseButtonPress(ActionEvent actionEvent) {

        changingLabel.setText("Machine ID");
    }

    /** The OnOutsourcedRadioButtonPress is used to switch the changing labels text to Company Name. */
    public void OnOutsourcedRadioButtonPress(ActionEvent actionEvent) {

        changingLabel.setText("Company Name");
    }

    /** Sets the text fields of the modify part screen to the selectedParts current values.
     * This method is responsible for populating the existing Part object variables into the text fields of the modify part screen.
     */
    public void setPart(Part selectedPart) {

        this.part = selectedPart;

        PartName.setText(part.getName());
        PartInventory.setText(String.valueOf(part.getStock()));
        PartPrice.setText(String.valueOf(part.getPrice()));
        PartMin.setText(String.valueOf(part.getMin()));
        PartMax.setText(String.valueOf(part.getMax()));

        if (part instanceof InHouse) {
            inHouseRadioButton.setSelected(true);
            changingLabel.setText("Machine ID");
            changingTextField.setText(String.valueOf(((InHouse) part).getMachineId()));
        } else if (part instanceof Outsourced) {
            outsourcedRadioButton.setSelected(true);
            changingLabel.setText("Company Name");
            changingTextField.setText(((Outsourced) part).getCompanyName());
        }
    }

    /** The showErrorDialog method is used to create a pop-up to alert the user of an invalid input parameter.
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


    /** Saves a modified part to the display parts table. This method is responsible for updating existing Part objects and validating their data inputs. */
    public void OnSaveModifiedPart(ActionEvent actionEvent) throws IOException {

        if (part != null) {
            part.setName(PartName.getText());

            int stock;
            try {
                stock = Integer.parseInt(PartInventory.getText());
            } catch (NumberFormatException e) {
                showErrorDialog("Inventory must be a valid number!");
                return;
            }
            part.setStock(stock);


            double price;
            try {
                price = Double.parseDouble(PartPrice.getText());
            } catch (NumberFormatException e) {
                showErrorDialog("Price must be a valid number!");
                return;
            }
            part.setPrice(price);


            int min;
            try {
                min = Integer.parseInt(PartMin.getText());
            } catch (NumberFormatException e) {
                showErrorDialog("Min must be a valid number!");
                return;
            }
            part.setMin(min);


            int max;
            try {
                max = Integer.parseInt(PartMax.getText());
            } catch (NumberFormatException e) {
                showErrorDialog("Max must be a valid number!");
                return;
            }
            part.setMax(max);

            if (inHouseRadioButton.isSelected()) {
                // Update the part as an InHouse part
                int machineId = Integer.parseInt(changingTextField.getText());
                if (part instanceof InHouse) {
                    ((InHouse) part).setMachineId(machineId);
                } else {
                    // If the part is changed from Outsourced to InHouse
                    part = new InHouse(part.getId(), part.getName(), part.getPrice(),
                            part.getStock(), part.getMin(), part.getMax(), machineId);
                }
            } else if (outsourcedRadioButton.isSelected()) {
                // Update the part as an Outsourced part
                String companyName = changingTextField.getText();
                if (part instanceof Outsourced) {
                    ((Outsourced) part).setCompanyName(companyName);
                } else {
                    // If the part is changed from InHouse to Outsourced
                    part = new Outsourced(part.getId(), part.getName(), part.getPrice(),
                            part.getStock(), part.getMin(), part.getMax(), companyName);
                }
            }

            if (min > max || stock > max || stock < min){
                showErrorDialog("Please ensure that the Max, Min, and Inventory fields are correctly input!");
                return;
            }
            boolean userConfirmed = showConfirmationDialog("Are you sure you would like to save these changes?");
            if (!userConfirmed) {
                return;
            }

            updatePart(part);

            Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1200, 700);
            stage.setTitle("MainMenu");
            stage.setScene(scene);
            stage.show();
        }
    }
}



