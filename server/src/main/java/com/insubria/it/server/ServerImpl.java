package com.insubria.it.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

import com.insubria.it.server.base.interfaces.Server;
import com.insubria.it.server.base.classes.AccessController;

import com.insubria.it.server.threads.playerThread.PlayerThread;
import com.insubria.it.server.threads.playerThread.interfaces.PlayerCredentials;
import com.insubria.it.server.threads.monitorThread.MonitorThread;
import com.insubria.it.server.threads.monitorThread.interfaces.MonitorClient;


public class ServerImpl extends UnicastRemoteObject implements Server {
  public ServerImpl () throws RemoteException {
    super();
  }

  public void createPlayerAccount (
    String name,
    String surname,
    String username,
    String email,
    String password,
    PlayerCredentials player
  ) throws RemoteException {
    PlayerThread playerThread = new PlayerThread(name, surname, username, email, password, player, "create");
    Thread thread = new Thread(playerThread);
    thread.start();
  }

  public void confirmPlayerAccount (String confirmationCode, PlayerCredentials player) throws RemoteException {
    PlayerThread playerThread = new PlayerThread(confirmationCode, player, "confirm");
    Thread thread = new Thread(playerThread);
    thread.start();
  }

  public void loginPlayerAccount (
    String email,
    String password,
    PlayerCredentials player
  ) throws RemoteException {
    PlayerThread playerThread = new PlayerThread(email, password, player, "login");
    Thread thread = new Thread(playerThread);
    thread.start();
  }

  public void resetPlayerPassword (String email, PlayerCredentials player) throws RemoteException {
    PlayerThread playerThread = new PlayerThread(player, email, "reset");
    Thread thread = new Thread(playerThread);
    thread.start();
  }

  public void changePlayerData (
    String email,
    String name,
    String surname,
    String username,
    String password,
    String oldPassword,
    PlayerCredentials player
  ) throws RemoteException {
    PlayerThread playerThread = new PlayerThread(
      email,
      name,
      surname,
      username,
      password,
      oldPassword,
      player,
      "change"
    );
    Thread thread = new Thread(playerThread);
    thread.start();
  }

  public void moreScoreGameAndSession (MonitorClient monitorClient) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, "moreScoreGameAndSession");
    Thread thread = new Thread(monitorThread);
    thread.start();
  }
  
  public void moreSessionsPlayed (MonitorClient monitorClient) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, "moreSessionsPlayed");
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  public void moreAvgScoreGameAndSession (MonitorClient monitorClient) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, "moreAvgScoreGameAndSession");
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  public void moreProposedDuplicatedWords (MonitorClient monitorClient) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, "moreProposedDuplicatedWords");
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  public void moreInvalidProposedWords (MonitorClient monitorClient) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, "moreInvalidWords");
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  public void validWordsOccurrences (MonitorClient monitorClient, int page) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, page, "validWordsOccurrences");
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  public void wordHighestScore (MonitorClient monitorClient, int page) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, page, "wordHighestScore");
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  public void averageRounds (MonitorClient monitorClient) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, "averageRounds");
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  public void minMaxRounds (MonitorClient monitorClient) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, "minMaxRounds");
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  public void definitionRequest (MonitorClient monitorClient, int page) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, page, "definitionRequest");
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  public void gameDefinitionRequest (MonitorClient monitorClient, int page) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, page, "gameDefinitionRequest");
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  public static void main (String[] args) throws RemoteException {
    /*if (System.getSecurityManager() == null) {
      System.setSecurityManager(new RMISecurityManager());
    }*/

    AccessController accessController = new AccessController();
    accessController.handleAccessProcess();

    try {
      ServerImpl server = new ServerImpl();
      Registry registry = LocateRegistry.createRegistry(1099);
      registry.rebind("com/insubria/it/server", server);
      System.out.println("Server is listening...");
    } catch (Exception e) {
      System.err.println("Error while starting the com.insubria.it.server");
      System.exit(1);
    }
  }
}
