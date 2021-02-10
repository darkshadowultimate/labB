package com.insubria.it.g_interface;

import com.insubria.it.g_components.*;
import com.insubria.it.context.*;
import com.insubria.it.helpers.FrameHandler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The Home class creates the Home frame to allow user to choose which action perform
 * (based on the buttons at disposal)
 */
public class Home {
  /**
   * Static text that will be used with some UI components to communicate with the user
   */
  private static final String[] BUTTONS_TEXTS = {
    "Organizza Partita",
    "Visualizza Partite",
    "Modifica Profilo",
    "Analizza Statistiche"
  };
  private static final String TITLE_WINDOW = "Il Paroliere - Home";
  /**
   * Rows for the grid container (0 stands for: unlimited number of rows)
   */
  private static final int ROWS = 0;
  /**
   * Columns for the grid containers
   */
  private static final int COLS_MAIN_CONTAINER = 1;
  private static final int COLS = 2;

  private String mainTitle;
  /**
   * createGameButton - create a game
   * viewGamesButton - see games in open and playing mode
   * editProfileButton - change account information
   * analyticsButton - visualize analytics information
   */
  private Button createGameButton, viewGamesButton, editProfileButton, analyticsButton;
  /**
   * Labels to communicate with the user what he's looking at
   */
  private Label titlePage;
  /**
   * Grid containers to handle UI elements visualization
   */
  private GridFrame gridButtonFrame, gridContainer;

  /**
   * Constructor of the class (creates the frame and its visual components)
   */
  public Home() {
    // The mainTitle needs to be placed inside the constructor to update its value after the user's profile update
    mainTitle = String.format(
      "Ciao %s, benvenuto nel Paroliere",
      PlayerContextProvider.getUsernamePlayer()
    );

    if(GameContextProvider.getGameClientReference() == null) {
      GameContextProvider.initGameClientReference();
    }

    RemoteObjectContextProvider.resetGameRemoteObject();

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

    FrameHandler.showMainGridContainer(gridContainer);
  }

  /**
   * This method defines and attaches all ActionListeners to the appropriate UI elements
   */
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
        redirectToAnalyticsFrame();
      }
    });
  }

  /**
   * This method displays on screen the CreateNewGame section
   */
  private void redirectToCreateNewGameFrame() {
    CreateNewGame createNewGame = new CreateNewGame();
  }

  /**
   * This method displays on screen the ListGames section
   */
  private void redirectToListGamesFrame() {
    ListGames listGames = new ListGames("open");
  }

  /**
   * This method displays on screen the UserRegistration section
   */
  private void redirectToUserRegistrationFrame() {
    UserRegistration userRegistration = new UserRegistration(true);
  }

  /**
   * This method displays on screen the Analytics section
   */
  private void redirectToAnalyticsFrame() {
    Analytics analytics = new Analytics();
  }
}