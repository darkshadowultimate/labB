package com.insubria.it.g_interface;


import com.insubria.it.g_components.*;
import com.insubria.it.helpers.FrameHandler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The GameWinner class creates the GameWinner frame to show the user the winner of the game
 */
public class GameWinner {
    /**
     * Static text that will be used with some UI components to communicate with the user
     */
    private static final String TITLE_WINDOW = "Il Paroliere - Vincitore della partita";
    private static final String THE_WINNER_IS_TEXT = "Il vincitore della partita è...";
    private static final String CONGRATULATIONS_TEXT = "Congratulazioni!";
    private static final String CONTINUE_BUTTON = "OK";
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
    private Label theWinnerIsText, winnerNameText, congratulationsText;
    /**
     * continueButton - Redirects to Home frame
     */
    private Button continueButton;
    /**
     * Grid containers to handle UI elements visualization
     */
    private GridFrame gridContainer;

    /**
     * Constructor of the class (creates the frame and its visual components)
     *
     * @param usernameWinner - username of the winner
     */
    public GameWinner(String usernameWinner) {
        gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS);

        theWinnerIsText = new Label(THE_WINNER_IS_TEXT);
        winnerNameText = new Label(usernameWinner);
        congratulationsText = new Label(CONGRATULATIONS_TEXT);

        continueButton = new Button(CONTINUE_BUTTON);

        addAllEventListeners();

        gridContainer.addToView(theWinnerIsText);
        gridContainer.addToView(winnerNameText);
        gridContainer.addToView(congratulationsText);

        gridContainer.addToView(continueButton);

        FrameHandler.showMainGridContainer(gridContainer);
    }

    /**
     * This method defines and attaches all ActionListeners to the appropriate UI elements
     */
    private void addAllEventListeners() {
        continueButton.attachActionListenerToButton(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                redirectToHomeFrame();
            }
        });
    }

    /**
     * This method displays on screen the Home section
     */
    private void redirectToHomeFrame() {
        Home home = new Home();
    }
}