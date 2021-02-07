package com.insubria.it.threads.playerThread;

import com.insubria.it.base.abstracts.Database;
import com.insubria.it.base.classes.JavaMailController;
import com.insubria.it.base.interfaces.JavaMail;

import com.insubria.it.org.mindrot.jbcrypt.BCrypt;

import com.insubria.it.sharedserver.threads.playerThread.interfaces.PlayerCredentials;
import com.insubria.it.threads.playerThread.abstracts.Player;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import javax.mail.MessagingException;

/**
 * The PlayerThread class represents the thread that will be created for each
 * reuqest made by the user that represents a player handle request (login,
 * registration, data change, password etc). This class extends the Player
 * abstract class that contains the signatures of the methods. This class
 * implements the Runnable interface to let the instances of this class to be
 * threads (so the infrastrucutre can handle multiple users' requests at the
 * same time).
 */
public class PlayerThread extends Player implements Runnable {
    /**
     * Constant that represents the reference of the client that made the request
     * (remote object)
     */
    private final PlayerCredentials player;

    /**
     * It represents the name of the user
     */
    private String name;

    /**
     * It represents the surname of the user
     */
    private String surname;

    /**
     * It represents the username of the user
     */
    private String username;

    /**
     * It represents the email of the user
     */
    private String email;

    /**
     * It represents the password of the user
     */
    private String password;

    /**
     * It represents the oldPassword of the user
     */
    private String oldPassword;

    /**
     * It represents the confirmationCode the user reached by email
     */
    private String confirmationCode;

    /**
     * It represents the reference to the DatabaseController object
     */
    private Database db;

    /**
     * It represents the string that has the keyword to let the run method
     * understand which method to call and execute
     */
    private final String action;

    /**
     * Constructor called by the ServerImpl when a user perform the registration
     * 
     * @param name     - The name of the new user
     * @param surname  - The surname of the new user
     * @param username - The username of the new user
     * @param email    - The email of the new user
     * @param password - The password of the new user
     * @param player   - The reference to the remote object that represents the
     *                 client (user) that made the request
     * @param action   - String that represents the action required
     * @param db       - The reference to the DatabaseController object
     */
    public PlayerThread(String name, String surname, String username, String email, String password,
            PlayerCredentials player, String action, Database db) {
        this.player = player;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.action = action;
        this.db = db;
    }

    /**
     * Constructor called by the ServerImpl when a user perform a confirmation of
     * his account
     * 
     * @param confirmationCode - The random confirmation code sent by email
     * @param player           - The reference to the remote object that represents
     *                         the client (user) that made the request
     * @param action           - String that represents the action required
     * @param db               - The reference to the DatabaseController object
     */
    public PlayerThread(String confirmationCode, PlayerCredentials player, String action, Database db) {
        this.confirmationCode = confirmationCode;
        this.player = player;
        this.action = action;
        this.db = db;
    }

    /**
     * Constructor called by the ServerImpl when a user performs a login
     * 
     * @param email    - The email of the user
     * @param password - The password of the user
     * @param player   - The reference to the remote object that represents the
     *                 client (user) that made the request
     * @param action   - String that represents the action required
     * @param db       - The reference to the DatabaseController object
     */
    public PlayerThread(String email, String password, PlayerCredentials player, String action, Database db) {
        this.email = email;
        this.password = password;
        this.player = player;
        this.action = action;
        this.db = db;
    }

    /**
     * Constructor called by the ServerImpl when a user performs a password reset
     * 
     * @param email  - The email of the user
     * @param player - The reference to the remote object that represents the client
     *               (user) that made the request
     * @param action - String that represents the action required
     * @param db     - The reference to the DatabaseController object
     */
    public PlayerThread(PlayerCredentials player, String email, String action, Database db) {
        this.email = email;
        this.player = player;
        this.action = action;
        this.db = db;
    }

