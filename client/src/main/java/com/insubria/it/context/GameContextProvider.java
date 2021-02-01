package com.insubria.it.context;


import com.insubria.it.serverImplClasses.GameClientImpl;
import com.insubria.it.sharedserver.threads.gameThread.abstracts.Game;

import java.rmi.RemoteException;

public class GameContextProvider {
    private static GameClientImpl gameClientReference = null;
    private static Game gameReference = null;

    public static void setGameReference(Game game) {
        gameReference = game;
    }

    public static void initGameClientReference() {
        String username = PlayerContextProvider.getUsernamePlayer();
        String email = PlayerContextProvider.getEmailPlayer();

        try {
            gameClientReference = new GameClientImpl(username, email);
        } catch(RemoteException exc) {}
    }

    public static Game getGameReference() {
        return gameReference;
    }

    public static GameClientImpl getGameClientReference() {
        return gameClientReference;
    }
}
