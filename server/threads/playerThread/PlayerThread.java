package server.threads.playerThread;


import server.base.abstracts.Database;
import server.base.classes.JavaMailController;
import server.base.interfaces.JavaMail;

import server.threads.playerThread.abstracts.Player;
import server.threads.playerThread.interfaces.PlayerCredentials;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import javax.mail.MessagingException;


public class PlayerThread extends Player implements Runnable {
    private final PlayerCredentials player;
    private final String name;
    private final String surname;
    private final String username;
    private final String email;
    private final String password;

    private Database db;
    private final String action;

    public PlayerThread (
        String name,
        String surname,
        String username,
        String email,
        String password,
        PlayerCredentials player,
        String action
    ) {
        this.player = player;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.action = action;
    }

    private boolean checkProfileExists (String email, String username, Connection dbConnection) throws SQLException {
        String sqlQueryEmail = "SELECT * FROM users WHERE email = ?", sqlQueryUsername = "SELECT * FROM users WHERE username = ?";
        PreparedStatement pst1 = dbConnection.prepareStatement(sqlQueryEmail), pst2 = dbConnection.prepareStatement(sqlQueryUsername);
        ResultSet result1 = this.db.performQuery(pst1), result2 = this.db.performQuery(pst2);

        if (result1.isBeforeFirst() || result2.isBeforeFirst()) {
            System.err.println("A user with the following email/username already exists");
            pst1.close();
            pst2.close();
            result1.close();
            result2.close();

            return true;
        } else {
            pst1.close();
            pst2.close();
            result1.close();
            result2.close();

            return false;
        }
    }

    private boolean checkHasConfirmedAccount (String email, Connection dbConnection) throws SQLException {
        String sqlQuery = "SELECT * FROM users WHERE email = ?";
        PreparedStatement pst = dbConnection.prepareStatement(sqlQuery);
        pst.setString(1, email);
        ResultSet result = this.db.performQuery(pst);
        boolean booleanResult = result.getBoolean("is_confirmed");

        pst.close();
        result.close();
        return booleanResult;
    }

    private void deleteUserAccount (String email, Connection dbConnection) throws SQLException {
        String sqlDelete = "DELETE FROM users WHERE email = ?";
        PreparedStatement pst = dbConnection.prepareStatement(sqlDelete);

        this.db.performChangeState(pst);
        pst.close();
    }

    protected void createPlayerAccount (
        String name,
        String surname,
        String username,
        String email,
        String password,
        PlayerCredentials player
    ) throws InterruptedException, RemoteException, SQLException, MessagingException {
        Connection dbConnection = this.db.getDatabaseConnection();

        if (!this.checkProfileExists(this.email, this.username, dbConnection)) {
            String token = UUID.randomUUID().toString();
            String sqlInsert = "INSERT INTO users(email, username, name, surname, password, code) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = dbConnection.prepareStatement(sqlInsert);
            pst.setString(1, email);
            pst.setString(2, username);
            pst.setString(3, name);
            pst.setString(4, surname);
            pst.setString(5, password);
            pst.setString(6, token);

            System.out.println("Creating the new player account...");
            this.db.performChangeState(pst);
            JavaMail mailController = new JavaMailController();
            mailController.sendEmail(
                    email,
                    "Welcome to the Il Paroliere Platform",
                    "Your activation code is the following: " + token
            );

            player.confirmPlayerRegistration();
            Thread.sleep(600000);

            if (!this.checkHasConfirmedAccount(email, dbConnection)) {
                System.out.println("Removing the account due to confirmation timeout...");
                this.deleteUserAccount(email, dbConnection);
                System.out.println("Removed the account");
            }
            System.out.println("Account creation flow completed correctly");
        } else {
            System.out.println("An account with the same email or username already exists");
            player.errorPlayerRegistration("An account with the same email or username already exists");
        }
        dbConnection.close();
    }

    public void run () {
        switch (this.action) {
            case "create": {
                try {
                    this.createPlayerAccount(this.name, this.surname, this.username, this.email, this.password, this.player);
                } catch (InterruptedException exc) {
                    System.err.println("Thread has been interrupted " + exc);
                    try {
                        this.player.errorPlayerRegistration("Thread has been interrupted " + exc);
                        break;
                    } catch (RemoteException e) {}
                } catch (RemoteException exc) {
                    System.err.println("Error while contacting the client " + exc);
                    try {
                        this.player.errorPlayerRegistration("Error while contacting the client " + exc);
                        break;
                    } catch (RemoteException e) {}
                } catch (SQLException exc) {
                    System.err.println("Error while performing DB operations " + exc);
                    try {
                        this.player.errorPlayerRegistration("Error while performing DB operations " + exc);
                        break;
                    } catch (RemoteException e) {}
                } catch (MessagingException exc) {
                    System.err.println("Error while sending the email " + exc);
                    try {
                        this.player.errorPlayerRegistration("Error while sending the email " + exc);
                        break;
                    } catch (RemoteException e) {}
                }
                break;
            }
        }
    }
}
