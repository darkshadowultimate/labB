package com.insubria.it.paroliere.g_interface;

import java.util.ArrayList;
import com.insubria.it.paroliere.g_components.*;

public class WordsAnalysis {
    private static final String TITLE_WINDOW = "Il Paroliere - Analisi parole trovate";
    private static final String MAIN_TITLE = "Tutte le parole trovate";
    private static final String TIMER_TEXT = " Timer: ";
    private static final String CHECK_TERM_BUTTON = "Verifica termine su vocabolario";
    private static final String STOP_ANALYSIS_BUTTON = "Termina analisi";
    private static final int ROWS = 0;
    private static final int COLS_GRID_CONTAINER = 1;
    private static final int COLS_GRID_WORDS = 5;
    private static final int COLS_GRID_BUTTONS = 3;

    private Label mainTitle, timerText;
    private Label[] wordsFoundLabels;
    private Button checkTermButton, stopAnalysisButton, cancelButton;
    private GridFrame gridContainer, gridWords, gridButtons;

    public WordsAnalysis(ArrayList<String> wordsFound) {
        gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS_GRID_CONTAINER);
        gridWords = new GridFrame(ROWS, COLS_GRID_WORDS);
        gridButtons = new GridFrame(ROWS, COLS_GRID_BUTTONS);

        mainTitle = new Label(MAIN_TITLE);
        timerText = new Label(TIMER_TEXT);

        this.initializeWordsLabels(wordsFound);

        checkTermButton = new Button(CHECK_TERM_BUTTON);
        stopAnalysisButton = new Button(STOP_ANALYSIS_BUTTON);
        cancelButton = new Button("CANCEL");

        // ***** gridButtons ***** //
        gridButtons.addToView(checkTermButton);
        gridButtons.addToView(stopAnalysisButton);
        gridButtons.addToView(cancelButton);
        
        // ***** gridContainer ***** //
        gridContainer.addToView(mainTitle);
        gridContainer.addToView(timerText);
        gridContainer.addToView(gridWords);
        gridContainer.addToView(gridButtons);

        gridContainer.showWindow();
    }

    public void initializeWordsLabels(ArrayList<String> wordsFound) {
      this.wordsFoundLabels = new Label[wordsFound.size()];

      for(int i = 0; i < wordsFound.size(); i++) {
        this.wordsFoundLabels[i] = new Label(wordsFound.get(i));
        // ***** gridWords ***** //
        this.gridWords.addToView(this.wordsFoundLabels[i]);
      }
    }
}