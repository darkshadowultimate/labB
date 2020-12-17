package com.insubria.it.paroliere.base.abstracts;


import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * The Database abstract class defines the signatures of the methods that will be implemented by the DatabaseController class.
 * Used an abstract class instead of an interface to allow setting the DB_NAME constant to be protected and not public.
 */
public abstract class Database {
  /**
   * This constant describes the name of the database that will be created and used
   */
  protected final String DB_NAME = "postgres";

  /**
   * The signature of the getDbName method.
   * This method is defined in the DatabaseController class
   */
  public abstract String getDbName ();

  /**
   * The signature of the getDatabaseConnection method.
   * This method is defined in the DatabaseController class
   */
  public abstract Connection getDatabaseConnection () throws SQLException;

  /**
   * The signature of the performSimpleQuery method.
   * This method is defined in the DatabaseController class
   */
  public abstract ResultSet performSimpleQuery (String query, Statement stm) throws SQLException;

  /**
   * The signature of the performChangeState method.
   * This method is defined in the DatabaseController class
   */
  public abstract void performChangeState (PreparedStatement query) throws SQLException;
}
