package com.insubria.it.paroliere.g_interface;

import com.insubria.it.paroliere.g_components.*;

public class GameWinner {
    private static final String TITLE_WINDOW = "Il Paroliere - Vincitore della partita";
    private static final String THE_WINNER_IS_TEXT = "Il vincitore della partita è...";
    private static final String WINNER_NAME_TEXT = "Tizio";
    private static final String CONGRATULATIONS_TEXT = "Congratulazioni!";
    private static final String CONTINUE_BUTTON = "OK";
    private static final int ROWS = 0;
    private static final int COLS = 1;

    private Label theWinnerIsText, winnerNameText, congratulationsText;
    private Button continueButton;
    private GridFrame gridContainer;

    public GameWinner() {
        gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS);

        theWinnerIsText = new Label(THE_WINNER_IS_TEXT);
        winnerNameText = new Label(WINNER_NAME_TEXT);
        congratulationsText = new Label(CONGRATULATIONS_TEXT);

        continueButton = new Button(CONTINUE_BUTTON);

        gridContainer.addToView(theWinnerIsText);
        gridContainer.addToView(winnerNameText);
        gridContainer.addToView(congratulationsText);

        gridContainer.addToView(continueButton);

        gridContainer.showWindow();
    }
}