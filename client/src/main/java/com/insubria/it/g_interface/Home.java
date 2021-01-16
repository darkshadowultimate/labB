package com.insubria.it.g_interface;

import com.insubria.it.g_components.*;
import com.insubria.it.context.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Home {
  private static final String[] BUTTONS_TEXTS = {
    "Organizza Partita",
    "Visualizza Partite",
    "Modifica Profilo",
    "Analizza Statistiche"
  };
  private static final String TITLE_WINDOW = "Il Paroliere - Home";
  private static final int COLS_MAIN_CONTAINER = 1;
  private static final int ROWS = 0;
  private static final int COLS = 2;

  private String mainTitle;
  private Button createGameButton, viewGamesButton, editProfileButton, analyticsButton;
  private Label titlePage;
  private GridFrame gridButtonFrame, gridContainer;

  public Home() {
    // The mainTitle needs to be placed inside the constructor to update its value after the user's profile update
    mainTitle = String.format(
      "Ciao %s, benvenuto nel Paroliere",
      PlayerContextProvider.getUsernamePlayer()
    );

    gridButtonFrame = new GridFrame(ROWS, COLS);

    gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS_MAIN_CONTAINER);

    titlePage = new Label(mainTitle);
    createGameButton = new Button(BUTTONS_TEXTS[0]);
    viewGamesButton = new Button(BUTTONS_TEXTS[1]);
    editProfileButton = new Button(BUTTONS_TEXTS[2]);
    analyticsButton = new Button(BUTTONS_TEXTS[3]);

    addAllEventListeners();

    gridContainer.addToView(titlePage);

    gridButtonFrame.addToView(createGameButton);
    gridButtonFrame.addToView(viewGamesButton);
    gridButtonFrame.addToView(editProfileButton);
    gridButtonFrame.addToView(analyticsButton);

    gridContainer.addToView(gridButtonFrame);

    gridContainer.showWindow();
  }

  private void addAllEventListeners() {
    createGameButton.attachActionListenerToButton(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        redirectToCreateNewGameFrame();
      }
    });
    viewGamesButton.attachActionListenerToButton(new ActionListener() {
      public void actionPerformed(ActionEvent me) {
        redirectToListGamesFrame();
      }
    });
    editProfileButton.attachActionListenerToButton(new ActionListener() {
      public void actionPerformed(ActionEvent me) {
        redirectToUserRegistrationFrame();
      }
    });
    analyticsButton.attachActionListenerToButton(new ActionListener() {
      public void actionPerformed(ActionEvent me) {
        redirectToCreateNewGameFrame();
      }
    });
  }

  private void redirectToCreateNewGameFrame() {
    CreateNewGame createNewGame = new CreateNewGame();
    gridContainer.disposeFrame();
  }

  private void redirectToListGamesFrame() {
    ListGames listGames = new ListGames();
    gridContainer.disposeFrame();
  }

  private void redirectToUserRegistrationFrame() {
    UserRegistration userRegistration = new UserRegistration(true);
    gridContainer.disposeFrame();
  }
}