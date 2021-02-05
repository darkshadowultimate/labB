package com.insubria.it.sharedserver.threads.gameThread.abstracts;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

import java.util.ArrayList;

import com.insubria.it.sharedserver.threads.gameThread.interfaces.GameClient;

/**
 * The interface Game defines the signatures of the methods that will be
 * implemented and available in the GameThread class. It extends the Remote interface because all the
 * methods are remote. This is the interface used by the
 * client to communicate with the remote object that handles the game.
 */
public interface Game extends Remote {

    /**
     * The signature of the createNewGame method This method is implemented in the
     * GameThread class
     */
    void createNewGame() throws SQLException, RemoteException;

    /**
     * The signature of the addNewPlayer method This method is implemented in the
     * GameThread class
     */
    void addNewPlayer(GameClient player) throws RemoteException;

    /**
     * The signature of the removePlayerNotStartedGame method This method is implemented
     * in the GameThread class
     */
    void removePlayerNotStartedGame(GameClient player) throws RemoteException;

    /**
     * The signature of the removePlayerInGame method This method is implemented in the
     * GameThread class
     */
    void removePlayerInGame(GameClient player) throws RemoteException;

    /**
     * The signature of the askForWordDefinition method This method is implemented in
     * the GameThread class
     */
    void askForWordDefinition(GameClient player, String word) throws RemoteException;

    /**
     * The signature of the completedReviewBefore method This method is implemented in
     * the GameThread class
     */
    void completedReviewBefore() throws RemoteException;
}