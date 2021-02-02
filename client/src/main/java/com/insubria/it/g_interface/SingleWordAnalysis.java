package com.insubria.it.g_interface;

import com.insubria.it.context.GameContextProvider;
import com.insubria.it.context.RemoteObjectContextProvider;
import com.insubria.it.g_components.Button;
import com.insubria.it.g_components.GridFrame;
import com.insubria.it.g_components.Label;
import com.insubria.it.sharedserver.threads.gameThread.utils.WordRecord;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class SingleWordAnalysis {
    private static final String TITLE_WINDOW = "Il Paroliere - Analisi parola singola";
    private static final String MAIN_TITLE = "Analisi della parola: ";
    private static final String PLAYER_FOUNDER_TEXT = "Parola trovata da: ";
    private static final String SCORE_WORD_TEXT = "Punteggio assegnato alla parola: ";
    private static final String REASON_WRONG_WORD_TEXT = "Chiarimenti: ";
    private static final String DEFINITION_REQUEST_BUTTON_TEXT = "Visualizza la definizione della parola";
    private static final String BACK_BUTTON_TEXT = "Chiudi finestra";

    private Label mainTitleLabel, playerFounderLabel, scoreWordLabel, reasonWrongWordLabel;
    private Button requestDefinitionButton, backButton;
    private GridFrame gridButtons;
    private static GridFrame gridContainer = null;

    private static final int ROWS = 0;
    private static final int COLS_GRID_CONTAINER = 1;
    private static final int COLS_GRID_BUTTONS = 2;

    public SingleWordAnalysis(WordRecord wordToAnalyse) {
        gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS_GRID_CONTAINER);
        gridButtons = new GridFrame(ROWS, COLS_GRID_BUTTONS);

        int wordScore = wordToAnalyse.getScore();

        mainTitleLabel = new Label(MAIN_TITLE + wordToAnalyse.getWord());
        playerFounderLabel = new Label(PLAYER_FOUNDER_TEXT + wordToAnalyse.getUsername());
        scoreWordLabel = new Label(SCORE_WORD_TEXT + wordScore);

        backButton = new Button(BACK_BUTTON_TEXT);

        addAllEventListeners(wordToAnalyse.getWord());

        gridContainer.addToView(mainTitleLabel);
        gridContainer.addToView(playerFounderLabel);
        gridContainer.addToView(scoreWordLabel);

        if(wordScore == 0) {
            reasonWrongWordLabel = new Label(REASON_WRONG_WORD_TEXT + wordToAnalyse.getReason());
            gridContainer.addToView(reasonWrongWordLabel);
        } else {
            requestDefinitionButton = new Button(DEFINITION_REQUEST_BUTTON_TEXT);
            gridButtons.addToView(requestDefinitionButton);
        }

        gridButtons.addToView(backButton);
        gridContainer.addToView(gridButtons);

        gridContainer.showWindow();
    }

    private void addAllEventListeners(final String wordString) {
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
        backButton.attachActionListenerToButton(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                redirectToWordsAnalysisFrame();
            }
        });
    }

    public static void showWordDefinition(String wordDefinition) {
        JOptionPane.showMessageDialog(null, wordDefinition);
    }

    public static void redirectToWordsAnalysisFrame() {
        if(gridContainer != null) {
            gridContainer.disposeFrame();
        }
    }
}
