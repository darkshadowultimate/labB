package com.insubria.it.base.classes;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.insubria.it.base.abstracts.Database;

/**
 * The DatabaseController class represents the class where all the methods
 * needed to interact with the database are defined. The DatabaseController
 * class extends the Database abstract class and defines its methods.
 * 
 * The instances of this class will be used by other objects to interact with
 * the database (both to create/update/delete and query data).
 */
public class DatabaseController extends Database {
  /**
   * This constant represents the host name of the database server
   */
  private final String host;

  /**
   * This constant represents the username used to log into the database server
   */
  private final String username;

  /**
   * This constant represents the password used to log into the database server
   */
  private final String password;

  /**
   * Constructor that will set the host, username, and password attributes for the
   * object created
   * 
   * @param host     - The host to use to connect to the database server
   * @param username - The username to use to connect to the database server
   * @param password - The password to use to connect to the database server
   */
  public DatabaseController(String host, String username, String password) {
    this.host = host;
    this.username = username;
    this.password = password;
  }

  /**
   * This method retrieves the name of the database (postgres)
   * 
   * @return The DB_NAME constant value (postgres)
   */
  public String getDbName() {
    return this.DB_NAME;
  }

  /**
   * This method creates a connection to the database that will be used to perform
   * operations. This method is invoked by all the objects that needs to perform
   * DB operations.
   * 
   * @return The Connection object
   * @throws SQLException - If a SQLException occurs while the creation of the
   *                      connection, this will be thrown to the caller
   */
  public Connection getDatabaseConnection() throws SQLException {
    return DriverManager.getConnection(this.host, this.username, this.password);
  }

  /**
   * This method will execute the SQL query passed as an argument. The object that
   * calls this method only needs to define the SQL query and pass it (as a
   * string).
   *
   * @return The result of the query as a ResultSet object
   * @param query - The SQL query as a String
   * @param stm - The statement obj
   * @throws SQLException - If a SQLException occurs while the query execution,
   *                      this will be thrown to the caller
   */
  public ResultSet performSimpleQuery(String query, Statement stm) throws SQLException {
    return stm.executeQuery(query);
  }

  /**
   * This method will execute the SQL query passed as an argument. The object that
   * calls this method only needs to define the SQL query and pass it (as a
   * PreparedStatement).
   *
   * @return The result of the query as a ResultSet object
   * @param pst - The PreparedStatement that represents the SQL query
   * @throws SQLException - If a SQLException occurs while the query execution,
   *                      this will be thrown to the caller
   */
  public ResultSet peroformComplexQuery(PreparedStatement pst) throws SQLException {
    return pst.executeQuery();
  }

  /**
   * This method will execute a CREATE/UPDATE/DELETE operation on the database.
   * The object that calls this method will need to cerate the PreparedStatement
   * object and pass it to the method.
   * 
   * @return Nothing
   * @param query - The PreparedStatement object that represents the SQL operation
   * @throws SQLException - If a SQLException occurs while the query execution,
   *                      this will be thrown to the caller
   */
  public void performChangeState(PreparedStatement query) throws SQLException {
    query.executeUpdate();
  }
}
