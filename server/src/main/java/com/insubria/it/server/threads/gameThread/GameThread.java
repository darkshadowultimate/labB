package com.insubria.it.server.threads.gameThread;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

import com.insubria.it.server.base.abstracts.Database;

import com.insubria.it.server.threads.gameThread.abstracts.Game;
import com.insubria.it.server.threads.gameThread.interfaces.GameClient;
import com.insubria.it.server.threads.gameThread.utils.GameThreadUtils;
import com.insubria.it.server.threads.gameThread.random.Matrix;

import com.insubria.it.server.threads.gameThread.utils.WordRecord;

import com.insubria.it.server.threads.gameThread.dictionary.Dictionary;
import com.insubria.it.server.threads.gameThread.dictionary.Loader;
import com.insubria.it.server.threads.gameThread.dictionary.InvalidKey;


public class GameThread extends Game implements Runnable {
    private int idGame;
    private String name;
    private int maxPlayers;
    private int sessionNumber;

    private GameClient gameCreator;
    private ArrayList<GameClient> gameClientObservers;
    private GameThreadUtils gameUtil;
    private TimerThread timerThread;

    private int triggerNextStep;

    private Dictionary dictionary;
    private Database db;
    private Connection dbConnection;

    public GameThread (GameClient gameCreator, String name, int maxPlayers, Database db) {
        this.gameCreator = gameCreator;
        this.gameClientObservers = new ArrayList<GameClient>();

        this.name = name;
        this.sessionNumber = 1;
        this.triggerNextStep = 0;

        this.maxPlayers = maxPlayers;
        this.db = db;

        this.gameUtil = new GameThreadUtils(db);
        this.dictionary = new Loader().loadDictionaryFromFile(new File("dict-it.oxt"));
    }

    private void handleStartNewSession () {
        System.out.println("Starting the game session " + this.sessionNumber);
        String[][] randomMatrix = new Matrix().getRandomMatrix();
        String stringMatrix = this.gameUtil.setMatrixToString(randomMatrix);

        HashMap<String, Integer> playerScore = null;

        if (this.sessionNumber == 1) {
            try {
                // The enter sessions in the DB already exists. We only need to populate the characters field and we don't need to reach the gamer score because it's 0
                playerScore = this.gameUtil.calculateCurrentPlayerScore(this.sessionNumber, this.gameClientObservers);
                this.dbConnection = this.db.getDatabaseConnection();
                String sqlUpdate = "UPDATE enter SET characters = ? WHERE email_user = ? AND session_number = ?";
                PreparedStatement pst = null;

                for (GameClient item : this.gameClientObservers) {
                    pst = this.dbConnection.prepareStatement(sqlUpdate);
                    pst.setString(1, stringMatrix);
                    pst.setString(2, item.getEmail());
                    pst.setInt(3, this.sessionNumber);
                    this.db.performChangeState(pst);
                }
                sqlUpdate = "UPDATE game SET status = ? WHERE id = ?";
                pst.setString(1, "playing");
                pst.setInt(2, this.idGame);
                this.db.performChangeState(pst);

                pst.close();
                this.dbConnection.close();
            } catch (SQLException exc) {
                System.err.println("Error while contacting the db " + exc);
            }
        } else {
            // @TODO complete this part
        }

        for (GameClient item : this.gameClientObservers) {
            try {
                item.confirmGameSession(this.name, this.sessionNumber, randomMatrix, playerScore);
            } catch (RemoteException exc) {
                System.err.println("Error while contacting the " + item.getEmail() + "player");
            }
        }
        this.timerThread = new TimerThread("isPlaying", this, this.gameClientObservers);
    }

    private void retrieveGameSessionWords () {
        System.out.println("Retrieving the words proposed in this session...");
        ArrayList<WordRecord> acceptedArray = new ArrayList<WordRecord>(), refusedArray = new ArrayList<WordRecord>();

        try {
            ResultSet accepted = this.gameUtil.getAcceptedWordForGameSession(this.idGame, this.sessionNumber), refused = this.gameUtil.getRefusedWordForGameSession(this.idGame, this.sessionNumber);

            // Populating the ArrayList of accepted words
            while (accepted.next()) {
                acceptedArray.add(new WordRecord(
                    accepted.getString("word"),
                    accepted.getString("username_user"),
                    accepted.getInt("score")
                ));
            }
            // Populating the ArrayList of refused words
            while (refused.next()) {
                refusedArray.add(new WordRecord(
                    refused.getString("word"),
                    refused.getString("username_user"),
                    refused.getInt("score"),
                    refused.getString("reason")
                ));
            }

            for (GameClient singlePlayer : this.gameClientObservers) {
                singlePlayer.sendWordsDiscoveredInSession(acceptedArray, refusedArray);
            }
        } catch (SQLException exc) {
            System.err.println("Error while performing DB operations " + exc);
        }

        this.timerThread = new TimerThread("isReviewing", this, this.gameClientObservers);
    }

