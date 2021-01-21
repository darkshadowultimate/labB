package com.insubria.it.g_interface;


import com.insubria.it.g_components.Button;
import com.insubria.it.g_components.GridFrame;
import com.insubria.it.g_components.Label;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AnalyticsDataPage {
    private static final String TITLE_WINDOW = "Il Paroliere - Analitycs";
    private static final String BUTTON_BACK_HOME_TEXT = "Torna alla Home";
    private static final String BUTTON_BACK_ANALYTICS_TEXT = "Torna ad analytics";
    private static final String BUTTON_PREVIOUS_PAGE_TEXT = "Pagina Precedente";
    private static final String BUTTON_NEXT_PAGE_TEXT = "Pagina Successiva";
    private static final int COLS_MAIN_CONTAINER = 1;
    private static final int COLS_BUTTONS = 4;
    private static final int ROWS = 0;

    private String mainTitle = "Analytics - ";
    private Button backToHome, backToAnalytics, nextPage, previousPage;
    private Label titlePage, data;
    private GridFrame gridButtonFrame, gridContainer, parentGridAnalytics;

    public AnalyticsDataPage(GridFrame gridAnalyticsHome, String titleSection, String dataToShow) {
        // The mainTitle needs to be placed inside the constructor to update its value after the user's profile update
        parentGridAnalytics = gridAnalyticsHome;

        mainTitle += titleSection;

        gridButtonFrame = new GridFrame(ROWS, COLS_BUTTONS);

        gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS_MAIN_CONTAINER);

        titlePage = new Label(mainTitle);
        data = new Label(dataToShow);

        backToHome = new Button(BUTTON_BACK_HOME_TEXT);
        backToAnalytics = new Button(BUTTON_BACK_ANALYTICS_TEXT);
        previousPage = new Button(BUTTON_PREVIOUS_PAGE_TEXT);
        nextPage = new Button(BUTTON_NEXT_PAGE_TEXT);

        addAllEventListeners();

        gridContainer.addToView(titlePage);
        gridContainer.addToView(data);

        gridButtonFrame.addToView(backToHome);
        gridButtonFrame.addToView(backToAnalytics);
        gridButtonFrame.addToView(previousPage);
        gridButtonFrame.addToView(nextPage);

        gridContainer.addToView(gridButtonFrame);

        gridContainer.showWindow();
    }

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

    private void redirectToHomeFrame() {
        Home home = new Home();
        parentGridAnalytics.disposeFrame();
        gridContainer.disposeFrame();
    }

    private void redirectToAnalyticsFrame() {
        gridContainer.disposeFrame();
    }
}
