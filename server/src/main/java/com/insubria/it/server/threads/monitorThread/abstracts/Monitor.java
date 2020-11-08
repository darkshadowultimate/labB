package com.insubria.it.server.threads.monitorThread.abstracts;


import java.rmi.RemoteException;
import java.sql.SQLException;

import com.insubria.it.server.threads.monitorThread.interfaces.MonitorClient;


public abstract class Monitor {
    protected abstract void moreSessionsPlayed () throws RemoteException, SQLException;

    protected abstract void moreProposedDuplicatedWords () throws RemoteException, SQLException;

    protected abstract void moreInvalidProposedWords () throws RemoteException, SQLException;
}