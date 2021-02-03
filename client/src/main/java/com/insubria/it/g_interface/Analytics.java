package com.insubria.it.g_interface;

import com.insubria.it.g_components.*;
import com.insubria.it.context.*;
import com.insubria.it.helpers.FrameHandler;
import com.insubria.it.serverImplClasses.MonitorClientImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Analytics {
    private static final String[] BUTTONS_TEXTS = {
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

    private Button[] buttons = new Button[BUTTONS_TEXTS.length];
    private Button homeButton;
    private GridFrame gridContainer;

    public Analytics() {
        gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS);

        homeButton = new Button(HOME_BUTTON_TEXT);
        
        addAllEventListeners();

        for(int i = 0; i < BUTTONS_TEXTS.length; i++) {
            buttons[i] = new Button(BUTTONS_TEXTS[i]);
            buttons[i].attachActionListenerToButton(getCorrectActionListener(i));
            gridContainer.addToView(buttons[i]);
        }

        gridContainer.addToView(homeButton);

        FrameHandler.showMainGridContainerWithSizes(gridContainer, 800, 1000);
    }

    private void scoreGameAndSession(final String sectionName) {
        try {
            RemoteObjectContextProvider
            .server
            .moreScoreGameAndSession(
                new MonitorClientImpl() {
                    @Override
                    public void confirmMoreScoreGameAndSession(String[] result) throws RemoteException {
                        super.confirmMoreScoreGameAndSession(result);

                        redirectToAnalyticsPagination(sectionName, convertArrayIntoStringFormatted(result));
                    }

                    @Override
                    public void errorMoreScoreGameAndSession(String reason) throws RemoteException {
                        super.errorMoreScoreGameAndSession(reason);

                        errorResultOperation(gridContainer, sectionName, reason);
                    }
                }
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void sessionsPlayed(final String sectionName) {
        try {
            RemoteObjectContextProvider
            .server
            .moreSessionsPlayed(
                new MonitorClientImpl() {
                    @Override
                    public void confirmMoreSessionsPlayed(String result) throws RemoteException {
                        super.confirmMoreSessionsPlayed(result);
                        redirectToAnalyticsPagination(sectionName, result);
                    }

                    @Override
                    public void errorMoreSessionsPlayed(String reason) throws RemoteException {
                        super.errorMoreSessionsPlayed(reason);

                        errorResultOperation(gridContainer, sectionName, reason);
                    }
                }
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void avgScoreGameAndSession(final String sectionName) {
        try {
            RemoteObjectContextProvider
            .server
            .moreAvgScoreGameAndSession(
                new MonitorClientImpl() {
                    @Override
                    public void confirmMoreAvgScoreGameAndSession(String[] result) throws RemoteException {
                        super.confirmMoreAvgScoreGameAndSession(result);

                        redirectToAnalyticsPagination(sectionName, convertArrayIntoStringFormatted(result));
                    }

                    @Override
                    public void errorMoreAvgScoreGameAndSession(String reason) throws RemoteException {
                        super.errorMoreAvgScoreGameAndSession(reason);

                        errorResultOperation(gridContainer, sectionName, reason);
                    }
                }
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void proposedDuplicatedWords(final String sectionName) {
        try {
            RemoteObjectContextProvider
            .server
            .moreProposedDuplicatedWords(
                new MonitorClientImpl() {
                    @Override
                    public void confirmMoreProposedDuplicatedWords(String result) throws RemoteException {
                        super.confirmMoreProposedDuplicatedWords(result);

                        redirectToAnalyticsPagination(sectionName, result);
                    }

                    @Override
                    public void errorMoreProposedDuplicatedWords(String reason) throws RemoteException {
                        super.errorMoreProposedDuplicatedWords(reason);

                        errorResultOperation(gridContainer, sectionName, reason);
                    }
                }
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void invalidProposedWords(final String sectionName) {
        try {
            RemoteObjectContextProvider
            .server
            .moreInvalidProposedWords(
                new MonitorClientImpl() {
                    @Override
                    public void confirmMoreInvalidProposedWords(String result) throws RemoteException {
                        super.confirmMoreInvalidProposedWords(result);

                        redirectToAnalyticsPagination(sectionName, result);
                    }

                    @Override
                    public void errorMoreInvalidProposedWords(String reason) throws RemoteException {
                        super.errorMoreInvalidProposedWords(reason);

                        errorResultOperation(gridContainer, sectionName, reason);
                    }
                }
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void validWordsOccurrencesList(final String sectionName) {
        try {
            RemoteObjectContextProvider
            .server
            .validWordsOccurrences(
                new MonitorClientImpl() {
                    @Override
                    public void confirmValidWordsOccurrences(String[] result) throws RemoteException {
                        super.confirmValidWordsOccurrences(result);

                        redirectToAnalyticsPagination(sectionName, convertArrayIntoStringFormatted(result));
                    }

                    @Override
                    public void errorValidWordsOccurrences(String reason) throws RemoteException {
                        super.errorValidWordsOccurrences(reason);

                        errorResultOperation(gridContainer, sectionName, reason);
                    }
                }
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void wordHighestScoreList(final String sectionName) {
        try {
            RemoteObjectContextProvider
            .server
            .wordHighestScore(
                new MonitorClientImpl() {
                    @Override
                    public void confirmWordHighestScore(String[] result) throws RemoteException {
                        super.confirmWordHighestScore(result);

                        redirectToAnalyticsPagination(sectionName, convertArrayIntoStringFormatted(result));
                    }

                    @Override
                    public void errorWordHighestScore(String reason) throws RemoteException {
                        super.errorWordHighestScore(reason);

                        errorResultOperation(gridContainer, sectionName, reason);
                    }
                }
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void averageRoundsList(final String sectionName) {
        try {
            RemoteObjectContextProvider
            .server
            .averageRounds(
                new MonitorClientImpl() {
                    @Override
                    public void confirmAverageRounds(String[] result) throws RemoteException {
                        super.confirmAverageRounds(result);

                        redirectToAnalyticsPagination(sectionName, convertArrayIntoStringFormatted(result));
                    }

                    @Override
                    public void errorAverageRounds(String reason) throws RemoteException {
                        super.errorAverageRounds(reason);

                        errorResultOperation(gridContainer, sectionName, reason);
                    }
                }
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void minMaxRoundsList(final String sectionName) {
        try {
            RemoteObjectContextProvider
            .server
            .averageRounds(
                new MonitorClientImpl() {
                    @Override
                    public void confirmAverageRounds(String[] result) throws RemoteException {
                        super.confirmAverageRounds(result);

                        redirectToAnalyticsPagination(sectionName, convertArrayIntoStringFormatted(result));
                    }

                    @Override
                    public void errorAverageRounds(String reason) throws RemoteException {
                        super.errorAverageRounds(reason);

                        errorResultOperation(gridContainer, sectionName, reason);
                    }
                }
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void charactersAvgOccurrenceList(final String sectionName) {
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

                        redirectToAnalyticsPagination(sectionName, resultString);
                    }

                    @Override
                    public void errorCharactersAvgOccurrence(String reason) throws RemoteException {
                        super.errorCharactersAvgOccurrence(reason);

                        errorResultOperation(gridContainer, sectionName, reason);
                    }
                }
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void definitionRequestList(final String sectionName) {
        try {
            RemoteObjectContextProvider
            .server
            .definitionRequest(
                new MonitorClientImpl() {
                    @Override
                    public void confirmDefinitionRequest(String[] result) throws RemoteException {
                        super.confirmDefinitionRequest(result);

                        redirectToAnalyticsPagination(sectionName, convertArrayIntoStringFormatted(result));
                    }

                    @Override
                    public void errorDefinitionRequest(String reason) throws RemoteException {
                        super.errorDefinitionRequest(reason);

                        errorResultOperation(gridContainer, sectionName, reason);
                    }
                }
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void gameDefinitionRequestList(final String sectionName) {
        try {
            RemoteObjectContextProvider
            .server
            .gameDefinitionRequest(
                new MonitorClientImpl() {
                    @Override
                    public void confirmGameDefinitionRequest(String[] result) throws RemoteException {
                        super.confirmGameDefinitionRequest(result);

                        redirectToAnalyticsPagination(sectionName, convertArrayIntoStringFormatted(result));
                    }

                    @Override
                    public void errorGameDefinitionRequest(String reason) throws RemoteException {
                        super.errorGameDefinitionRequest(reason);

                        errorResultOperation(gridContainer, sectionName, reason);
                    }
                }
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    public static String convertArrayIntoStringFormatted(String[] arrayString) {
        String resultStringFormatted = "<html>";

        for(String singleRecord: arrayString) {
            resultStringFormatted += singleRecord + "<br />";
        }

        return resultStringFormatted;
    }

    public static void errorResultOperation(GridFrame mainGridContainer, String sectionName, String messageFromServer) {
        redirectToAnalyticsPagination(sectionName, ERROR_NO_DATA_TEXT);
        System.out.println(messageFromServer);
    }

    private void addAllEventListeners() {
        homeButton.attachActionListenerToButton(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                redirectToHomeFrame();
            }
        });
    }

    private ActionListener getCorrectActionListener(final int indexButton) {
        switch(indexButton) {
            case 0:
                return new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        scoreGameAndSession(BUTTONS_TEXTS[indexButton]);
                    }
                };
            case 1:
                return new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        sessionsPlayed(BUTTONS_TEXTS[indexButton]);
                    }
                };
            case 2:
                return new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        avgScoreGameAndSession(BUTTONS_TEXTS[indexButton]);
                    }
                };
            case 3:
                return new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        proposedDuplicatedWords(BUTTONS_TEXTS[indexButton]);
                    }
                };
            case 4:
                return new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        invalidProposedWords(BUTTONS_TEXTS[indexButton]);
                    }
                };
            case 5:
                return new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        validWordsOccurrencesList(BUTTONS_TEXTS[indexButton]);
                    }
                };
            case 6:
                return new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        wordHighestScoreList(BUTTONS_TEXTS[indexButton]);
                    }
                };
            case 7:
                return new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        averageRoundsList(BUTTONS_TEXTS[indexButton]);
                    }
                };
            case 8:
                return new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        minMaxRoundsList(BUTTONS_TEXTS[indexButton]);
                    }
                };
            case 9:
                return new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        charactersAvgOccurrenceList(BUTTONS_TEXTS[indexButton]);
                    }
                };
            case 10:
                return new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        definitionRequestList(BUTTONS_TEXTS[indexButton]);
                    }
                };
            case 11:
                return new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        gameDefinitionRequestList(BUTTONS_TEXTS[indexButton]);
                    }
                };
            default:
                return null;
        }
    }

    private void redirectToHomeFrame() {
        Home home = new Home();
    }

    private static void redirectToAnalyticsPagination(String sectionName, String dataFromServer) {
        AnalyticsDataPage analyticsDataPage = new AnalyticsDataPage(sectionName, dataFromServer);
    }
}
