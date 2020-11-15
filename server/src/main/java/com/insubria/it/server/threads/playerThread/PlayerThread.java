package com.insubria.it.server.threads.playerThread;


import com.insubria.it.server.base.abstracts.Database;
import com.insubria.it.server.base.classes.JavaMailController;
import com.insubria.it.server.base.interfaces.JavaMail;

import com.insubria.it.server.org.mindrot.jbcrypt.BCrypt;

import com.insubria.it.server.threads.playerThread.abstracts.Player;
import com.insubria.it.server.threads.playerThread.interfaces.PlayerCredentials;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import javax.mail.MessagingException;


public class PlayerThread extends Player implements Runnable {
    private final PlayerCredentials player;
    private String name;
    private String surname;
    private String username;
    private String email;
    private String password;
    private String oldPassword;
    private String confirmationCode;

    private Database db;
    private final String action;

    public PlayerThread (
        String name,
        String surname,
        String username,
        String email,
        String password,
        PlayerCredentials player,
        String action,
        Database db
    ) {
        this.player = player;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.action = action;
        this.db = db;
    }

    public PlayerThread (String confirmationCode, PlayerCredentials player, String action, Database db) {
        this.confirmationCode = confirmationCode;
        this.player = player;
        this.action = action;
        this.db = db;
    }

    public PlayerThread (String email, String password, PlayerCredentials player, String action, Database db) {
        this.email = email;
        this.password = password;
        this.player = player;
        this.action = action;
        this.db = db;
    }

    public PlayerThread (PlayerCredentials player, String email, String action, Database db) {
        this.email = email;
        this.player = player;
        this.action = action;
        this.db = db;
    }

