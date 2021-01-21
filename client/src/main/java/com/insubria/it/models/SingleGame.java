package com.insubria.it.models;

public class SingleGame {
    private String id;
    private String dateCreation;
    private String maxPlayers;
    private String currentNumPlayers;
    private String players;
    private String status;

    public SingleGame(String id, String dateCreation, String maxPlayers, String currentNumPlayers, String players, String status) {
        this.id = id;
        this.dateCreation = dateCreation;
        this.maxPlayers = maxPlayers;
        this.currentNumPlayers = currentNumPlayers;
        this.players = players;
        this.status = status;
    }

    public static SingleGame createSingleGameFromString(String singleGameString, String playersGame) {
        String[] gameFields = singleGameString.split("//");
        return new SingleGame(
            gameFields[0],
            gameFields[1],
            gameFields[2],
            gameFields[3],
            playersGame,
            gameFields[5]
        );
    }

    public String getId() {
        return id;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public String getMaxPlayers() {
        return maxPlayers;
    }

    public String getCurrentNumPlayers() {
        return currentNumPlayers;
    }

    public String getPlayers() {
        return players;
    }

    public String getStatus() {
        return status;
    }
}
