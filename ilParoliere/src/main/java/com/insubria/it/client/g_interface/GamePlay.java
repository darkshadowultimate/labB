package com.insubria.it.client.g_interface;

import com.insubria.it.client.g_components.*;

public class GamePlay {
  // Constants
  private static final String TITLE_WINDOW = "Il Paroliere - Svolgimento partita";
  private static final String TIMER_TEXT = "Timer: ";
  private static final String WORDS_FOUND_TEXT = "<html>Parole trovate: <br/>- Parola1 <br/>- Parola2";
  private static final String LIST_SCORES_TEXT = "Classifica: ";
  private static final String INSERT_WORD_TEXT = "Nuova parola trovata";
  private static final String ADD_WORD_BUTTON = "Aggiungi parola";
  private static final int ROWS = 0;
  private static final int ROWS_GRID_LETTERS = 4;
  private static final int COLS_CONTAINER = 1;
  private static final int COLS_GRID_LETTER_TIMER_POINTS = 3;
  private static final int COLS_GRID_LETTERS = 4;
  private static final int COLS_GRID_TIMER_WORDS = 1;
  private static final int COLS_GRID_ADD_WORD = 1;
  private static final int COLS_BUTTONS = 2;
  // Variables
  private Label mainTitle, timerText, wordsFoundText, listScoresText, insertWordText;
  private InputLabel addNewWordInput;
  private Button addWordButton, cancelButton;
  private GridFrame gridContainer, gridLettersTimerPoints, gridLetters, gridTimerWords, gridAddWord, gridButtons;

  public GamePlay(String gameName, String[][] matrixLetters) {
    // initialize grids
    gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS_CONTAINER);
    gridLettersTimerPoints = new GridFrame(ROWS, COLS_GRID_LETTER_TIMER_POINTS);
    gridLetters = new GridFrame(ROWS_GRID_LETTERS, COLS_GRID_LETTERS);
    gridTimerWords = new GridFrame(ROWS, COLS_GRID_TIMER_WORDS);
    gridAddWord = new GridFrame(ROWS, COLS_GRID_ADD_WORD);
    gridButtons = new GridFrame(ROWS, COLS_BUTTONS);
    
    // initialize labels
    mainTitle = new Label(gameName);
    timerText = new Label(TIMER_TEXT);
    wordsFoundText = new Label(WORDS_FOUND_TEXT);
    listScoresText = new Label(LIST_SCORES_TEXT);

    // initialize input labels
    addNewWordInput = new InputLabel(INSERT_WORD_TEXT);

    // initialize buttons
    addWordButton = new Button(ADD_WORD_BUTTON);
    cancelButton = new Button("Cancel");

    // ***** gridLettersTimerPoints *****
    fillGridLetters(matrixLetters);

    gridTimerWords.addToView(timerText);
    gridTimerWords.addToView(wordsFoundText);

    gridLettersTimerPoints.addToView(gridLetters);
    gridLettersTimerPoints.addToView(gridTimerWords);
    gridLettersTimerPoints.addToView(listScoresText);

    // ***** gridAddWord *****
    gridAddWord.addToView(addNewWordInput);
    gridAddWord.addToView(addWordButton);

    // ***** gridButtons *****
    gridButtons.addToView(gridAddWord);
    gridButtons.addToView(cancelButton);

    // ***** gridContainer *****
    gridContainer.addToView(mainTitle);
    gridContainer.addToView(gridLettersTimerPoints);
    gridContainer.addToView(gridButtons);

    gridContainer.showWindow(1200, 500);
  }

  // fill gridLetters with letters from matrixLetters (4X4)
  private void fillGridLetters(String[][] matrixLetters) {
    for(int row = 0; row < ROWS_GRID_LETTERS; row++) {
      for(int col = 0; col < COLS_GRID_LETTERS; col++) {
        this.gridLetters.addToView(new Label(matrixLetters[row][col]));
      }
    }
  }
}