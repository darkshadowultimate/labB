package com.insubria.it.sharedserver.base.interfaces;


import com.insubria.it.sharedserver.threads.playerThread.interfaces.PlayerCredentials;
import com.insubria.it.sharedserver.threads.monitorThread.interfaces.MonitorClient;
import com.insubria.it.sharedserver.threads.gameThread.interfaces.GameClient;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The interface Server defines the signatures of the methods that will be
 * defined in the ServerImpl class. It extends the Remote interface because the
 * methods will be called by the client using RMI
 */
public interface Server extends Remote {
  /**
   * The signature of the createPlayerAccount method. This method is defined in
   * the ServerImpl class
   */
  void createPlayerAccount(String name, String surname, String username, String email, String password,
                           PlayerCredentials player) throws RemoteException;

  /**
   * The signature of the confirmPlayerAccount method. This method is defined in
   * the ServerImpl class
   */
  void confirmPlayerAccount(String confirmationCode, PlayerCredentials player) throws RemoteException;

  /**
   * The signature of the loginPlayerAccount method. This method is defined in the
   * ServerImpl class
   */
  void loginPlayerAccount(String email, String password, PlayerCredentials player) throws RemoteException;

  /**
   * The signature of the resetPlayerPassword method. This method is defined in
   * the ServerImpl class
   */
  void resetPlayerPassword(String email, PlayerCredentials player) throws RemoteException;

  /**
   * The signature of the changePlayerData method. This method is defined in the
   * ServerImpl class
   */
  void changePlayerData(String email, String name, String surname, String username, String password, String oldPassword,
                        PlayerCredentials player) throws RemoteException;

  /**
   * The signature of the moreScoreGameAndSession method. This method is defined
   * in the ServerImpl class
   */
  void moreScoreGameAndSession(MonitorClient monitorClient) throws RemoteException;

  /**
   * The signature of the moreSessionsPlayed method. This method is defined in the
   * ServerImpl class
   */
  void moreSessionsPlayed(MonitorClient monitorClient) throws RemoteException;

  /**
   * The signature of the moreAvgScoreGameAndSession method. This method is
   * defined in the ServerImpl class
   */
  void moreAvgScoreGameAndSession(MonitorClient monitorClient) throws RemoteException;

  /**
   * The signature of the moreProposedDuplicatedWords method. This method is
   * defined in the ServerImpl class
   */
  void moreProposedDuplicatedWords(MonitorClient monitorClient) throws RemoteException;

  /**
   * The signature of the moreInvalidProposedWords method. This method is defined
   * in the ServerImpl class
   */
  void moreInvalidProposedWords(MonitorClient monitorClient) throws RemoteException;

  /**
   * The signature of the validWordsOccurrences method. This method is defined in
   * the ServerImpl class
   */
  void validWordsOccurrences(MonitorClient monitorClient) throws RemoteException;

  /**
   * The signature of the wordHighestScore method. This method is defined in the
   * ServerImpl class
   */
  void wordHighestScore(MonitorClient monitorClient) throws RemoteException;

  /**
   * The signature of the averageRounds method. This method is defined in the
   * ServerImpl class
   */
  void averageRounds(MonitorClient monitorClient) throws RemoteException;

  /**
   * The signature of the minMaxRounds method. This method is defined in the
   * ServerImpl class
   */
  void minMaxRounds(MonitorClient monitorClient) throws RemoteException;

  /**
   * The signature of the charactersAvgOccurrence method. This method is defined
   * in the ServerImpl class
   */
  void charactersAvgOccurrence(MonitorClient monitorClient) throws RemoteException;

  /**
   * The signature of the definitionRequest method. This method is defined in the
   * ServerImpl class
   */
  void definitionRequest(MonitorClient monitorClient) throws RemoteException;

  /**
   * The signature of the gameDefinitionRequest method. This method is defined in
   * the ServerImpl class
   */
  void gameDefinitionRequest(MonitorClient monitorClient) throws RemoteException;

  /**
   * The signature of the createNewGame method. This method is defined in the
   * ServerImpl class
   */
  void createNewGame(String name, int maxPlayers, GameClient gameCreator) throws RemoteException, IOException;

  /**
   * The signature of the getListOfGames method. This method is defined in the
   * ServerImpl class
   */
  void getListOfGames(MonitorClient monitorClient, String status) throws RemoteException;
}
