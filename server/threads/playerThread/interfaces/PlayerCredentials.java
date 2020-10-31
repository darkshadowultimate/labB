package server.threads.playerThread.interfaces;


import java.rmi.Remote;
import java.rmi.RemoteException;


public interface PlayerCredentials extends Remote {
    void confirmPlayerRegistration () throws RemoteException;

    void errorPlayerRegistration (String reason) throws RemoteException;

    void confirmCodeConfirmation () throws RemoteException;

    void errorCodeConfirmation (String reason) throws RemoteException;

    void confirmLoginPlayerAccount (String name, String surname, String username) throws RemoteException;

    void errorLoginPlayerAccount (String reason) throws RemoteException;

    void confirmResetPlayerPassword () throws RemoteException;

    void errorResetPlayerPassword (String reason) throws RemoteException;

    void confirmChangePlayerData () throws RemoteException;

    void errorChangePlayerData (String reason) throws RemoteException;
}
