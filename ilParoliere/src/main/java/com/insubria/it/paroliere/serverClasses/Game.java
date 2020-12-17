package com.insubria.it.paroliere.serverClasses;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;


public abstract class Game extends UnicastRemoteObject implements Remote {
    public Game() throws RemoteException {}

    private static final long serialVersionUID = 1L;

    protected abstract void createNewGame () throws SQLException, RemoteException;

    protected abstract void addNewPlayer (GameClient player) throws RemoteException;

    protected abstract void removePlayerNotStartedGame (GameClient player) throws RemoteException;
}