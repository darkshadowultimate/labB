package com.insubria.it.server.threads.gameThread.abstracts;


import com.insubria.it.server.threads.gameThread.interfaces.GameClient;


import java.rmi.Remote;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;


public abstract class Game extends UnicastRemoteObject implements Remote {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    protected abstract void createNewGame() throws SQLException;
}