    private void removeThread () throws Exception {
        System.out.println("Removing the thread");
        Registry registry = LocateRegistry.getRegistry(1099);
        registry.unbind(Integer.toString(this.idGame));
        Thread.currentThread().interrupt();
    }

    private void removeGame () throws SQLException, Exception {
        String sqlDelete = "DELETE FROM game WHERE id = ?";
        this.dbConnection = this.db.getDatabaseConnection();
        
        PreparedStatement pst = this.dbConnection.prepareStatement(sqlDelete);
        pst.setInt(1, this.idGame);
        this.db.performChangeState(pst);
        System.out.println("Gamed removed");

        this.removeThread();
    }

    private void handleTimer (int seconds) {
        while (seconds > 0) {
            System.out.println("Seconds to wait until game will start: " + seconds);
            try {
                for (GameClient singleClient : this.gameClientObservers) {
                    singleClient.synchronizePreStartGameTimer(seconds);
                }
                seconds--;
                Thread.sleep(1000);
            } catch (RemoteException exc) {
                System.err.println("Error while contacting the client " + exc);
            } catch (InterruptedException exc) {
                System.err.println("Error while sleeping " + exc);
            }
        }

        CompletableFuture.runAsync(() -> {
            this.handleStartNewSession();
        });
    }

    public void triggerEndOfSessionGameClient () {
        System.out.println("Triggering the end of game on clients...");
        for (GameClient singlePlayer : this.gameClientObservers) {
            try {
                singlePlayer.triggerEndOfSession();
            } catch (RemoteException exc) {
                System.err.println("Error while contacting the client " + exc);
            }
        }
    }

    public void createNewGame () throws SQLException, RemoteException {
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
        this.dbConnection.close();
    }

    public synchronized void addNewPlayer (GameClient player) throws RemoteException {
        System.out.println("Adding a user to the game...");
        boolean flag = true;

        try {
            this.dbConnection = this.db.getDatabaseConnection();

            if (this.gameClientObservers.size() < this.maxPlayers) {
                this.gameClientObservers.add(player);
    
                String sqlInsert = "INSERT INTO enter (id_game, email_user, username_user) VALUES (?, ?, ?)";
                PreparedStatement pst = this.dbConnection.prepareStatement(sqlInsert);
                pst.setInt(1, this.idGame);
                pst.setString(2, player.getEmail());
                pst.setString(3, player.getUsername());
                this.db.performChangeState(pst);
                pst.close();
            } else {
                flag = false;
                System.err.println("The game reached the maximum number of players");
                player.errorAddNewPlayer("The game reached the maximum number of players");
            }

            this.dbConnection.close();
        } catch (SQLException exc) {
            flag = false;
            System.err.println("Error while performing DB operations " + exc);
            player.errorAddNewPlayer("Error while performing DB operations " + exc);
        }
        
        if (flag) {
            player.confirmAddNewPlayer();

            if (this.gameClientObservers.size() == this.maxPlayers) {
                System.out.println("The game is starting...");
                CompletableFuture.runAsync(() -> {
                    this.handleTimer(30);
                });
            }
        }
    }

    public synchronized void removePlayerNotStartedGame (GameClient player) throws RemoteException {
        System.out.println("Removing a user to not started game...");

        try {
            this.dbConnection = this.db.getDatabaseConnection();

            if (this.gameClientObservers.remove(player)) {
                String sqlDelete = "DELETE FROM enter WHERE email_user = ? AND id_game = ?";
                PreparedStatement pst = this.dbConnection.prepareStatement(sqlDelete);
                pst.setString(1, player.getEmail());
                pst.setInt(2, this.idGame);
                this.db.performChangeState(pst);

                if (this.gameClientObservers.isEmpty()) {
                    System.out.println("Removing the game...");
                    player.confirmRemovePlayerNotStartedGame();
                    this.removeGame();
                }
                player.confirmRemovePlayerNotStartedGame();
                pst.close();
            } else {
                System.err.println("Error while removing the player from the game");
                player.errorRemovePlayerNotStartedGame("Error while removing the player from the game");
            }

            this.dbConnection.close();
        } catch (SQLException exc) {
            System.err.println("Error while performing DB operations " + exc);
            player.errorRemovePlayerNotStartedGame("Error while performing DB operations " + exc);
        } catch (Exception exc) {
            System.err.println("Error while removing thread " + exc);
            player.errorRemovePlayerNotStartedGame("Error while removing thread " + exc);
        }
    }

