package com.insubria.it.server.threads.monitorThread.interfaces;


import java.rmi.Remote;
import java.rmi.RemoteException;


public interface MonitorClient extends Remote {
    void confirmMoreSessionsPlayed (String result) throws RemoteException;

    void errorMoreSessionsPlayed (String error) throws RemoteException;

    void confirmMoreProposedDuplicatedWords (String result) throws RemoteException;

    void errorMoreProposedDuplicatedWords (String error) throws RemoteException;

    void confirmMoreInvalidProposedWords (String result) throws RemoteException;

    void errorMoreInvalidProposedWords (String error) throws RemoteException;

    void confirmValidWordsOccurrences (String[] result) throws RemoteException;

    void errorValidWordsOccurrences (String reason) throws RemoteException;

    void confirmWordHighestScore (String[] result) throws RemoteException;

    void errorWordHighestScore (String reason) throws RemoteException;

    void confirmAverageRounds (String[] result) throws RemoteException;

    void errorAverageRounds (String reason) throws RemoteException;

    void confirmMinMaxRounds (String[] result) throws RemoteException;

    void errorMinMaxRounds (String reason) throws RemoteException;

    void confirmDefinitionRequest (String[] result) throws RemoteException;

    void errorDefinitionRequest (String reason) throws RemoteException;

    void confirmGameDefinitionRequest (String[] result) throws RemoteException;

    void errorGameDefinitionRequest (String reason) throws RemoteException;
}