    public PlayerThread (
        String email,
        String name,
        String surname,
        String username,
        String password,
        String oldPassword,
        PlayerCredentials player,
        String action,
        Database db
    ) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.oldPassword = oldPassword;
        this.player = player;
        this.action = action;
        this.db = db;
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
            pst.setString(5, BCrypt.hashpw(password, BCrypt.gensalt()));
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
            pst.close();
        } else {
            System.out.println("An account with the same email or username already exists");
            player.errorPlayerRegistration("An account with the same email or username already exists");
        }
        dbConnection.close();
    }

    protected void confirmPlayerAccount (String confirmationCode, PlayerCredentials player) throws RemoteException, SQLException {
        System.out.println("Confirming the player account...");
        Connection dbConnection = this.db.getDatabaseConnection();
        String sqlUpdate = "UPDATE users SET is_confirmed = true AND code = NULL WHERE code = ?";
        PreparedStatement pst = dbConnection.prepareStatement(sqlUpdate);
        pst.setString(1, confirmationCode);

        this.db.performChangeState(pst);
        player.confirmCodeConfirmation();
        System.out.println("The player account has been correctly confirmed");

        pst.close();
        dbConnection.close();
    }

    protected void loginPlayerAccount (
        String email,
        String password,
        PlayerCredentials player
    ) throws RemoteException, SQLException {
        System.out.println("Logging in the player...");
        Connection dbConnection = this.db.getDatabaseConnection();
        String sqlQuery = "SELECT name, surname, username, password FROM users WHERE email = ?";
        PreparedStatement pst = dbConnection.prepareStatement(sqlQuery);
        pst.setString(1, email);

        ResultSet result = this.db.performQuery(pst);
        if (result.isBeforeFirst()) {
            result.next();
            if (BCrypt.checkpw(password, result.getString("password"))) {
                System.out.println("User correctly logged in");
                player.confirmLoginPlayerAccount(
                    result.getString("name"),
                    result.getString("surname"),
                    result.getString("username")
                );
            } else {
                System.out.println("The password is incorrect");
                player.errorLoginPlayerAccount("The password is not correct");
            }
        } else {
            System.out.println("No player account found with the given email");
            player.errorLoginPlayerAccount("No player account found with the given email");
        }
        result.close();
        pst.close();
        dbConnection.close();
    }

    protected void resetPlayerPassword (String email, PlayerCredentials player) throws RemoteException, SQLException, MessagingException {
        System.out.println("Resetting the player password...");
        Connection dbConnection = this.db.getDatabaseConnection();
        String sqlUpdate = "UPDATE users SET password = ? WHERE email = ?";
        PreparedStatement pst = dbConnection.prepareStatement(sqlUpdate);

        String password = UUID.randomUUID().toString();
        pst.setString(1, BCrypt.hashpw(password, BCrypt.gensalt()));
        pst.setString(2, email);
        this.db.performChangeState(pst);

        JavaMail mailController = new JavaMailController();
        mailController.sendEmail(
            email,
            "Reset Password - Il Paroliere Platform",
            "Your new password is: " + password
        );
        player.confirmResetPlayerPassword();
        System.out.println("Correctly reset the player password");

        pst.close();
        dbConnection.close();
    }

    private boolean checkOldPassword (String password, String email, Connection dbConnection) throws SQLException {
        String sqlQuery = "SELECT password FROM users WHERE email = ?";
        PreparedStatement pst = dbConnection.prepareStatement(sqlQuery);
        pst.setString(1, email);
        ResultSet result = this.db.performQuery(pst);

        if (BCrypt.checkpw(password, result.getString("password"))) {
            System.out.println("Passord matches");

            result.close();
            pst.close();
            return true;
        } else {
            System.out.println("Passord doesn't match");

            result.close();
            pst.close();
            return false;
        }
    }

    protected void changePlayerData (
        String email,
        String name,
        String surname,
        String username,
        String password,
        String oldPassword,
        PlayerCredentials player
    ) throws RemoteException, SQLException, MessagingException {
        System.out.println("Changing player data...");
        Connection dbConnection = this.db.getDatabaseConnection();
        String sqlUpdate = "UPDATE users SET name = ?, surname = ?, username = ? WHERE email = ?";
        PreparedStatement pst = dbConnection.prepareStatement(sqlUpdate);
        pst.setString(1, name);
        pst.setString(2, surname);
        pst.setString(3, username);
        pst.setString(4, email);
        this.db.performChangeState(pst);

        if (password != null) {
            System.out.println("Changing also password...");
            if (this.checkOldPassword(oldPassword, email, dbConnection)) {
                sqlUpdate = "UPDATE users SET password = ? WHERE email = ?";
                pst = dbConnection.prepareStatement(sqlUpdate);
                pst.setString(1, BCrypt.hashpw(password, BCrypt.gensalt()));
                pst.setString(2, email);
                this.db.performChangeState(pst);

                JavaMail mailController = new JavaMailController();
                mailController.sendEmail(
                    email,
                    "Change Password Confirm - Il Paroliere Platform",
                    "Your new password has been correctly set"
                );
                player.confirmChangePlayerData();
            } else {
                player.errorChangePlayerData("Passord doesn't match");
            }
        }
        System.out.println("The player data have been correctly changed");
        pst.close();
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
                    } catch (RemoteException e) {}
                } catch (RemoteException exc) {
                    System.err.println("Error while contacting the client " + exc);
                    try {
                        this.player.errorPlayerRegistration("Error while contacting the client " + exc);
                    } catch (RemoteException e) {}
                } catch (SQLException exc) {
                    System.err.println("Error while performing DB operations " + exc);
                    try {
                        this.player.errorPlayerRegistration("Error while performing DB operations " + exc);
                    } catch (RemoteException e) {}
                } catch (MessagingException exc) {
                    System.err.println("Error while sending the email " + exc);
                    try {
                        this.player.errorPlayerRegistration("Error while sending the email " + exc);
                    } catch (RemoteException e) {}
                }
                break;
            }
            case "confirm": {
                try {
                    this.confirmPlayerAccount(this.confirmationCode, this.player);
                } catch (RemoteException exc) {
                    System.err.println("Error while contacting the client " + exc);
                    try {
                        this.player.errorCodeConfirmation("Error while contacting the client " + exc);
                    } catch (RemoteException e) {}
                } catch (SQLException exc) {
                    System.err.println("Error while performing DB operations " + exc);
                    try {
                        this.player.errorCodeConfirmation("Error while performing DB operations " + exc);
                    } catch (RemoteException e) {}
                }
                break;
            }
            case "login": {
                try {
                    this.loginPlayerAccount(this.email, this.password, this.player);
                } catch (RemoteException exc) {
                    System.err.println("Error while contacting the client " + exc);
                    try {
                        this.player.errorLoginPlayerAccount("Error while contacting the client " + exc);
                    } catch (RemoteException e) {}
                } catch (SQLException exc) {
                    System.err.println("Error while performing DB operations " + exc);
                    try {
                        this.player.errorLoginPlayerAccount("Error while performing DB operations " + exc);
                    } catch (RemoteException e) {}
                }
                break;
            }
            case "reset": {
                try {
                    this.resetPlayerPassword(this.email, this.player);
                } catch (RemoteException exc) {
                    System.err.println("Error while contacting the client " + exc);
                    try {
                        this.player.errorResetPlayerPassword("Error while contacting the client " + exc);
                    } catch (RemoteException e) {}
                } catch (SQLException exc) {
                    System.err.println("Error while performing DB operations " + exc);
                    try {
                        this.player.errorResetPlayerPassword("Error while performing DB operations " + exc);
                    } catch (RemoteException e) {}
                } catch (MessagingException exc) {
                    System.err.println("Error while sending the email " + exc);
                    try {
                        this.player.errorResetPlayerPassword("Error while sending the email " + exc);
                    } catch (RemoteException e) {}
                }
                break;
            }
            case "change": {
                try {
                    this.changePlayerData(this.email, this.name, this.surname, this.username, this.password, this.oldPassword, this.player);
                } catch (RemoteException exc) {
                    System.err.println("Error while contacting the client " + exc);
                    try {
                        this.player.errorChangePlayerData("Error while contacting the client " + exc);
                    } catch (RemoteException e) {}
                } catch (SQLException exc) {
                    System.err.println("Error while performing DB operations " + exc);
                    try {
                        this.player.errorChangePlayerData("Error while performing DB operations " + exc);
                    } catch (RemoteException e) {}
                } catch (MessagingException exc) {
                    System.err.println("Error while sending the email " + exc);
                    try {
                        this.player.errorChangePlayerData("Error while sending the email " + exc);
                    } catch (RemoteException e) {}
                }
                break;
            }
        }
    }
}
