package com.insubria.it.server.threads.gameThread.abstracts;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

import com.insubria.it.server.threads.gameThread.interfaces.GameClient;


public abstract class Game extends UnicastRemoteObject implements Remote {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public abstract void triggerEndOfSessionGameClient ();

    public abstract void createNewGame () throws SQLException, RemoteException;

    public abstract void addNewPlayer (GameClient player) throws RemoteException;

    public abstract void removePlayerNotStartedGame (GameClient player) throws RemoteException;

    public abstract void removePlayerInGame (GameClient player) throws RemoteException;

    public abstract void checkPlayerWords (GameClient player, ArrayList<String> wordsList) throws RemoteException;

    public abstract void askForWordDefinition (GameClient player, String word) throws RemoteException;
}