module jacob.mainscreen {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;


    opens jacob.mainscreen to javafx.fxml;
    exports jacob.mainscreen;

    exports jacob.mainscreen.model;
    opens jacob.mainscreen.model to javafx.base;

}
