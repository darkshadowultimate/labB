package com.insubria.it.base.classes;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.insubria.it.ServerImpl;
import com.insubria.it.base.abstracts.Access;
import com.insubria.it.base.abstracts.Database;
import com.insubria.it.base.constants.DBCreation;

/**
 * The AccessController class extends the abstract class Access where there are
 * the signatures of the methods defined. This class is used by the ServerImpl
 * class to handle the process for checking whether an administrator user exists
 * or not.
 */
public class AccessController extends Access {
  /**
   * Attribute that contains the reference of the Scanner object used to ask the
   * user to insert information
   */
  private final Scanner scanner;

  /**
   * Attribute of type Database that contains the reference of the
   * DatabaseController object
   */
  private Database db;

  /**
   * Class constructor that will create an instance of the Scanner object and
   * assign the reference to the scanner attribute
   */
  public AccessController() {
    this.scanner = new Scanner(System.in);
  }

  /**
   * This method will ask the user to insert the DB host, username and password to
   * access. This method is called by the handleAccessProcess method.
   * 
   * @return It returns a String array that contains the three parameters required
   */
  protected String[] askForCredentials() {
    String[] credentials = new String[3];

    System.out.print("Insert the DB host and port (e.g. => localhost:5432): ");
    String midString = this.scanner.nextLine();
    credentials[0] = "jdbc:postgresql://" + midString + "/";
    System.out.println("");

    System.out.println("Insert the DB username: ");
    credentials[1] = this.scanner.nextLine();
    System.out.println("");

    System.out.println("Insert the DB password: ");
    credentials[2] = this.scanner.nextLine();
    System.out.println("");

    return credentials;
  }

  /**
   * This method will create the database and will populate it with the tables
   * needed for the "Il paroliere" game to be executed. This method is called by
   * the handleAccessProcess method.
   * 
   * @return Nothing
   */
  protected void createDatabase() {
    Connection dbConnection = null;
    try {
      dbConnection = this.db.getDatabaseConnection();
    } catch (SQLException exc) {
      System.err.println("Error while establishing the connection with the DB " + exc);
      System.exit(1);
    }

    System.out.println("Creating the postgres db...");
    try {
      PreparedStatement pst = dbConnection.prepareStatement(DBCreation.sqlInitialize);
      this.db.performChangeState(pst);
    } catch (SQLException exc) {
      System.err.println("DB already exists");
    }

    System.out.println("Successfully created the Database");
  }

  /**
   * This method will ask the user to insert the uid and password of the
   * administrator user to create. This method is called by the createAdminProfile
   * method whether no administrator user is found in the DB.
   * 
   * @return A String array that contains the uid and password chosen by the user
   */
  protected String[] askAdministratorCredentials() {
    String[] credentials = new String[2];

    System.out.print("Insert the uid: ");
    credentials[0] = this.scanner.nextLine();
    System.out.println("");

    System.out.println("Insert the password: ");
    credentials[1] = this.scanner.nextLine();
    System.out.println("");

    return credentials;
  }

  /**
   * This method will check the existence of at least one administrator user. If
   * it's found, it will login with this one, if not it will log that no
   * administrator user has been found. This method is called by the
   * handleAccessProcess method to check the existence of an administrator user.
   * 
   * @return true if the administrator exists, false otherwise
   */
  protected boolean checkAdminProfile() {
    String sqlQuery = "SELECT * FROM administrator";
    Connection dbConnection = null;
    Statement stm = null;

    try {
      dbConnection = this.db.getDatabaseConnection();
      stm = dbConnection.createStatement();
    } catch (SQLException exc) {
      System.err.println("Error while establishing the connection with the DB " + exc);
      System.exit(1);
    }

    try {
      ResultSet result = this.db.performSimpleQuery(sqlQuery, stm);
      if (result.isBeforeFirst()) {
        result.next();
        System.out.println("Logging with " + result.getString(1) + " profile...");

        result.close();
        stm.close();
        dbConnection.close();
        return true;
      } else {
        System.out.println("No administrator profile found. Creating new one...");

        result.close();
        stm.close();
        dbConnection.close();
        return false;
      }
    } catch (SQLException exc) {
      System.err.println("Error while performing SQL operations " + exc);
      System.exit(1);
    }
    return false;
  }

  /**
   * This method will create an administrator profile in the db. This method is
   * called by the handleAccessProcess method when the administrator user is not
   * in the db and a new one needs to be created.
   * 
   * @return Nothing
   */
  protected void createAdminProfile() {
    String[] credentials = this.askAdministratorCredentials();
    Connection dbConnection = null;

    try {
      dbConnection = this.db.getDatabaseConnection();
    } catch (SQLException exc) {
      System.err.println("Error while establishing the connection with the DB " + exc);
      System.exit(1);
    }

    String sqlInsert = "INSERT INTO administrator(uid, password) VALUES(?, ?)";
    try {
      PreparedStatement pst = dbConnection.prepareStatement(sqlInsert);
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

  /**
   * This method will handle the process of checking and, in case, creating the
   * administrator profile. Because this method creates an instance of the
   * DatabaseController object, it will call the setDbReference method of
   * ServerImpl to set this reference also in the ServerImpl object. This method
   * is called by the main method of the ServerImpl object before registering the
   * server object.
   * 
   * @see ServerImpl#main(String[])
   * @param server - The reference to the ServerImpl object
   * @return Nothing
   */
  public void handleAccessProcess(ServerImpl server) {
    String[] credentials;

    System.out.println("Asking the DB credentials...");
    credentials = this.askForCredentials();

    this.db = new DatabaseController(credentials[0], credentials[1], credentials[2]);
    this.createDatabase();
    if (!this.checkAdminProfile()) {
      this.createAdminProfile();
    }

    server.setDbReference(this.db);
  }
}
