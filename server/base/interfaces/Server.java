package server.base.interfaces;


import server.threads.playerThread.interfaces.PlayerCredentials;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface Server extends Remote {
    void createPlayerAccount (
            String name,
            String surname,
            String username,
            String email,
            String password,
            PlayerCredentials player
    ) throws RemoteException;
}
