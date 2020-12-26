package com.insubria.it.g_interface;

import com.insubria.it.g_components.*;

public class NoWinners {
    private static final String TITLE_WINDOW = "Il Paroliere - Nessun vincitore";
    private static final String NO_WINNER_TEXT = "Nessun vincitore. \nNessun giocatore ha raggiunto 50 punti.";
    private static final String NEW_GAME_TEXT = "Giocare un'altra partita?";
    private static final String BUTTON_PLAY = "Gioca";
    private static final int ROWS = 0;
    private static final int COLS_CONTAINER = 1;
    private static final int COLS_BUTTONS = 2;

    private Label noWinnerText, newGameText;
    private Button playButton, cancelButton;
    private GridFrame gridContainer, gridButtons;

    public NoWinners() {
        gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS_CONTAINER);
        gridButtons = new GridFrame(ROWS, COLS_BUTTONS);

        noWinnerText = new Label(NO_WINNER_TEXT);
        newGameText = new Label(NEW_GAME_TEXT);

        playButton = new Button(BUTTON_PLAY);
        cancelButton = new Button("CANCEL");

        gridButtons.addToView(playButton);
        gridButtons.addToView(cancelButton);

        gridContainer.addToView(noWinnerText);
        gridContainer.addToView(newGameText);
        gridContainer.addToView(gridButtons);

        gridContainer.showWindow();
    }
}