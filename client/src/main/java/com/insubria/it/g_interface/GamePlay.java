package com.insubria.it.g_interface;

import com.insubria.it.context.GameContextProvider;
import com.insubria.it.context.RemoteObjectContextProvider;
import com.insubria.it.g_components.*;
import com.insubria.it.g_components.Button;
import com.insubria.it.g_components.Label;
import com.insubria.it.helpers.FrameHandler;
import com.insubria.it.sharedserver.threads.gameThread.utils.WordRecord;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * The GamePlay class creates the GamePlay frame, to allow the user to play the game.
 * In this frame there's a matrix 4x4 of letters, a countdown of 3 minutes (180s),
 * a list with the players that are participating and their current scores,
 * a list with the words found by the user,
 * a button to add a new word found and another one to exit the game
 */
public class GamePlay {
  /**
   * Static text that will be used with some UI components to communicate with the user
   */
  private static final String TITLE_WINDOW = "Il Paroliere - Svolgimento partita";
  private static final String GAME_NAME_TEXT = "<html>Nome della partita: ";
  private static final String SESSION_TEXT = "Sessione n° ";
  private static final String TIMER_TEXT = "Timer: 180s";
  private static final String WORDS_FOUND_TEXT = "<html>Parole trovate: <br/><br/>";
  private static final String LIST_SCORES_TEXT = "<html>Classifica: <br /><br />";
  private static final String INSERT_WORD_TEXT = "Nuova parola trovata";
  private static final String ADD_WORD_BUTTON = "Aggiungi parola";
  private static final String INCORRECT_WORD_INPUT = "La parola da inserire deve avere almeno 3 lettere";
  /**
   * Rows for the grid container (0 stands for: unlimited number of rows)
   */
  private static final int ROWS = 0;
  private static final int ROWS_GRID_LETTERS = 4;
  /**
   * Columns for the grid containers
   */
  private static final int COLS_CONTAINER = 1;
  private static final int COLS_GRID_LETTER_TIMER_POINTS = 3;
  private static final int COLS_GRID_LETTERS = 4;
  private static final int COLS_GRID_TIMER_WORDS = 1;
  private static final int COLS_GRID_ADD_WORD = 1;
  private static final int COLS_GRID_LIST_WORD = 1;
  private static final int COLS_BUTTONS = 2;

  /**
   * wordsFound contains the words that the user found in this game play
   */
  private static ArrayList<String> wordsFound = new ArrayList<String>();
  /**
   * Labels to communicate with the user what he's looking at
   */
  private Label mainTitle, wordsFoundText, listScoresText, insertWordText;
  private static Label timerText = new Label(TIMER_TEXT);
  /**
   * addNewWordInput input field where to insert the word just found
   */
  private InputLabel addNewWordInput;
  /**
   * addWordButton - button to add the new word found to the listWordsView and wordsFound
   * cancelButton - button to exit the game return to Home
   */
  private Button addWordButton, cancelButton;
  /**
   * listWordsView - list which contains the words found by the user (this is used to update the jlist UI element)
   */
  private DefaultListModel<String> listWordsView = new DefaultListModel<>();
  /**
   * jlist - allows to set a scroll in order to visualize all the words found
   */
  private JList<String> jlist = new JList<>(listWordsView);
  /**
   * Grid containers to handle UI elements visualization
   */
  private GridFrame gridLettersTimerPoints, gridLetters, gridListWords, gridTimerWords, gridAddWord, gridButtons;
  private static GridFrame gridContainer;

  /**
   * Constructor of the class (creates the frame and its visual components)
   *
   * @param gameName - Name of the game
   * @param sessionNumber - Number of the current session of this game
   * @param matrixLetters - Matrix 4x4 containing letter (used by user to find the words)
   * @param playersWithScore - List of participants and their current score
   */
  public GamePlay(String gameName, int sessionNumber, String[][] matrixLetters, HashMap<String, Integer> playersWithScore) {
    wordsFound = new ArrayList<>();

    // initialize grids
    gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS_CONTAINER);
    gridLettersTimerPoints = new GridFrame(ROWS, COLS_GRID_LETTER_TIMER_POINTS);
    gridLetters = new GridFrame(ROWS_GRID_LETTERS, COLS_GRID_LETTERS);
    gridListWords = new GridFrame(ROWS, COLS_GRID_LIST_WORD);
    gridTimerWords = new GridFrame(ROWS, COLS_GRID_TIMER_WORDS);
    gridAddWord = new GridFrame(ROWS, COLS_GRID_ADD_WORD);
    gridButtons = new GridFrame(ROWS, COLS_BUTTONS);

