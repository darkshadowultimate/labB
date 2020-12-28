package com.insubria.it.sharedserver.threads.gameThread.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

import java.util.ArrayList;

import com.insubria.it.threads.gameThread.abstracts.Game;
import com.insubria.it.sharedserver.threads.gameThread.utils.WordRecord;

/**
 * The GameClient interface defines the remote methods that the instance on the
 * client side will define. It extends the Remote interface because all the
 * methods are remote and defined client side. This is the interface used by the
 * GameThread to communicate with the client that made the request.
 */
public interface GameClient extends Remote {
    /**
     * The signature of the getUsername method Called when the GameThread object
     * needs to retrieve the username of the player It is implemented client side
     * 
     * @return - The username of the player
     */
    String getUsername() throws RemoteException;

    /**
     * The signature of the getEmail method Called when the GameThread object needs
     * to retrieve the email of the player It is implemented client side
     * 
     * @return - The email of the player
     */
    String getEmail() throws RemoteException;

    /**
     * The signature of the confirmCreateNewGame method Called when the game has
     * been created and the reference to the thread is returned to the client It is
     * implemented client side
     */
    void confirmCreateNewGame(String gameThread) throws RemoteException;

    /**
     * The signature of the errorCreateNewGame method Called when the game has not
     * created and the reason is returned It is implemented client side
     */
    void errorCreateNewGame(String reason) throws RemoteException;

    /**
     * The signature of the confirmAddNewPlayer method Called when the player is
     * correctly added to the game It is implemented client side
     */
    void confirmAddNewPlayer() throws RemoteException;

    /**
     * The signature of the errorAddNewPlayer method Called when the player is not
     * added and the reason is returned It is implemented client side
     */
    void errorAddNewPlayer(String reason) throws RemoteException;

    /**
     * The signature of the confirmRemovePlayerNotStartedGame method Called when the
     * player is correctly removed form a not started game It is implemented client
     * side
     */
    void confirmRemovePlayerNotStartedGame() throws RemoteException;

    /**
     * The signature of the errorRemovePlayerNotStartedGame method Called when the
     * player is not removed from a not started game and the reason is returned It
     * is implemented client side
     */
    void errorRemovePlayerNotStartedGame(String reason) throws RemoteException;

    /**
     * The signature of the gameHasBeenRemoved method Called when the game has been
     * removed It is implemented client side
     */
    void gameHasBeenRemoved(String reason) throws RemoteException;

    /**
     * The signature of the synchronizePreStartGameTimer method Called when the
     * thread needs to synchronize the player with the pre-start timer (30 seconds)
     * It is implemented client side
     */
    void synchronizePreStartGameTimer(int seconds) throws RemoteException;

    /**
     * The signature of the synchronizeInGameTimer method Called when the thread
     * needs to synchronize the player with the in-game timer (3 minutes) It is
     * implemented client side
     */
    void synchronizeInGameTimer(int seconds) throws RemoteException;

    /**
     * The signature of the synchronizeInWaitTimer method Called when the thread
     * needs to synchronize the player with the review timer (3 minutes) It is
     * implemented client side
     */
    void synchronizeInWaitTimer(int seconds) throws RemoteException;

    /**
     * The signature of the confirmGameSession method Called when a new session is
     * triggered and the player can start play It is implemented client side
     */
    void confirmGameSession(String name, int sessionNumber, String[][] matrix, int playerScore) throws RemoteException;

    /**
     * The signature of the triggerEndOfSession method Called when the current
     * session expires (because of time out) It is implemented client side
     */
    void triggerEndOfSession() throws RemoteException;

    /**
     * The signature of the sendWordsDiscoveredInSession method Called when the
     * thread sends both the valid and invalid words to each player for review It is
     * implemented client side
     */
    void sendWordsDiscoveredInSession(ArrayList<WordRecord> acceptedArray, ArrayList<WordRecord> refusedArray)
            throws RemoteException;

    /**
     * The signature of the confirmWordDefinitions method Called when the thread
     * looked for a word definition and it returns it to the player It is
     * implemented client side
     */
    void confirmWordDefinitions(String wordDefinitions) throws RemoteException;

    /**
     * The signature of the errorWordDefinitions method Called when the thread
     * didn't find the word definition correctly and the reason is returned It is
     * implemented client side
     */
    void errorWordDefinitions(String reason) throws RemoteException;

    /**
     * The signature of the gameWonByUser method Called when the game is won by a
     * player and the username is sent to all the players It is implemented client
     * side
     */
    void gameWonByUser(String username) throws RemoteException;
}