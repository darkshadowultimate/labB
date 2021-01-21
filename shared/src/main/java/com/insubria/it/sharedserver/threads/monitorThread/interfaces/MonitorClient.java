package com.insubria.it.sharedserver.threads.monitorThread.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * The MonitorClient interface defines the remote methods that the instance on
 * the client side will define. It extends the Remote interface because all the
 * methods are remote and defined client side. This is the interface used by the
 * MonitorThread to communicate with the client that made the request.
 */
public interface MonitorClient extends Remote {
    /**
     * The signature of the confirmMoreScoreGameAndSession method Called when query
     * is okay and the String[] array with values need to be returned to the client
     * It is implemented client side
     */
    void confirmMoreScoreGameAndSession(String[] result) throws RemoteException;

    /**
     * The signature of the errorMoreScoreGameAndSession method Called when query
     * didn't go well It is implemented client side
     */
    void errorMoreScoreGameAndSession(String reason) throws RemoteException;

    /**
     * The signature of the confirmMoreSessionsPlayed method Called when query is
     * okay and the String value needs to be returned to the client It is
     * implemented client side
     */
    void confirmMoreSessionsPlayed(String result) throws RemoteException;

    /**
     * The signature of the errorMoreSessionsPlayed method Called when query didn't
     * go well It is implemented client side
     */
    void errorMoreSessionsPlayed(String reason) throws RemoteException;

    /**
     * The signature of the confirmMoreAvgScoreGameAndSession method Called when
     * query is okay and the String[] array with values need to be returned to the
     * client It is implemented client side
     */
    void confirmMoreAvgScoreGameAndSession(String[] result) throws RemoteException;

    /**
     * The signature of the errorMoreAvgScoreGameAndSession method Called when query
     * didn't go well It is implemented client side
     */
    void errorMoreAvgScoreGameAndSession(String reason) throws RemoteException;

    /**
     * The signature of the confirmMoreProposedDuplicatedWords method Called when
     * query is okay and the String value needs to be returned to the client It is
     * implemented client side
     */
    void confirmMoreProposedDuplicatedWords(String result) throws RemoteException;

    /**
     * The signature of the errorMoreProposedDuplicatedWords method Called when
     * query didn't go well It is implemented client side
     */
    void errorMoreProposedDuplicatedWords(String reason) throws RemoteException;

    /**
     * The signature of the confirmMoreInvalidProposedWords method Called when query
     * is okay and the String value needs to be returned to the client It is
     * implemented client side
     */
    void confirmMoreInvalidProposedWords(String result) throws RemoteException;

    /**
     * The signature of the errorMoreInvalidProposedWords method Called when query
     * didn't go well It is implemented client side
     */
    void errorMoreInvalidProposedWords(String reason) throws RemoteException;

    /**
     * The signature of the confirmValidWordsOccurrences method Called when query is
     * okay and the String[] array with values need to be returned to the client It
     * is implemented client side
     */
    void confirmValidWordsOccurrences(String[] result) throws RemoteException;

    /**
     * The signature of the errorValidWordsOccurrences method Called when query
     * didn't go well It is implemented client side
     */
    void errorValidWordsOccurrences(String reason) throws RemoteException;

    /**
     * The signature of the confirmWordHighestScore method Called when query is okay
     * and the String[] array with values need to be returned to the client It is
     * implemented client side
     */
    void confirmWordHighestScore(String[] result) throws RemoteException;

    /**
     * The signature of the errorWordHighestScore method Called when query didn't go
     * well It is implemented client side
     */
    void errorWordHighestScore(String reason) throws RemoteException;

    /**
     * The signature of the confirmAverageRounds method Called when query is okay
     * and the String[] array with values need to be returned to the client It is
     * implemented client side
     */
    void confirmAverageRounds(String[] result) throws RemoteException;

    /**
     * The signature of the errorAverageRounds method Called when query didn't go
     * well It is implemented client side
     */
    void errorAverageRounds(String reason) throws RemoteException;

    /**
     * The signature of the confirmMinMaxRounds method Called when query is okay and
     * the String[] array with values need to be returned to the client It is
     * implemented client side
     */
    void confirmMinMaxRounds(String[] result) throws RemoteException;

    /**
     * The signature of the errorMinMaxRounds method Called when query didn't go
     * well It is implemented client side
     */
    void errorMinMaxRounds(String reason) throws RemoteException;

    /**
     * The signature of the confirmCharactersAvgOccurrence method Called when query
     * is okay and the HashMap with occurrences need to be returned to the client It
     * is implemented client side
     */
    void confirmCharactersAvgOccurrence(HashMap<Character, Double> result) throws RemoteException;

    /**
     * The signature of the errorCharactersAvgOccurrence method Called when query
     * didn't go well It is implemented client side
     */
    void errorCharactersAvgOccurrence(String reason) throws RemoteException;

    /**
     * The signature of the confirmDefinitionRequest method Called when query is
     * okay and the String[] array with values need to be returned to the client It
     * is implemented client side
     */
    void confirmDefinitionRequest(String[] result) throws RemoteException;

    /**
     * The signature of the errorDefinitionRequest method Called when query didn't
     * go well It is implemented client side
     */
    void errorDefinitionRequest(String reason) throws RemoteException;

    /**
     * The signature of the confirmGameDefinitionRequest method Called when query is
     * okay and the String[] array with values need to be returned to the client It
     * is implemented client side
     */
    void confirmGameDefinitionRequest(String[] result) throws RemoteException;

    /**
     * The signature of the errorGameDefinitionRequest method Called when query
     * didn't go well It is implemented client side
     */
    void errorGameDefinitionRequest(String reason) throws RemoteException;

    /**
     * The signature of the confirmGetListOfGames method Called when query is okay
     * and the String[] array with values need to be returned to the client It is
     * implemented client side
     */
    void confirmGetListOfGames(String[][] result) throws RemoteException;

    /**
     * The signature of the errorGetListOfGames method Called when query didn't go
     * well It is implemented client side
     */
    void errorGetListOfGames(String reason) throws RemoteException;

    /**
     * The signature of the confirmGetListOfPlayersForGame method Called when query
     * is okay and the String[] array with values need to be returned to the client
     * It is implemented client side
     */
    void confirmGetListOfPlayersForGame(String[] result) throws RemoteException;

    /**
     * The signature of the errorGetListOfPlayersForGame method Called when query
     * didn't go well It is implemented client side
     */
    void errorGetListOfPlayersForGame(String reason) throws RemoteException;
}