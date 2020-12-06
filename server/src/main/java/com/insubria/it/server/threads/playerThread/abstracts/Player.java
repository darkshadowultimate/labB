package com.insubria.it.server.threads.playerThread.abstracts;


import java.rmi.RemoteException;
import java.sql.SQLException;
import javax.mail.MessagingException;

import com.insubria.it.server.threads.playerThread.interfaces.PlayerCredentials;


/**
 * The abstract class Player defines the signatures of the methods that will be defined and available only in the PlayerThread class (because of the protected visibility).
 * Used an abstract class instead of an interface because we want to make these methods protected and not public
 */
public abstract class Player  {
    /**
     * The signature of the createPlayerAccount method
     * This method is defined in the PlayerThread class
     */
    protected abstract void createPlayerAccount (
        String name,
        String surname,
        String username,
        String email,
        String password,
        PlayerCredentials player
    ) throws InterruptedException, RemoteException, SQLException, MessagingException;

    /**
     * The signature of the confirmPlayerAccount method
     * This method is defined in the PlayerThread class
     */
    protected abstract void confirmPlayerAccount (
        String confirmationCode,
        PlayerCredentials playerCredentials
    ) throws RemoteException, SQLException;

    /**
     * The signature of the loginPlayerAccount method
     * This method is defined in the PlayerThread class
     */
    protected abstract void loginPlayerAccount (
        String email,
        String password,
        PlayerCredentials player
    ) throws RemoteException, SQLException;

    /**
     * The signature of the resetPlayerPassword method
     * This method is defined in the PlayerThread class
     */
    protected abstract void resetPlayerPassword (
        String email,
        PlayerCredentials player
    ) throws RemoteException, SQLException, MessagingException;

    /**
     * The signature of the changePlayerData method
     * This method is defined in the PlayerThread class
     */
    protected abstract void changePlayerData (
        String email,
        String name,
        String surname,
        String username,
        String password,
        String oldPassword,
        PlayerCredentials player
    ) throws RemoteException, SQLException, MessagingException;
}
