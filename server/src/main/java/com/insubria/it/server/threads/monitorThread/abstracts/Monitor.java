package com.insubria.it.server.threads.monitorThread.abstracts;


import java.rmi.RemoteException;
import java.sql.SQLException;

import com.insubria.it.server.threads.monitorThread.interfaces.MonitorClient;


public abstract class Monitor {
    protected abstract void moreScoreGameAndSession () throws RemoteException, SQLException;

    protected abstract void moreSessionsPlayed () throws RemoteException, SQLException;

    protected abstract void moreAvgScoreGameAndSession () throws RemoteException, SQLException;

    protected abstract void moreProposedDuplicatedWords () throws RemoteException, SQLException;

    protected abstract void moreInvalidProposedWords () throws RemoteException, SQLException;

    protected abstract void validWordsOccurrences (int page) throws RemoteException, SQLException;

    protected abstract void wordHighestScore (int page) throws RemoteException, SQLException;

    protected abstract void averageRounds () throws RemoteException, SQLException;

    protected abstract void minMaxRounds () throws RemoteException, SQLException;

    protected abstract void definitionRequest (int page) throws RemoteException, SQLException;

    protected abstract void gameDefinitionRequest (int page) throws RemoteException, SQLException;
}