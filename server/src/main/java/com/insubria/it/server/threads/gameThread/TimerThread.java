package com.insubria.it.server.threads.gameThread;


import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import com.insubria.it.server.threads.gameThread.GameThread;
import com.insubria.it.server.threads.gameThread.interfaces.GameClient;


public class TimerThread extends Thread {
    private String scope;
    private ArrayList<GameClient> gameClientObservers;

    private GameThread gameResponsible;
    
    public TimerThread (String scope, GameThread gameResponsible, ArrayList<GameClient> gameClientObservers) {
        this.scope = scope;
        this.gameClientObservers = gameClientObservers;
        this.gameResponsible = gameResponsible;

        this.start();
    }

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

    public void run () {
        try {
            this.performCountdown(180);

            if (this.scope.equals("isPlaying")) {
                CompletableFuture.runAsync(() -> {
                    this.gameResponsible.triggerEndOfSessionGameClient();
                });
            } else {
                // @TODO Implement logic to trigger when the check timer ends (so new session needs to be provided in nobody reached 50 score)
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