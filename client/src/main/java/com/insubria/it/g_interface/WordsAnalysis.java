package com.insubria.it.g_interface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import com.insubria.it.context.GameContextProvider;
import com.insubria.it.context.RemoteObjectContextProvider;
import com.insubria.it.g_components.*;
import com.insubria.it.helpers.FrameHandler;
import com.insubria.it.sharedserver.threads.gameThread.utils.WordRecord;

/**
 * The WordsAnalysis class creates the WordsAnalysis frame to allow the user
 * to analyse the words found after a session of game
 */
public class WordsAnalysis {
  /**
   * Static text that will be used with some UI components to communicate with the user
   */
  private static final String TITLE_WINDOW = "Il Paroliere - Analisi parole trovate";
  private static final String MAIN_TITLE = "Tutte le parole trovate";
  private static final String ACCEPTED_WORDS = "Parole accettate (seleziona una parola per maggiori dettagli)";
  private static final String REFUSED_WORDS = "Parole rifiutate (seleziona una parola per maggiori dettagli)";
  private static final String TIMER_TEXT = "";
  private static final String STOP_ANALYSIS_BUTTON = "Termina analisi";
  private static final String BACK_TO_HOME_BUTTON = "Esci e torna alla Home";
  /**
   * Rows for the grid container (0 stands for: unlimited number of rows)
   */
  private static final int ROWS = 0;
  /**
   * Columns for the grid container (only one element for row)
   */
  private static final int COLS_GRID_CONTAINER = 1;
  private static final int COLS_GRID_WORDS = 6;
  private static final int COLS_GRID_BUTTONS = 2;

  /**
   * correctWords - words accepted by the server
   * wrongWords - words NOT accepted by the server
   */
  private static ArrayList<WordRecord>
        correctWords = new ArrayList<WordRecord>(),
        wrongWords = new ArrayList<WordRecord>();
  /**
   * Labels to communicate with the user what he's looking at
   */
  private Label mainTitle, acceptedWords, refusedWords;
  private static Label timerText;
  private Label[] correctWordsLabels, wrongWordsLabels;
  /**
   * stopAnalysisButton - Terminate the analysis
   * cancelButton - Exit from the game/words analyse section e return to Home
   */
  private Button stopAnalysisButton, cancelButton;
  /**
   * Grid containers to handle UI elements visualization
   */
  private GridFrame gridCorrectWords, gridWrongWords, gridButtons;
  private static GridFrame gridContainer = null;

  /**
   * Constructor of the class (creates the frame and its visual components)
   *
   * @param correctListWords - words accepted by the server
   * @param wrongListWords - words NOT accepted by the server
   */
  public WordsAnalysis(ArrayList<WordRecord> correctListWords, ArrayList<WordRecord> wrongListWords) {

    correctWords = correctListWords;
    wrongWords = wrongListWords;

    gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS_GRID_CONTAINER);
    gridCorrectWords = new GridFrame(ROWS, COLS_GRID_WORDS);
    gridWrongWords = new GridFrame(ROWS, COLS_GRID_WORDS);
    gridButtons = new GridFrame(ROWS, COLS_GRID_BUTTONS);

    mainTitle = new Label(MAIN_TITLE);
    acceptedWords = new Label(ACCEPTED_WORDS);
    refusedWords = new Label(REFUSED_WORDS);
    timerText = new Label(TIMER_TEXT);

    this.initializeWordsLabels();

    stopAnalysisButton = new Button(STOP_ANALYSIS_BUTTON);
    cancelButton = new Button(BACK_TO_HOME_BUTTON);

    addAllEventListeners();

    // ***** gridButtons ***** //
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

    FrameHandler.showMainGridContainer(gridContainer);
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
    stopAnalysisButton.attachActionListenerToButton(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          RemoteObjectContextProvider
          .game
          .completedReviewBefore();
        } catch(RemoteException exc) {
          exc.printStackTrace();
        }
        CompletableFuture.runAsync(() -> {
          redirectToWaitingPlayersFrame();
        });
      }
    });
  }

  /**
   * This method attaches listeners to the Labels words both right and wrong
   */
  private void initializeWordsLabels() {
    this.correctWordsLabels = new Label[correctWords.size()];
    this.wrongWordsLabels = new Label[wrongWords.size()];

    attachEventListenersToLabels(correctWords, this.correctWordsLabels, gridCorrectWords);

    attachEventListenersToLabels(wrongWords, this.wrongWordsLabels, gridWrongWords);
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
   * This method displays on screen the GameWinner section
   *
   * @param usernameWinner - username of the winner
   */
  public static void redirectToGameWinnerFrame(String usernameWinner) {
    GameWinner gameWinner = new GameWinner(usernameWinner);
  }

  /**
   * This method displays on screen the WaitingPlayers section
   */
  private void redirectToWaitingPlayersFrame() {
    WaitingPlayers waitingPlayers = new WaitingPlayers(WaitingPlayers.WORDS_ANALYSIS);
  }

  /**
   * This method displays on screen the Home section
   */
  public static void redirectToHomeFrame() {
    Home home = new Home();
  }

  /**
   * This method attaches listeners to the Labels words both right and wrong
   *
   * @param list - list of words to display (accepted or not)
   * @param labels - list of labels to attach listeners on
   * @param grid - grid which contains these labels
   */
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