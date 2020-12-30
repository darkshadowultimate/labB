package com.insubria.it.context;

import com.insubria.it.sharedserver.threads.gameThread.abstracts.Game;

public class GameContextProvider {
    private static Game gameReference;

    public static void setGameReference(Game game) {
        gameReference = game;
    }

    public static Game getGameReference() {
        return gameReference;
    }
}
