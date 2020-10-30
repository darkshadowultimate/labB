package server.threads.playerThread.abstracts;


import java.rmi.RemoteException;
import java.sql.SQLException;
import javax.mail.MessagingException;

import server.threads.playerThread.interfaces.PlayerCredentials;


public abstract class Player  {
    protected abstract void createPlayerAccount (
        String name,
        String surname,
        String username,
        String email,
        String password,
        PlayerCredentials player
    ) throws InterruptedException, RemoteException, SQLException, MessagingException;

    protected abstract void confirmPlayerAccount (
        String confirmationCode,
        PlayerCredentials playerCredentials
    ) throws RemoteException, SQLException;

    protected abstract void loginPlayerAccount (
        String email,
        String password,
        PlayerCredentials player
    ) throws RemoteException, SQLException;
}
