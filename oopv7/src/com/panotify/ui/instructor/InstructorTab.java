package com.panotify.ui.instructor;

import javafx.scene.layout.VBox;

/**
 * Interface for all instructor dashboard tabs
 */
public interface InstructorTab {
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