    /**
     * Constructor called by the ServerImpl when a user performs the change of his
     * data
     * 
     * @param name        - The name of the new user
     * @param surname     - The surname of the new user
     * @param username    - The username of the new user
     * @param email       - The email of the new user
     * @param password    - The new password of the new user
     * @param oldPassword - The old password of the new user
     * @param player      - The reference to the remote object that represents the
     *                    client (user) that made the request
     * @param action      - String that represents the action required
     * @param db          - The reference to the DatabaseController object
     */
    public PlayerThread(String email, String name, String surname, String username, String password, String oldPassword,
            PlayerCredentials player, String action, Database db) {
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

    /**
     * This is an utility method invoked when a user needs to be registered. It
     * checks that none of other users have same email and username
     * 
     * @param email        - The email of the new user
     * @param username     - The username of the new user
     *
     * @return - true if a user with email/username already exists, false otherwise
     * @throws SQLException - If something goes wrong with the DB operations
     *                      SQLException is thrown
     */
    private boolean checkProfileExists(String email, String username) throws SQLException {
        String sqlQuery = "SELECT * FROM users WHERE email = ? OR username = ?";
        Connection dbConnection = null;
        PreparedStatement pst = null;

        try {
            dbConnection = this.db.getDatabaseConnection();

            pst =  dbConnection.prepareStatement(sqlQuery);
            pst.setString(1, email);
            pst.setString(2, username);
        } catch (SQLException exc) {
            System.err.println("Error while establishing the connection with the DB " + exc);
        }

        ResultSet result = this.db.peroformComplexQuery(pst);

        if (result.isBeforeFirst()) {
            pst.close();
            result.close();
            dbConnection.close();
            System.err.println("A user with the following email/username already exists");
            return true;
        } else {
            pst.close();
            result.close();
            dbConnection.close();
            return false;
        }
    }

    /**
     * This is an utility method that checks whether a user confirmed his account or
     * not
     * 
     * @param email        - The email of the user
     *
     * @return - true if the user confirmed the account, false otherwise
     * @throws SQLException - If something goes wrong with the DB operations
     *                      SQLException is thrown
     */
    private boolean checkHasConfirmedAccount(String email) throws SQLException {
        String sqlQuery = "SELECT * FROM users WHERE email = ?";
        Connection dbConnection = null;
        PreparedStatement pst = null;
        try {
            dbConnection = this.db.getDatabaseConnection();

            pst =  dbConnection.prepareStatement(sqlQuery);
            pst.setString(1, email);
        } catch (SQLException exc) {
            System.err.println("Error while establishing the connection with the DB " + exc);
        }

        ResultSet result = this.db.peroformComplexQuery(pst);
        result.next();

        boolean booleanResult = result.getBoolean("is_confirmed");

        result.close();
        pst.close();
        dbConnection.close();
        return booleanResult;
    }

    /**
     * This is an utility method that will perform a delete on the DB to delete a
     * user
     * 
     * @param email        - The email of the user
     * @param dbConnection - The reference to the Connection object
     * 
     * @return Nothing
     * @throws SQLException - If something goes wrong with the DB operations
     *                      SQLException is thrown
     */
    private void deleteUserAccount(String email, Connection dbConnection) throws SQLException {
        String sqlDelete = "DELETE FROM users WHERE email = ?";
        PreparedStatement pst = dbConnection.prepareStatement(sqlDelete);
        pst.setString(1, email);

        this.db.performChangeState(pst);
        pst.close();
    }

    /**
     * This is the method called when a new registration needs to be handled. It
     * will handle the process of the creation of a new user for the platform
     * 
     * @param name     - The name of the new user
     * @param surname  - The surname of the new user
     * @param username - The username of the new user
     * @param email    - The email of the new user
     * @param password - The password of the new user
     * @param player   - The reference to the remote object that represents the
     *                 client (user) that made the request
     * 
     * @throws InterruptedException - If the thread is interrupted when sleeping, it
     *                              throws InterruptedException
     * @throws RemoteException      - If there is an error while the client contact,
     *                              it throws RemoteException
     * @throws SQLException         - If there is an error while the DB operations,
     *                              it throws SQLException
     * @throws MessagingException   - If there is an error while the sending of the
     *                              email, it throws MessagingException
     */
    protected void createPlayerAccount(
        String name,
        String surname,
        String username,
        String email,
        String password,
        PlayerCredentials player
    ) throws InterruptedException, RemoteException, SQLException, MessagingException {

        if (!this.checkProfileExists(email, username)) {
            Connection dbConnection = this.db.getDatabaseConnection();
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
            mailController.sendEmail(email, "Welcome to the Il Paroliere Platform",
                    "Your activation code is the following: " + token);

            player.confirmPlayerRegistration();
            Thread.sleep(600000);

            if (!this.checkHasConfirmedAccount(email)) {
                System.out.println("Removing the account due to confirmation timeout...");
                this.deleteUserAccount(email, dbConnection);
                System.out.println("Removed the account");
            }
            System.out.println("Account creation flow completed correctly");
            pst.close();
            dbConnection.close();
        } else {
            System.out.println("An account with the same email or username already exists");
            player.errorPlayerRegistration("An account with the same email or username already exists");
        }
    }

    /**
     * This is the method called when the user confirmation flow needs to be
     * handled.
     * 
     * @param confirmationCode - The confirmationCode to look for in the DB to reach
     *                         the user that confirmed the account
     * @param player           - The reference to the remote object that represents
     *                         the client (user) that made the request
     * 
     * @throws RemoteException - If there is an error while the client contact, it
     *                         throws RemoteException
     * @throws SQLException    - If there is an error while the DB operations, it
     *                         throws SQLException
     */
    protected void confirmPlayerAccount(String confirmationCode, PlayerCredentials player)
            throws RemoteException, SQLException {
        System.out.println("Confirming the player account...");
        Connection dbConnection = this.db.getDatabaseConnection();
        String sqlUpdate = "UPDATE users SET code = null AND is_confirmed = true WHERE code = ?";
        PreparedStatement pst = dbConnection.prepareStatement(sqlUpdate);
        pst.setString(1, confirmationCode);

        this.db.performChangeState(pst);
        player.confirmCodeConfirmation();
        System.out.println("The player account has been correctly confirmed");

        pst.close();
        dbConnection.close();
    }

    /**
     * This is the method called when a user needs to perform login. It will handle
     * the login flow.
     * 
     * @param email    - The email of the user
     * @param password - The password of the user
     * @param player   - The reference to the remote object that represents the
     *                 client (user) that made the request
     * 
     * @throws RemoteException - If there is an error while the client contact, it
     *                         throws RemoteException
     * @throws SQLException    - If there is an error while the DB operations, it
     *                         throws SQLException
     */
    protected void loginPlayerAccount(String email, String password, PlayerCredentials player)
            throws RemoteException, SQLException {
        System.out.println("Logging in the player...");
        String sqlQuery = "SELECT name, surname, username, password FROM users WHERE email = ?";
        Connection dbConnection = null;
        PreparedStatement pst = null;

        try {
            dbConnection = this.db.getDatabaseConnection();

            pst =  dbConnection.prepareStatement(sqlQuery);
            pst.setString(1, email);
        } catch (SQLException exc) {
            System.err.println("Error while establishing the connection with the DB " + exc);
        }

        ResultSet result = this.db.peroformComplexQuery(pst);
        if (result.isBeforeFirst()) {
            result.next();
            if (BCrypt.checkpw(password, result.getString("password"))) {
                System.out.println("User correctly logged in");
                player.confirmLoginPlayerAccount(result.getString("name"), result.getString("surname"),
                        result.getString("username"));
            } else {
                System.out.println("The password is incorrect");
                player.errorLoginPlayerAccount("The password is not correct");
            }
        } else {
            System.out.println("No player account found with the given email");
            player.errorLoginPlayerAccount("No player account found with the given email");
        }
        result.close();
        dbConnection.close();
        pst.close();
    }

    /**
     * This is the methdo called when a user needs to reset the password. It will
     * handle the reset password flow.
     * 
     * @param email  - The email of the user that needs to change the password
     * @param player - The reference to the remote object that represents the client
     *               (user) that made the request
     * 
     * @throws RemoteException    - If there is an error while the client contact,
     *                            it throws RemoteException
     * @throws SQLException       - If there is an error while the DB operations, it
     *                            throws SQLException
     * @throws MessagingException - If there is an error while the sending of the
     *                            email, it throws MessagingException
     */
    protected void resetPlayerPassword(String email, PlayerCredentials player)
            throws RemoteException, SQLException, MessagingException {
        System.out.println("Resetting the player password...");
        Connection dbConnection = this.db.getDatabaseConnection();
        String sqlUpdate = "UPDATE users SET password = ? WHERE email = ?";
        PreparedStatement pst = dbConnection.prepareStatement(sqlUpdate);

        String password = UUID.randomUUID().toString();
        pst.setString(1, BCrypt.hashpw(password, BCrypt.gensalt()));
        pst.setString(2, email);
        this.db.performChangeState(pst);

        JavaMail mailController = new JavaMailController();
        mailController.sendEmail(email, "Reset Password - Il Paroliere Platform", "Your new password is: " + password);
        player.confirmResetPlayerPassword();
        System.out.println("Correctly reset the player password");

        pst.close();
        dbConnection.close();
    }

    /**
     * This is an utility method used when the user needs to change the password. It
     * checkes that the old pw is equals to the one the user specified
     * 
     * @param password     - The password the user sent
     * @param email        - The email of the user
     *
     * @return - True if the passwords match, false otherwise
     * 
     * @throws SQLException - If there is an error while the DB operations, it
     *                      throws SQLException
     */
    private boolean checkOldPassword(String password, String email) throws SQLException {
        String sqlQuery = "SELECT * FROM users WHERE email = ?";
        Connection dbConnection = null;
        PreparedStatement pst = null;
        try {
            dbConnection = this.db.getDatabaseConnection();
            pst = dbConnection.prepareStatement(sqlQuery);
            pst.setString(1, email);
        } catch (SQLException exc) {
            System.err.println("Error while establishing the connection with the DB " + exc);
        }

        ResultSet result = this.db.peroformComplexQuery(pst);

        if (result.isBeforeFirst()) {
            result.next();
            if (BCrypt.checkpw(password, result.getString("password"))) {
                System.out.println("Passord matches");
    
                result.close();
                dbConnection.close();
                pst.close();
                return true;
            } else {
                System.out.println("Passord doesn't match");
    
                result.close();
                dbConnection.close();
                pst.close();
                return false;
            }
        } else {
            System.out.println("No records found");
            result.close();
            dbConnection.close();
            pst.close();

            return false;
        }
    }

    /**
     * This is the method called when the user needs to change his data. It will
     * handle the change flow.
     * 
     * @param name        - The name of the new user
     * @param surname     - The surname of the new user
     * @param username    - The username of the new user
     * @param email       - The email of the new user
     * @param password    - The new password of the new user
     * @param oldPassword - The old password of the new user
     * @param player      - The reference to the remote object that represents the
     *                    client (user) that made the request
     * 
     * @throws RemoteException    - If there is an error while the client contact,
     *                            it throws RemoteException
     * @throws SQLException       - If there is an error while the DB operations, it
     *                            throws SQLException
     * @throws MessagingException - If there is an error while the sending of the
     *                            email, it throws MessagingException
     */
    protected void changePlayerData(String email, String name, String surname, String username, String password,
            String oldPassword, PlayerCredentials player) throws RemoteException, SQLException, MessagingException {
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
            if (this.checkOldPassword(oldPassword, email)) {
                sqlUpdate = "UPDATE users SET password = ? WHERE email = ?";
                pst = dbConnection.prepareStatement(sqlUpdate);
                pst.setString(1, BCrypt.hashpw(password, BCrypt.gensalt()));
                pst.setString(2, email);
                this.db.performChangeState(pst);

                JavaMail mailController = new JavaMailController();
                mailController.sendEmail(email, "Change Password Confirm - Il Paroliere Platform",
                        "Your new password has been correctly set");
                player.confirmChangePlayerData();
            } else {
                player.errorChangePlayerData("Password doesn't match");
            }
        }
        System.out.println("The player data have been correctly changed");
        pst.close();
        dbConnection.close();
    }

