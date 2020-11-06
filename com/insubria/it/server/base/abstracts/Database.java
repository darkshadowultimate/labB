package com.insubria.it.server.base.abstracts;


import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class Database {
  public abstract Connection getDatabaseConnection () throws SQLException;

  public abstract ResultSet performQuery (PreparedStatement query) throws SQLException;

  public abstract void performChangeState (PreparedStatement query) throws SQLException;
}