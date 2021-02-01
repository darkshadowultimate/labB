package com.insubria.it.g_interface;

import com.insubria.it.context.GameContextProvider;
import com.insubria.it.context.RemoteObjectContextProvider;
import com.insubria.it.g_components.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.HashMap;

public class WaitingStartGame {
    private static final String TITLE_WINDOW = "Il Paroliere - Attesa inizio partita";
    private static final String WAIT_GAME_START_TEXT = "La partita inizierà fra...";
    private static final String BACK_TO_HOME_BUTTON = "Torna alla Home";
    private static final int ROWS = 0;
    private static final int COLS = 1;

    private Label waitGameStartText;
    private static Label secondsText = new Label("30s");
    private Button cancel;
    private static GridFrame gridContainer;

    public WaitingStartGame() {
        gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS);

        waitGameStartText = new Label(WAIT_GAME_START_TEXT);
        secondsText = new Label("30s");

        cancel = new Button(BACK_TO_HOME_BUTTON);

        addAllEventListeners();

        gridContainer.addToView(waitGameStartText);
        gridContainer.addToView(secondsText);

        gridContainer.addToView(cancel);

        gridContainer.showWindow();
    }

    private void addAllEventListeners() {
        cancel.attachActionListenerToButton(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    RemoteObjectContextProvider
                    .game
                    .removePlayerInGame(GameContextProvider.getGameClientReference());
                } catch(RemoteException exc) {
                    exc.printStackTrace();
                }
            }
        });
    }

    public static void updateCountdown(int currentValue) {
        secondsText.setLabelValue(currentValue + "s");
    }

    public static void redirectToGamePlayFrame(
            String name,
           int sessionNumber,
           String[][] matrix,
           HashMap<String, Integer> playerScore
    ) {
        gridContainer.disposeFrame();
        GamePlay gamePlay = new GamePlay(
            name,
            sessionNumber,
            matrix,
            playerScore
        );
    }

    public static void redirectToHomeFrame() {
        gridContainer.disposeFrame();
        Home home = new Home();
    }
}