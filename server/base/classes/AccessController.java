package server.base.classes;


import java.util.Scanner;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import server.base.interfaces.Access;
import server.base.interfaces.Database;

import server.base.classes.DatabaseController;


public class AccessController implements Access {
  private Scanner scanner;
  private Database db;

  public AccessController () {
    this.scanner = new Scanner(System.in);
  }

  private String[] askForCredentials () {
    String[] credentials = new String[3];

    System.out.print("Insert the DB host: ");
    credentials[0] = this.scanner.nextLine();
    System.out.println("");

    System.out.println("Insert the DB username: ");
    credentials[1] = this.scanner.nextLine();
    System.out.println("");

    System.out.println("Insert the DB password: ");
    credentials[2] = this.scanner.nextLine();
    System.out.println("");

    return credentials;
  }

  private String[] askAdministratorCredentials() {
    String[] credentials = new String[2];

    System.out.print("Insert the uid: ");
    credentials[0] = this.scanner.nextLine();
    System.out.println("");

    System.out.println("Insert the password: ");
    credentials[1] = this.scanner.nextLine();
    System.out.println("");

    return credentials;
  }

  private boolean checkAdminProfile () {
    try {
      Connection dbConnection = this.db.getDatabaseConnection();
    } catch (SQLException exc) {
      System.err.println("Error while establishing the connection with the DB " + exc);
      System.exit(1);
    }

    String sqlQuery = "SELECT * FROM administrator";
    try {
      PreparedStatement pst = dbConnection.prepareStatement(sqlQuery);
      ResultSet result = this.db.performQuery(pst);
      if (result.isBeforeFirst()) {
        result.next();
        System.out.println("Logging with " + result.getString(1) + " profile...");
        dbConnection.close();
        pst.close();
        result.close();

        return true;
      } else {
        System.out.println("No administrator profile found. Creating new one...");
        return false;
      }
    } catch (SQLException exc) {
      System.err.println("Error while performing SQL operations " + exc);
      System.exit(1);
    }
  }

  private void createAdminProfile () {
    String[] credentials = this.askAdministratorCredentials();

    try {
      Connection dbConnection = this.db.getDatabaseConnection();
    } catch (SQLException exc) {
      System.err.println("Error while establishing the connection with the DB " + exc);
      System.exit(1);
    }

    String sqlInsert = "INSERT INTO administrator(uid, password) VALUES(?, ?)";
    try {
      pst = dbConnection.prepareStatement(sqlInsert);
      pst.setString(1, credentials[0]);
      pst.setString(2, credentials[1]);
      this.db.performChangeState(pst);

      dbConnection.close();
      pst.close();
    } catch (SQLException exc) {
      System.err.println("Error while performing SQL operations " + exc);
      System.exit(1);
    }

    System.out.println("Logging with " + credentials[0] + " profile...");
  }

  public void handleAccessProcess () {
    String[] credentials;

    System.out.println("Asking the DB credentials...");
    credentials = this.askForCredentials();

    this.db = new DatabaseController(credentials[0], credentials[1], credentials[2]);
    if (!this.checkAdminProfile()) {
      this.createAdminProfile();
    }
  }
}