    /**
     * This is the method invoked when the thread is started. it will call one of
     * the protected methods depending on the value of the action attribute
     */
    public void run() {
        switch (this.action) {
            case "create": {
                try {
                    this.createPlayerAccount(this.name, this.surname, this.username, this.email, this.password,
                            this.player);
                } catch (InterruptedException exc) {
                    System.err.println("Thread has been interrupted " + exc);
                    try {
                        this.player.errorPlayerRegistration("Thread has been interrupted " + exc);
                    } catch (RemoteException e) {
                    }
                } catch (RemoteException exc) {
                    System.err.println("Error while contacting the client " + exc);
                    try {
                        this.player.errorPlayerRegistration("Error while contacting the client " + exc);
                    } catch (RemoteException e) {
                    }
                } catch (SQLException exc) {
                    System.err.println("Error while performing DB operations " + exc);
                    try {
                        this.player.errorPlayerRegistration("Error while performing DB operations " + exc);
                    } catch (RemoteException e) {
                    }
                } catch (MessagingException exc) {
                    System.err.println("Error while sending the email " + exc);
                    try {
                        this.player.errorPlayerRegistration("Error while sending the email " + exc);
                    } catch (RemoteException e) {
                    }
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
                    } catch (RemoteException e) {
                    }
                } catch (SQLException exc) {
                    System.err.println("Error while performing DB operations " + exc);
                    try {
                        this.player.errorCodeConfirmation("Error while performing DB operations " + exc);
                    } catch (RemoteException e) {
                    }
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
                    } catch (RemoteException e) {
                    }
                } catch (SQLException exc) {
                    System.err.println("Error while performing DB operations " + exc);
                    try {
                        this.player.errorLoginPlayerAccount("Error while performing DB operations " + exc);
                    } catch (RemoteException e) {
                    }
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
                    } catch (RemoteException e) {
                    }
                } catch (SQLException exc) {
                    System.err.println("Error while performing DB operations " + exc);
                    try {
                        this.player.errorResetPlayerPassword("Error while performing DB operations " + exc);
                    } catch (RemoteException e) {
                    }
                } catch (MessagingException exc) {
                    System.err.println("Error while sending the email " + exc);
                    try {
                        this.player.errorResetPlayerPassword("Error while sending the email " + exc);
                    } catch (RemoteException e) {
                    }
                }
                break;
            }
            case "change": {
                try {
                    this.changePlayerData(this.email, this.name, this.surname, this.username, this.password,
                            this.oldPassword, this.player);
                } catch (RemoteException exc) {
                    System.err.println("Error while contacting the client " + exc);
                    try {
                        this.player.errorChangePlayerData("Error while contacting the client " + exc);
                    } catch (RemoteException e) {
                    }
                } catch (SQLException exc) {
                    System.err.println("Error while performing DB operations " + exc);
                    try {
                        this.player.errorChangePlayerData("Error while performing DB operations " + exc);
                    } catch (RemoteException e) {
                    }
                } catch (MessagingException exc) {
                    System.err.println("Error while sending the email " + exc);
                    try {
                        this.player.errorChangePlayerData("Error while sending the email " + exc);
                    } catch (RemoteException e) {
                    }
                }
                break;
            }
        }
    }
}
