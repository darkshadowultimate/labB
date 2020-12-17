package com.insubria.it.paroliere.context;


import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;

import com.insubria.it.paroliere.base.interfaces.Server;

public class RemoteObjectContextProvider {
  // Constants
  private static final String LOCALHOST = "127.0.0.1";
  private static final int PORT = 1099;
  // Variables
  public static Server server;

  public static void initializeRemoteServerObj (String[] arguments) {
    if (System.getSecurityManager() == null) {
      System.out.println("Configuration Security Manager...");
      System.setSecurityManager(new SecurityManager());
    }

    Registry registry;
    String serverIP = arguments.length > 0 ? arguments[0] : LOCALHOST;

    try {
        registry = LocateRegistry.getRegistry(serverIP, PORT);
        server = (Server) registry.lookup("server");
    } catch(RemoteException exe) {
      System.err.print("There's an exeception while getting the registry =====> " + exe);
      System.exit(1);
    } catch (Exception exe) {
      System.err.print("STRANGE EXECEPTION OCCURED =====> " + exe);
      System.exit(1);
    }
  }
}