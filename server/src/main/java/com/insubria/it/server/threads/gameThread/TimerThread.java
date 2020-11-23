package com.insubria.it.server.threads.gameThread;


import java.rmi.RemoteException;
import java.util.ArrayList;

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

            //@TODO Implement the async call to the gameResponsible to trigger the end of game or new game session
        } catch (RemoteException exc) {
            System.err.println("Error while contacting the client " + exc);
        } catch (InterruptedException exc) {
            System.err.println("Error while sleeping " + exc);
        }
    }
}