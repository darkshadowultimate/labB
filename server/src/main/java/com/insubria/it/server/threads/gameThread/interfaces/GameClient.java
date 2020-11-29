package com.insubria.it.server.threads.gameThread.interfaces;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

import com.insubria.it.server.threads.gameThread.abstracts.Game;


public interface GameClient extends Remote {
    String getUsername () throws RemoteException;

    String getEmail () throws RemoteException;


    void confirmCreateNewGame (Game gameThread) throws RemoteException;

    void errorCreateNewGame (String reason) throws RemoteException;

    void confirmAddNewPlayer () throws RemoteException;

    void errorAddNewPlayer (String reason) throws RemoteException;

    void confirmRemovePlayerNotStartedGame () throws RemoteException;

    void errorRemovePlayerNotStartedGame (String reason) throws RemoteException;

    void gameHasBeenRemoved (String reason) throws RemoteException;

    void synchronizePreStartGameTimer (int seconds) throws RemoteException;

    void synchronizeInGameTimer (int seconds) throws RemoteException;

    void synchronizeInWaitTimer (int seconds) throws RemoteException;

    void confirmGameSession (String name, int sessionNumber, String[][] matrix, int playerScore) throws RemoteException;

    void triggerEndOfSession () throws RemoteException;

    void sendWordsDiscoveredInSession (ArrayList<WordRecord> acceptedArray, ArrayList<WordRecord> refusedArray) throws RemoteException;

    void confirmWordDefinitions (String wordDefinitions) throws RemoteException;

    void errorWordDefinitions (String reason) throws RemoteException;

    void gameWonByUser (String username) throws RemoteException;
}