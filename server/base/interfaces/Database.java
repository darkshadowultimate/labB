package server.base.interfaces;


import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;

public interface Database {
  public Connection getDatabaseConnection () throws SQLException;

  public ResultSet performQuery (String query) throws SQLException;

  public void performChangeState (String query) throws SQLException;
}
