package com.insubria.it.g_interface;

import com.insubria.it.context.GameContextProvider;
import com.insubria.it.context.RemoteObjectContextProvider;
import com.insubria.it.g_components.*;
import com.insubria.it.helpers.FrameHandler;
import com.insubria.it.models.SingleGame;
import com.insubria.it.serverImplClasses.MonitorClientImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

/**
 * The ListGames class creates the ListGames frame to allow the user
 * to see the list of games in open and playing status
 */
public class ListGames {
    /**
     * Static text that will be used with some UI components to communicate with the user
     */
    private static final String TITLE_WINDOW = "Il Paroliere - Lista partite";
    private static final String MAIN_TITLE = "Visualizzazione delle partite";
    private static final String GAME_NAME_TEXT = "Nome partita";
    private static final String DATE_TEXT = "Data & ora creazione";
    private static final String MAX_PLAYERS_TEXT = "N° max giocatori";
    private static final String CURRENT_PLAYERS_TEXT = "N° giocatori attuali";
    private static final String PLAYERS_TEXT = "Giocatori";
    private static final String GAME_STATUS_TEXT = "Stato partita";
    private static final String OPEN_GAMES_BUTTON = "Visualizza partite aperte";
    private static final String STARTED_GAMES_BUTTON = "Visualizza partite in corso";
    private static final String JOIN_BUTTON = "PARTECIPA";
    private static final String NO_GAMES_AVAILABLE = "Al momento non ci sono partite disponibili";
    /**
     * Rows for the grid container (0 stands for: unlimited number of rows)
     */
    private static final int ROWS = 0;
    /**
     * Columns for the grid container (only one element for row)
     */
    private static final int COLS_CONTAINER = 1;
    private static final int COLS_TITLES = 7;
    private static final int COLS_BUTTONS = 4;

    /**
     * gameUserMatrix - matrix composed by game info and list of players partecipating to that game
     */
    private String[][] gameUserMatrix;
    /**
     * singleGames - array of games fetched from server
     */
    private SingleGame[] singleGames;
    /**
     * Labels to communicate with the user what he's looking at
     */
    private Label[] gameNameText, dateText, maxPlayersText, currentPlayersText, playersText, gameStatusText;
    private Label mainTitle, noGamesAvailableLabel;
    /**
     * joinGameButton - array of buttons placed in every row to allow the user to participate to the game
     */
    private ButtonWithData[] joinGameButton;
    /**
     * viewOpenGamesButton - show all games with status open
     * viewStartedGamesButton - show all games with status playing
     * homeButton - Redirect user to the Home frame
     */
    private Button viewOpenGamesButton, viewStartedGamesButton, homeButton;
    /**
     * Grid containers to handle UI elements visualization
     */
    private static GridFrame gridContainer;
    private GridFrame gridTableGames, gridButtons;

    /**
     * Constructor of the class (creates the frame and its visual components)
     *
     * @param gameStatus - "open" or "playing"
     */
    public ListGames(String gameStatus) {
        getListOfGamesFromServer(gameStatus).join();

        singleGames = createListOfSingleGames(gameUserMatrix, gameStatus);

        boolean noGamesFetched = singleGames == null;

        gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS_CONTAINER);
        gridTableGames = new GridFrame(ROWS, COLS_TITLES);
        gridButtons = new GridFrame(ROWS, COLS_BUTTONS);

        mainTitle = new Label(MAIN_TITLE);

        if(noGamesFetched) {
            noGamesAvailableLabel = new Label(NO_GAMES_AVAILABLE);
        } else {
            createListGameTable(gameStatus);
        }

        viewOpenGamesButton = new Button(OPEN_GAMES_BUTTON);
        viewStartedGamesButton = new Button(STARTED_GAMES_BUTTON);
        homeButton = new Button(Button.BACK_TO_HOME);

        addAllEventListeners();

        gridButtons.addToView(viewOpenGamesButton);
        gridButtons.addToView(viewStartedGamesButton);
        gridButtons.addToView(homeButton);

        gridContainer.addToView(mainTitle);
        gridContainer.addToView(noGamesFetched ? noGamesAvailableLabel : gridTableGames);
        gridContainer.addToView(gridButtons);

