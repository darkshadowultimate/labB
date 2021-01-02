package com.insubria.it.threads.monitorThread.abstracts;

import java.rmi.RemoteException;
import java.sql.SQLException;


/**
 * The abstract class Monitor defines the signatures of the methods that will be
 * defined and available only in the MonitorThread class (because of the
 * protected visibility). Used an abstract class instead of an interface because
 * we want to make these methods protected and not public
 */
public abstract class Monitor {
    /**
     * The signature of the moreScoreGameAndSession method This method is defined in
     * the MonitorThread class
     */
    protected abstract void moreScoreGameAndSession() throws RemoteException, SQLException;

    /**
     * The signature of the moreSessionsPlayed method This method is defined in the
     * MonitorThread class
     */
    protected abstract void moreSessionsPlayed() throws RemoteException, SQLException;

    /**
     * The signature of the moreAvgScoreGameAndSession method This method is defined
     * in the MonitorThread class
     */
    protected abstract void moreAvgScoreGameAndSession() throws RemoteException, SQLException;

    /**
     * The signature of the moreProposedDuplicatedWords method This method is
     * defined in the MonitorThread class
     */
    protected abstract void moreProposedDuplicatedWords() throws RemoteException, SQLException;

    /**
     * The signature of the moreInvalidProposedWords method This method is defined
     * in the MonitorThread class
     */
    protected abstract void moreInvalidProposedWords() throws RemoteException, SQLException;

    /**
     * The signature of the validWordsOccurrences method This method is defined in
     * the MonitorThread class
     */
    protected abstract void validWordsOccurrences() throws RemoteException, SQLException;

    /**
     * The signature of the wordHighestScore method This method is defined in the
     * MonitorThread class
     */
    protected abstract void wordHighestScore() throws RemoteException, SQLException;

    /**
     * The signature of the averageRounds method This method is defined in the
     * MonitorThread class
     */
    protected abstract void averageRounds() throws RemoteException, SQLException;

    /**
     * The signature of the minMaxRounds method This method is defined in the
     * MonitorThread class
     */
    protected abstract void minMaxRounds() throws RemoteException, SQLException;

    /**
     * The signature of the charactersAvgOccurrence method This method is defined in
     * the MonitorThread class
     */
    protected abstract void charactersAvgOccurrence() throws RemoteException, SQLException;

    /**
     * The signature of the definitionRequest method This method is defined in the
     * MonitorThread class
     */
    protected abstract void definitionRequest() throws RemoteException, SQLException;

    /**
     * The signature of the gameDefinitionRequest method This method is defined in
     * the MonitorThread class
     */
    protected abstract void gameDefinitionRequest() throws RemoteException, SQLException;

    /**
     * The signature of the getListOfGames method This method is defined in the
     * MonitorThread class
     */
    protected abstract void getListOfGames(String status) throws RemoteException, SQLException;

    /**
     * The signature of the getListOfPlayersForGame method This method is defined in
     * the MonitorThread class
     */
    protected abstract void getListOfPlayersForGame(int id) throws RemoteException, SQLException;
}