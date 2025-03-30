package jacob.mainscreen;

import jacob.mainscreen.model.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

import static jacob.mainscreen.model.Inventory.*;
import static java.util.Collections.emptyList;

/**
 * This class creates an application that displays an inventory system of Parts and Products displayed in two separate tables.
 */



public class HelloApplication extends Application {

    /**
     * This Method initializes our first screen titled MainMenu.fxml. It is responsible for loading the main menu screen and setting the size of the application window.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
        stage.setTitle("MainMenu");
        stage.setScene(scene);
        stage.show();
    }
/** This is the main method. This is the first method that is called when running the application. */

    /**
     * The JavaDocs file is located right off the MainScreen file in the folder titled "javadocs"
     * <p>
     * FUTURE_ENHANCEMENT: Currently the search method is case-sensitive, for example if a user was to search Wheel with a 'w' like wheel it would not find the item. I would like to add further functionality to the partial search where both upper and lower case letters can be used to search.
     * <p>
     *  RUNTIME-ERROR When coding this project I ran into a runtime error attempting to initialize the data in the display associated parts table.
     *      *              * Every time I would try to exit to the main menu and look from the modify product screen the data would duplicate.
     *      *              * This led to multiple identical items in the display associated parts table for each time the table was being initialized.
     *      *              * After attempting to fix this issue my program was refusing to compile citing that I was calling a non static Object from a static context.
     *      *              * I attempted to change the product class to static but realized that I cannot because in doing so how would each product have its own individual list of associated parts?
     *      *              * After doing some reading I realized that I need to clear the data in the associated parts table directly from the initialize method, from there I needed to create an instance of a Product to use, hence the newProduct.
     *      *              * This ensured that the data was being input correctly rather than duplicating or the Program simply not compiling at all.
     * @param args
     */
    public static void main(String[] args) {

        Outsourced part1 = new Outsourced(1, "Wheel", 30.00, 5, 1, 10, "BestWheelCompany");

        Outsourced part2 = new Outsourced(2, "Brake Pad", 10.00, 10, 1, 10, "BestWheelCompany");

        InHouse part3 = new InHouse(3, "Horn", 20.00, 5, 1, 10, 22);

        InHouse part4 = new InHouse(4, "Bell", 10.00, 5, 1, 10, 23);

        Product product1 = new Product(11,"Tricycle", 50.00, 1,1,12);

        Product product2 = new Product(12, "Dirt Bike", 200.00, 6, 1, 10);

        Inventory.addProduct(product1);
        Inventory.addProduct(product2);
        Inventory.addPart(part1);
        Inventory.addPart(part2);
        Inventory.addPart(part3);
        Inventory.addPart(part4);

        launch();
    }
}