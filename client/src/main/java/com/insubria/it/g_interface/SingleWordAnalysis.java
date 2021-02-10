package com.insubria.it.g_interface;

import com.insubria.it.context.GameContextProvider;
import com.insubria.it.context.RemoteObjectContextProvider;
import com.insubria.it.g_components.Button;
import com.insubria.it.g_components.GridFrame;
import com.insubria.it.g_components.Label;
import com.insubria.it.helpers.FrameHandler;
import com.insubria.it.sharedserver.threads.gameThread.utils.WordRecord;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

/**
 * The SingleWordAnalysis class creates the SingleWordAnalysis frame to allow the user
 * to consult the information relative to a single word during words analysis
 */
public class SingleWordAnalysis {
    /**
     * Static text that will be used with some UI components to communicate with the user
     */
    private static final String TITLE_WINDOW = "Il Paroliere - Analisi parola singola";
    private static final String MAIN_TITLE = "Analisi della parola: ";
    private static final String PLAYER_FOUNDER_TEXT = "Parola trovata da: ";
    private static final String SCORE_WORD_TEXT = "Punteggio assegnato alla parola: ";
    private static final String REASON_WRONG_WORD_TEXT = "Chiarimenti: ";
    private static final String DEFINITION_REQUEST_BUTTON_TEXT = "Visualizza la definizione della parola";
    private static final String BACK_BUTTON_TEXT = "Chiudi finestra";

    /**
     * Labels to communicate with the user what he's looking at
     */
    private Label mainTitleLabel, playerFounderLabel, scoreWordLabel, reasonWrongWordLabel;
    /**
     * requestDefinitionButton - shows a JOptionPane with the definition of the current word
     * backButton - closes this frame
     */
    private Button requestDefinitionButton, backButton;
    /**
     * Grid containers to handle UI elements visualization
     */
    private GridFrame gridButtons;
    private static GridFrame gridContainer = null;

    /**
     * Rows for the grid container (0 stands for: unlimited number of rows)
     */
    private static final int ROWS = 0;
    /**
     * Columns for the grid container
     */
    private static final int COLS_GRID_CONTAINER = 1;
    private static final int COLS_GRID_BUTTONS = 2;

    /**
     * Constructor of the class (creates the frame and its visual components)
     *
     * @param wordToAnalyse - Class with all the information about the word to analyse
     */
    public SingleWordAnalysis(WordRecord wordToAnalyse) {
        gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS_GRID_CONTAINER);
        gridButtons = new GridFrame(ROWS, COLS_GRID_BUTTONS);

        int wordScore = wordToAnalyse.getScore();
        boolean isThereAReason = wordScore == 0;

        mainTitleLabel = new Label(MAIN_TITLE + wordToAnalyse.getWord());
        playerFounderLabel = new Label(PLAYER_FOUNDER_TEXT + wordToAnalyse.getUsername());
        scoreWordLabel = new Label(SCORE_WORD_TEXT + wordScore);

        backButton = new Button(BACK_BUTTON_TEXT);

        if(isThereAReason) {
            reasonWrongWordLabel = new Label(REASON_WRONG_WORD_TEXT + wordToAnalyse.getReason());
        } else {
            requestDefinitionButton = new Button(DEFINITION_REQUEST_BUTTON_TEXT);
        }

        addAllEventListeners(wordToAnalyse.getWord(), isThereAReason);

        gridContainer.addToView(mainTitleLabel);
        gridContainer.addToView(playerFounderLabel);
        gridContainer.addToView(scoreWordLabel);

        if(isThereAReason) {
            gridContainer.addToView(reasonWrongWordLabel);
        } else {
            gridButtons.addToView(requestDefinitionButton);
        }

        gridButtons.addToView(backButton);
        gridContainer.addToView(gridButtons);

        FrameHandler.showSecondaryGridContainer(gridContainer);
    }

    /**
     * This method defines and attaches all ActionListeners to the appropriate UI elements
     */
    private void addAllEventListeners(final String wordString, boolean isThereAReason) {
        // if the word is wrong, then it doesn't have a definition
        if(!isThereAReason) {
            requestDefinitionButton.attachActionListenerToButton(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        RemoteObjectContextProvider
                        .game
                        .askForWordDefinition(
                            GameContextProvider.getGameClientReference(),
                            wordString
                        );
                    } catch(RemoteException exc) {
                        exc.printStackTrace();
                    }
                }
            });
        }
        backButton.attachActionListenerToButton(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FrameHandler.disposeSecondaryGridContainer();
            }
        });
    }

    /**
     * This method shows a JOptionPane with the definition of the current word
     *
     * @param wordDefinition - definition of the word to analyse
     */
    public static void showWordDefinition(String wordDefinition) {
        JOptionPane.showMessageDialog(null, wordDefinition);
    }
}
