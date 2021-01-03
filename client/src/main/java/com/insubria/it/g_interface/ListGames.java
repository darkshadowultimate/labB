package com.insubria.it.g_interface;

import com.insubria.it.context.RemoteObjectContextProvider;
import com.insubria.it.g_components.*;
import com.insubria.it.models.SingleGame;
import com.insubria.it.serverImplClasses.MonitorClientImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

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
    private static final String GET_LIST_GAMES_ERROR_TEXT = "Ops... Sembra che ci sia stato un errore nel caricare le partite";
    private static final int ROWS = 0;
    private static final int COLS_CONTAINER = 1;
    private static final int COLS_TABLE_GAMES = 6;
    private static final int COLS_BUTTONS = 4;

    private SingleGame[] singleGames;
    private Label[] gameNameText, dateText, maxPlayersText, currentPlayersText, playersText, gameStatusText;
    private Label mainTitle;
    private Button viewOpenGamesButton, viewStartedGamesButton, joinGameButton, cancelButton;
    private GridFrame gridContainer, gridTableGames, gridButtons;

    public ListGames() {
        String[] gamesArrayString = getListOfGamesFromServer();

        //TODO: handle the case when gamesArrayString is null by showing a label like: "Al momento non ci sono partite disponibili"

        int lengthListGames = gamesArrayString.length + 1;

        singleGames = createListOfSingleGames(gamesArrayString);


        gameNameText = new Label[lengthListGames];
        dateText = new Label[lengthListGames];
        maxPlayersText = new Label[lengthListGames];
        currentPlayersText = new Label[lengthListGames];
        playersText = new Label[lengthListGames];
        gameStatusText = new Label[lengthListGames];

        gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS_CONTAINER);
        gridTableGames = new GridFrame(ROWS, COLS_TABLE_GAMES);
        gridButtons = new GridFrame(ROWS, COLS_BUTTONS);

        mainTitle = new Label(MAIN_TITLE);

        gameNameText[0] = new Label(GAME_NAME_TEXT);
        dateText[0] = new Label(DATE_TEXT);
        maxPlayersText[0] = new Label(MAX_PLAYERS_TEXT);
        currentPlayersText[0] = new Label(CURRENT_PLAYERS_TEXT);
        playersText[0] = new Label(PLAYERS_TEXT);
        gameStatusText[0] = new Label(GAME_STATUS_TEXT);

        viewOpenGamesButton = new Button(OPEN_GAMES_BUTTON);
        viewStartedGamesButton = new Button(STARTED_GAMES_BUTTON);
        joinGameButton = new Button(JOIN_BUTTON);
        cancelButton = new Button("CANCEL");

        addAllEventListeners();

        for(int i = 1; i < lengthListGames; i++) {
            gameNameText[i] = new Label("??");
            dateText[i] = new Label(singleGames[i - 1].getDateCreation());
            maxPlayersText[i] = new Label(singleGames[i - 1].getMaxPlayers());
            currentPlayersText[i] = new Label("??");
            playersText[i] = new Label("??");
            gameStatusText[i] = new Label("??");
        }

        for(int i = 0; i < lengthListGames; i++) {
            gridTableGames.addToView(gameNameText[i]);
            gridTableGames.addToView(dateText[i]);
            gridTableGames.addToView(maxPlayersText[i]);
            gridTableGames.addToView(currentPlayersText[i]);
            gridTableGames.addToView(playersText[i]);
            gridTableGames.addToView(gameStatusText[i]);
        }

        gridButtons.addToView(viewOpenGamesButton);
        gridButtons.addToView(viewStartedGamesButton);
        gridButtons.addToView(joinGameButton);
        gridButtons.addToView(cancelButton);

        gridContainer.addToView(mainTitle);
        gridContainer.addToView(gridTableGames);
        gridContainer.addToView(gridButtons);

        gridContainer.showWindow(1200, 500);
    }

    private SingleGame[] createListOfSingleGames(String[] stringsOfSingleGames) {
        SingleGame[] gamesArray = new SingleGame[stringsOfSingleGames.length];
        for(int i = 0; i < stringsOfSingleGames.length; i++) {
            gamesArray[i] = SingleGame.createSingleGameFromString(stringsOfSingleGames[i]);
        }
        return gamesArray;
    }

    private String[] getListOfGamesFromServer() {
        final String[][] listOfGames = {null};

        try {
            RemoteObjectContextProvider
            .server
            .getListOfGames(new MonitorClientImpl() {
                @Override
                public void confirmGetListOfGames(String[] result) throws RemoteException {
                    super.confirmGetListOfGames(result);

                    for(String singleResult : result) {
                        System.out.println(singleResult);
                    }
                    listOfGames[0] = result;
                }

                @Override
                public void errorGetListOfGames(String reason) throws RemoteException {
                    super.errorGetListOfGames(reason);

                    JOptionPane.showMessageDialog(null, GET_LIST_GAMES_ERROR_TEXT);
                }
            }, "open");
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }

        return listOfGames[0];
    }

    private void addAllEventListeners() {
        viewOpenGamesButton.attachActionListenerToButton(new ActionListener() {
            public void actionPerformed(ActionEvent me) {

            }
        });
        viewStartedGamesButton.attachActionListenerToButton(new ActionListener() {
            public void actionPerformed(ActionEvent me) {

            }
        });
        joinGameButton.attachActionListenerToButton(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
        cancelButton.attachActionListenerToButton(new ActionListener() {
            public void actionPerformed(ActionEvent me) {
                redirectToHomeFrame();
            }
        });
    }

    private void redirectToHomeFrame() {
        Home home = new Home();
        gridContainer.disposeFrame();
    }
}