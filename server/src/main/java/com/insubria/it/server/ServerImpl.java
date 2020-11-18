package com.insubria.it.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

import com.insubria.it.server.base.interfaces.Server;
import com.insubria.it.server.base.abstracts.Database;
import com.insubria.it.server.base.classes.AccessController;

import com.insubria.it.server.threads.playerThread.PlayerThread;
import com.insubria.it.server.threads.playerThread.interfaces.PlayerCredentials;
import com.insubria.it.server.threads.monitorThread.MonitorThread;
import com.insubria.it.server.threads.monitorThread.interfaces.MonitorClient;
import com.insubria.it.server.threads.gameThread.GameThread;
import com.insubria.it.server.threads.gameThread.interfaces.GameClient;


public class ServerImpl extends UnicastRemoteObject implements Server {
  private Database db;

  public ServerImpl () throws RemoteException {
    super();
  }

  public void setDbReference (Database db) {
    this.db = db;
  }

  public void createPlayerAccount (
    String name,
    String surname,
    String username,
    String email,
    String password,
    PlayerCredentials player
  ) throws RemoteException {
    PlayerThread playerThread = new PlayerThread(name, surname, username, email, password, player, "create", this.db);
    Thread thread = new Thread(playerThread);
    thread.start();
  }

  public void confirmPlayerAccount (String confirmationCode, PlayerCredentials player) throws RemoteException {
    PlayerThread playerThread = new PlayerThread(confirmationCode, player, "confirm", this.db);
    Thread thread = new Thread(playerThread);
    thread.start();
  }

  public void loginPlayerAccount (
    String email,
    String password,
    PlayerCredentials player
  ) throws RemoteException {
    PlayerThread playerThread = new PlayerThread(email, password, player, "login", this.db);
    Thread thread = new Thread(playerThread);
    thread.start();
  }

  public void resetPlayerPassword (String email, PlayerCredentials player) throws RemoteException {
    PlayerThread playerThread = new PlayerThread(player, email, "reset", this.db);
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
      "change",
      this.db
    );
    Thread thread = new Thread(playerThread);
    thread.start();
  }

  public void moreScoreGameAndSession (MonitorClient monitorClient) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, "moreScoreGameAndSession", this.db);
    Thread thread = new Thread(monitorThread);
    thread.start();
  }
  
  public void moreSessionsPlayed (MonitorClient monitorClient) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, "moreSessionsPlayed", this.db);
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  public void moreAvgScoreGameAndSession (MonitorClient monitorClient) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, "moreAvgScoreGameAndSession", this.db);
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  public void moreProposedDuplicatedWords (MonitorClient monitorClient) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, "moreProposedDuplicatedWords", this.db);
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  public void moreInvalidProposedWords (MonitorClient monitorClient) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, "moreInvalidWords", this.db);
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  public void validWordsOccurrences (MonitorClient monitorClient, int page) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, page, "validWordsOccurrences", this.db);
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  public void wordHighestScore (MonitorClient monitorClient, int page) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, page, "wordHighestScore", this.db);
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  public void averageRounds (MonitorClient monitorClient) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, "averageRounds", this.db);
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  public void minMaxRounds (MonitorClient monitorClient) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, "minMaxRounds", this.db);
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  public void charactersAvgOccurrence (MonitorClient monitorClient) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, "charactersAvgOccurrence", this.db);
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  public void definitionRequest (MonitorClient monitorClient, int page) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, page, "definitionRequest", this.db);
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  public void gameDefinitionRequest (MonitorClient monitorClient, int page) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, page, "gameDefinitionRequest", this.db);
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  public void createNewGame (String name, int maxPlayers, GameClient gameCreator) throws RemoteException {
    GameThread gameThread = new GameThread(gameCreator, name, maxPlayers, this.db);
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  public void getListOfGames (MonitorClient monitorClient, String status) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, status, "getListOfGames", this.db);
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  public void getListOfPlayersForGame (MonitorClient monitorClient, int id) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, id, this.db, "getListOfGames");
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  public static void main (String[] args) throws RemoteException {
    /*if (System.getSecurityManager() == null) {
      System.setSecurityManager(new RMISecurityManager());
    }*/
    ServerImpl server = new ServerImpl();

    AccessController accessController = new AccessController();
    accessController.handleAccessProcess(server);

    try {
      Registry registry = LocateRegistry.createRegistry(1099);
      registry.rebind("server", server);
      System.out.println("Server is listening...");
    } catch (Exception e) {
      System.err.println("Error while starting the com.insubria.it.server");
      System.exit(1);
    }
  }
}
