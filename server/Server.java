package server;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RMISecurityManager;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

import server.base.interfaces.Server;
import server.base.classes.AccessController;


public class ServerImpl extends UnicastRemoteObject implements Server {
  public static void main (String[] args) {
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new RMISecurityManager());
    }

    AccessController accessController = new AccessController();
    accessController.handleAccessProcess();

    try {
      ServerImpl server = new ServerImpl();
      Registry registry = LocateRegistry.createRegistry(1099);
      registry.rebind("server", server);
      System.out.println("Server is listening...");
    } catch (Exception e) {}
  }
}
