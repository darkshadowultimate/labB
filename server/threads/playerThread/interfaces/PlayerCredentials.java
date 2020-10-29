package server.threads.playerThread.interfaces;


import java.rmi.Remote;
import java.rmi.RemoteException;


public interface PlayerCredentials extends Remote {
    void confirmPlayerRegistration () throws RemoteException;

    void errorPlayerRegistration (String reason) throws RemoteException;

    void confirmCodeConfirmation () throws RemoteException;

    void errorCodeConfirmation (String reason) throws RemoteException;
}
