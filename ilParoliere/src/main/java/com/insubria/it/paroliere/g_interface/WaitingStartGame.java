package com.insubria.it.paroliere.g_interface;

import com.insubria.it.paroliere.g_components.*;

public class WaitingStartGame {
    private static final String TITLE_WINDOW = "Il Paroliere - Attesa inizio partita";
    private static final String WAIT_GAME_START_TEXT = "La partita inizierà fra...";
    private static final String SECONDS_TEXT = " secondi";
    private static final int ROWS = 0;
    private static final int COLS = 1;

    private Label waitGameStartText, secondsText;
    private Button cancel;
    private GridFrame gridContainer;

    public WaitingStartGame() {
        gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS);

        waitGameStartText = new Label(WAIT_GAME_START_TEXT);
        secondsText = new Label(SECONDS_TEXT);

        cancel = new Button("CANCEL");

        gridContainer.addToView(waitGameStartText);
        gridContainer.addToView(secondsText);

        gridContainer.addToView(cancel);

        gridContainer.showWindow();
    }
}