package com.insubria.it.paroliere.threads.playerThread.interfaces;


import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * The PlayerCredentials interface defines the remote methods that the instance on the client side will define.
 * It extends the Remote interface because all the methods are remote and defined client side. This is the interface used by the PlayerThread to communicate with the client that made the request.
 */
public interface PlayerCredentials extends Remote {
    /**
     * The signature of the confirmPlayerRegistration method
     * Called when the registration of a new user is okay
     * It is implemented client side
     */
    void confirmPlayerRegistration () throws RemoteException;

    /**
     * The signature of the errorPlayerRegistration method
     * Called when the registration of a new user didn't go well
     * It is implemented client side
     */
    void errorPlayerRegistration (String reason) throws RemoteException;

    /**
     * The signature of the confirmCodeConfirmation method
     * Called when the confirmation of a new user is okay
     * It is implemented client side
     */
    void confirmCodeConfirmation () throws RemoteException;

    /**
     * The signature of the errorCodeConfirmation method
     * Called when the confirmation of a new user didn't go well
     * It is implemented client side
     */
    void errorCodeConfirmation (String reason) throws RemoteException;

    /**
     * The signature of the confirmLoginPlayerAccount method
     * Called when the login of a user is okay
     * It is implemented client side
     */
    void confirmLoginPlayerAccount (String name, String surname, String username) throws RemoteException;

    /**
     * The signature of the errorLoginPlayerAccount method
     * Called when the login of a user didn't go well
     * It is implemented client side
     */
    void errorLoginPlayerAccount (String reason) throws RemoteException;

    /**
     * The signature of the confirmResetPlayerPassword method
     * Called when the reset of the password of a user is okay
     * It is implemented client side
     */
    void confirmResetPlayerPassword () throws RemoteException;

    /**
     * The signature of the errorResetPlayerPassword method
     * Called when the reset of the password of a user didn't go well
     * It is implemented client side
     */
    void errorResetPlayerPassword (String reason) throws RemoteException;

    /**
     * The signature of the confirmChangePlayerData method
     * Called when the reset of the data of a user is okay
     * It is implemented client side
     */
    void confirmChangePlayerData () throws RemoteException;

    /**
     * The signature of the errorChangePlayerData method
     * Called when the reset of the data of a user didn't go well
     * It is implemented client side
     */
    void errorChangePlayerData (String reason) throws RemoteException;
}
