package com.insubria.it.models;

public class SingleGame {
    private String id;
    private String dateCreation;
    private String maxPlayers;
    private String rounds;

    public SingleGame(String id, String dateCreation, String maxPlayers, String rounds) {
        this.id = id;
        this.dateCreation = dateCreation;
        this.maxPlayers = maxPlayers;
        this.rounds = rounds;
    }

    public static SingleGame createSingleGameFromString(String singleGameString) {
        String[] gameFields = singleGameString.split("//");
        return new SingleGame(
            gameFields[0],
            gameFields[1],
            gameFields[2],
            gameFields[3]
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

    public String getRounds() {
        return rounds;
    }
}
