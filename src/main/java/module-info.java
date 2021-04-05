module mathtrain {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.poi;
    requires java.prefs;
    requires java.logging;

    opens ru.zeroapps.mathtrain to javafx.fxml;
    opens ru.zeroapps.mathtrain.controllers to javafx.fxml;
    opens ru.zeroapps.mathtrain.dialogs to javafx.fxml;
    exports ru.zeroapps.mathtrain;
}