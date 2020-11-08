package com.insubria.it.server.threads.monitorThread;


import java.sql.Statement;

import com.insubria.it.server.base.abstracts.Database;

import com.insubria.it.server.threads.monitorThread.abstracts.Monitor;
import com.insubria.it.server.threads.monitorThread.interfaces.MonitorClient;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class MonitorThread extends Monitor implements Runnable {
    private final MonitorClient monitorClient;

    private Database db;
    private final String action;

    public MonitorThread (MonitorClient monitorClient, String action) {
        this.monitorClient = monitorClient;
        this.action = action;
    }

    protected void moreSessionsPlayed () throws RemoteException, SQLException {
        System.out.println("Reaching the user with the maximum number of sessions played...");
        String sqlQuery = "SELECT u.email, u.username, COUNT(*) as number " +
                          "FROM users as u INNER JOIN enter as e ON u.email = e.email_user and u.username = e.username_user " +
                          "GROUP BY u.email, u.username " +
                          "ORDER BY number DESC;";
        ResultSet result = this.db.performSimpleQuery(sqlQuery);
        if (result.isBeforeFirst()) {
            System.out.println("Successfully performed the query");
            String returnString = result.getString("email") + " " + result.getString("username") + " " + result.getInt("number");
            this.monitorClient.confirmMoreSessionsPlayed(returnString);
        } else {
            System.out.println("No sessions played yet");
            this.monitorClient.errorMoreSessionsPlayed("No sessions played yet");
        }
        result.close();
    }

    public void run () {
        switch (this.action) {
            case "moreSessionsPlayed": {
                try {
                    this.moreSessionsPlayed();
                } catch (RemoteException exc) {
                    System.err.println("Error while contacting the client " + exc);
                    try {
                        this.monitorClient.errorMoreSessionsPlayed("Error while contacting the client " + exc);
                    } catch (RemoteException e) {}
                } catch (SQLException exc) {
                    try {
                        this.monitorClient.errorMoreSessionsPlayed("Error while performing DB operations " + exc);
                    } catch (RemoteException e) {}
                }
            }
        }
    }
}