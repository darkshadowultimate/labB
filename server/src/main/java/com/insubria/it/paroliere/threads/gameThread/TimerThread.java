package com.insubria.it.paroliere.threads.gameThread;


import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import com.insubria.it.paroliere.threads.gameThread.GameThread;
import com.insubria.it.paroliere.threads.gameThread.interfaces.GameClient;


/**
 * The TimerThread class represents the thread that will handle the timers (game timer and review timer).
 * This class follows the observable/observer pattwern. This thread is the observable object and the clients that plays the game this thread represents are the observers.
 * This class extends the Thread interface to let the instances of this class to be threads.
 */
public class TimerThread extends Thread {
    /**
     * This represents the scope/role of the thread (in game timer, or review timer)
     */
    private String scope;

    /**
     * This is the list of clients the timer thread communicate the available time.
     */
    private ArrayList<GameClient> gameClientObservers;


    /**
     * The reference to the GameThread instance responsible for the game the timer thread is counting for
     */
    private GameThread gameResponsible;
    
    /**
     * Constructor called by the GameThread when timer thread needs to be started
     * 
     * @param scope - The scope of the timer
     * @param gameResponsible - The reference of the GameThread that started the TimerThread thread
     * @param gameClientObservers - The list of clients the timer thread communicate the available time.
     */
    public TimerThread (String scope, GameThread gameResponsible, ArrayList<GameClient> gameClientObservers) {
        this.scope = scope;
        this.gameClientObservers = gameClientObservers;
        this.gameResponsible = gameResponsible;

        this.start();
    }

    /**
     * This method perform the count down and sends to the clients the remaining time available.
     * 
     * @param seconds - Time to count down from
     * 
     * @throws InterruptedException - If the thread is interrupted while sleeping, it throws InterruptedException
     * @throws RemoteException - If there is an error while the client contact, it throws RemoteException
     */
    private void performCountdown (int seconds) throws InterruptedException, RemoteException {
        while (seconds > 0) {
            for (GameClient singleClient : this.gameClientObservers) {
                if (this.scope.equals("isPlaying")) {
                    singleClient.synchronizeInGameTimer(seconds);
                } else {
                    singleClient.synchronizeInWaitTimer(seconds);
                }
            }
            seconds--;
            Thread.sleep(1000);
        }
    }

    /**
     * When the thread starts it count the 3 minutes. After that, if the scope is "isPlaying" the thread call the GameThread method to stop the clients from playing.
     * If the scope is different it means that the review time is ended and a new session needs to be played (if nobody reached 50 score)
     */
    public void run () {
        try {
            this.performCountdown(180);

            if (this.scope.equals("isPlaying")) {
                CompletableFuture.runAsync(() -> {
                    this.gameResponsible.triggerEndOfSessionGameClient();
                });
            } else {
                this.gameResponsible.increaseSessionNumber();
                CompletableFuture.runAsync(() -> {
                    this.gameResponsible.handleStartNewSession();
                });
            }
        } catch (SQLException exc) {
            System.err.println("Error while contacting the db " + exc);
        } catch (RemoteException exc) {
            System.err.println("Error while contacting the client " + exc);
        } catch (InterruptedException exc) {
            System.err.println("Error while sleeping " + exc);
        }
    }
}