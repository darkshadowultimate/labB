package com.insubria.it.server.threads.gameThread;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.insubria.it.server.base.abstracts.Database;

import com.insubria.it.server.threads.gameThread.abstracts.Game;
import com.insubria.it.server.threads.gameThread.interfaces.GameClient;


public class GameThread extends Game implements Runnable {
    private int idGame;
    private String name;
    private int maxPlayers;

    private GameClient gameCreator;
    private ArrayList<GameClient> gameClientObservers;

    private Database db;
    private Connection dbConnection;

    public GameThread (GameClient gameCreator, String name, int maxPlayers, Database db) {
        this.gameCreator = gameCreator;
        this.gameClientObservers = new ArrayList<GameClient>();

        this.name = name;

        this.maxPlayers = maxPlayers;
        this.db = db;
    }

    protected void createNewGame () throws SQLException, RemoteException {
        System.out.println("Creating a new game and adding the user to it...");
        this.dbConnection = this.db.getDatabaseConnection();
        
        this.gameClientObservers.add(this.gameCreator);

        String sqlInsert = "INSERT INTO game (id, name, max_players, status) VALUES (?, ?, ?, ?)";
        PreparedStatement pst = this.dbConnection.prepareStatement(sqlInsert);
        pst.setInt(1, this.idGame);
        pst.setString(2, this.name);
        pst.setInt(3, this.maxPlayers);
        pst.setString(4, "open");
        this.db.performChangeState(pst);

        sqlInsert = "INSERT INTO enter (id_game, email_user, username_user) VALUES (?, ?, ?)";
        pst = this.dbConnection.prepareStatement(sqlInsert);
        pst.setInt(1, this.idGame);
        pst.setString(2, this.gameCreator.getEmail());
        pst.setString(3, this.gameCreator.getUsername());
        this.db.performChangeState(pst);

        this.gameCreator.confirmCreateNewGame(this);
        System.out.println("Created the game and added the creator to it");
        pst.close();
    }

    protected synchronized void addNewPlayer (GameClient player) throws RemoteException {
        System.out.println("Adding a user to the game...");
        boolean flag = true;

        try {
            this.dbConnection = this.db.getDatabaseConnection();

            this.gameClientObservers.add(player);
    
            String sqlInsert = "INSERT INTO game (id, name, max_players, status) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = this.dbConnection.prepareStatement(sqlInsert);
            pst.setInt(1, this.idGame);
            pst.setString(2, player.getEmail());
            pst.setString(3, player.getUsername());
            this.db.performChangeState(pst);
        } catch (SQLException exc) {
            flag = false;
            System.err.println("Error while performing DB operations " + exc);
            this.gameCreator.errorAddNewPlayer("Error while performing DB operations " + exc);
        }

        player.confirmAddNewPlayer();
    }

    public void run () {
        boolean flag = true;
        this.idGame = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);

        try {
            this.createNewGame();
        } catch (RemoteException exc) {
            flag = false;
            System.err.println("Error while contacting the client " + exc);
            try {
                this.gameCreator.errorCreateNewGame("Error while contacting the client " + exc);
            } catch (RemoteException e) {}
        } catch (SQLException exc) {
            flag = false;
            System.err.println("Error while performing DB operations " + exc);
            try {
                this.gameCreator.errorCreateNewGame("Error while performing DB operations " + exc);
            } catch (RemoteException e) {}
        }

        if (flag) {
            try {
                Registry registry = LocateRegistry.getRegistry(1099);
                registry.rebind(Integer.toString(this.idGame), this);
                System.out.println("Game thread " + this.idGame + " is listening...");
              } catch (Exception e) {
                System.err.println("Error while registering " + this.idGame + " game thread");
              }
        }
    }
}