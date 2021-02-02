package com.insubria.it.threads.gameThread;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.io.File;
import java.io.IOException;

import com.insubria.it.base.abstracts.Database;

import com.insubria.it.sharedserver.threads.gameThread.interfaces.GameClient;
import com.insubria.it.sharedserver.threads.gameThread.utils.WordRecord;
import com.insubria.it.sharedserver.threads.gameThread.abstracts.Game;
import com.insubria.it.threads.gameThread.utils.GameThreadUtils;
import com.insubria.it.threads.gameThread.random.Matrix;


import com.insubria.it.threads.gameThread.dictionary.Dictionary;
import com.insubria.it.threads.gameThread.dictionary.Loader;
import com.insubria.it.threads.gameThread.dictionary.InvalidKey;

/**
 * The GameThread class represents the remote object that will be created when a user
 * creates a new game. The reference to this object will be stored in the RMI
 * registry so it will be able to players that enter the specific game. This
 * class follows the observable/observer pattern. This RMI object is the observable
 * object and the clients that plays the game are the
 * observers. This class extends the Game interface that contains the
 * signatures of the methods. This class extends the triggerEndOfSessionGameClient to let the instances be inserted in the RMI registry.
 */
public class GameThread extends UnicastRemoteObject implements Game {
    /**
     * It represents the id of the game
     */
    private int idGame;

    /**
     * It represents the name of the game
     */
    private String name;

    /**
     * It represents the max players allowed in the game
     */
    private int maxPlayers;

    /**
     * It represents the session number reached in the game
     */
    private int sessionNumber;

    /**
     * It represents the reference to the client that created the game
     */
    private GameClient gameCreator;

    /**
     * It represents the matrix used in the game session
     */
    private String[][] randomMatrix;

    /**
     * It represents the ArrayList that contains the references to the clients that
     * are in the game
     */
    private ArrayList<GameClient> gameClientObservers;

    /**
     * It represents the reference to the instance that serves different utils to
     * the GameThread thread
     */
    private GameThreadUtils gameUtil;

    /**
     * It represents the reference to the instance that acts as the timer while the
     * game and the review part of the game
     */
    private TimerThread timerThread;

    /**
     * It is used as a counter to understand if the last client sent the list of
     * words. If so, the thread will retrieve the list of words proposed in the
     * session and will send them to the clients
     */
    private int triggerNextStep;

    /**
     * It represents the reference of the Dictionary object used to check if words
     * are eligible
     */
    private Dictionary dictionary;

    /**
     * It represents the reference to the DatabaseController object
     */
    private Database db;

    /**
     * It represents the reference to the Connection object used to interact with
     * the DB
     */
    private Connection dbConnection;

    /**
     * Constructor called by the ServerImpl when a user wants to create a new game
     * 
     * @param gameCreator - The reference to the client that crated the game
     * @param name        - The name of the game
     * @param maxPlayers  - The max players number of the game
     * @param db          - The reference to the DatabaseController object
     * 
     * @throws RemoteException - If there is an error while the client contact, it
     *                         throws RemoteException
     * @throws IOException     - If there is an error while the loading of the
     *                         dictionary with the "dict-it.oxt" file, it throws
     *                         IOException
     */
    public GameThread(GameClient gameCreator, String name, int maxPlayers, Database db)
            throws RemoteException, IOException {
        this.gameCreator = gameCreator;
        this.gameClientObservers = new ArrayList<GameClient>();

        this.name = name;
        this.sessionNumber = 1;
        this.triggerNextStep = 0;

        this.maxPlayers = maxPlayers;
        this.db = db;

        this.gameUtil = new GameThreadUtils(db);
        //this.dictionary = new Loader().loadDictionaryFromFile(new File("dict-it.oxt"));
    }

