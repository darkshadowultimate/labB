package com.insubria.it.g_interface;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import com.insubria.it.g_components.*;
import com.insubria.it.sharedserver.threads.gameThread.utils.WordRecord;

public class WordsAnalysis {
  private static final String TITLE_WINDOW = "Il Paroliere - Analisi parole trovate";
  private static final String MAIN_TITLE = "Tutte le parole trovate";
  private static final String ACCEPTED_WORDS = "Parole accettate (seleziona una parola per maggiori dettagli)";
  private static final String REFUSED_WORDS = "Parole rifiutate (seleziona una parola per maggiori dettagli)";
  private static final String TIMER_TEXT = " Timer: 180s";
  private static final String CHECK_TERM_BUTTON = "Verifica termine su vocabolario";
  private static final String STOP_ANALYSIS_BUTTON = "Termina analisi";
  private static final String BACK_TO_HOME_BUTTON = "Esci e torna alla Home";
  private static final int ROWS = 0;
  private static final int COLS_GRID_CONTAINER = 1;
  private static final int COLS_GRID_WORDS = 6;
  private static final int COLS_GRID_BUTTONS = 3;

  private static boolean isFrameActive = false;
  private static ArrayList<WordRecord>
        correctWords = new ArrayList<WordRecord>(),
        wrongWords = new ArrayList<WordRecord>();
  private Label mainTitle, acceptedWords, refusedWords;
  private static Label timerText;
  private Label[] correctWordsLabels, wrongWordsLabels;
  private Button checkTermButton, stopAnalysisButton, cancelButton;
  private GridFrame gridCorrectWords, gridWrongWords, gridButtons;
  private static GridFrame gridContainer = null;

  public WordsAnalysis(ArrayList<WordRecord> correctListWords, ArrayList<WordRecord> wrongListWords) {

    correctWords = correctListWords;
    wrongWords = wrongListWords;

    /*correctWords.add(new WordRecord("pane", "username1", 5));
    correctWords.add(new WordRecord("bacca", "username1", 4));
    correctWords.add(new WordRecord("auto", "username2", 1));

    wrongWords.add(new WordRecord("ddd", "username2", 0, "What is this??"));
    wrongWords.add(new WordRecord("auti", "username1", 0, "Come on, man!"));*/

    isFrameActive = true;

    gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS_GRID_CONTAINER);
    gridCorrectWords = new GridFrame(ROWS, COLS_GRID_WORDS);
    gridWrongWords = new GridFrame(ROWS, COLS_GRID_WORDS);
    gridButtons = new GridFrame(ROWS, COLS_GRID_BUTTONS);

    mainTitle = new Label(MAIN_TITLE);
    acceptedWords = new Label(ACCEPTED_WORDS);
    refusedWords = new Label(REFUSED_WORDS);
    timerText = new Label(TIMER_TEXT);

    this.initializeWordsLabels();

    checkTermButton = new Button(CHECK_TERM_BUTTON);
    stopAnalysisButton = new Button(STOP_ANALYSIS_BUTTON);
    cancelButton = new Button(BACK_TO_HOME_BUTTON);

    // ***** gridButtons ***** //
    gridButtons.addToView(checkTermButton);
    gridButtons.addToView(stopAnalysisButton);
    gridButtons.addToView(cancelButton);

    // ***** gridContainer ***** //
    gridContainer.addToView(mainTitle);
    gridContainer.addToView(timerText);
    gridContainer.addToView(acceptedWords);
    gridContainer.addToView(gridCorrectWords);
    gridContainer.addToView(refusedWords);
    gridContainer.addToView(gridWrongWords);
    gridContainer.addToView(gridButtons);

    gridContainer.showWindow();
  }

  private void initializeWordsLabels() {
    this.correctWordsLabels = new Label[correctWords.size()];
    this.wrongWordsLabels = new Label[wrongWords.size()];

    attachEventListenersToLabels(correctWords, this.correctWordsLabels, gridCorrectWords);

    attachEventListenersToLabels(wrongWords, this.wrongWordsLabels, gridWrongWords);
  }

  public static void updateCountdown(int currentValue) {
    timerText.setLabelValue("Timer: " + currentValue + "s");
  }

  public static boolean isWordsAnalysisFrameActive() {
    return isFrameActive;
  }

  public static void redirectToGameWinnerFrame(String usernameWinner) {
    isFrameActive = false;
    if(gridContainer != null) {
      gridContainer.disposeFrame();
    }
    GameWinner gameWinner = new GameWinner(usernameWinner);
  }

  public static void redirectToGamePlayFrame() {
    SingleWordAnalysis.redirectToWordsAnalysisFrame();
    gridContainer.disposeFrame();
  }

  private void attachEventListenersToLabels(ArrayList<WordRecord> list, Label[] labels, GridFrame grid) {
    for (int i = 0; i < list.size(); i++) {
      labels[i] = new Label(list.get(i).getWord());

      int tmpCounter = i;

      labels[i].attachMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent me) {
          SingleWordAnalysis singleWordAnalysis = new SingleWordAnalysis(list.get(tmpCounter));
        }
      });
      // ***** gridWords ***** //
      grid.addToView(labels[i]);
    }
  }
}