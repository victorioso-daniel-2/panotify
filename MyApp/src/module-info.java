module MyApp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    
    // Open packages to JavaFX
    opens com.panotify.controller to javafx.fxml;
    opens com.panotify to javafx.graphics;
    opens com.panotify.model to javafx.base;
    
    // Export packages that need to be accessible
    exports com.panotify;
    exports com.panotify.model;
    exports com.panotify.dao;
}