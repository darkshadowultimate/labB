package com.insubria.it.server.threads.monitorThread;


import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

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
    private int page;

    private Database db;
    private final String action;

    public MonitorThread (MonitorClient monitorClient, String action) {
        this.monitorClient = monitorClient;
        this.action = action;
    }

    public MonitorThread (MonitorClient monitorClient, int page, String action) {
        this.monitorClient = monitorClient;
        this.page = page;
        this.action = action;
    }

    protected void moreScoreGameAndSession () throws RemoteException, SQLException {
        System.out.println("Reaching the user with maximum score per game and per session...");
        String sqlQuery1 = "SELECT id_game, username_user, email_user, SUM(score) as score " +
                           "FROM discover " +
                           "WHERE is_valid = True " +
                           "GROUP BY id_game, username_user, email_user " +
                           "ORDER BY SUM(score) DESC " +
                           "LIMIT 1;";
        String sqlQuery2 = "SELECT id_game, session_number_enter, username_user, email_user, SUM(score) as score " +
                           "FROM discover " +
                           "WHERE is_valid = True " +
                           "GROUP BY id_game, session_number_enter, username_user, email_user " +
                           "ORDER BY SUM(score) DESC " +
                           "LIMIT 1;";
        ResultSet result1 = this.db.performSimpleQuery(sqlQuery1), result2 = this.db.performSimpleQuery(sqlQuery2);
        if (result1.isBeforeFirst() && result2.isBeforeFirst()) {
            result1.next(); result2.next();
            System.out.println("Successfully performed the query");
            String[] returnArray = new String[2];
            returnArray[0] = result1.getString("id_game") + " " + result1.getString("username_user") + " " + result1.getString("email_user") + " " + result1.getInt("score");
            returnArray[1] = result2.getString("id_game") + " " + result2.getInt("session_number") + " " + result2.getString("username_user") + " " + result2.getString("email_user") + " " + result2.getInt("score");
            this.monitorClient.confirmMoreScoreGameAndSession(returnArray);
        } else {
            System.out.println("No sessions played yet");
            this.monitorClient.errorMoreScoreGameAndSession("No sessions played yet");
        }
        result1.close();
        result2.close();
    }

    protected void moreSessionsPlayed () throws RemoteException, SQLException {
        System.out.println("Reaching the user with the maximum number of sessions played...");
        String sqlQuery = "SELECT u.email, u.username, COUNT(*) as number " +
                          "FROM users as u INNER JOIN enter as e ON u.email = e.email_user and u.username = e.username_user " +
                          "GROUP BY u.email, u.username " +
                          "ORDER BY number DESC;";
        ResultSet result = this.db.performSimpleQuery(sqlQuery);
        if (result.isBeforeFirst()) {
            result.next();
            System.out.println("Successfully performed the query");
            String returnString = result.getString("email") + " " + result.getString("username") + " " + result.getInt("number");
            this.monitorClient.confirmMoreSessionsPlayed(returnString);
        } else {
            System.out.println("No sessions played yet");
            this.monitorClient.errorMoreSessionsPlayed("No sessions played yet");
        }
        result.close();
    }

    protected void moreAvgScoreGameAndSession () throws RemoteException, SQLException {
        System.out.println("Reaching the user with maximum avg score per game and per session...");
        String sqlQuery1 = "SELECT id_game, username_user, email_user, AVG(score) as score " +
                           "FROM discover " +
                           "WHERE is_valid = True " +
                           "GROUP BY id_game, username_user, email_user " +
                           "ORDER BY AVG(score) DESC " +
                           "LIMIT 1;";
        String sqlQuery2 = "SELECT id_game, session_number_enter, username_user, email_user, AVG(score) as score " +
                           "FROM discover " +
                           "WHERE is_valid = True " +
                           "GROUP BY id_game, session_number_enter, username_user, email_user " +
                           "ORDER BY AVG(score) DESC " +
                           "LIMIT 1;";
        ResultSet result1 = this.db.performSimpleQuery(sqlQuery1), result2 = this.db.performSimpleQuery(sqlQuery2);
        if (result1.isBeforeFirst() && result2.isBeforeFirst()) {
            result1.next(); result2.next();
            System.out.println("Successfully performed the query");
            String[] returnArray = new String[2];
            returnArray[0] = result1.getString("id_game") + " " + result1.getString("username_user") + " " + result1.getString("email_user") + " " + result1.getInt("score");
            returnArray[1] = result2.getString("id_game") + " " + result2.getInt("session_number") + " " + result2.getString("username_user") + " " + result2.getString("email_user") + " " + result2.getInt("score");
            this.monitorClient.confirmMoreAvgScoreGameAndSession(returnArray);
        } else {
            System.out.println("No sessions played yet");
            this.monitorClient.errorMoreAvgScoreGameAndSession("No sessions played yet");
        }
        result1.close();
        result2.close();
    }

    protected void moreProposedDuplicatedWords () throws RemoteException, SQLException {
        System.out.println("Reaching the user that proposed the highest number of duplicated words...");
        String sqlQuery = "SELECT email_user, username_user, COUNT(*) as number " +
                          "FROM discover as d " +
                          "WHERE EXISTS (" +
                                "SELECT word " +
                                "FROM discover as di " +
                                "WHERE di.word = d.word AND di.id_game = d.id_game AND di.session_number_enter = d.session_number_enter AND di.email_user != d.email_user" +
                          ") " +
                          "GROUP BY email_user, username_user " +
                          "ORDER BY number DESC;";
        ResultSet result = this.db.performSimpleQuery(sqlQuery);
        if (result.isBeforeFirst()) {
            result.next();
            System.out.println("Successfully performed the query");
            String returnString = result.getString("email_user") + " " + result.getString("username_user") + " " + result.getInt("number");
            this.monitorClient.confirmMoreProposedDuplicatedWords(returnString);
        } else {
            System.out.println("No sessions played yet");
            this.monitorClient.errorMoreProposedDuplicatedWords("No sessions played yet");
        }
        result.close();
    }

    protected void moreInvalidProposedWords () throws RemoteException, SQLException {
        System.out.println("Reaching the user that proposed the highest number of invalid words...");
        String sqlQuery = "SELECT email_user, username_user, COUNT(*) as number " +
                          "FROM discover " +
                          "WHERE is_valid = FALSE " +
                          "GROUP BY email_user, username_user " +
                          "ORDER BY number DESC;";
        ResultSet result = this.db.performSimpleQuery(sqlQuery);
        if (result.isBeforeFirst()) {
            result.next();
            System.out.println("Successfully performed the query");
            String returnString = result.getString("email_user") + " " + result.getString("username_user") + " " + result.getInt("number");
            this.monitorClient.confirmMoreInvalidProposedWords(returnString);
        } else {
            System.out.println("No sessions played yet");
            this.monitorClient.errorMoreInvalidProposedWords("No sessions played yet");
        }
        result.close();
    }

    private String[] transformString (ResultSet result, int lenght) throws SQLException {
        String[] returnArray = new String[10];
        String tmp = "";
        int i = 0;
        while (result.next()) {
            for (int index = 0; index < lenght; index++) {
                tmp += (String) result.getObject(index + 1) + " ";
            }
            returnArray[i++] = tmp;
            tmp = "";
        }
        return returnArray;
    }

    protected void validWordsOccurrences (int page) throws RemoteException, SQLException {
        System.out.println("Reaching the valid word occurrences list...");
        String sqlQuery = "SELECT word, COUNT(*) as occurrences " +
                          "FROM discover " +
                          "WHERE is_valid = True " +
                          "GROUP BY word " +
                          "ORDER BY occurrences DESC " +
                          "LIMIT 10 OFFSET " + (page - 1) * 10 + ";";
        ResultSet result = this.db.performSimpleQuery(sqlQuery);
        if (result.isBeforeFirst()) {
            System.out.println("Successfully performed the query");
            String[] clientResult = this.transformString(result, 2);
            this.monitorClient.confirmValidWordsOccurrences(clientResult);
        } else {
            System.out.println("No sessions played yet");
            this.monitorClient.errorValidWordsOccurrences("No sessions played yet");
        }
        result.close();
    }

    protected void wordHighestScore (int page) throws RemoteException, SQLException {
        System.out.println("Reaching the highest score valid words...");
        String sqlQuery = "SELECT DISTINCT word, id_game, score " +
                          "FROM discover " +
                          "WHERE is_valid = True " +
                          "ORDER BY score DESC;";
        ResultSet result = this.db.performSimpleQuery(sqlQuery);
        if (result.isBeforeFirst()) {
            System.out.println("Successfully performed the query");
            String[] clientResult = this.transformString(result, 3);
            this.monitorClient.confirmWordHighestScore(clientResult);
        } else {
            System.out.println("No sessions played yet");
            this.monitorClient.errorWordHighestScore("No sessions played yet");
        }
        result.close();
    }

    protected void averageRounds () throws RemoteException, SQLException {
        System.out.println("Reaching the average rounds for games...");
        String sqlQuery = "SELECT max_players, AVG(n_rounds) as average_rounds " +
                          "FROM game " +
                          "WHERE status = 'closed' " +
                          "GROUP BY max_players;";
        ResultSet result = this.db.performSimpleQuery(sqlQuery);
        if (result.isBeforeFirst()) {
            System.out.println("Successfully performed the query");
            String[] clientResult = this.transformString(result, 2);
            this.monitorClient.confirmAverageRounds(clientResult);
        } else {
            System.out.println("No sessions played yet");
            this.monitorClient.errorAverageRounds("No sessions played yet");
        }
        result.close();
    }

    protected void minMaxRounds () throws RemoteException, SQLException {
        System.out.println("Reaching the min/max rounds for games...");
        String sqlQuery = "SELECT max_players, MAX(n_rounds) as max_round, MIN(n_rounds) as min_round " +
                          "FROM game " +
                          "WHERE status = 'closed' " +
                          "GROUP BY max_players;";
        ResultSet result = this.db.performSimpleQuery(sqlQuery);
        if (result.isBeforeFirst()) {
            System.out.println("Successfully performed the query");
            String[] clientResult = this.transformString(result, 3);
            this.monitorClient.confirmMinMaxRounds(clientResult);
        } else {
            System.out.println("No sessions played yet");
            this.monitorClient.errorMinMaxRounds("No sessions played yet");
        }
        result.close();
    }

    private void calculateAvgForEachItemInHashMap (HashMap<Character, Double> hashMap, int nRows) {
        for (Map.Entry<Character, Double> item : hashMap.entrySet()) {
            hashMap.replace(item.getKey(), item.getValue() / nRows);
        }
    }

    private HashMap<Character, Double> mapAvgOccurrence (ResultSet result) throws SQLException {
        HashMap<Character, Double> hashMap = new HashMap<Character, Double>();
        String charsInMatrix = "";
        char singleChar = 0;
        int nRows = 0;
        while (result.next()) {
            charsInMatrix = result.getString("characters");
            for (int i = 0; i < charsInMatrix.length(); i++) {
                singleChar = charsInMatrix.charAt(i);
                if (hashMap.containsKey(singleChar)) {
                    hashMap.replace(singleChar, hashMap.get(singleChar) + 1.0);
                } else {
                    hashMap.put(singleChar, 1.0);
                }
            }
            nRows++;
        }
        this.calculateAvgForEachItemInHashMap(hashMap, nRows);
        return hashMap;
    }

    protected void charactersAvgOccurrence () throws RemoteException, SQLException {
        System.out.println("Reaching the characters average occurrence...");
        String sqlQuery = "SELECT characters " +
                          "FROM enter;";
        ResultSet result = this.db.performSimpleQuery(sqlQuery);
        if (result.isBeforeFirst()) {
            System.out.println("Successfully performed the query");
            HashMap<Character, Double> hashMap = this.mapAvgOccurrence(result);
            this.monitorClient.confirmCharactersAvgOccurrence(hashMap);
        } else {
            System.out.println("No sessions played yet");
            this.monitorClient.errorCharactersAvgOccurrence("No sessions played yet");
        }
        result.close();
    }

    protected void definitionRequest (int page) throws RemoteException, SQLException {
        System.out.println("Reaching the words users required the definition...");
        String sqlQuery = "SELECT word, AVG(n_requests) as avg_requests " +
                          "FROM discover " +
                          "WHERE n_requests > 0 " +
                          "GROUP BY word " +
                          "ORDER BY AVG(n_requests) DESC " +
                          "LIMIT 10 OFFSET " + (page - 1) * 10 + ";";
        ResultSet result = this.db.performSimpleQuery(sqlQuery);
        if (result.isBeforeFirst()) {
            System.out.println("Successfully performed the query");
            String[] clientResult = this.transformString(result, 2);
            this.monitorClient.confirmDefinitionRequest(clientResult);
        } else {
            System.out.println("No sessions played yet");
            this.monitorClient.errorDefinitionRequest("No sessions played yet");
        }
        result.close();
    }

    protected void gameDefinitionRequest (int page) throws RemoteException, SQLException {
        System.out.println("Reaching the games where users required the definition...");
        String sqlQuery = "SELECT DISTINCT id_game " +
                          "FROM discover " +
                          "WHERE n_requests > 0 " +
                          "LIMIT 10 OFFSET " + (page - 1) * 10 + ";";
        ResultSet result = this.db.performSimpleQuery(sqlQuery);
        if (result.isBeforeFirst()) {
            System.out.println("Successfully performed the query");
            String[] clientResult = this.transformString(result, 1);
            this.monitorClient.confirmGameDefinitionRequest(clientResult);
        } else {
            System.out.println("No sessions played yet");
            this.monitorClient.errorGameDefinitionRequest("No sessions played yet");
        }
        result.close();
    }

    public void run () {
        switch (this.action) {
            case "moreScoreGameAndSession": {
                try {
                    this.moreScoreGameAndSession();
                } catch (RemoteException exc) {
                    System.err.println("Error while contacting the client " + exc);
                    try {
                        this.monitorClient.errorMoreScoreGameAndSession("Error while contacting the client " + exc);
                    } catch (RemoteException e) {}
                } catch (SQLException exc) {
                    try {
                        this.monitorClient.errorMoreScoreGameAndSession("Error while performing DB operations " + exc);
                    } catch (RemoteException e) {}
                }
                break;
            }
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
                break;
            }
            case "moreAvgScoreGameAndSession": {
                try {
                    this.moreAvgScoreGameAndSession();
                } catch (RemoteException exc) {
                    System.err.println("Error while contacting the client " + exc);
                    try {
                        this.monitorClient.errorMoreAvgScoreGameAndSession("Error while contacting the client " + exc);
                    } catch (RemoteException e) {}
                } catch (SQLException exc) {
                    try {
                        this.monitorClient.errorMoreAvgScoreGameAndSession("Error while performing DB operations " + exc);
                    } catch (RemoteException e) {}
                }
                break;
            }
            case "moreProposedDuplicatedWords": {
                try {
                    this.moreProposedDuplicatedWords();
                } catch (RemoteException exc) {
                    System.err.println("Error while contacting the client " + exc);
                    try {
                        this.monitorClient.errorMoreProposedDuplicatedWords("Error while contacting the client " + exc);
                    } catch (RemoteException e) {}
                } catch (SQLException exc) {
                    try {
                        this.monitorClient.errorMoreProposedDuplicatedWords("Error while performing DB operations " + exc);
                    } catch (RemoteException e) {}
                }
                break;
            }
            case "moreInvalidWords": {
                try {
                    this.moreInvalidProposedWords();
                } catch (RemoteException exc) {
                    System.err.println("Error while contacting the client " + exc);
                    try {
                        this.monitorClient.errorMoreInvalidProposedWords("Error while contacting the client " + exc);
                    } catch (RemoteException e) {}
                } catch (SQLException exc) {
                    try {
                        this.monitorClient.errorMoreInvalidProposedWords("Error while performing DB operations " + exc);
                    } catch (RemoteException e) {}
                }
                break;
            }
            case "validWordsOccurrences": {
                try {
                    this.validWordsOccurrences(this.page);
                } catch (RemoteException exc) {
                    System.err.println("Error while contacting the client " + exc);
                    try {
                        this.monitorClient.errorValidWordsOccurrences("Error while contacting the client " + exc);
                    } catch (RemoteException e) {}
                } catch (SQLException exc) {
                    try {
                        this.monitorClient.errorValidWordsOccurrences("Error while performing DB operations " + exc);
                    } catch (RemoteException e) {}
                }
                break;
            }
            case "wordHighestScore": {
                try {
                    this.wordHighestScore(this.page);
                } catch (RemoteException exc) {
                    System.err.println("Error while contacting the client " + exc);
                    try {
                        this.monitorClient.errorWordHighestScore("Error while contacting the client " + exc);
                    } catch (RemoteException e) {}
                } catch (SQLException exc) {
                    try {
                        this.monitorClient.errorWordHighestScore("Error while performing DB operations " + exc);
                    } catch (RemoteException e) {}
                }
                break;
            }
            case "averageRounds": {
                try {
                    this.averageRounds();
                } catch (RemoteException exc) {
                    System.err.println("Error while contacting the client " + exc);
                    try {
                        this.monitorClient.errorAverageRounds("Error while contacting the client " + exc);
                    } catch (RemoteException e) {}
                } catch (SQLException exc) {
                    try {
                        this.monitorClient.errorAverageRounds("Error while performing DB operations " + exc);
                    } catch (RemoteException e) {}
                }
                break;
            }
            case "minMaxRounds": {
                try {
                    this.minMaxRounds();
                } catch (RemoteException exc) {
                    System.err.println("Error while contacting the client " + exc);
                    try {
                        this.monitorClient.errorMinMaxRounds("Error while contacting the client " + exc);
                    } catch (RemoteException e) {}
                } catch (SQLException exc) {
                    try {
                        this.monitorClient.errorMinMaxRounds("Error while performing DB operations " + exc);
                    } catch (RemoteException e) {}
                }
                break;
            }
            case "charactersAvgOccurrence": {
                try {
                    this.charactersAvgOccurrence();
                } catch (RemoteException exc) {
                    System.err.println("Error while contacting the client " + exc);
                    try {
                        this.monitorClient.errorCharactersAvgOccurrence("Error while contacting the client " + exc);
                    } catch (RemoteException e) {}
                } catch (SQLException exc) {
                    try {
                        this.monitorClient.errorCharactersAvgOccurrence("Error while performing DB operations " + exc);
                    } catch (RemoteException e) {}
                }
                break;
            }
            case "definitionRequest": {
                try {
                    this.definitionRequest(this.page);
                } catch (RemoteException exc) {
                    System.err.println("Error while contacting the client " + exc);
                    try {
                        this.monitorClient.errorDefinitionRequest("Error while contacting the client " + exc);
                    } catch (RemoteException e) {}
                } catch (SQLException exc) {
                    try {
                        this.monitorClient.errorDefinitionRequest("Error while performing DB operations " + exc);
                    } catch (RemoteException e) {}
                }
                break;
            }
            case "gameDefinitionRequest": {
                try {
                    this.gameDefinitionRequest(this.page);
                } catch (RemoteException exc) {
                    System.err.println("Error while contacting the client " + exc);
                    try {
                        this.monitorClient.errorGameDefinitionRequest("Error while contacting the client " + exc);
                    } catch (RemoteException e) {}
                } catch (SQLException exc) {
                    try {
                        this.monitorClient.errorGameDefinitionRequest("Error while performing DB operations " + exc);
                    } catch (RemoteException e) {}
                }
                break;
            }
        }
    }
}