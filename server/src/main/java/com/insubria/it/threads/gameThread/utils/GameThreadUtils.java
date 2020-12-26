package com.insubria.it.threads.gameThread.utils;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import com.insubria.it.base.abstracts.Database;
import com.insubria.it.sharedserver.threads.gameThread.interfaces.GameClient;


/**
 * This class is used by the GameThread because it offers some utils
 * (implemented here to avoid code duplications) such as DB queries
 */
public class GameThreadUtils {
    /**
     * The reference to the DatabaseController object
     */
    private Database db;

    /**
     * The reference to the DB Connection object
     */
    private Connection dbConnection;

    /**
     * Constructor to initialize instances of this class
     * 
     * @param db - The reference to the DatabaseController object
     */
    public GameThreadUtils(Database db) {
        this.db = db;
    }

    /**
     * This method calculates the current score for each player and returns an
     * HashMap where each key is the username of the user and the value its score.
     * If the session is the first one, everybode will have a zero score, otherwise
     * the method will perform DB queries to reach the actual score.
     * 
     * @return - The HasMap with the usernames and scores
     * @param sessionNumber       - The number of the session the game reached
     * @param idGame              - The id of the game
     * @param gameClientObservers - The list of players (observers) that are playing
     *                            the game
     */
    public HashMap<String, Integer> calculateCurrentPlayerScore(int sessionNumber, int idGame,
            ArrayList<GameClient> gameClientObservers) {
        HashMap<String, Integer> returnValue = new HashMap<String, Integer>();

        try {
            if (sessionNumber == 1) {
                for (GameClient item : gameClientObservers) {
                    returnValue.put(item.getUsername(), 0);
                }
            } else {
                ResultSet result;
                String sqlQuery;

                Statement stm = null;
                try {
                    this.dbConnection = this.db.getDatabaseConnection();
                    stm = this.dbConnection.createStatement();
                } catch (SQLException exc) {
                    System.err.println("Error while establishing the connection with the DB " + exc);
                }

                for (GameClient item : gameClientObservers) {
                    sqlQuery = "SELECT SUM(score) as total_score " + "FROM discover " + "WHERE email_user = "
                            + item.getEmail() + " AND id_game = " + idGame;
                    try {
                        result = this.db.performSimpleQuery(sqlQuery, stm);
                        if (result.isBeforeFirst()) {
                            result.next();
                            returnValue.put(item.getUsername(), result.getInt("total_score"));
                        }
                    } catch (SQLException exc) {
                        System.err.println("Error while contacting the db " + exc);
                    }
                }

                stm.close();
                this.dbConnection.close();
            }
        } catch (RemoteException exc) {
            System.err.println("Error while contacting the player");
        } catch (SQLException exc) {
            System.err.println("Error while closing the connection with the DB " + exc);
        }
        return returnValue;
    }

