package com.insubria.it.g_interface;

import com.insubria.it.context.GameContextProvider;
import com.insubria.it.context.RemoteObjectContextProvider;
import com.insubria.it.g_components.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GamePlay {
  // Constants
  private static final String TITLE_WINDOW = "Il Paroliere - Svolgimento partita";
  private static final String TIMER_TEXT = "Timer: 180s";
  private static final String WORDS_FOUND_TEXT = "<html>Parole trovate: <br/><br/>";
  private static final String LIST_SCORES_TEXT = "<html>Classifica: <br /><br />";
  private static final String INSERT_WORD_TEXT = "Nuova parola trovata";
  private static final String ADD_WORD_BUTTON = "Aggiungi parola";
  private static final String EXIT_GAMEPLAY_BUTTON = "Esci dalla partita";
  private static final int ROWS = 0;
  private static final int ROWS_GRID_LETTERS = 4;
  private static final int COLS_CONTAINER = 1;
  private static final int COLS_GRID_LETTER_TIMER_POINTS = 3;
  private static final int COLS_GRID_LETTERS = 4;
  private static final int COLS_GRID_TIMER_WORDS = 1;
  private static final int COLS_GRID_ADD_WORD = 1;
  private static final int COLS_BUTTONS = 2;
  // Variables
  private ArrayList<String> wordsFound = new ArrayList<String>();
  private Label mainTitle, wordsFoundText, listScoresText, insertWordText;
  private static Label timerText = new Label(TIMER_TEXT);
  private InputLabel addNewWordInput;
  private Button addWordButton, cancelButton;
  private GridFrame gridContainer, gridLettersTimerPoints, gridLetters, gridTimerWords, gridAddWord, gridButtons;

  public GamePlay(String gameName, int sessionNumber, String[][] matrixLetters, HashMap<String, Integer> playersWithScore) {
    // initialize grids
    gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS_CONTAINER);
    gridLettersTimerPoints = new GridFrame(ROWS, COLS_GRID_LETTER_TIMER_POINTS);
    gridLetters = new GridFrame(ROWS_GRID_LETTERS, COLS_GRID_LETTERS);
    gridTimerWords = new GridFrame(ROWS, COLS_GRID_TIMER_WORDS);
    gridAddWord = new GridFrame(ROWS, COLS_GRID_ADD_WORD);
    gridButtons = new GridFrame(ROWS, COLS_BUTTONS);

    String playersScores = getLabelTextForPlayerScores(playersWithScore);

    // initialize labels
    mainTitle = new Label(gameName);
    wordsFoundText = new Label(WORDS_FOUND_TEXT);
    listScoresText = new Label(playersScores);

    // initialize input labels
    addNewWordInput = new InputLabel(INSERT_WORD_TEXT);

    // initialize buttons
    addWordButton = new Button(ADD_WORD_BUTTON);
    cancelButton = new Button(EXIT_GAMEPLAY_BUTTON);

    // ***** gridLettersTimerPoints *****
    fillGridLetters(matrixLetters);

    addAllEventListeners();

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

  private void addAllEventListeners() {
    cancelButton.attachActionListenerToButton(new ActionListener() {
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
    addWordButton.attachActionListenerToButton(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String wordInserted = addNewWordInput.getValueTextField();

        wordsFound.add(wordInserted);
        wordsFoundText.setLabelValue(wordsFoundText.getLabelText() + "- " + wordInserted + "<br />");
        addNewWordInput.setValueInputField("");
      }
    });
  }

  private String getLabelTextForPlayerScores(HashMap<String, Integer> playersWithScore) {
    String labelText = "";

    Iterator it = playersWithScore.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry pair = (Map.Entry)it.next();
      labelText += pair.getKey() + " - " + pair.getValue() + " / ";
      // this avoids ConcurrentModificationException
      it.remove();
    }

    return LIST_SCORES_TEXT + labelText;
  }

  // fill gridLetters with letters from matrixLetters (4X4)
  private void fillGridLetters(String[][] matrixLetters) {
    for (int row = 0; row < ROWS_GRID_LETTERS; row++) {
      for (int col = 0; col < COLS_GRID_LETTERS; col++) {
        this.gridLetters.addToView(new Label(matrixLetters[row][col]));
      }
    }
  }

  public static void updateCountdown(int currentValue) {
    timerText.setLabelValue("Timer: " + currentValue + "s");
  }
}