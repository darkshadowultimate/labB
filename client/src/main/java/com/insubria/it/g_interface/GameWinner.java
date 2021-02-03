package com.insubria.it.g_interface;


import com.insubria.it.g_components.*;
import com.insubria.it.helpers.FrameHandler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWinner {
    private static final String TITLE_WINDOW = "Il Paroliere - Vincitore della partita";
    private static final String THE_WINNER_IS_TEXT = "Il vincitore della partita è...";
    private static final String CONGRATULATIONS_TEXT = "Congratulazioni!";
    private static final String CONTINUE_BUTTON = "OK";
    private static final int ROWS = 0;
    private static final int COLS = 1;

    private Label theWinnerIsText, winnerNameText, congratulationsText;
    private Button continueButton;
    private GridFrame gridContainer;

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

    private void addAllEventListeners() {
        continueButton.attachActionListenerToButton(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                redirectToHomeFrame();
            }
        });
    }

    private void redirectToHomeFrame() {
        Home home = new Home();
    }
}