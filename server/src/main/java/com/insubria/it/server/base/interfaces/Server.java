package com.insubria.it.server.base.interfaces;


import com.insubria.it.server.threads.playerThread.interfaces.PlayerCredentials;
import com.insubria.it.server.threads.monitorThread.interfaces.MonitorClient;
import com.insubria.it.server.threads.gameThread.interfaces.GameClient;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface Server extends Remote {
    void createPlayerAccount (
        String name,
        String surname,
        String username,
        String email,
        String password,
        PlayerCredentials player
    ) throws RemoteException;

    void confirmPlayerAccount (String confirmationCode, PlayerCredentials player) throws RemoteException;

    void loginPlayerAccount (
        String email,
        String password,
        PlayerCredentials player
    ) throws RemoteException;

    void resetPlayerPassword (String email, PlayerCredentials player) throws RemoteException;

    void changePlayerData (
        String email,
        String name,
        String surname,
        String username,
        String password,
        String oldPassword,
        PlayerCredentials player
    ) throws RemoteException;


    void moreScoreGameAndSession (MonitorClient monitorClient) throws RemoteException;
    
    void moreSessionsPlayed (MonitorClient monitorClient) throws RemoteException;

    void moreAvgScoreGameAndSession (MonitorClient monitorClient) throws RemoteException;

    void moreProposedDuplicatedWords (MonitorClient monitorClient) throws RemoteException;

    void moreInvalidProposedWords (MonitorClient monitorClient) throws RemoteException;

    void validWordsOccurrences (MonitorClient monitorClient, int page) throws RemoteException;

    void wordHighestScore (MonitorClient monitorClient, int page) throws RemoteException;

    void averageRounds (MonitorClient monitorClient) throws RemoteException;

    void minMaxRounds (MonitorClient monitorClient) throws RemoteException;

    void charactersAvgOccurrence (MonitorClient monitorClient) throws RemoteException;

    void definitionRequest (MonitorClient monitorClient, int page) throws RemoteException;

    void gameDefinitionRequest (MonitorClient monitorClient, int page) throws RemoteException;


    void createNewGame (String name, int maxPlayers, GameClient gameCreator) throws RemoteException, IOException;


    void getListOfGames (MonitorClient monitorClient, String status) throws RemoteException;

    void getListOfPlayersForGame (MonitorClient monitorClient, int id) throws RemoteException;
}
