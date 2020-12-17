package com.insubria.it.paroliere.g_interface;

import com.insubria.it.paroliere.g_components.*;
import com.insubria.it.paroliere.context.*;

public class Home {
  private static final String[] BUTTONS_TEXTS = { "Organizza Partita", "Visualizza Partite", "Modifica Profilo", "Analizza Statistiche" };
  private static final String TITLE_WINDOW = "Il Paroliere - Home";
  private static final String MAIN_TITLE = String.format("Ciao %s, benvenuto nel Paroliere", PlayerContextProvider.getEmail());
  private static final int COLS_MAIN_CONTAINER = 1;
  private static final int ROWS = 0;
  private static final int COLS = 2;

  private GraphicComponent createGameButton, viewGamesButton, editProfileButton, analyticsButton;
  private Label titlePage;
  private GridFrame gridButtonFrame, gridContainer;

  public Home() {
    gridButtonFrame = new GridFrame(ROWS, COLS);

    gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS_MAIN_CONTAINER);

    titlePage = new Label(MAIN_TITLE);
    createGameButton = new Button(BUTTONS_TEXTS[0]);
    viewGamesButton = new Button(BUTTONS_TEXTS[1]);
    editProfileButton = new Button(BUTTONS_TEXTS[2]);
    analyticsButton = new Button(BUTTONS_TEXTS[3]);

    gridContainer.addToView(titlePage);

    gridButtonFrame.addToView(createGameButton);
    gridButtonFrame.addToView(viewGamesButton);
    gridButtonFrame.addToView(editProfileButton);
    gridButtonFrame.addToView(analyticsButton);

    gridContainer.addToView(gridButtonFrame);

    gridContainer.showWindow();
  }
}