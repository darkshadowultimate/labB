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

public class ListGames {
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
    private static final String HOME_BUTTON = "Torna alla Home";
    private static final String NO_GAMES_AVAILABLE = "Al momento non ci sono partite disponibili";
    private static final String GET_LIST_GAMES_ERROR_TEXT = "Ops... Sembra che ci sia stato un errore nel caricare le partite";
    private static final int ROWS = 0;
    private static final int COLS_CONTAINER = 1;
    private static final int COLS_TITLES = 7;
    private static final int COLS_BUTTONS = 4;

    private String[][] gameUserMatrix;
    private SingleGame[] singleGames;
    private Label[] gameNameText, dateText, maxPlayersText, currentPlayersText, playersText, gameStatusText;
    private Label mainTitle, noGamesAvailableLabel;
    private ButtonWithData[] joinGameButton;
    private Button viewOpenGamesButton, viewStartedGamesButton, homeButton;
    private static GridFrame gridContainer;
    private GridFrame gridTableGames, gridButtons;

    public ListGames(String gameStatus) {
        getListOfGamesFromServer(gameStatus).join();

        singleGames = createListOfSingleGames(gameUserMatrix);

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
        homeButton = new Button(HOME_BUTTON);

        addAllEventListeners();

        gridButtons.addToView(viewOpenGamesButton);
        gridButtons.addToView(viewStartedGamesButton);
        gridButtons.addToView(homeButton);

        gridContainer.addToView(mainTitle);
        gridContainer.addToView(noGamesFetched ? noGamesAvailableLabel : gridTableGames);
        gridContainer.addToView(gridButtons);

        FrameHandler.showMainGridContainerWithSizes(gridContainer, 1200, 500);
    }

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

    private SingleGame[] createListOfSingleGames(String[][] matrix) {
        if(matrix == null) {
            return null;
        }

        int matrixLength = matrix[0].length;

        ArrayList<SingleGame> validGamesOpen = new ArrayList<SingleGame>();

        for(int i = 0; i < matrixLength; i++) {
            SingleGame tmpSingleGame = SingleGame.createSingleGameFromString(matrix[0][i], matrix[1][i]);

            if(Integer.parseInt(tmpSingleGame.getMaxPlayers()) > Integer.parseInt(tmpSingleGame.getCurrentNumPlayers())) {
                validGamesOpen.add(tmpSingleGame);
            }
        }

        if(validGamesOpen.size() == 0) {
            return null;
        }

        singleGames = new SingleGame[validGamesOpen.size()];

        return validGamesOpen.toArray(singleGames);
    }

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

                        System.out.println("Error function called by the server");

                        JOptionPane.showMessageDialog(null, GET_LIST_GAMES_ERROR_TEXT);
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

    private void addAllEventListeners() {
        viewOpenGamesButton.attachActionListenerToButton(new ActionListener() {
            public void actionPerformed(ActionEvent me) {
                redirectToNewListGame("open");
            }
        });
        viewStartedGamesButton.attachActionListenerToButton(new ActionListener() {
            public void actionPerformed(ActionEvent me) {
                redirectToNewListGame("started");
            }
        });
        homeButton.attachActionListenerToButton(new ActionListener() {
            public void actionPerformed(ActionEvent me) {
                redirectToHomeFrame();
            }
        });
    }

    private void redirectToHomeFrame() {
        Home home = new Home();
    }

    public static void redirectToWaitingPlayersFrame() {
        WaitingPlayers waitingPlayers = new WaitingPlayers(WaitingPlayers.START_GAME);
    }

    private void redirectToNewListGame(String statusGames) {
        ListGames listGames = new ListGames(statusGames);
    }
}