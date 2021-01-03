package com.insubria.it.serverImplClasses;

import com.insubria.it.sharedserver.threads.monitorThread.interfaces.MonitorClient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class MonitorClientImpl extends UnicastRemoteObject implements MonitorClient {

    public MonitorClientImpl() throws RemoteException {}
    /**
     * The signature of the confirmMoreScoreGameAndSession method Called when query
     * is okay and the String[] array with values need to be returned to the client
     * It is implemented client side
     */
    public void confirmMoreScoreGameAndSession(String[] result) throws RemoteException {}

    /**
     * The signature of the errorMoreScoreGameAndSession method Called when query
     * didn't go well It is implemented client side
     */
    public void errorMoreScoreGameAndSession(String reason) throws RemoteException {}

    /**
     * The signature of the confirmMoreSessionsPlayed method Called when query is
     * okay and the String value needs to be returned to the client It is
     * implemented client side
     */
    public void confirmMoreSessionsPlayed(String result) throws RemoteException {}

    /**
     * The signature of the errorMoreSessionsPlayed method Called when query didn't
     * go well It is implemented client side
     */
    public void errorMoreSessionsPlayed(String reason) throws RemoteException {}

    /**
     * The signature of the confirmMoreAvgScoreGameAndSession method Called when
     * query is okay and the String[] array with values need to be returned to the
     * client It is implemented client side
     */
    public void confirmMoreAvgScoreGameAndSession(String[] result) throws RemoteException {}

    /**
     * The signature of the errorMoreAvgScoreGameAndSession method Called when query
     * didn't go well It is implemented client side
     */
    public void errorMoreAvgScoreGameAndSession(String reason) throws RemoteException {}

    /**
     * The signature of the confirmMoreProposedDuplicatedWords method Called when
     * query is okay and the String value needs to be returned to the client It is
     * implemented client side
     */
    public void confirmMoreProposedDuplicatedWords(String result) throws RemoteException {}

    /**
     * The signature of the errorMoreProposedDuplicatedWords method Called when
     * query didn't go well It is implemented client side
     */
    public void errorMoreProposedDuplicatedWords(String reason) throws RemoteException {}

    /**
     * The signature of the confirmMoreInvalidProposedWords method Called when query
     * is okay and the String value needs to be returned to the client It is
     * implemented client side
     */
    public void confirmMoreInvalidProposedWords(String result) throws RemoteException {}

    /**
     * The signature of the errorMoreInvalidProposedWords method Called when query
     * didn't go well It is implemented client side
     */
    public void errorMoreInvalidProposedWords(String reason) throws RemoteException {}

    /**
     * The signature of the confirmValidWordsOccurrences method Called when query is
     * okay and the String[] array with values need to be returned to the client It
     * is implemented client side
     */
    public void confirmValidWordsOccurrences(String[] result) throws RemoteException {}

    /**
     * The signature of the errorValidWordsOccurrences method Called when query
     * didn't go well It is implemented client side
     */
    public void errorValidWordsOccurrences(String reason) throws RemoteException {}

    /**
     * The signature of the confirmWordHighestScore method Called when query is okay
     * and the String[] array with values need to be returned to the client It is
     * implemented client side
     */
    public void confirmWordHighestScore(String[] result) throws RemoteException {}

    /**
     * The signature of the errorWordHighestScore method Called when query didn't go
     * well It is implemented client side
     */
    public void errorWordHighestScore(String reason) throws RemoteException {}

    /**
     * The signature of the confirmAverageRounds method Called when query is okay
     * and the String[] array with values need to be returned to the client It is
     * implemented client side
     */
    public void confirmAverageRounds(String[] result) throws RemoteException {}

    /**
     * The signature of the errorAverageRounds method Called when query didn't go
     * well It is implemented client side
     */
    public void errorAverageRounds(String reason) throws RemoteException {}

    /**
     * The signature of the confirmMinMaxRounds method Called when query is okay and
     * the String[] array with values need to be returned to the client It is
     * implemented client side
     */
    public void confirmMinMaxRounds(String[] result) throws RemoteException {}

    /**
     * The signature of the errorMinMaxRounds method Called when query didn't go
     * well It is implemented client side
     */
    public void errorMinMaxRounds(String reason) throws RemoteException {}

    /**
     * The signature of the confirmCharactersAvgOccurrence method Called when query
     * is okay and the HashMap with occurrences need to be returned to the client It
     * is implemented client side
     */
    public void confirmCharactersAvgOccurrence(HashMap<Character, Double> result) throws RemoteException {}

    /**
     * The signature of the errorCharactersAvgOccurrence method Called when query
     * didn't go well It is implemented client side
     */
    public void errorCharactersAvgOccurrence(String reason) throws RemoteException {}

    /**
     * The signature of the confirmDefinitionRequest method Called when query is
     * okay and the String[] array with values need to be returned to the client It
     * is implemented client side
     */
    public void confirmDefinitionRequest(String[] result) throws RemoteException {}

    /**
     * The signature of the errorDefinitionRequest method Called when query didn't
     * go well It is implemented client side
     */
    public void errorDefinitionRequest(String reason) throws RemoteException {}

    /**
     * The signature of the confirmGameDefinitionRequest method Called when query is
     * okay and the String[] array with values need to be returned to the client It
     * is implemented client side
     */
    public void confirmGameDefinitionRequest(String[] result) throws RemoteException {}

    /**
     * The signature of the errorGameDefinitionRequest method Called when query
     * didn't go well It is implemented client side
     */
    public void errorGameDefinitionRequest(String reason) throws RemoteException {}

    /**
     * The signature of the confirmGetListOfGames method Called when query is okay
     * and the String[] array with values need to be returned to the client It is
     * implemented client side
     */
    public void confirmGetListOfGames(String[] result) throws RemoteException {}

    /**
     * The signature of the errorGetListOfGames method Called when query didn't go
     * well It is implemented client side
     */
    public void errorGetListOfGames(String reason) throws RemoteException {}

    /**
     * The signature of the confirmGetListOfPlayersForGame method Called when query
     * is okay and the String[] array with values need to be returned to the client
     * It is implemented client side
     */
    public void confirmGetListOfPlayersForGame(String[] result) throws RemoteException {}

    /**
     * The signature of the errorGetListOfPlayersForGame method Called when query
     * didn't go well It is implemented client side
     */
    public void errorGetListOfPlayersForGame(String reason) throws RemoteException {}
}
