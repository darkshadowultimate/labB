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

    protected abstract void createNewGame () throws SQLException, RemoteException;

    protected abstract void addNewPlayer (GameClient player) throws RemoteException;
}