package com.insubria.it;

import java.io.IOException;
import java.net.SocketPermission;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

import com.insubria.it.base.abstracts.Database;
import com.insubria.it.base.classes.AccessController;

import com.insubria.it.sharedserver.base.interfaces.Server;
import com.insubria.it.sharedserver.threads.gameThread.interfaces.GameClient;
import com.insubria.it.sharedserver.threads.monitorThread.interfaces.MonitorClient;
import com.insubria.it.sharedserver.threads.playerThread.interfaces.PlayerCredentials;
import com.insubria.it.threads.playerThread.PlayerThread;
import com.insubria.it.threads.monitorThread.MonitorThread;
import com.insubria.it.threads.gameThread.GameThread;

/**
 * The ServerImpl class implements the Server class and extends the
 * UnicastRemoteObject class. This class represents the server that is in
 * listening mode and waits all the clients requests (both related to account
 * management, monitoring, and game). The main method first check for the
 * existence of an administrator account, then register the ServerImpl object in
 * the Registry that will be used from clients to retrieve the reference of the
 * server. So, the ServerImpl class implements remote object pattern.
 */
public class ServerImpl extends UnicastRemoteObject implements Server {
  /**
   * Attribute of type Database that contains the reference of the
   * DatabaseController object
   */
  private Database db;

  /**
   * Constructor explicitly defined to call the super method.
   * 
   * @throws RemoteException - In case of something goes wrong while the
   */
  public ServerImpl() throws RemoteException {
    super();
  }

  /**
   * This method is called when the DatabaseController object is created
   * (DatabaseController is subclass of Database)
   * 
   * @param db - The reference to the DatabaseController object
   * @see AccessController#handleAccessProcess(ServerImpl)
   */
  public void setDbReference(Database db) {
    this.db = db;
  }

  /**
   * This is a remote method that is called by the client when it needs to create
   * a new user. This method will create a PlayerThread thread and pass the
   * "create" value to the action attribute
   * 
   * @param name     - The name of the new user
   * @param surname  - The surname of the new user
   * @param username - The username of the new user
   * @param email    - The email of the new user
   * @param password - The password of the new user
   * @param player   - The reference to the remote object that represents the
   *                 client (user) that made the request
   * 
   * @throws RemoteException - Thrown if there are errors while the remote call
   */
  public void createPlayerAccount(String name, String surname, String username, String email, String password,
      PlayerCredentials player) throws RemoteException {
    PlayerThread playerThread = new PlayerThread(name, surname, username, email, password, player, "create", this.db);
    Thread thread = new Thread(playerThread);
    thread.start();
  }

  /**
   * This is a remote method that is called by the client when it needs to confirm
   * the new user. This method will create a PlayerThread thread and pass the
   * "confirm" value to the action attribute
   * 
   * @param confirmationCode - The random code to identify the user and confirm
   *                         his registration
   * @param player           - The reference to the remote object that represents
   *                         the client (user) that made the request
   * 
   * @throws RemoteException - Thrown if there are errors while the remote call
   */
  public void confirmPlayerAccount(String confirmationCode, PlayerCredentials player) throws RemoteException {
    PlayerThread playerThread = new PlayerThread(confirmationCode, player, "confirm", this.db);
    Thread thread = new Thread(playerThread);
    thread.start();
  }

  /**
   * This is a remote method that is called by the client when it needs to login.
   * This method will create a PlayerThread thread and pass the "login" value to
   * the action attribute
   * 
   * @param email    - The email of the user
   * @param password - The password of the user
   * @param player   - The reference to the remote object that represents the
   *                 client (user) that made the request
   * 
   * @throws RemoteException - Thrown if there are errors while the remote call
   */
  public void loginPlayerAccount(String email, String password, PlayerCredentials player) throws RemoteException {
    PlayerThread playerThread = new PlayerThread(email, password, player, "login", this.db);
    Thread thread = new Thread(playerThread);
    thread.start();
  }

  /**
   * This is a remote method that is called by the client when it needs to reset
   * the password. This method will create a PlayerThread thread and pass the
   * "reset" value to the action attribute
   * 
   * @param email  - The email of the user
   * @param player - The reference to the remote object that represents the client
   *               (user) that made the request
   * 
   * @throws RemoteException - Thrown if there are errors while the remote call
   */
  public void resetPlayerPassword(String email, PlayerCredentials player) throws RemoteException {
    PlayerThread playerThread = new PlayerThread(player, email, "reset", this.db);
    Thread thread = new Thread(playerThread);
    thread.start();
  }

