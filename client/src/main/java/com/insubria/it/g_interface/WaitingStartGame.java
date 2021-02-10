package com.insubria.it.g_interface;


import com.insubria.it.g_components.*;
import com.insubria.it.helpers.FrameHandler;

/**
 * The WaitingStartGame class creates the WaitingStartGame frame
 * which shows a countdown of 30 seconds before the start of game
 */
public class WaitingStartGame {
    /**
     * Static text that will be used with some UI components to communicate with the user
     */
    private static final String TITLE_WINDOW = "Il Paroliere - Attesa inizio partita";
    private static final String WAIT_GAME_START_TEXT = "La partita inizierà fra...";
    /**
     * Rows for the grid container (0 stands for: unlimited number of rows)
     */
    private static final int ROWS = 0;
    /**
     * Columns for the grid container (only one element for row)
     */
    private static final int COLS = 1;

    /**
     * Labels to communicate with the user what he's looking at
     */
    private Label waitGameStartText;
    private static Label secondsText = new Label("30s");
    /**
     * Grid containers to handle UI elements visualization
     */
    private static GridFrame gridContainer;

    /**
     * Constructor of the class (creates the frame and its visual components)
     */
    public WaitingStartGame() {
        gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS);

        waitGameStartText = new Label(WAIT_GAME_START_TEXT);
        secondsText = new Label("30s");

        gridContainer.addToView(waitGameStartText);
        gridContainer.addToView(secondsText);

        FrameHandler.showMainGridContainer(gridContainer);
    }

    /**
     * This method updates the timer (30s to 0s)
     *
     * @param currentValue - current value of the timer
     */
    public static void updateCountdown(int currentValue) {
        secondsText.setLabelValue(currentValue + "s");
    }

    /**
     * This method displays on screen the Home section
     */
    public static void redirectToHomeFrame() {
        Home home = new Home();
    }
}