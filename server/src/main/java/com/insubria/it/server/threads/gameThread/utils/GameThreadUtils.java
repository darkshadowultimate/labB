package com.insubria.it.server.threads.gameThread.utils;

import java.sql.Connection;
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
}