    /**
     * Method invoked when a new session needs to be started (if this is not the
     * first session, it will check if someone reached the 50 score. If so, the game
     * will be end, if not a new session will be triggered).
     */
    public void handleStartNewSession() {
        System.out.println("Starting the game session " + this.sessionNumber);
        String[][] randomMatrix = new Matrix().getRandomMatrix();
        String stringMatrix = this.gameUtil.setMatrixToString(randomMatrix);

        HashMap<String, Integer> playerScore = null;

        if (this.sessionNumber == 1) {
            try {
                // The enter sessions in the DB already exists. We only need to populate the
                // characters field and we don't need to reach the gamer score because it's 0
                playerScore = this.gameUtil.calculateCurrentPlayerScore(this.sessionNumber, this.idGame,
                        this.gameClientObservers);
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
                pst = this.dbConnection.prepareStatement(sqlUpdate);
                pst.setString(1, "playing");
                pst.setInt(2, this.idGame);
                this.db.performChangeState(pst);

                pst.close();
                this.dbConnection.close();
            } catch (SQLException exc) {
                System.err.println("Error while contacting the db " + exc);
            } catch (RemoteException exc) {
                System.err.println("Error while contacting the client " + exc);
            }
        } else {
            try {
                ResultSet result = this.gameUtil.checkReached50Score(this.idGame);
                if (result.isBeforeFirst()) {
                    // A user won
                    result.next();
                    for (GameClient singlePlayer : this.gameClientObservers) {
                        try {
                            singlePlayer.gameWonByUser(result.getString("username_user"));
                        } catch (RemoteException exc) {
                            System.err.println("Error while contacting the " + singlePlayer.getEmail() + "player");
                        }
                    }

                    this.dbConnection = this.db.getDatabaseConnection();
                    String sqlUpdate = "UPDATE game SET status = ? WHERE id = ?";
                    PreparedStatement pst = this.dbConnection.prepareStatement(sqlUpdate);
                    pst.setString(1, "closed");
                    pst.setInt(2, this.idGame);
                    this.db.performChangeState(pst);

                    pst.close();
                    this.dbConnection.close();

                    this.removeObject();
                } else {
                    // New session needs to be triggered
                    this.gameUtil.createNewEnterForNewSession(this.idGame, this.sessionNumber, stringMatrix,
                            this.gameClientObservers);
                    playerScore = this.gameUtil.calculateCurrentPlayerScore(this.sessionNumber, this.idGame,
                            this.gameClientObservers);
                }
            } catch (SQLException exc) {
                System.err.println("Error while contacting the db " + exc);
            } catch (Exception exc) {
                System.err.println("Error in the current thread" + exc);
            }
        }

        this.randomMatrix = randomMatrix;

        for (GameClient item : this.gameClientObservers) {
            try {
                item.confirmGameSession(this.name, this.sessionNumber, randomMatrix,
                        playerScore);
            } catch (RemoteException exc) {
                System.err.println("Error while contacting the player");
            }
        }
        this.timerThread = new TimerThread("isPlaying", this, this.gameClientObservers);
        this.timerThread.start();
    }

    /**
     * This method is called when all the players sent the discovered words for the
     * specific session and the thread needs to retrieve them all and sent to the
     * players for review.
     */
    private void retrieveGameSessionWords() {
        System.out.println("Retrieving the words proposed in this session...");
        ArrayList<WordRecord> acceptedArray = new ArrayList<WordRecord>(), refusedArray = new ArrayList<WordRecord>();

        try {
            ResultSet accepted = this.gameUtil.getAcceptedWordForGameSession(this.idGame, this.sessionNumber),
                    refused = this.gameUtil.getRefusedWordForGameSession(this.idGame, this.sessionNumber);

            // Populating the ArrayList of accepted words
            while (accepted.next()) {
                acceptedArray.add(new WordRecord(accepted.getString("word"), accepted.getString("username_user"),
                        accepted.getInt("score")));
            }
            // Populating the ArrayList of refused words
            while (refused.next()) {
                refusedArray.add(new WordRecord(refused.getString("word"), refused.getString("username_user"),
                        refused.getInt("score"), refused.getString("reason")));
            }
        } catch (SQLException exc) {
            System.err.println("Error while performing DB operations " + exc);
        }

        for (GameClient singlePlayer : this.gameClientObservers) {
            try {
                singlePlayer.sendWordsDiscoveredInSession(acceptedArray, refusedArray);
            } catch (RemoteException exc) {
                System.err.println("Error while contacting the player");
            }
        }

        this.timerThread = new TimerThread("isReviewing", this, this.gameClientObservers);
    }

