package com.insubria.it.paroliere.serverClasses;


import java.rmi.Remote;
import java.rmi.RemoteException;


public interface GameClient extends Remote {
    String getUsername () throws RemoteException;

    String getEmail () throws RemoteException;


    void confirmCreateNewGame (Game gameThread) throws RemoteException;

    void errorCreateNewGame (String reason) throws RemoteException;

    void confirmAddNewPlayer () throws RemoteException;

    void errorAddNewPlayer (String reason) throws RemoteException;

    void confirmRemovePlayerNotStartedGame () throws RemoteException;

    void errorRemovePlayerNotStartedGame (String reason) throws RemoteException;
}