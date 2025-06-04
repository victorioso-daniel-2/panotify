package com.panotify.ui.student;

import javafx.scene.layout.VBox;

/**
 * Interface for student dashboard tabs
 * Each tab must implement this interface
 */
public interface StudentTab {
    /**
     * Get the content panel for this tab
     * @return VBox containing the tab's content
     */
    VBox getContent();
    
    /**
     * Called when this tab is navigated to
     * Implementations should refresh their content as needed
     */
    void onNavigatedTo();
} 