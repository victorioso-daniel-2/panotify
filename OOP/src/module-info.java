/**
 * The main module for the PaNotify application.
 * <p>
 * This module defines the dependencies and exported packages for the secure online examination platform.
 * It requires JavaFX for the user interface and java.sql for database operations.
 * </p>
 */
module MyApp {
    requires javafx.controls;
    requires java.sql;
    
    // Open packages to JavaFX
    opens com.panotify to javafx.graphics;
    opens com.panotify.model to javafx.base;
    
    // Export packages that need to be accessible
    exports com.panotify;
    exports com.panotify.model;
    exports com.panotify.service;
    exports com.panotify.util;
}