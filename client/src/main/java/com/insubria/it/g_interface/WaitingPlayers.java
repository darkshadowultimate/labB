package com.insubria.it.g_interface;

import com.insubria.it.g_components.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WaitingPlayers {
    private static final String TITLE_WINDOW = "Il Paroliere - Attesa giocatori";
    private static final String WAIT_PLAYERS_TEXT = "In attesa di altri giocatori...";
    private static final String MAX_PLAYERS_TEXT = "Max iscritti: ";
    private static final String CURRENT_PLAYERS_TEXT = "Giocatori attuali: ";
    private static final String HOME_BUTTON = "Torna alla Home";
    private static final int ROWS = 0;
    private static final int COLS = 1;

    private Label waitPlayersText, maxPlayersText, currentPlayersText;
    private Button cancel;
    private GridFrame gridContainer;

    public WaitingPlayers(boolean isGameCreator) {
        gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS);

        waitPlayersText = new Label(WAIT_PLAYERS_TEXT);
        maxPlayersText = new Label(MAX_PLAYERS_TEXT);
        currentPlayersText = new Label(CURRENT_PLAYERS_TEXT);

        cancel = new Button(HOME_BUTTON);

        addAllEventListeners();

        gridContainer.addToView(waitPlayersText);
        gridContainer.addToView(maxPlayersText);
        gridContainer.addToView(currentPlayersText);

        gridContainer.addToView(cancel);

        gridContainer.showWindow();
    }

    private void addAllEventListeners() {
        cancel.attachActionListenerToButton(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                redirectToHomeFrame();
            }
        });
    }

    private void redirectToHomeFrame() {
        Home home = new Home();
        gridContainer.disposeFrame();
    }
}