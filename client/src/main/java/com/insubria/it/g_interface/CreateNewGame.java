package com.insubria.it.g_interface;

import com.insubria.it.context.GameContextProvider;
import com.insubria.it.context.PlayerContextProvider;
import com.insubria.it.context.RemoteObjectContextProvider;
import com.insubria.it.g_components.*;
import com.insubria.it.g_components.Button;
import com.insubria.it.g_components.Label;
import com.insubria.it.helpers.FrameHandler;
import com.insubria.it.helpers.Validation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.RemoteException;

/**
 * The CreateNewGame class creates the CreateNewGame frame to allow the user
 * to create a new game by inserting the game's name and max number of players (min 2 - max 6)
 */
public class CreateNewGame {
  /**
   * Static text that will be used with some UI components to communicate with the user
   */
  private static final String TITLE_WINDOW = "Il Paroliere - Crea partita";
  private static final String TITLE_TOP = "Inserisci i dati della partita";
  private static final String NAME_GAME_LABEL = "Nome partita";
  private static final String NUM_PLAYERS_LABEL = "N° giocatori (2 - 6)";
  private static final String CREATE_GAME_BUTTON_TEXT = "Organizza partita";
  private static final String FIELDS_ERROR_TEXT = "Tutti i campi devono essere compilati.\nIl numero di giocatori deve essere compreso fra 2 e 6";
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
  private Label titleLabel;
  /**
   * nameGame - The game's name
   * numPlayers - number of players (min 2 - max 6)
   */
  private InputLabel nameGame, numPlayers;
  /**
   * createGameButton - Create a new game with the information inserted
   * backToHome - Redirect the user to the Home section
   */
  private Button createGameButton, backToHome;
  /**
   * Grid containers to handle UI elements visualization
   */
  private static GridFrame gridFrame;

  /**
   * Constructor of the class (creates the frame and its visual components)
   */
  public CreateNewGame() {
    gridFrame = new GridFrame(TITLE_WINDOW, ROWS, COLS);

    titleLabel = new Label(TITLE_TOP);
    nameGame = new InputLabel(NAME_GAME_LABEL, false);
    numPlayers = new InputLabel(NUM_PLAYERS_LABEL, false);
    createGameButton = new Button(CREATE_GAME_BUTTON_TEXT);
    backToHome = new Button(Button.BACK_TO_HOME);

    addAllEventListeners();

    gridFrame.addToView(titleLabel);
    gridFrame.addToView(nameGame);
    gridFrame.addToView(numPlayers);
    gridFrame.addToView(createGameButton);
    gridFrame.addToView(backToHome);

    FrameHandler.showMainGridContainer(gridFrame);
  }

  /**
   * This method defines and attaches all ActionListeners to the appropriate UI elements
   */
  private void addAllEventListeners() {
    createGameButton.attachActionListenerToButton(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          String nameGameString = nameGame.getValueTextField();
          int numPlayersInt = Integer.parseInt(numPlayers.getValueTextField());

          if(checkInputFields(nameGameString, numPlayersInt)) {
            String userEmail = PlayerContextProvider.getEmailPlayer();
            String username = PlayerContextProvider.getUsernamePlayer();

            try {
              RemoteObjectContextProvider
              .server
              .createNewGame(
                nameGameString,
                numPlayersInt,
                GameContextProvider.getGameClientReference()
              );
            } catch(RemoteException exc) {
              exc.printStackTrace();
            }
            catch(IOException exc) {
              exc.printStackTrace();
            }
          } else {
            JOptionPane.showMessageDialog(null, FIELDS_ERROR_TEXT);
          }
        } catch(NumberFormatException exc) {
          JOptionPane.showMessageDialog(null, FIELDS_ERROR_TEXT);
        }
      }
    });
    backToHome.attachActionListenerToButton(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        redirectToHomeFrame();
      }
    });
  }

  /**
   * This method returns true if game's name is not empty and if the num of players is between 2 and 6; otherwise false
   *
   * @param nameGame Game's name
   * @param numMaxPlayers number of the players that need to participate in order to start the game
   */
  private boolean checkInputFields(String nameGame, int numMaxPlayers) {
    return
      Validation.isFieldFilled(nameGame) &&
      (numMaxPlayers > 1 && numMaxPlayers < 7);
  }

  /**
   * This method displays on screen the WaitingPlayers section
   */
  public static void redirectToWaitingRoomFrame() {
    WaitingPlayers waitingPlayers = new WaitingPlayers(WaitingPlayers.START_GAME);
  }

  /**
   * This method displays on screen the Home section
   */
  public static void redirectToHomeFrame() {
    Home home = new Home();
  }
}