    /**
     * This method transforms a String matrix in a single string. The different rows
     * are interlocked as a single row
     * 
     * @return - The string that represents the matrix
     * @param matrix - The matrix to work on
     */
    public String setMatrixToString(String[][] matrix) {
        String returnValue = "";
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                returnValue += matrix[i][j];
            }
        }
        return returnValue;
    }

    /**
     * This method returns the score associated to a word depending on its length
     * 
     * @return - The score associated to the word
     * @param word - The word
     */
    public int getCurrentWordScore(String word) {
        switch (word.length()) {
            case 3:
            case 4: {
                return 1;
            }
            case 5: {
                return 2;
            }
            case 6: {
                return 3;
            }
            case 7: {
                return 5;
            }
            default: {
                return 11;
            }
        }
    }

    /**
     * This method set the score of particular words to 0 because they have been
     * proposed multiple times in a single session of a game
     * 
     * @param word          - The word to invalidate
     * @param idGame        - The idGame the word has been proposed
     * @param sessionNumber - The session number the word has been proposed
     * 
     * @throws SQLException - If there is an error while the DB operations, it
     *                      throws SQLException
     */
    public void invalidateOtherPlayersSameWords(String word, int idGame, int sessionNumber) throws SQLException {
        this.dbConnection = this.db.getDatabaseConnection();

        String sqlUpdate = "UPDATE discover SET is_valid = FALSE, score = 0, reason = ? WHERE word = ? AND id_game = ? AND session_number_enter = ?";
        PreparedStatement pst = this.dbConnection.prepareStatement(sqlUpdate);
        pst.setString(1, "Already proposed by another user");
        pst.setString(2, word);
        pst.setInt(3, idGame);
        pst.setInt(4, sessionNumber);
        this.db.performChangeState(pst);

        pst.close();
        this.dbConnection.close();
    }

    /**
     * This method will query all the words proposed in a single session of a
     * specific game
     * 
     * @param idGame        - The id of the game
     * @param sessionNumber - The session number the query will be associated to
     * 
     * @return - The ResultSet object that contains the words discovered
     * 
     * @throws SQLException - If there is an error while the DB operations, it
     *                      throws SQLException
     */
    public ResultSet getAcceptedWordForGameSession(int idGame, int sessionNumber) throws SQLException {
        String sqlQuery = "SELECT word, username_user, score " + "FROM discover " + "WHERE id_game = " + idGame
                + " AND session_number_enter = " + sessionNumber + " AND score > 0 " + "ORDER BY score DESC";
        Statement stm = null;
        try {
            this.dbConnection = this.db.getDatabaseConnection();
            stm = this.dbConnection.createStatement();
        } catch (SQLException exc) {
            System.err.println("Error while establishing the connection with the DB " + exc);
        }
        return this.db.performSimpleQuery(sqlQuery, stm);
    }

    /**
     * This method will query all the refused words proposed in a single session of
     * a specific game
     * 
     * @param idGame        - The id of the game
     * @param sessionNumber - The session number the query will be associated to
     * 
     * @return - The ResultSet object that contains the refused words discovered
     * 
     * @throws SQLException - If there is an error while the DB operations, it
     *                      throws SQLException
     */
    public ResultSet getRefusedWordForGameSession(int idGame, int sessionNumber) throws SQLException {
        String sqlQuery = "SELECT word, username_user, score, reason " + "FROM discover " + "WHERE id_game = " + idGame
                + " AND session_number_enter = " + sessionNumber + " AND score = 0";
        Statement stm = null;
        try {
            this.dbConnection = this.db.getDatabaseConnection();
            stm = this.dbConnection.createStatement();
        } catch (SQLException exc) {
            System.err.println("Error while establishing the connection with the DB " + exc);
        }
        return this.db.performSimpleQuery(sqlQuery, stm);
    }

    /**
     * This method will increase the n_requests params for the specific word
     * discovered in a session of the game (because a user required its definition)
     * 
     * @param idGame        - The id of the game
     * @param sessionNumber - The session number
     * @param word          - The word to increment the n_request
     * 
     * @throws SQLException - If there is an error while the DB operations, it
     *                      throws SQLException
     */
    public void increaseNumberOfDefinitionRequests(int idGame, int sessionNumber, String word) throws SQLException {
        this.dbConnection = this.db.getDatabaseConnection();

        String sqlUpdate = "UPDATE discover SET n_requests = n_requests + 1 WHERE word = ? AND id_game = ? AND session_number_enter = ?";
        PreparedStatement pst = this.dbConnection.prepareStatement(sqlUpdate);
        pst.setString(1, word);
        pst.setInt(2, idGame);
        pst.setInt(3, sessionNumber);
        this.db.performChangeState(pst);

        pst.close();
        this.dbConnection.close();
    }

    /**
     * This method will perform a query to reach the users that reached 50 score or
     * more (if anyone did)
     * 
     * @return - The ResultSet object with the users that reached 50 score
     * 
     * @param idGame - The id of the game to check for
     * 
     * @throws SQLException - If there is an error while the DB operations, it
     *                      throws SQLException
     */
    public ResultSet checkReached50Score(int idGame) throws SQLException {
        String sqlQuery = "SELECT username_user " + "FROM discover " + "WHERE id_game = " + idGame + " "
                + "GROUP BY username_user " + "HAVING SUM(score) >= 50";
        Statement stm = null;
        try {
            this.dbConnection = this.db.getDatabaseConnection();
            stm = this.dbConnection.createStatement();
        } catch (SQLException exc) {
            System.err.println("Error while establishing the connection with the DB " + exc);
        }

        return this.db.performSimpleQuery(sqlQuery, stm);
    }

    /**
     * This method will create a new session partecipation (enter table record) to a
     * game for each player
     * 
     * @param idGame        - The id of the game
     * @param sessionNumber - The session number
     * @param stringMatrix  - The matrix for the specified game session
     * @param players       - The list of players to register to the new game
     *                      session
     * 
     * @throws SQLException - If there is an error while the DB operations, it
     *                      throws SQLException
     */
    public void createNewEnterForNewSession(int idGame, int sessionNumber, String stringMatrix,
            ArrayList<GameClient> players) throws SQLException {
        this.dbConnection = this.db.getDatabaseConnection();

        String sqlInsert = "INSERT INTO enter (id_game, email_user, username_user, session_number, characters) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pst = null;

        try {
            for (GameClient single : players) {
                pst = this.dbConnection.prepareStatement(sqlInsert);

                pst.setInt(1, idGame);
                pst.setString(2, single.getEmail());
                pst.setString(3, single.getUsername());
                pst.setInt(4, sessionNumber);
                pst.setString(5, stringMatrix);
                this.db.performChangeState(pst);
            }
        } catch (RemoteException exc) {
            System.err.println("Error while contacting the player");
        }

        pst.close();
        this.dbConnection.close();
    }

    /**
     * This method increase the n_rounds field for the game record in the DB
     * 
     * @param idGame - The id of the game
     * 
     * @throws SQLException - If there is an error while the DB operations, it
     *                      throws SQLException
     */
    public void increaseNumberOfRounds(int idGame) throws SQLException {
        this.dbConnection = this.db.getDatabaseConnection();

        String sqlUpdate = "UPDATE game SET n_rounds = n_rounds + 1 WHERE id = " + idGame;
        PreparedStatement pst = this.dbConnection.prepareStatement(sqlUpdate);
        this.db.performChangeState(pst);

        pst.close();
        this.dbConnection.close();
    }
}