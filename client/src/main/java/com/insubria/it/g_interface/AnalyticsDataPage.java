package com.insubria.it.g_interface;


import com.insubria.it.g_components.Button;
import com.insubria.it.g_components.GridFrame;
import com.insubria.it.g_components.Label;
import com.insubria.it.helpers.FrameHandler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The AnalyticsDataPage class creates the AnalyticsDataPage frame to allow the user
 * to show a specific information passed as argument to the constructor
 */
public class AnalyticsDataPage {
    /**
     * Static text that will be used with some UI components to communicate with the user
     */
    private static final String TITLE_WINDOW = "Il Paroliere - Analitycs";
    private static final String BUTTON_BACK_HOME_TEXT = "Torna alla Home";
    private static final String BUTTON_BACK_ANALYTICS_TEXT = "Torna ad analytics";
    /**
     * Columns for the grid containers
     */
    private static final int COLS_MAIN_CONTAINER = 1;
    private static final int COLS_BUTTONS = 2;
    /**
     * Rows for the grid container (0 stands for: unlimited number of rows)
     */
    private static final int ROWS = 0;

    /**
     * Informative title
     */
    private String mainTitle = "Analytics - ";
    /**
     * backToHome - Redirect the user to the Home section
     * backToAnalytics - close the current frame (redirect the user to the Analytics section)
     */
    private Button backToHome, backToAnalytics;
    /**
     * titlePage - Label containing the informative title MAIN_TITLE
     * data - Label containing the data to be shown
     */
    private Label titlePage, data;
    /**
     * Grid containers to handle UI elements visualization
     */
    private GridFrame gridButtonFrame, gridContainer, parentGridAnalytics;

    /**
     * Constructor of the class (creates the frame and its visual components)
     *
     * @param titleSection title which defines the type of data currently shown
     * @param dataToShow data to show (which was retrieved from the server)
     */
    public AnalyticsDataPage(String titleSection, String dataToShow) {
        // The mainTitle needs to be placed inside the constructor to update its value after the user's profile update
        mainTitle += titleSection;

        gridButtonFrame = new GridFrame(ROWS, COLS_BUTTONS);

        gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS_MAIN_CONTAINER);

        titlePage = new Label(mainTitle);
        data = new Label(dataToShow);

        backToHome = new Button(BUTTON_BACK_HOME_TEXT);
        backToAnalytics = new Button(BUTTON_BACK_ANALYTICS_TEXT);

        addAllEventListeners();

        gridContainer.addToView(titlePage);
        gridContainer.addToView(data);

        gridButtonFrame.addToView(backToHome);
        gridButtonFrame.addToView(backToAnalytics);

        gridContainer.addToView(gridButtonFrame);

        FrameHandler.showSecondaryGridContainer(gridContainer);
    }

    /**
     * This method defines and attaches all ActionListeners to the appropriate UI elements
     */
    private void addAllEventListeners() {
        backToHome.attachActionListenerToButton(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                redirectToHomeFrame();
            }
        });
        backToAnalytics.attachActionListenerToButton(new ActionListener() {
            public void actionPerformed(ActionEvent me) {
                redirectToAnalyticsFrame();
            }
        });
    }

    /**
     * This method displays on screen the Home section
     */
    private void redirectToHomeFrame() {
        Home home = new Home();
    }

    /**
     * Redirect the user to the Analytics section by removing the current frame of AnalyticsDataPage
     */
    private void redirectToAnalyticsFrame() {
        FrameHandler.disposeSecondaryGridContainer();
    }
}
