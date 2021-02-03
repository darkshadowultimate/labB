package com.insubria.it.serverImplClasses;

import com.insubria.it.context.GameContextProvider;
import com.insubria.it.context.RemoteObjectContextProvider;
import com.insubria.it.g_interface.*;
import com.insubria.it.sharedserver.threads.gameThread.interfaces.GameClient;
import com.insubria.it.sharedserver.threads.gameThread.utils.WordRecord;

import javax.swing.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class GameClientImpl extends UnicastRemoteObject implements GameClient {

    private String username;
    private String email;

    public GameClientImpl(String username, String email) throws RemoteException {
        this.username = username;
        this.email = email;
    }
    /**
     * The signature of the getUsername method Called when the GameThread object
     * needs to retrieve the username of the player It is implemented client side
     *
     * @return - The username of the player
     */
    public String getUsername() throws RemoteException { return this.username; }

    /**
     * The signature of the getEmail method Called when the GameThread object needs
     * to retrieve the email of the player It is implemented client side
     *
     * @return - The email of the player
     */
    public String getEmail() throws RemoteException { return this.email; }

    /**
     * The signature of the confirmCreateNewGame method Called when the game has
     * been created and the reference to the thread is returned to the client It is
     * implemented client side
     */
    public void confirmCreateNewGame(String gameThreadId) throws RemoteException {
        CompletableFuture.runAsync(() -> {
            RemoteObjectContextProvider.setGameRemoteObject(gameThreadId);

            CreateNewGame.redirectToWaitingRoomFrame();
        });
    }

    /**
     * The signature of the errorCreateNewGame method Called when the game has not
     * created and the reason is returned It is implemented client side
     */
    public void errorCreateNewGame(String reason) throws RemoteException {
        CompletableFuture.runAsync(() -> {
            JOptionPane.showMessageDialog(null, reason);
        });
    }

    /**
     * The signature of the confirmAddNewPlayer method Called when the player is
     * correctly added to the game It is implemented client side
     */
    public void confirmAddNewPlayer() throws RemoteException {
        ListGames.redirectToWaitingPlayersFrame();
    }

    /**
     * The signature of the errorAddNewPlayer method Called when the player is not
     * added and the reason is returned It is implemented client side
     */
    public void errorAddNewPlayer(String reason) throws RemoteException {
        CompletableFuture.runAsync(() -> {
            JOptionPane.showMessageDialog(null, reason);
        });
    }

    /**
     * The signature of the confirmRemovePlayerNotStartedGame method Called when the
     * player is correctly removed form a not started game It is implemented client
     * side
     */
    public void confirmRemovePlayerNotStartedGame() throws RemoteException {
        CompletableFuture.runAsync(() -> {
            WaitingPlayers.redirectToHomeFrame();
        });
    }

    /**
     * The signature of the errorRemovePlayerNotStartedGame method Called when the
     * player is not removed from a not started game and the reason is returned It
     * is implemented client side
     */
    public void errorRemovePlayerNotStartedGame(String reason) throws RemoteException {
        CompletableFuture.runAsync(() -> {
            JOptionPane.showMessageDialog(null, reason);
        });
    }

    /**
     * The signature of the gameHasBeenRemoved method Called when the game has been
     * removed It is implemented client side
     */
    public void gameHasBeenRemoved(String reason) throws RemoteException {
        CompletableFuture.runAsync(() -> {
            if(WordsAnalysis.isWordsAnalysisFrameActive()) {
                WordsAnalysis.redirectToHomeFrame();
            } else {
                GamePlay.redirectToHomeFrame();
            }
        });
    }

    /**
     * The signature of the synchronizePreStartGameTimer method Called when the
     * thread needs to synchronize the player with the pre-start timer (30 seconds)
     * It is implemented client side
     */
    public void synchronizePreStartGameTimer(int seconds) throws RemoteException {
        CompletableFuture.runAsync(() -> {
            if(seconds == 30) {
                WaitingPlayers.redirectToCountdownFrame();
            } else {
                WaitingStartGame.updateCountdown(seconds);
            }
        });
    }

    /**
     * The signature of the synchronizeInGameTimer method Called when the thread
     * needs to synchronize the player with the in-game timer (3 minutes) It is
     * implemented client side
     */
    public void synchronizeInGameTimer(int seconds) throws RemoteException {
        CompletableFuture.runAsync(() -> {
            GamePlay.updateCountdown(seconds);
        });
    }

    /**
     * The signature of the synchronizeInWaitTimer method Called when the thread
     * needs to synchronize the player with the review timer (3 minutes) It is
     * implemented client side
     */
    public void synchronizeInWaitTimer(int seconds) throws RemoteException {
        CompletableFuture.runAsync(() -> {
            WordsAnalysis.updateCountdown(seconds);
        });
    }

    /**
     * The signature of the confirmGameSession method Called when a new session is
     * triggered and the player can start play It is implemented client side
     */
    public void confirmGameSession(
        String name,
        int sessionNumber,
        String[][] matrix,
        HashMap<String, Integer> playerScore
    ) throws RemoteException {
        CompletableFuture.runAsync(() -> {
            if(WordsAnalysis.isWordsAnalysisFrameActive()) {
                WordsAnalysis.redirectToGamePlayFrame();
            } else if(WaitingPlayers.checkIsFrameActive()) {
                WaitingPlayers.closeWindow();
            } else {
                WaitingStartGame.redirectToGamePlayFrame();
            }
            GamePlay gamePlay = new GamePlay(
                name,
                sessionNumber,
                matrix,
                playerScore
            );
        });
    }

    /**
     * The signature of the triggerEndOfSession method Called when the current
     * session expires (because of time out) It is implemented client side
     */
    public ArrayList<String> triggerEndOfSession() throws RemoteException {
        System.out.println("Ricevuta triggerEndOfSession");

        return GamePlay.getWordsFound();
    }

    /**
     * The signature of the sendWordsDiscoveredInSession method Called when the
     * thread sends both the valid and invalid words to each player for review It is
     * implemented client side
     */
    public void sendWordsDiscoveredInSession(
        ArrayList<WordRecord> acceptedArray,
        ArrayList<WordRecord> refusedArray
    ) throws RemoteException {
        System.out.println("Ricevuta sendWordsDiscoveredInSession");

        CompletableFuture.runAsync(() -> {
            GamePlay.redirectToWordsAnalysisFrame(acceptedArray, refusedArray);
        });
    }

    /**
     * The signature of the confirmWordDefinitions method Called when the thread
     * looked for a word definition and it returns it to the player It is
     * implemented client side
     */
    public void confirmWordDefinitions(String wordDefinitions) throws RemoteException {
        SingleWordAnalysis.showWordDefinition(wordDefinitions);
    }

    /**
     * The signature of the errorWordDefinitions method Called when the thread
     * didn't find the word definition correctly and the reason is returned It is
     * implemented client side
     */
    public void errorWordDefinitions(String reason) throws RemoteException {
        JOptionPane.showMessageDialog(null, reason);
    }

    /**
     * The signature of the gameWonByUser method Called when the game is won by a
     * player and the username is sent to all the players It is implemented client
     * side
     */
    public void gameWonByUser(String username) throws RemoteException {
        WordsAnalysis.redirectToGameWinnerFrame(username);
    }
}
