package com.insubria.it.server.threads.monitorThread.interfaces;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;


public interface MonitorClient extends Remote {
    void confirmMoreScoreGameAndSession (String[] result) throws RemoteException;

    void errorMoreScoreGameAndSession (String reason) throws RemoteException;

    void confirmMoreSessionsPlayed (String result) throws RemoteException;

    void errorMoreSessionsPlayed (String reason) throws RemoteException;

    void confirmMoreAvgScoreGameAndSession (String[] result) throws RemoteException;

    void errorMoreAvgScoreGameAndSession (String reason) throws RemoteException;

    void confirmMoreProposedDuplicatedWords (String result) throws RemoteException;

    void errorMoreProposedDuplicatedWords (String reason) throws RemoteException;

    void confirmMoreInvalidProposedWords (String result) throws RemoteException;

    void errorMoreInvalidProposedWords (String reason) throws RemoteException;

    void confirmValidWordsOccurrences (String[] result) throws RemoteException;

    void errorValidWordsOccurrences (String reason) throws RemoteException;

    void confirmWordHighestScore (String[] result) throws RemoteException;

    void errorWordHighestScore (String reason) throws RemoteException;

    void confirmAverageRounds (String[] result) throws RemoteException;

    void errorAverageRounds (String reason) throws RemoteException;

    void confirmMinMaxRounds (String[] result) throws RemoteException;

    void errorMinMaxRounds (String reason) throws RemoteException;

    void confirmCharactersAvgOccurrence (HashMap<Character, Double> result) throws RemoteException;

    void errorCharactersAvgOccurrence (String reason) throws RemoteException;

    void confirmDefinitionRequest (String[] result) throws RemoteException;

    void errorDefinitionRequest (String reason) throws RemoteException;

    void confirmGameDefinitionRequest (String[] result) throws RemoteException;

    void errorGameDefinitionRequest (String reason) throws RemoteException;


    void confirmGetListOfGames (String[] result) throws RemoteException;

    void errorGetListOfGames (String reason) throws RemoteException;

    void confirmGetListOfPlayersForGame (String[] result) throws RemoteException;

    void errorGetListOfPlayersForGame (String reason) throws RemoteException;
}