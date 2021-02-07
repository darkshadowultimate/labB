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

public class CreateNewGame {
  // Constants
  private static final String TITLE_WINDOW = "Il Paroliere - Crea partita";
  private static final String TITLE_TOP = "Inserisci i dati della partita";
  private static final String NAME_GAME_LABEL = "Nome partita";
  private static final String NUM_PLAYERS_LABEL = "N° giocatori (2 - 6)";
  private static final String CREATE_GAME_BUTTON_TEXT = "Organizza partita";
  private static final String FIELDS_ERROR_TEXT = "Tutti i campi devono essere compilati.\nIl numero di giocatori deve essere compreso fra 2 e 6";
  private static final int ROWS = 0;
  private static final int COLS = 1;
  // Variables
  private Label titleLabel;
  private InputLabel nameGame, numPlayers;
  private Button createGameButton, backToHome;
  private static GridFrame gridFrame;

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

  private boolean checkInputFields(String nameGame, int numMaxPlayers) {
    return
      Validation.isFieldFilled(nameGame) &&
      (numMaxPlayers > 1 && numMaxPlayers < 7);
  }

  public static void redirectToWaitingRoomFrame() {
    WaitingPlayers waitingPlayers = new WaitingPlayers(WaitingPlayers.START_GAME);
  }

  public static void redirectToHomeFrame() {
    Home home = new Home();
  }
}