    public synchronized void removePlayerInGame (GameClient player) throws RemoteException {
        System.out.println("Removing a user to started game...");

        try {
            this.dbConnection = this.db.getDatabaseConnection();
            System.out.println("Removing the whole game, sessions and words discovered...");

            for (GameClient singlePlayer : this.gameClientObservers) {
                singlePlayer.gameHasBeenRemoved("Player " + player.getUsername() + " left the game");
            }

            System.out.println("Removing the game...");
            this.removeGame();
        } catch (SQLException exc) {
            System.err.println("Error while performing DB operations " + exc);
            player.errorRemovePlayerNotStartedGame("Error while performing DB operations " + exc);
        } catch (Exception exc) {
            System.err.println("Error while removing thread " + exc);
            player.errorRemovePlayerNotStartedGame("Error while removing thread " + exc);
        }
    }

    public synchronized void checkPlayerWords (GameClient player, ArrayList<String> wordsList) throws RemoteException {
        System.out.println("Checking words reached by player " + player.getUsername());

        try {
            String sqlInsert = "INSERT INTO discover (word, id_game, email_user, username_user, session_number_enter, score, is_valid, reason) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            this.dbConnection = this.db.getDatabaseConnection();
            PreparedStatement pst;

            for (String singleWord : wordsList) {
                pst = this.dbConnection.prepareStatement(sqlInsert);
                pst.setString(1, singleWord);
                pst.setInt(2, this.idGame);
                pst.setString(3, player.getEmail());
                pst.setString(4, player.getUsername());
                pst.setInt(5, this.sessionNumber);

                // Check the word exists in the dictionary
                if (this.dictionary.exists(singleWord)) {
                    // @TODO: Check the word exists in the matrix
                    if (/* Check the word exists in the matrix */true) {
                        if (singleWord.length() >= 3) {
                            String sqlQuery = "SELECT * FROM discover WHERE word = " + singleWord + " AND id_game = " + this.idGame + " AND session_number_enter = " + this.sessionNumber;
                            ResultSet result = this.db.performSimpleQuery(sqlQuery);

                            if (result.isBeforeFirst()) {
                                // This word already exists
                                pst.setInt(6, 0);
                                pst.setBoolean(7, false);
                                pst.setString(8, "Already proposed by another user");

                                this.gameUtil.invalidateOtherPlayersSameWords(singleWord, this.idGame, this.sessionNumber);
                            } else {
                                pst.setInt(6, this.gameUtil.getCurrentWordScore(singleWord));
                                pst.setBoolean(7, true);
                                pst.setString(8, "");
                            }
                        } else {
                            pst.setInt(6, 0);
                            pst.setBoolean(7, false);
                            pst.setString(8, "Word is less than 3 chars");
                        }
                    } else {
                        pst.setInt(6, 0);
                        pst.setBoolean(7, false);
                        pst.setString(8, "Word does not exist in the matrix"); 
                    }
                } else {
                    pst.setInt(6, 0);
                    pst.setBoolean(7, false);
                    pst.setString(8, "Word does not exist in the dictionary");
                }

                this.db.performChangeState(pst);
                pst.close();
            }
            this.dbConnection.close();
        } catch (SQLException exc) {
            System.err.println("Error while performing DB operations " + exc);
        }

        this.triggerNextStep++;
        if (this.triggerNextStep == this.maxPlayers) {
            CompletableFuture.runAsync(() -> {
                this.retrieveGameSessionWords();
            });
        }
    }

    public void askForWordDefinition (GameClient player, String word) throws RemoteException {
        System.out.println("Getting the definitions for the " + word);

        try {
            // Increasing the requests number for this word
            this.gameUtil.increaseNumberOfDefinitionRequests(this.idGame, this.sessionNumber, word);
            player.confirmWordDefinitions(this.dictionary.getTerm(word).toString());
        } catch (SQLException exc) {
            System.err.println("Error while performing DB operations " + exc);
            player.errorWordDefinitions("Error while performing DB operations " + exc);
        } catch (InvalidKey exc) {
            System.err.println("Word not found " + exc);
            player.errorWordDefinitions("Word not found " + exc);
        }
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