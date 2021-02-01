package com.insubria.it.g_interface;

import com.insubria.it.context.GameContextProvider;
import com.insubria.it.context.PlayerContextProvider;
import com.insubria.it.context.RemoteObjectContextProvider;
import com.insubria.it.g_components.*;
import com.insubria.it.serverImplClasses.GameClientImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.concurrent.CompletableFuture;

public class WaitingPlayers {
    private static final String TITLE_WINDOW = "Il Paroliere - Attesa giocatori";
    private static final String WAIT_PLAYERS_TEXT = "In attesa di altri giocatori...";
    private static final String HOME_BUTTON = "Torna alla Home";
    private static final int ROWS = 0;
    private static final int COLS = 1;

    private Label waitPlayersText;
    private Button cancel;
    private static GridFrame gridContainer;

    public WaitingPlayers() {
        gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS);

        waitPlayersText = new Label(WAIT_PLAYERS_TEXT);

        cancel = new Button(HOME_BUTTON);

        addAllEventListeners();

        gridContainer.addToView(waitPlayersText);

        gridContainer.addToView(cancel);
        gridContainer.showWindow();
    }

    private void addAllEventListeners() {
        cancel.attachActionListenerToButton(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    RemoteObjectContextProvider
                    .game
                    .removePlayerNotStartedGame(GameContextProvider.getGameClientReference());
                } catch(RemoteException exc) {
                    exc.printStackTrace();
                }
            }
        });
    }

    public static void redirectToCountdownFrame() {
        gridContainer.disposeFrame();
        WaitingStartGame waitingStartGame = new WaitingStartGame();
    }

    public static void redirectToHomeFrame() {
        gridContainer.disposeFrame();
        Home home = new Home();
    }
}