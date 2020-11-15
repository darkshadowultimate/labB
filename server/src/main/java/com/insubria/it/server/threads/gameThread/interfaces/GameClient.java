package com.insubria.it.server.threads.gameThread.interfaces;


import java.rmi.Remote;
import java.rmi.RemoteException;


import com.insubria.it.server.threads.gameThread.abstracts.Game;


public interface GameClient extends Remote {
    void confirmCreateNewGame (Game gameThread) throws RemoteException;

    void errorCreateNewGame (String reason) throws RemoteException;
}