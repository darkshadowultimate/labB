package com.insubria.it.client.g_interface;

import com.insubria.it.client.g_components.*;

public class CreateNewGame {
  // Constants
  private static final String TITLE_WINDOW = "Il Paroliere - Crea partita";
  private static final String TITLE_TOP = "Inserisci i dati della partita";
  private static final String NAME_GAME_LABEL = "Nome partita";
  private static final String NUM_PLAYERS_LABEL = "N° giocatori (2 - 6)";
  private static final String CREATE_GAME_BUTTON_TEXT = "Organizza partita";
  private static final int ROWS = 0;
  private static final int COLS = 1;
  // Variables
  private Label titleLabel;
  private InputLabel nameGame, numPlayers;
  private Button createGameButton;
  private GridFrame gridFrame;

  public CreateNewGame() {
    gridFrame = new GridFrame(TITLE_WINDOW, ROWS, COLS);
    
    titleLabel = new Label(TITLE_TOP);
    nameGame = new InputLabel(NAME_GAME_LABEL);
    numPlayers = new InputLabel(NUM_PLAYERS_LABEL);
    createGameButton = new Button(CREATE_GAME_BUTTON_TEXT);

    gridFrame.addToView(titleLabel);
    gridFrame.addToView(nameGame);
    gridFrame.addToView(numPlayers);
    gridFrame.addToView(createGameButton);

    gridFrame.showWindow();
  }
}