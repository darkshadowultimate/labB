package com.insubria.it.server.threads.gameThread.utils;


import java.io.Serializable;


/**
 * This class is an util class that will be used to send words discovere on each game. Each of the instance of the WordRecord class will represent a word discovere, with the username of the user that discovered it and the score reached by the word.
 * This class is serializable.
 */
public class WordRecord implements Serializable {
    /**
     * The version used for the serialization (shared with the client to avoid exceptions)
     */
    private static final long serialVersionUID = 1L;

    /**
     * The word that the instance represent
     */
    private String word;

    /**
     * The username that proposed the word
     */
    private String username;

    /**
     * The score the word reached
     */
    private int score;

    /**
     * If the score is 0, this represents the reason of the zero score
     */
    private String reasonRefused;

    /**
     * Constructor used when the score is > 0, so no refused reason to give
     * 
     * @param word - The word
     * @param username - The username of the user that proposed the word
     * @param score - The score the word reached
     */
    public WordRecord (String word, String username, int score) {
        this.word = word;
        this.username = username;
        this.score = score;
    }

    /**
     * Constructor used when the score is = 0, reason need to be given
     * 
     * @param word - The word
     * @param username - The username of the user that proposed the word
     * @param score - The score the word reached
     * @param reasonRefused - The reason of the zero score
     */
    public WordRecord (String word, String username, int score, String reasonRefused) {
        this.word = word;
        this.username = username;
        this.score = score;
        this.reasonRefused = reasonRefused;
    }

    /**
     * Method to get the value of the word attribute
     */
    public String getWord () { return this.word; }

    /**
     * Method to get the value of the username attribute
     */
    public String getUsername () { return this.username; }

    /**
     * Method to get the value of the score attribute
     */
    public int getScore () { return this.score; }

    /**
     * Method to get the value of the reasonRefused attribute
     */
    public String getReason () { return (this.reasonRefused == null) ? "" : this.reasonRefused; }
}