        FrameHandler.showMainGridContainerWithSizes(gridContainer, 500, 1200);
    }

    /**
     * This method update the UI in order to show the games fetched from the server
     *
     * @param gameStatus - "open" or "playing"
     */
    private void createListGameTable(String gameStatus) {
        int lengthListGames = singleGames.length + 1;

        gameNameText = new Label[lengthListGames];
        dateText = new Label[lengthListGames];
        maxPlayersText = new Label[lengthListGames];
        currentPlayersText = new Label[lengthListGames];
        playersText = new Label[lengthListGames];
        gameStatusText = new Label[lengthListGames];
        joinGameButton = new ButtonWithData[lengthListGames];

        gameNameText[0] = new Label(GAME_NAME_TEXT);
        dateText[0] = new Label(DATE_TEXT);
        maxPlayersText[0] = new Label(MAX_PLAYERS_TEXT);
        currentPlayersText[0] = new Label(CURRENT_PLAYERS_TEXT);
        playersText[0] = new Label(PLAYERS_TEXT);
        gameStatusText[0] = new Label(GAME_STATUS_TEXT);

        for(int i = 1; i < lengthListGames; i++) {
            gameNameText[i] = new Label(singleGames[i - 1].getNameGame());
            dateText[i] = new Label(singleGames[i - 1].getDateCreation());
            maxPlayersText[i] = new Label(singleGames[i - 1].getMaxPlayers());
            currentPlayersText[i] = new Label(singleGames[i - 1].getCurrentNumPlayers());
            playersText[i] = new Label(singleGames[i - 1].getPlayers());
            gameStatusText[i] = new Label(singleGames[i - 1].getStatus());

            if(gameStatus.equals("open")) {
                joinGameButton[i] = new ButtonWithData(JOIN_BUTTON, singleGames[i - 1].getId());

                final String idGameCopy = singleGames[i - 1].getId();

                joinGameButton[i].attachActionListenerToButton(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        RemoteObjectContextProvider.setGameRemoteObject(idGameCopy);

                        CompletableFuture.runAsync(() -> {
                            try {
                                RemoteObjectContextProvider
                                .game
                                .addNewPlayer(GameContextProvider.getGameClientReference());
                            } catch (RemoteException exc) {
                                exc.printStackTrace();
                            }
                        });
                    }
                });
            }
        }

        gridTableGames.addToView(gameNameText[0]);
        gridTableGames.addToView(dateText[0]);
        gridTableGames.addToView(maxPlayersText[0]);
        gridTableGames.addToView(currentPlayersText[0]);
        gridTableGames.addToView(playersText[0]);
        gridTableGames.addToView(gameStatusText[0]);
        gridTableGames.addToView(new Label(""));

        for(int i = 1; i < lengthListGames; i++) {
            gridTableGames.addToView(gameNameText[i]);
            gridTableGames.addToView(dateText[i]);
            gridTableGames.addToView(maxPlayersText[i]);
            gridTableGames.addToView(currentPlayersText[i]);
            gridTableGames.addToView(playersText[i]);
            gridTableGames.addToView(gameStatusText[i]);
            if(gameStatus.equals("open")) {
                gridTableGames.addToView(joinGameButton[i]);
            }
        }
    }

    /**
     * This method returns and array of SingleGame, extracting info from matrix of String
     *
     * @param matrix - matrix of games with their relative participants
     * @param gameStatus - "open" or "playing"
     */
    private SingleGame[] createListOfSingleGames(String[][] matrix, String gameStatus) {
        if(matrix == null) {
            return null;
        }

        int matrixLength = matrix[0].length;

        ArrayList<SingleGame> validGamesOpen = new ArrayList<SingleGame>();

        for(int i = 0; i < matrixLength; i++) {
            SingleGame tmpSingleGame = SingleGame.createSingleGameFromString(matrix[0][i], matrix[1][i]);

            if(gameStatus.equals("playing")) {
                validGamesOpen.add(tmpSingleGame);
            } else if(Integer.parseInt(tmpSingleGame.getMaxPlayers()) > Integer.parseInt(tmpSingleGame.getCurrentNumPlayers())) {
                validGamesOpen.add(tmpSingleGame);
            }
        }

        if(validGamesOpen.size() == 0) {
            return null;
        }

        singleGames = new SingleGame[validGamesOpen.size()];

        return validGamesOpen.toArray(singleGames);
    }

    /**
     * Fetch list of games (matrix String[][]) from server
     *
     * @param gameStatus - "open" or "playing"
     */
    private CompletableFuture getListOfGamesFromServer(String gameStatus) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                RemoteObjectContextProvider
                .server
                .getListOfGames(new MonitorClientImpl() {
                    @Override
                    public void confirmGetListOfGames(String[][] result) throws RemoteException {
                        super.confirmGetListOfGames(result);

                        gameUserMatrix = result;
                    }

                    @Override
                    public void errorGetListOfGames(String reason) throws RemoteException {
                        super.errorGetListOfGames(reason);

                        System.out.println("Error function called by the server:\n" + reason);

                        //JOptionPane.showMessageDialog(null, GET_LIST_GAMES_ERROR_TEXT);
                    }
                }, gameStatus);
            } catch(RemoteException exc) {
                exc.printStackTrace();
            }

            try {
                Thread.sleep(800);
            } catch(InterruptedException exc) {
                exc.printStackTrace();
            }
            return 0;
        });
    }

    /**
     * This method defines and attaches all ActionListeners to the appropriate UI elements
     */
    private void addAllEventListeners() {
        viewOpenGamesButton.attachActionListenerToButton(new ActionListener() {
            public void actionPerformed(ActionEvent me) {
                redirectToNewListGame("open");
            }
        });
        viewStartedGamesButton.attachActionListenerToButton(new ActionListener() {
            public void actionPerformed(ActionEvent me) {
                redirectToNewListGame("playing");
            }
        });
        homeButton.attachActionListenerToButton(new ActionListener() {
            public void actionPerformed(ActionEvent me) {
                redirectToHomeFrame();
            }
        });
    }

    /**
     * This method displays on screen the Home section
     */
    private void redirectToHomeFrame() {
        Home home = new Home();
    }

    /**
     * This method displays on screen the WaitingPlayers section
     */
    public static void redirectToWaitingPlayersFrame() {
        WaitingPlayers waitingPlayers = new WaitingPlayers(WaitingPlayers.START_GAME);
    }

    /**
     * This method displays on screen the ListGames section showing games with status "playing"
     */
    private void redirectToNewListGame(String statusGames) {
        ListGames listGames = new ListGames(statusGames);
    }
}