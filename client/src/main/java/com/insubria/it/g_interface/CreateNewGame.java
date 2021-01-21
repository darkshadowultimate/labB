package com.insubria.it.g_interface;

import com.insubria.it.context.GameContextProvider;
import com.insubria.it.context.PlayerContextProvider;
import com.insubria.it.context.RemoteObjectContextProvider;
import com.insubria.it.g_components.*;
import com.insubria.it.g_components.Button;
import com.insubria.it.g_components.Label;
import com.insubria.it.helpers.Validation;
import com.insubria.it.serverImplClasses.GameClientImpl;
import com.insubria.it.sharedserver.threads.gameThread.abstracts.Game;

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
  private static final String CREATE_GAME_ERROR_TEXT = "Ops.. C'è stato un problema durante la creazione del gioco";
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

    addAllEventListeners();

    gridFrame.addToView(titleLabel);
    gridFrame.addToView(nameGame);
    gridFrame.addToView(numPlayers);
    gridFrame.addToView(createGameButton);

    gridFrame.showWindow();
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
                new GameClientImpl(username, userEmail) {
                  @Override
                  public void confirmCreateNewGame(String gameThreadId) throws RemoteException {
                    super.confirmCreateNewGame(gameThreadId);

                    Game gameThread = RemoteObjectContextProvider.getGameThreadRemoteObect(gameThreadId);

                    GameContextProvider.setGameReference(gameThread);

                    redirectToWaitingRoomFrame();
                  }

                  @Override
                  public void errorCreateNewGame(String reason) throws RemoteException {
                    super.errorCreateNewGame(reason);

                    JOptionPane.showMessageDialog(null, CREATE_GAME_ERROR_TEXT);
                  }
                }
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
  }

  private boolean checkInputFields(String nameGame, int numMaxPlayers) {
    return
      Validation.isFieldFilled(nameGame) &&
      (numMaxPlayers > 1 && numMaxPlayers < 7);
  }

  private void redirectToWaitingRoomFrame() {
    WaitingPlayers waitingPlayers = new WaitingPlayers(true);
    //gridFrame.disposeFrame();
  }
}