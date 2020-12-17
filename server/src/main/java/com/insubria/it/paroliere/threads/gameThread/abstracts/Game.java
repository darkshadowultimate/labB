package com.insubria.it.paroliere.threads.gameThread.abstracts;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

import java.util.ArrayList;

import com.insubria.it.paroliere.threads.gameThread.interfaces.GameClient;


/**
 * The abstract class Game defines the signatures of the methods that will be defined and available in the GameThread class.
 * Used an abstract class instead of an interface because of the UnicastRemoteObject extends (and to follow the convention used for other parts)
 * This class is serializable (it extends UnicastRemoteObject).
 */
public abstract class Game extends UnicastRemoteObject implements Remote {
    /**
     * The version used for the serialization (shared with the client to avoid exceptions)
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor due to the extension of UnicastRemoteObject
     */
    public Game () throws RemoteException {}

    /**
     * The signature of the triggerEndOfSessionGameClient method
     * This method is defined in the GameThread class
     */
    public abstract void triggerEndOfSessionGameClient ();

    /**
     * The signature of the createNewGame method
     * This method is defined in the GameThread class
     */
    public abstract void createNewGame () throws SQLException, RemoteException;

    /**
     * The signature of the addNewPlayer method
     * This method is defined in the GameThread class
     */
    public abstract void addNewPlayer (GameClient player) throws RemoteException;

    /**
     * The signature of the removePlayerNotStartedGame method
     * This method is defined in the GameThread class
     */
    public abstract void removePlayerNotStartedGame (GameClient player) throws RemoteException;

    /**
     * The signature of the removePlayerInGame method
     * This method is defined in the GameThread class
     */
    public abstract void removePlayerInGame (GameClient player) throws RemoteException;

    /**
     * The signature of the checkPlayerWords method
     * This method is defined in the GameThread class
     */
    public abstract void checkPlayerWords (GameClient player, ArrayList<String> wordsList) throws RemoteException;

    /**
     * The signature of the askForWordDefinition method
     * This method is defined in the GameThread class
     */
    public abstract void askForWordDefinition (GameClient player, String word) throws RemoteException;
}