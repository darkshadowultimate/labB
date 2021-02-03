package com.insubria.it.g_interface;

import com.insubria.it.context.GameContextProvider;
import com.insubria.it.context.PlayerContextProvider;
import com.insubria.it.context.RemoteObjectContextProvider;
import com.insubria.it.g_components.*;
import com.insubria.it.helpers.FrameHandler;
import com.insubria.it.serverImplClasses.GameClientImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.concurrent.CompletableFuture;

public class WaitingPlayers {
    public static final int START_GAME = 1;
    public static final int WORDS_ANALYSIS = 2;

    private static final String TITLE_WINDOW = "Il Paroliere - Attesa giocatori";
    private static final String WAIT_PLAYERS_TEXT = "In attesa di altri giocatori...";
    private static final String WAIT_PLAYERS_WORD_ANALYSIS_TEXT = "In attesa che gli altri giocatori terminino la fase di analisi...";
    private static final String HOME_BUTTON = "Torna alla Home";
    private static final int ROWS = 0;
    private static final int COLS = 1;

    private Label waitPlayersText;
    private Button cancel;
    private static GridFrame gridContainer;

    public WaitingPlayers(int context) {
        gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS);

        waitPlayersText = new Label(getMainTitleLabelText(context));

        cancel = new Button(HOME_BUTTON);

        addAllEventListeners();

        gridContainer.addToView(waitPlayersText);

        gridContainer.addToView(cancel);

        FrameHandler.showMainGridContainer(gridContainer);
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

    private String getMainTitleLabelText(int context) {
        switch(context) {
            case START_GAME:
                return WAIT_PLAYERS_TEXT;
            case WORDS_ANALYSIS:
                return WAIT_PLAYERS_WORD_ANALYSIS_TEXT;
            default:
                return "In attesa degli altri giocatori...";
        }
    }

    public static void redirectToCountdownFrame() {
        WaitingStartGame waitingStartGame = new WaitingStartGame();
    }

    public static void redirectToHomeFrame() {
        Home home = new Home();
    }
}