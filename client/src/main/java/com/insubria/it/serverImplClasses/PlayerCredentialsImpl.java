package com.insubria.it.serverImplClasses;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.insubria.it.g_components.GridFrame;
import com.insubria.it.helpers.FrameTransitions;
import com.insubria.it.sharedserver.threads.playerThread.interfaces.PlayerCredentials;


public class PlayerCredentialsImpl extends UnicastRemoteObject implements PlayerCredentials {

  public PlayerCredentialsImpl() throws RemoteException {
  }

  public void confirmPlayerRegistration() throws RemoteException {}

  public void errorPlayerRegistration(String reason) throws RemoteException {}

  public void confirmCodeConfirmation() throws RemoteException {}

  /**
   * The signature of the errorCodeConfirmation method Called when the
   * confirmation of a new user didn't go well It is implemented client side
   */
  public void errorCodeConfirmation(String reason) throws RemoteException {}

  /**
   * The signature of the confirmLoginPlayerAccount method Called when the login
   * of a user is okay It is implemented client side
   */
  public void confirmLoginPlayerAccount(String name, String surname, String username) throws RemoteException {}

  /**
   * The signature of the errorLoginPlayerAccount method Called when the login of
   * a user didn't go well It is implemented client side
   */
  public void errorLoginPlayerAccount(String reason) throws RemoteException {}

  /**
   * The signature of the confirmResetPlayerPassword method Called when the reset
   * of the password of a user is okay It is implemented client side
   */
  public void confirmResetPlayerPassword() throws RemoteException {}

  /**
   * The signature of the errorResetPlayerPassword method Called when the reset of
   * the password of a user didn't go well It is implemented client side
   */
  public void errorResetPlayerPassword(String reason) throws RemoteException {}

  /**
   * The signature of the confirmChangePlayerData method Called when the reset of
   * the data of a user is okay It is implemented client side
   */
  public void confirmChangePlayerData() throws RemoteException {}

  /**
   * The signature of the errorChangePlayerData method Called when the reset of
   * the data of a user didn't go well It is implemented client side
   */
  public void errorChangePlayerData(String reason) throws RemoteException {}
}
