package com.insubria.it.g_interface;


import com.insubria.it.g_components.*;
import java.util.HashMap;

public class WaitingStartGame {
    private static final String TITLE_WINDOW = "Il Paroliere - Attesa inizio partita";
    private static final String WAIT_GAME_START_TEXT = "La partita inizierà fra...";
    private static final int ROWS = 0;
    private static final int COLS = 1;

    private Label waitGameStartText;
    private static Label secondsText = new Label("30s");
    private static GridFrame gridContainer;

    public WaitingStartGame() {
        gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS);

        waitGameStartText = new Label(WAIT_GAME_START_TEXT);
        secondsText = new Label("30s");


        gridContainer.addToView(waitGameStartText);
        gridContainer.addToView(secondsText);

        gridContainer.showWindow();
    }

    public static void updateCountdown(int currentValue) {
        secondsText.setLabelValue(currentValue + "s");
    }

    public static void redirectToGamePlayFrame() {
        gridContainer.disposeFrame();
    }

    public static void redirectToHomeFrame() {
        gridContainer.disposeFrame();
        Home home = new Home();
    }
}