package com.insubria.it.context;


import com.insubria.it.serverImplClasses.GameClientImpl;

import java.rmi.RemoteException;

public class GameContextProvider {
    private static GameClientImpl gameClientReference = null;

    public static void initGameClientReference() {
        String username = PlayerContextProvider.getUsernamePlayer();
        String email = PlayerContextProvider.getEmailPlayer();

        try {
            gameClientReference = new GameClientImpl(username, email);
        } catch(RemoteException exc) {}
    }

    public static GameClientImpl getGameClientReference() {
        return gameClientReference;
    }
}