  /**
   * This is a remote method that is called by the client when it needs to change
   * the user's data. This method will create a PlayerThread thread and pass the
   * "change" value to the action attribute
   * 
   * @param name        - The name of the user
   * @param surname     - The surname of the user
   * @param username    - The username of the user
   * @param email       - The email of the user
   * @param password    - The new password of the user
   * @param oldPassword - The old password of the user
   * @param player      - The reference to the remote object that represents the
   *                    client (user) that made the request
   * 
   * @throws RemoteException - Thrown if there are errors while the remote call
   */
  public void changePlayerData(String email, String name, String surname, String username, String password,
      String oldPassword, PlayerCredentials player) throws RemoteException {
    PlayerThread playerThread = new PlayerThread(email, name, surname, username, password, oldPassword, player,
        "change", this.db);
    Thread thread = new Thread(playerThread);
    thread.start();
  }

  /**
   * This is a remote method that is called by the client when it needs to
   * retrieve the user that has more score for each game and session. This method
   * will create a MonitorThread thread and pass the "moreScoreGameAndSession"
   * value to the action attribute
   * 
   * @param monitorClient - The reference to the remote object that represents the
   *                      client (user) that made the request
   * 
   * @throws RemoteException - Thrown if there are errors while the remote call
   */
  public void moreScoreGameAndSession(MonitorClient monitorClient) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, "moreScoreGameAndSession", this.db);
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  /**
   * This is a remote method that is called by the client when it needs to
   * retrieve the user that has played highest number of sessions This method will
   * create a MonitorThread thread and pass the "moreSessionsPlayed" value to the
   * action attribute
   * 
   * @param monitorClient - The reference to the remote object that represents the
   *                      client (user) that made the request
   * 
   * @throws RemoteException - Thrown if there are errors while the remote call
   */
  public void moreSessionsPlayed(MonitorClient monitorClient) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, "moreSessionsPlayed", this.db);
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  /**
   * This is a remote method that is called by the client when it needs to
   * retrieve the user that has highest average of score for each game and session
   * This method will create a MonitorThread thread and pass the
   * "moreAvgScoreGameAndSession" value to the action attribute
   * 
   * @param monitorClient - The reference to the remote object that represents the
   *                      client (user) that made the request
   * 
   * @throws RemoteException - Thrown if there are errors while the remote call
   */
  public void moreAvgScoreGameAndSession(MonitorClient monitorClient) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, "moreAvgScoreGameAndSession", this.db);
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  /**
   * This is a remote method that is called by the client when it needs to
   * retrieve the user that proposed the highest number of duplicated wordrs This
   * method will create a MonitorThread thread and pass the
   * "moreProposedDuplicatedWords" value to the action attribute
   * 
   * @param monitorClient - The reference to the remote object that represents the
   *                      client (user) that made the request
   * 
   * @throws RemoteException - Thrown if there are errors while the remote call
   */
  public void moreProposedDuplicatedWords(MonitorClient monitorClient) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, "moreProposedDuplicatedWords", this.db);
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  /**
   * This is a remote method that is called by the client when it needs to
   * retrieve the user that proposed the highest number of invalid wordrs This
   * method will create a MonitorThread thread and pass the "moreInvalidWords"
   * value to the action attribute
   * 
   * @param monitorClient - The reference to the remote object that represents the
   *                      client (user) that made the request
   * 
   * @throws RemoteException - Thrown if there are errors while the remote call
   */
  public void moreInvalidProposedWords(MonitorClient monitorClient) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, "moreInvalidWords", this.db);
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  /**
   * This is a remote method that is called by the client when it needs to
   * retrieve the occurrence of the valid words This method will create a
   * MonitorThread thread and pass the "validWordsOccurrences" value to the action
   * attribute
   * 
   * @param monitorClient - The reference to the remote object that represents the
   *                      client (user) that made the request
   * 
   * @throws RemoteException - Thrown if there are errors while the remote call
   */
  public void validWordsOccurrences(MonitorClient monitorClient) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, "validWordsOccurrences", this.db);
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  /**
   * This is a remote method that is called by the client when it needs to
   * retrieve the list of words that gave more scores This method will create a
   * MonitorThread thread and pass the "wordHighestScore" value to the action
   * attribute
   * 
   * @param monitorClient - The reference to the remote object that represents the
   *                      client (user) that made the request
   * 
   * @throws RemoteException - Thrown if there are errors while the remote call
   */
  public void wordHighestScore(MonitorClient monitorClient) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, "wordHighestScore", this.db);
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  /**
   * This is a remote method that is called by the client when it needs to
   * retrieve the average of sessions played (2, 3, 4, 5, 6 players) This method
   * will create a MonitorThread thread and pass the "averageRounds" value to the
   * action attribute
   * 
   * @param monitorClient - The reference to the remote object that represents the
   *                      client (user) that made the request
   * 
   * @throws RemoteException - Thrown if there are errors while the remote call
   */
  public void averageRounds(MonitorClient monitorClient) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, "averageRounds", this.db);
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  /**
   * This is a remote method that is called by the client when it needs to
   * retrieve the min/max of sessions played (2, 3, 4, 5, 6 players) This method
   * will create a MonitorThread thread and pass the "minMaxRounds" value to the
   * action attribute
   * 
   * @param monitorClient - The reference to the remote object that represents the
   *                      client (user) that made the request
   * 
   * @throws RemoteException - Thrown if there are errors while the remote call
   */
  public void minMaxRounds(MonitorClient monitorClient) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, "minMaxRounds", this.db);
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  /**
   * This is a remote method that is called by the client when it needs to
   * retrieve the avg of the chars appeared in the matrixes This method will
   * create a MonitorThread thread and pass the "charactersAvgOccurrence" value to
   * the action attribute
   * 
   * @param monitorClient - The reference to the remote object that represents the
   *                      client (user) that made the request
   * 
   * @throws RemoteException - Thrown if there are errors while the remote call
   */
  public void charactersAvgOccurrence(MonitorClient monitorClient) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, "charactersAvgOccurrence", this.db);
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  /**
   * This is a remote method that is called by the client when it needs to
   * retrieve the words that have the highest number of definition requests This
   * method will create a MonitorThread thread and pass the "definitionRequest"
   * value to the action attribute
   * 
   * @param monitorClient - The reference to the remote object that represents the
   *                      client (user) that made the request
   * 
   * @throws RemoteException - Thrown if there are errors while the remote call
   */
  public void definitionRequest(MonitorClient monitorClient) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, "definitionRequest", this.db);
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  /**
   * This is a remote method that is called by the client when it needs to
   * retrieve the games that have the highest number of definition requests (for
   * words) This method will create a MonitorThread thread and pass the
   * "gameDefinitionRequest" value to the action attribute
   * 
   * @param monitorClient - The reference to the remote object that represents the
   *                      client (user) that made the request
   * 
   * @throws RemoteException - Thrown if there are errors while the remote call
   */
  public void gameDefinitionRequest(MonitorClient monitorClient) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, "gameDefinitionRequest", this.db);
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  /**
   * This is a remote method that is called by the client when it needs to
   * create a new game. This method will create a new object and call its run method. The run
   * method will register the object as an RMI remote object.
   * 
   * @param name - The name of the game
   * @param maxPlayers - The maximum number of users in the game
   * @param gameCreator - The reference to the game creator
   * 
   * @throws RemoteException - Thrown if there are errors while the remote call
   * @throws IOException - Thrown if there are errors while the dictionary handling
   */
  public void createNewGame(String name, int maxPlayers, GameClient gameCreator) throws RemoteException, IOException {
    GameThread gameThread = new GameThread(gameCreator, name, maxPlayers, this.db);
    gameThread.run();
  }

  /**
   * This is a remote method that is called by the client when it needs to
   * retrieve the list of games (with date, max players and actual players) for
   * both "open" and "playing" statuses This method will create a MonitorThread
   * thread and pass the "getListOfGames" value to the action attribute
   * 
   * @param monitorClient - The reference to the remote object that represents the
   *                      client (user) that made the request
   * @param status        - The status of the games to retrieve
   * 
   * @throws RemoteException - Thrown if there are errors while the remote call
   */
  public void getListOfGames(MonitorClient monitorClient, String status) throws RemoteException {
    MonitorThread monitorThread = new MonitorThread(monitorClient, status, "getListOfGames", this.db);
    Thread thread = new Thread(monitorThread);
    thread.start();
  }

  /**
   * This main method starts when the server is started. As first thing it will
   * set the RMI security manager (it will allow any kind of operations) Then the
   * server will use the AccessController class and its methods to check that an
   * administrator user exists. If it exists, the server will impersonate that
   * user; if not, the server will create a new one asking the user for uid and
   * password. After that, the method will create a new RMI registry and it will
   * register the ServerImpl object to let the clients look at the registry,
   * retrieve the object, and then make remote calls.
   * 
   * @param args - args passed while starting the server
   * @throws RemoteException - thrown in case of something goes wrong while the
   *                         creation of the registry
   */
  public static void main(String[] args) throws RemoteException {
    /*if (System.getSecurityManager() == null) {
      System.setSecurityManager(new SecurityManager());
    }*/
    ServerImpl server = new ServerImpl();

    AccessController accessController = new AccessController();
    accessController.handleAccessProcess(server);

    try {
      Registry registry = LocateRegistry.createRegistry(1099);
      registry.rebind("server", server);
      System.out.println("Server is listening...");
    } catch (Exception e) {
      System.err.println("Error while starting the com.insubria.it");
      System.exit(1);
    }
  }
}