package com.insubria.it.server.threads.monitorThread.interfaces;


import java.rmi.Remote;
import java.rmi.RemoteException;


public interface MonitorClient extends Remote {
    void confirmMoreSessionsPlayed (String result) throws RemoteException;

    void errorMoreSessionsPlayed (String error) throws RemoteException;
}