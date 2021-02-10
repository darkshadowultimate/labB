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

/**
 * The WaitingPlayers class creates the WaitingPlayers frame to create a waiting room for players
 * during the start of a game or the end of the words analysis
 */
public class WaitingPlayers {
    /**
     * Fields to identify the right context for this class (other classes use theme
     */
    public static final int START_GAME = 1;
    public static final int WORDS_ANALYSIS = 2;
    /**
     * Static text that will be used with some UI components to communicate with the user
     */
    private static final String TITLE_WINDOW = "Il Paroliere - Attesa giocatori";
    private static final String WAIT_PLAYERS_TEXT = "In attesa di altri giocatori...";
    private static final String WAIT_PLAYERS_WORD_ANALYSIS_TEXT = "In attesa che gli altri giocatori terminino la fase di analisi...";
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
    private Label waitPlayersText;
    /**
     * cancel - return to Home frame
     */
    private Button cancel;
    /**
     * Grid containers to handle UI elements visualization
     */
    private static GridFrame gridContainer;

    /**
     * Constructor of the class (creates the frame and its visual components)
     *
     * @param context
     */
    public WaitingPlayers(int context) {
        gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS);

        waitPlayersText = new Label(getMainTitleLabelText(context));

        cancel = new Button(Button.BACK_TO_HOME);

        addAllEventListeners();

        gridContainer.addToView(waitPlayersText);

        if(context != WORDS_ANALYSIS) {
            gridContainer.addToView(cancel);
        }

        FrameHandler.showMainGridContainer(gridContainer);
    }

    /**
     * This method defines and attaches all ActionListeners to the appropriate UI elements
     */
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

    /**
     * Get the right main title label based on the context
     *
     * @param context
     */
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

    /**
     * This method displays on screen the WaitingStartGame section
     */
    public static void redirectToCountdownFrame() {
        WaitingStartGame waitingStartGame = new WaitingStartGame();
    }

    /**
     * This method displays on screen the Home section
     */
    public static void redirectToHomeFrame() {
        Home home = new Home();
    }
}