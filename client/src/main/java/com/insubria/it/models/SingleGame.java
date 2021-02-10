package com.insubria.it.models;

/**
 * The SingleGame class is the Model of a single game
 */
public class SingleGame {
    /**
     * id of the game
     */
    private String id;
    /**
     * name of the game
     */
    private String nameGame;
    /**
     * date of creation of the game
     */
    private String dateCreation;
    /**
     * max players of the game
     */
    private String maxPlayers;
    /**
     * current number of players waiting to start the game
     */
    private String currentNumPlayers;
    /**
     * list of players waiting
     */
    private String players;
    /**
     * status of the game "open" or "playing"
     */
    private String status;

    /**
     * Constructor of the class (creates the frame and its visual components)
     *
     * @params (The same of the class' attributes)
     */
    public SingleGame(String id, String nameGame, String dateCreation, String maxPlayers, String currentNumPlayers, String players, String status) {
        this.id = id;
        this.nameGame = nameGame;
        this.dateCreation = dateCreation;
        this.maxPlayers = maxPlayers;
        this.currentNumPlayers = currentNumPlayers;
        this.players = players;
        this.status = status;
    }

    /**
     * Returns a SingleGame object which represent the game describes by singleGameString and playersGame
     *
     * @param singleGameString - String will all the game's info to extract
     * @param playersGame - list of players waiting
     */
    public static SingleGame createSingleGameFromString(String singleGameString, String playersGame) {
        String[] gameFields = singleGameString.split("//");
        return new SingleGame(
            gameFields[0],
            gameFields[1],
            gameFields[2],
            gameFields[3],
            gameFields[4],
            playersGame,
            gameFields[5]
        );
    }

    /**
     * returns the id of the game
     */
    public String getId() {
        return id;
    }
    /**
     * returns the name of the game
     */
    public String getNameGame() {
        return nameGame;
    }
    /**
     * returns the date of creation of the game
     */
    public String getDateCreation() {
        return dateCreation;
    }
    /**
     * returns the number (String object for convenience) max players of the game
     */
    public String getMaxPlayers() {
        return maxPlayers;
    }
    /**
     * returns the current number (String object for convenience) of players waiting to start the game
     */
    public String getCurrentNumPlayers() {
        return currentNumPlayers;
    }
    /**
     * returns the list of players waiting
     */
    public String getPlayers() {
        return players;
    }
    /**
     * returns the status of the game "open" or "playing"
     */
    public String getStatus() {
        return status;
    }
}
