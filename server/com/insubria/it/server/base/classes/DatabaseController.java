package com.insubria.it.server.base.classes;


import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.insubria.it.server.base.abstracts.Database;


public class DatabaseController extends Database {
  private final String host;
  private final String username;
  private final String password;

  public DatabaseController (String host, String username, String password) {
    this.host = host;
    this.username = username;
    this.password = password;
  }

  public Connection getDatabaseConnection () throws SQLException {
    return DriverManager.getConnection(this.host, this.username, this.password);
  }

  public ResultSet performQuery (PreparedStatement query) throws SQLException {
    return query.executeQuery();
  }

  public void performChangeState (PreparedStatement query) throws SQLException {
    query.executeUpdate();
  }
}
