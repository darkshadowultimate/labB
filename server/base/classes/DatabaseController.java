package server.base.classes;


import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import server.base.interfaces.Database;


public class DatabaseController implements Database {
  private String host;
  private String username;
  private String password;

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
