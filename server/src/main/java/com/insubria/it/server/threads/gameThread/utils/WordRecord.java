package com.insubria.it.server.threads.gameThread.utils;


import java.io.Serializable;


public class WordRecord implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Sring word;
    private String username;
    private int score;

    private String reasonRefused;

    public WordRecord (String word, String username, int score) {
        this.word = word;
        this.username = username;
        this.score = score;
    }

    public WordRecord (String word, String username, int score, String reasonRefused) {
        this.word = word;
        this.username = username;
        this.score = score;
        this.reasonRefused = reasonRefused;
    }

    public String getWord () { return this.word; }

    public String getUsername () { return this.username; }

    public int getScore () { return this.score; }

    public String getReason () { return (this.reasonRefused == null) ? "" : this.reasonRefused; }
}