    String playersScores = getLabelTextForPlayerScores(playersWithScore);

    // initialize labels
    mainTitle = new Label(GAME_NAME_TEXT + gameName + "  /  " + SESSION_TEXT + sessionNumber);
    wordsFoundText = new Label(WORDS_FOUND_TEXT);
    listScoresText = new Label(playersScores);

    // initialize input labels
    addNewWordInput = new InputLabel(INSERT_WORD_TEXT, false);

    // initialize buttons
    addWordButton = new Button(ADD_WORD_BUTTON);
    cancelButton = new Button(Button.EXIT_GAME);

    JScrollPane scrollPane = new JScrollPane();
    scrollPane.setViewportView(jlist);

    // ***** gridLettersTimerPoints *****
    fillGridLetters(matrixLetters);

    addAllEventListeners();

    gridListWords.addToView(wordsFoundText);
    gridListWords.addToView(scrollPane);

    gridTimerWords.addToView(timerText);
    //gridTimerWords.addToView(wordsFoundText);
    gridTimerWords.addToView(gridListWords);

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

    FrameHandler.showMainGridContainerWithSizes(gridContainer, 500, 1400);
  }

  /**
   * This method defines and attaches all ActionListeners to the appropriate UI elements
   */
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

        if(wordInserted.length() > 2) {
          wordsFound.add(wordInserted);
          listWordsView.addElement(wordInserted);
          addNewWordInput.setValueInputField("");
        } else {
          JOptionPane.showMessageDialog(null, INCORRECT_WORD_INPUT);
        }
      }
    });
  }

  /**
   * This method returns a String object which is the representation of HashMap playersWithScore
   *
   * @param playersWithScore - HashMap key value pair user - score
   */
  private String getLabelTextForPlayerScores(HashMap<String, Integer> playersWithScore) {
    String labelText = "";

    Iterator it = playersWithScore.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry pair = (Map.Entry)it.next();
      labelText += pair.getKey() + " - " + pair.getValue() + "<br />";
      // this avoids ConcurrentModificationException
      it.remove();
    }

    return LIST_SCORES_TEXT + labelText;
  }

  /**
   * This method create a matrix 4x4 of letters using a GridFrame object
   *
   * @param matrixLetters - Matrix 4x4 containing letter (used by user to find the words)
   */
  private void fillGridLetters(String[][] matrixLetters) {
    for (int row = 0; row < ROWS_GRID_LETTERS; row++) {
      for (int col = 0; col < COLS_GRID_LETTERS; col++) {
        this.gridLetters.addToView(new Label(matrixLetters[row][col]));
      }
    }
  }

  /**
   * This method returns the wordsFound ArrayList<String>
   */
  public static ArrayList<String> getWordsFound() {
    return wordsFound;
  }

  /**
   * This method updates the timer (180s to 0s)
   *
   * @param currentValue - current value of the timer
   */
  public static void updateCountdown(int currentValue) {
    timerText.setLabelValue("Timer: " + currentValue + "s");
  }

  /**
   * This method updates the timer (180s to 0s)
   *
   * @param correctListWords - ArrayList of accepted words by the server
   * @param wrongListWords - ArrayList of NOT accepted words by the server
   */
  public static void redirectToWordsAnalysisFrame(
    ArrayList<WordRecord> correctListWords,
    ArrayList<WordRecord> wrongListWords
  ) {
    WordsAnalysis wordsAnalysis = new WordsAnalysis(correctListWords, wrongListWords);
  }

  /**
   * This method displays on screen the Home section
   */
  public static void redirectToHomeFrame() {
    Home home = new Home();
  }
}