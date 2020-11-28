package com.insubria.it.server.threads.gameThread.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.insubria.it.server.base.abstracts.Database;

import com.insubria.it.server.threads.gameThread.interfaces.GameClient;


public class GameThreadUtils {
    private Database db;
    private Connection dbConnection;

    public GameThreadUtils (Database db) {
        this.db = db;
    }

    public HashMap<String, Integer> calculateCurrentPlayerScore (int sessionNumber, ArrayList<GameClient> gameClientObservers) {
        HashMap<String, Integer> returnValue = new HashMap<String, Integer>();
        if (sessionNumber == 1) {
            for (GameClient item : gameClientObservers) {
                returnValue.put(item.getUsername(), 0);
            }
        } else {
            // @TODO Perform queries to reach the current score of the user
        }
        return returnValue;
    }

    public String setMatrixToString (String[][] matrix) {
        String returnValue = "";
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                returnValue += matrix[i][j];
            }
        }
        return returnValue;
    }

    public int getCurrentWordScore (String word) {
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

    public void invalidateOtherPlayersSameWords (String word, int idGame, int sessionNumber) throws SQLException {
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

    public ResultSet getAcceptedWordForGameSession (int idGame, int sessionNumber) throws SQLException {
        String sqlQuery = "SELECT word, username_user, score " +
                          "FROM discover " +
                          "WHERE id_game = " + idGame + " AND session_number_enter = " + sessionNumber + " AND score > 0 " +
                          "ORDER BY score DESC";
        return this.db.performSimpleQuery(sqlQuery);
    }

    public ResultSet getRefusedWordForGameSession (int idGame, int sessionNumber) throws SQLException {
        String sqlQuery = "SELECT word, username_user, score, reason " +
                          "FROM discover " +
                          "WHERE id_game = " + idGame + " AND session_number_enter = " + sessionNumber + " AND score = 0";
        return this.db.performSimpleQuery(sqlQuery);
    }

    public void increaseNumberOfDefinitionRequests (int idGame, int sessionNumber, String word) throws SQLException {
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
}