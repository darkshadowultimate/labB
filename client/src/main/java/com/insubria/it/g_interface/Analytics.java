package com.insubria.it.g_interface;

import com.insubria.it.g_components.*;
import com.insubria.it.context.*;
import com.insubria.it.serverImplClasses.MonitorClientImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Analytics {
    private static final String[] LABELS_INFO = {
        "Giocatore con punteggio più alto (per sessione e partita): ",
        "Giocatore con più sessioni di gioco: ",
        "Giocatore con la media più alta di punti (per sessione e partita): ",
        "Giocatore con il più alto numero di parole duplicate (ed il loro numero): ",
        "Giocatore con il più alto numero di parole non derivate dalla griglia: ",
        "Classifica occorenze parole valide identificate: ",
        "Parole che hanno causato il più alto incremento di punti: ",
        "Numero medio di turni di gioco: ",
        "Numero minimo e massimo di turni di gioco: ",
        "Occorrenza media delle lettere apparse nella griglia: ",
        "Parole di cui è stata richiesta la definizione: ",
        "Identificativo partita di cui si è richiesta la definizione di una parola: ",
    };
    private static final String TITLE_WINDOW = "Il Paroliere - Analitycs";
    private static final String HOME_BUTTON_TEXT = "Torna alla Home";
    private static final String ERROR_NO_DATA_TEXT = "Nessun dato disponibile";
    private static final int ROWS = 0;
    private static final int COLS = 1;

    private Label[] labels = new Label[LABELS_INFO.length];
    private Button homeButton;
    private GridFrame gridContainer;

    public Analytics() {
        gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS);

        homeButton = new Button(HOME_BUTTON_TEXT);

        initializeLabels();

        addAllEventListeners();

        for(int i = 0; i < LABELS_INFO.length; i++) {
            if(labels[i] != null) {
                System.out.println("Text => " + labels[i].getLabelText());
                gridContainer.addToView(labels[i]);
            }
        }

        gridContainer.addToView(homeButton);

        gridContainer.showWindow(800, 1000);
    }

    private void initializeLabels() {
        scoreGameAndSession(0);
        sessionsPlayed(1);
        avgScoreGameAndSession(2);
        proposedDuplicatedWords(3);
        invalidProposedWords(4);
        validWordsOccurrencesList(5);
        wordHighestScoreList(6);
        averageRoundsList(7);
        definitionRequestList(10);
        gameDefinitionRequestList(11);
        minMaxRoundsList(8);
        charactersAvgOccurrenceList(9);
    }

    private void scoreGameAndSession(final int indexLabelArray) {
        try {
            RemoteObjectContextProvider
            .server
            .moreScoreGameAndSession(
                new MonitorClientImpl() {
                    @Override
                    public void confirmMoreScoreGameAndSession(String[] result) throws RemoteException {
                        super.confirmMoreScoreGameAndSession(result);

                        updateLabelArray(indexLabelArray, convertArrayIntoStringFormatted(result));
                    }

                    @Override
                    public void errorMoreScoreGameAndSession(String reason) throws RemoteException {
                        super.errorMoreScoreGameAndSession(reason);

                        errorResultOperation(indexLabelArray);
                    }
                }
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void sessionsPlayed(final int indexLabelArray) {
        try {
            RemoteObjectContextProvider
            .server
            .moreSessionsPlayed(
                new MonitorClientImpl() {
                    @Override
                    public void confirmMoreSessionsPlayed(String result) throws RemoteException {
                        super.confirmMoreSessionsPlayed(result);
                        updateLabelArray(indexLabelArray, result);
                    }

                    @Override
                    public void errorMoreSessionsPlayed(String reason) throws RemoteException {
                        super.errorMoreSessionsPlayed(reason);

                        errorResultOperation(indexLabelArray);
                    }
                }
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void avgScoreGameAndSession(final int indexLabelArray) {
        try {
            RemoteObjectContextProvider
            .server
            .moreAvgScoreGameAndSession(
                new MonitorClientImpl() {
                    @Override
                    public void confirmMoreAvgScoreGameAndSession(String[] result) throws RemoteException {
                        super.confirmMoreAvgScoreGameAndSession(result);

                        updateLabelArray(indexLabelArray, convertArrayIntoStringFormatted(result));
                    }

                    @Override
                    public void errorMoreAvgScoreGameAndSession(String reason) throws RemoteException {
                        super.errorMoreAvgScoreGameAndSession(reason);

                        errorResultOperation(indexLabelArray);
                    }
                }
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void proposedDuplicatedWords(final int indexLabelArray) {
        try {
            RemoteObjectContextProvider
            .server
            .moreProposedDuplicatedWords(
                new MonitorClientImpl() {
                    @Override
                    public void confirmMoreProposedDuplicatedWords(String result) throws RemoteException {
                        super.confirmMoreProposedDuplicatedWords(result);

                        updateLabelArray(indexLabelArray, result);
                    }

                    @Override
                    public void errorMoreProposedDuplicatedWords(String reason) throws RemoteException {
                        super.errorMoreProposedDuplicatedWords(reason);

                        errorResultOperation(indexLabelArray);
                    }
                }
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void invalidProposedWords(final int indexLabelArray) {
        try {
            RemoteObjectContextProvider
            .server
            .moreInvalidProposedWords(
                new MonitorClientImpl() {
                    @Override
                    public void confirmMoreInvalidProposedWords(String result) throws RemoteException {
                        super.confirmMoreInvalidProposedWords(result);

                        updateLabelArray(indexLabelArray, result);
                    }

                    @Override
                    public void errorMoreInvalidProposedWords(String reason) throws RemoteException {
                        super.errorMoreInvalidProposedWords(reason);

                        errorResultOperation(indexLabelArray);
                    }
                }
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void validWordsOccurrencesList(final int indexLabelArray) {
        try {
            RemoteObjectContextProvider
            .server
            .validWordsOccurrences(
                new MonitorClientImpl() {
                    @Override
                    public void confirmValidWordsOccurrences(String[] result) throws RemoteException {
                        super.confirmValidWordsOccurrences(result);

                        updateLabelArray(indexLabelArray, convertArrayIntoStringFormatted(result));
                    }

                    @Override
                    public void errorValidWordsOccurrences(String reason) throws RemoteException {
                        super.errorValidWordsOccurrences(reason);

                        errorResultOperation(indexLabelArray);
                    }
                },1
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void wordHighestScoreList(final int indexLabelArray) {
        try {
            RemoteObjectContextProvider
            .server
            .wordHighestScore(
                new MonitorClientImpl() {
                    @Override
                    public void confirmWordHighestScore(String[] result) throws RemoteException {
                        super.confirmWordHighestScore(result);

                        updateLabelArray(indexLabelArray, convertArrayIntoStringFormatted(result));
                    }

                    @Override
                    public void errorWordHighestScore(String reason) throws RemoteException {
                        super.errorWordHighestScore(reason);

                        errorResultOperation(indexLabelArray);
                    }
                },1
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void averageRoundsList(final int indexLabelArray) {
        try {
            RemoteObjectContextProvider
            .server
            .averageRounds(
                new MonitorClientImpl() {
                    @Override
                    public void confirmAverageRounds(String[] result) throws RemoteException {
                        super.confirmAverageRounds(result);

                        updateLabelArray(indexLabelArray, convertArrayIntoStringFormatted(result));
                    }

                    @Override
                    public void errorAverageRounds(String reason) throws RemoteException {
                        super.errorAverageRounds(reason);

                        errorResultOperation(indexLabelArray);
                    }
                }
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void minMaxRoundsList(final int indexLabelArray) {
        try {
            RemoteObjectContextProvider
            .server
            .averageRounds(
                new MonitorClientImpl() {
                    @Override
                    public void confirmAverageRounds(String[] result) throws RemoteException {
                        super.confirmAverageRounds(result);

                        updateLabelArray(indexLabelArray, convertArrayIntoStringFormatted(result));
                    }

                    @Override
                    public void errorAverageRounds(String reason) throws RemoteException {
                        super.errorAverageRounds(reason);

                        errorResultOperation(indexLabelArray);
                    }
                }
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void charactersAvgOccurrenceList(final int indexLabelArray) {
        try {
            RemoteObjectContextProvider
            .server
            .charactersAvgOccurrence(
                new MonitorClientImpl() {
                    @Override
                    public void confirmCharactersAvgOccurrence(HashMap<Character, Double> result) throws RemoteException {
                        super.confirmCharactersAvgOccurrence(result);

                        String resultString = "";

                        Iterator it = result.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            resultString += pair.getKey() + " - " + pair.getValue();
                            // this avoids ConcurrentModificationException
                            it.remove();
                        }

                        updateLabelArray(indexLabelArray, resultString);
                    }

                    @Override
                    public void errorCharactersAvgOccurrence(String reason) throws RemoteException {
                        super.errorCharactersAvgOccurrence(reason);

                        errorResultOperation(indexLabelArray);
                    }
                }
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void definitionRequestList(final int indexLabelArray) {
        try {
            RemoteObjectContextProvider
            .server
            .definitionRequest(
                new MonitorClientImpl() {
                    @Override
                    public void confirmDefinitionRequest(String[] result) throws RemoteException {
                        super.confirmDefinitionRequest(result);

                        updateLabelArray(indexLabelArray, convertArrayIntoStringFormatted(result));
                    }

                    @Override
                    public void errorDefinitionRequest(String reason) throws RemoteException {
                        super.errorDefinitionRequest(reason);

                        errorResultOperation(indexLabelArray);
                    }
                }, 1
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void gameDefinitionRequestList(final int indexLabelArray) {
        try {
            RemoteObjectContextProvider
            .server
            .gameDefinitionRequest(
                new MonitorClientImpl() {
                    @Override
                    public void confirmGameDefinitionRequest(String[] result) throws RemoteException {
                        super.confirmGameDefinitionRequest(result);

                        updateLabelArray(indexLabelArray, convertArrayIntoStringFormatted(result));
                    }

                    @Override
                    public void errorGameDefinitionRequest(String reason) throws RemoteException {
                        super.errorGameDefinitionRequest(reason);

                        errorResultOperation(indexLabelArray);
                    }
                }, 1
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private String convertArrayIntoStringFormatted(String[] arrayString) {
        String resultStringFormatted = "";

        for(String singleRecord: arrayString) {
            resultStringFormatted += singleRecord + "\n";
        }

        return resultStringFormatted;
    }

    private void errorResultOperation(int labelIndex) {
        updateLabelArray(labelIndex, ERROR_NO_DATA_TEXT);
    }

    private void updateLabelArray(int indexLabel, String serverDataLabel) {
        labels[indexLabel] = new Label(LABELS_INFO[indexLabel] + serverDataLabel);
    }

    private void addAllEventListeners() {
        homeButton.attachActionListenerToButton(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                redirectToHomeFrame();
            }
        });
    }

    private void redirectToHomeFrame() {
        Home home = new Home();
        gridContainer.disposeFrame();
    }
}
