package com.insubria.it.g_interface;

import com.insubria.it.g_components.*;

public class ListGames {
    private static final String TITLE_WINDOW = "Il Paroliere - Lista partite";
    private static final String MAIN_TITLE = "Visualizzazione delle partite";
    private static final String GAME_NAME_TEXT = "Nome partita";
    private static final String DATE_TEXT = "Data & ora creazione";
    private static final String MAX_PLAYERS_TEXT = "N° max giocatori";
    private static final String CURRENT_PLAYERS_TEXT = "N° giocatori attuali";
    private static final String PLAYERS_TEXT = "Giocatori";
    private static final String GAME_STATUS_TEXT = "Stato partita";
    private static final String JOIN_BUTTON = "PARTECIPA";
    private static final int ROWS = 0;
    private static final int COLS_CONTAINER = 1;
    private static final int COLS_TABLE_GAMES = 6;
    private static final int COLS_BUTTONS = 2;

    private Label mainTitle, gameNameText, dateText, maxPlayersText, currentPlayersText, playersText, gameStatusText;
    private Button joinGameButton, cancelButton;
    private GridFrame gridContainer, gridTableGames, gridButtons;

    public ListGames() {
        gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS_CONTAINER);
        gridTableGames = new GridFrame(ROWS, COLS_TABLE_GAMES);
        gridButtons = new GridFrame(ROWS, COLS_BUTTONS);

        mainTitle = new Label(MAIN_TITLE);

        gameNameText = new Label(GAME_NAME_TEXT);
        dateText = new Label(DATE_TEXT);
        maxPlayersText = new Label(MAX_PLAYERS_TEXT);
        currentPlayersText = new Label(CURRENT_PLAYERS_TEXT);
        playersText = new Label(PLAYERS_TEXT);
        gameStatusText = new Label(GAME_STATUS_TEXT);

        joinGameButton = new Button(JOIN_BUTTON);
        cancelButton = new Button("CANCEL");

        gridTableGames.addToView(gameNameText);
        gridTableGames.addToView(dateText);
        gridTableGames.addToView(maxPlayersText);
        gridTableGames.addToView(currentPlayersText);
        gridTableGames.addToView(playersText);
        gridTableGames.addToView(gameStatusText);

        gridButtons.addToView(joinGameButton);
        gridButtons.addToView(cancelButton);

        gridContainer.addToView(mainTitle);
        gridContainer.addToView(gridTableGames);
        gridContainer.addToView(gridButtons);

        gridContainer.showWindow(1200, 500);
    }
}