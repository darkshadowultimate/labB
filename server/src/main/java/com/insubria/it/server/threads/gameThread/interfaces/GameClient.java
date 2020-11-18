package com.insubria.it.server.threads.gameThread.interfaces;


import java.rmi.Remote;
import java.rmi.RemoteException;


import com.insubria.it.server.threads.gameThread.abstracts.Game;


public interface GameClient extends Remote {
    String getUsername () throws RemoteException;

    String getEmail () throws RemoteException;


    void confirmCreateNewGame (Game gameThread) throws RemoteException;

    void errorCreateNewGame (String reason) throws RemoteException;

    void confirmAddNewPlayer () throws RemoteException;

    void errorAddNewPlayer () throws RemoteException;
}