    /**
     * Service method invoked when the object needs to be removed
     * 
     * @throws Exception - If any other exception occurs (while the thread
     *                   interruption and unbind of the object), it throws Exception
     */
    private void removeObject() throws Exception {
        System.out.println("Removing the RMI object");

        Registry registry = LocateRegistry.getRegistry(1099);
        registry.unbind(Integer.toString(this.idGame));
    }

    /**
     * Service method invoked when the game needs to be removed from the DB and the
     * RMI object removed
     * 
     * @throws SQLException - If there is an error while the DB operations, it
     *                      throws SQLException
     * @throws Exception    - If any other exception occurs (while the thread
     *                      interruption and unbind of the object), it throws
     *                      Exception
     */
    private void removeGame() throws SQLException, Exception {
        String sqlDelete = "DELETE FROM game WHERE id = ?";
        this.dbConnection = this.db.getDatabaseConnection();

        PreparedStatement pst = this.dbConnection.prepareStatement(sqlDelete);
        pst.setInt(1, this.idGame);
        this.db.performChangeState(pst);
        System.out.println("Gamed removed");

        this.removeObject();
    }

    /**
     * Service method that is triggered before the first session of the game is
     * started. It will count 30 seconds and then trigger the first session.
     * 
     * @param seconds - Seconds left before the first session will be triggered
     */
    private void handleTimer(int seconds) {
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

    /**
     * Service method triggered by the timer instance to let the players know the
     * session is ended
     */
    public void triggerEndOfSessionGameClient() {
        System.out.println("Triggering the end of game on clients...");
        for (GameClient singlePlayer : this.gameClientObservers) {
            CompletableFuture.runAsync(() -> {
                try {
                    singlePlayer.triggerEndOfSession();
                } catch (RemoteException exc) {
                    System.err.println("Error while contacting the client " + exc);
                }
            });
        }
        System.out.println("End of triggerEndOfSessionGameClient method");
    }

    /**
     * This method will create a new game record in the DB and will register to the
     * game the user that started the game (adding an "enter" record in the DB) It
     * will add the creator to the gameClientObservers array because the client is
     * now an observer of the observable thread
     * 
     * @throws SQLException    - If there is an error while the DB operations, it
     *                         throws SQLException
     * @throws RemoteException - If there is an error while the client contact, it
     *                         throws RemoteException
     */
    public void createNewGame() throws SQLException, RemoteException {
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

        System.out.println("Created the game and added the creator to it");

        pst.close();
        this.dbConnection.close();
    }

    /**
     * This method will add the player client as a new observer and will register
     * the player to the game adding a new record in the "enter" table This
     * operation is only allowed whether the max player of the game is not reached
     * yet If the max player number is reached after having added the user, the
     * timer for the start of the game will be triggered
     * 
     * @param player - The reference of the client to be added
     * 
     * @throws RemoteException - If there is an error while the client contact, it
     *                         throws RemoteException
     */
    public void addNewPlayer(GameClient player) throws RemoteException {
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

    /**
     * This method will delete the player that made the request when the game has
     * not started yet. If there are no more players left in the game, this will be
     * deleted and the thread terminated
     * 
     * @param player - The reference of the player to delete from the game
     * 
     * @throws RemoteException - If there is an error while the client contact, it
     *                         throws RemoteException
     */
    public void removePlayerNotStartedGame(GameClient player) throws RemoteException {
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

    /**
     * This method is called when a player leaves the game while this has started.
     * Because of this action, the whole game will be removed and the thread
     * terminated
     * 
     * @param player - The reference of the player that left the game
     * 
     * @throws RemoteException - If there is an error while the client contact, it
     *                         throws RemoteException
     */
    public void removePlayerInGame(GameClient player) throws RemoteException {
        System.out.println("Removing a user to started game...");

        try {
            this.dbConnection = this.db.getDatabaseConnection();
            System.out.println("Removing the whole game, sessions and words discovered...");

            for (GameClient singlePlayer : this.gameClientObservers) {
                CompletableFuture.runAsync(() -> {
                    try {
                        singlePlayer.gameHasBeenRemoved("Player " + player.getUsername() + " left the game");
                    } catch(RemoteException exc) {
                        exc.printStackTrace();
                    }
                });
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

    /**
     * This method is called from each player when the session is end and the thread
     * needs to check the words discovered by the players. If the word is valid
     * (both length, matrix, and dictionary) it will be registered with the score;
     * if not, it will be registered with score 0 and with the reason. If all users
     * have sent the list of words, the thread will progress and retrieve the list
     * of words to each player
     * 
     * @param player    - The reference of the player that sent the list of words
     * @param wordsList - The list of words discovered by the player in the game
     *                  session
     * 
     * @throws RemoteException - If there is an error while the client contact, it
     *                         throws RemoteException
     */
    public void checkPlayerWords(GameClient player, ArrayList<String> wordsList) throws RemoteException {
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
                    // Check the word exists in the matrix
                    if (this.gameUtil.checkWordInMatrix(this.randomMatrix, singleWord)) {
                        if (singleWord.length() >= 3) {
                            String sqlQuery = "SELECT * FROM discover WHERE word = ? AND id_game = ? AND session_number_enter = ?";
                            PreparedStatement pst1 = null;
                            try {
                                pst1 =  dbConnection.prepareStatement(sqlQuery);
                                pst1.setString(1, singleWord);
                                pst1.setInt(2, this.idGame);
                                pst1.setInt(3, this.sessionNumber);
                            } catch (SQLException exc) {
                                System.err.println("Error while establishing the connection with the DB " + exc);
                                System.exit(1);
                            }

                            ResultSet result = this.db.peroformComplexQuery(pst1);

                            if (result.isBeforeFirst()) {
                                // This word already exists
                                pst.setInt(6, 0);
                                pst.setBoolean(7, false);
                                pst.setString(8, "Already proposed by another user");

                                this.gameUtil.invalidateOtherPlayersSameWords(singleWord, this.idGame,
                                        this.sessionNumber);
                            } else {
                                pst.setInt(6, this.gameUtil.getCurrentWordScore(singleWord));
                                pst.setBoolean(7, true);
                                pst.setString(8, "");
                            }

                            result.close();
                            pst1.close();
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
            this.triggerNextStep = 0;
            CompletableFuture.runAsync(() -> {
                this.retrieveGameSessionWords();
            });
        }
    }

    /**
     * This method is called from the players that want to retrieve the definition
     * of a specific word. The method first increment the number of requests for the
     * word (it increases a counter field in the DB), then it returns the definition
     * to the client. It interacts with the Dictionary to retrieve the definition
     * 
     * @param player - The reference of the player that made the definition request
     * @param word   - The word the user made the definition request for
     * 
     * @throws RemoteException - If there is an error while the client contact, it
     *                         throws RemoteException
     */
    public void askForWordDefinition(GameClient player, String word) throws RemoteException {
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

    /**
     * This is a service method invoked by the timer instance to let the game knows
     * a new session will be done
     * 
     * @throws SQLException - If there is an error while the DB operations, it
     *                      throws SQLException
     */
    public void increaseSessionNumber() throws SQLException {
        this.sessionNumber++;
        this.gameUtil.increaseNumberOfRounds(this.idGame);
    }

    /**
     * Method started by the ServerImpl object when a new game needs to be created. It will call the
     * createNewGame() method to create a new game in DB and registering the user
     * that created the game to the game (creating an "enter" record in the DB). If
     * the creation is fine, the thread reference is registered in the RMI registry
     * to be found by other users
     */
    public void run() {
        boolean flag = true;
        this.idGame = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);

        try {
            this.createNewGame();
        } catch (RemoteException exc) {
            flag = false;
            System.err.println("Error while contacting the client " + exc);
            try {
                this.gameCreator.errorCreateNewGame("Error while contacting the client " + exc);
            } catch (RemoteException e) {
            }
        } catch (SQLException exc) {
            flag = false;
            System.err.println("Error while performing DB operations " + exc);
            try {
                this.gameCreator.errorCreateNewGame("Error while performing DB operations " + exc);
            } catch (RemoteException e) {
            }
        }

        if (flag) {
            try {
                Registry registry = LocateRegistry.getRegistry(1099);
                registry.rebind(Integer.toString(this.idGame), this);
                this.gameCreator.confirmCreateNewGame(Integer.toString(this.idGame));

                System.out.println("Game thread " + this.idGame + " is listening...");
            } catch (Exception e) {
                System.err.println("Error while registering " + this.idGame + " game thread");
            }
        }
    }
}