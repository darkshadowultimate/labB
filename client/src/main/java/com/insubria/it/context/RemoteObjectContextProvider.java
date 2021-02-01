package com.insubria.it.context;

import com.insubria.it.sharedserver.base.interfaces.Server;
import com.insubria.it.sharedserver.threads.gameThread.abstracts.Game;

import java.rmi.NotBoundException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;


public class RemoteObjectContextProvider {
  // Constants
  private static final String LOCALHOST = "127.0.0.1";
  private static final int PORT = 1099;
  // Variables
  private static String IP_ADDRESS_SERVER = null;
  private static Registry registry = null;
  public static Server server;
  public static Game game;

  private static void setRegistryFromServer(String[] arguments) {
    /*if (System.getSecurityManager() == null) {
      System.out.println("Configuration Security Manager...");
      System.setSecurityManager(new SecurityManager());
    }*/

    IP_ADDRESS_SERVER = arguments.length > 0 ? arguments[0] : LOCALHOST;

    try {
      registry = LocateRegistry.getRegistry(IP_ADDRESS_SERVER, PORT);
    } catch (RemoteException exe) {
      System.err.print("There's an exeception while getting the registry =====> " + exe);
      System.exit(1);
    }
  }

  private static Object getRemoteObjectFromRegistry(String nameRemoteObject) {
    Object remoteObject = null;

    try {
      remoteObject = registry.lookup(nameRemoteObject);
    } catch(RemoteException exc) {
      System.err.print("Exeception while getting the remote object =====> " + exc);
      System.exit(1);
    } catch(NotBoundException exc) {
      System.err.print("Exeception not bound while getting the remote object =====> " + exc);
      System.exit(1);
    }

    return remoteObject;
  }

  private static boolean areIPOrRegistryNull() {
    return
      IP_ADDRESS_SERVER == null ||
      registry == null;
  }

  public static void setServerRemoteObject(String[] arguments) {
    if(areIPOrRegistryNull()) {
      setRegistryFromServer(arguments);
    }
    server = (Server) getRemoteObjectFromRegistry("server");
  }

  public static void setGameRemoteObject(String idGame) {
    game = (Game) getRemoteObjectFromRegistry(idGame);
  }
}