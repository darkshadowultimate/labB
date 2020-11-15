package com.insubria.it.server.threads.gameThread;


import java.util.ArrayList;

import com.insubria.it.server.base.abstracts.Database;

import com.insubria.it.server.threads.gameThread.abstracts.Game;
import com.insubria.it.server.threads.gameThread.interfaces.GameClient;


public class GameThread extends Game implements Runnable {
    private int idGame;

    private GameClient gameCreator;
    private ArrayList<GameClient> gameClientObservers;

    private Database db;

    public GameThread (GameClient gameCreator) {
        this.gameCreator = gameCreator;
        this.gameClientObservers = new ArrayList<GameClient>();
    }

    protected void createNewGame() throws SQLException {
        System.out.println("Creating a new game...");

    }

    public void run () {

    }
}