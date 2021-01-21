package com.insubria.it.g_interface;

import com.insubria.it.g_components.*;
import com.insubria.it.context.*;
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

/*    private static final String[] testStrings = {
        "UserTot - 1",
        "UserTot - 2",
        "UserTot - 3",
        "UserTot - 4",
        "UserTot - 5",
        "UserTot - 6",
        "UserTot - 7",
        "UserTot - 8",
        "UserTot - 9",
        "UserTot - 10",
        "UserTot - 11",
        "UserTot - 12",
        "UserTot - 13",
        "UserTot - 14",
        "UserTot - 15",
        "UserTot - 16",
        "UserTot - 17",
        "UserTot - 18",
        "UserTot - 19",
        "UserTot - 20",
    };*/

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

        gridContainer.showWindow(800, 1000);
    }

    private void scoreGameAndSession() {
        try {
            RemoteObjectContextProvider
            .server
            .moreScoreGameAndSession(
                new MonitorClientImpl() {
                    @Override
                    public void confirmMoreScoreGameAndSession(String[] result) throws RemoteException {
                        super.confirmMoreScoreGameAndSession(result);

                        updateLabelArray(convertArrayIntoStringFormatted(result));
                    }

                    @Override
                    public void errorMoreScoreGameAndSession(String reason) throws RemoteException {
                        super.errorMoreScoreGameAndSession(reason);

                        errorResultOperation(reason);
                    }
                }
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void sessionsPlayed() {
        try {
            RemoteObjectContextProvider
            .server
            .moreSessionsPlayed(
                new MonitorClientImpl() {
                    @Override
                    public void confirmMoreSessionsPlayed(String result) throws RemoteException {
                        super.confirmMoreSessionsPlayed(result);
                        updateLabelArray(result);
                    }

                    @Override
                    public void errorMoreSessionsPlayed(String reason) throws RemoteException {
                        super.errorMoreSessionsPlayed(reason);

                        errorResultOperation(reason);
                    }
                }
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void avgScoreGameAndSession() {
        try {
            RemoteObjectContextProvider
            .server
            .moreAvgScoreGameAndSession(
                new MonitorClientImpl() {
                    @Override
                    public void confirmMoreAvgScoreGameAndSession(String[] result) throws RemoteException {
                        super.confirmMoreAvgScoreGameAndSession(result);

                        updateLabelArray(convertArrayIntoStringFormatted(result));
                    }

                    @Override
                    public void errorMoreAvgScoreGameAndSession(String reason) throws RemoteException {
                        super.errorMoreAvgScoreGameAndSession(reason);

                        errorResultOperation(reason);
                    }
                }
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void proposedDuplicatedWords() {
        try {
            RemoteObjectContextProvider
            .server
            .moreProposedDuplicatedWords(
                new MonitorClientImpl() {
                    @Override
                    public void confirmMoreProposedDuplicatedWords(String result) throws RemoteException {
                        super.confirmMoreProposedDuplicatedWords(result);

                        updateLabelArray(result);
                    }

                    @Override
                    public void errorMoreProposedDuplicatedWords(String reason) throws RemoteException {
                        super.errorMoreProposedDuplicatedWords(reason);

                        errorResultOperation(reason);
                    }
                }
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void invalidProposedWords() {
        try {
            RemoteObjectContextProvider
            .server
            .moreInvalidProposedWords(
                new MonitorClientImpl() {
                    @Override
                    public void confirmMoreInvalidProposedWords(String result) throws RemoteException {
                        super.confirmMoreInvalidProposedWords(result);

                        updateLabelArray(result);
                    }

                    @Override
                    public void errorMoreInvalidProposedWords(String reason) throws RemoteException {
                        super.errorMoreInvalidProposedWords(reason);

                        errorResultOperation(reason);
                    }
                }
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void validWordsOccurrencesList() {
        try {
            RemoteObjectContextProvider
            .server
            .validWordsOccurrences(
                new MonitorClientImpl() {
                    @Override
                    public void confirmValidWordsOccurrences(String[] result) throws RemoteException {
                        super.confirmValidWordsOccurrences(result);

                        redirectToAnalyticsPagination("occorrenzeParoleValide", convertArrayIntoStringFormatted(result));
                    }

                    @Override
                    public void errorValidWordsOccurrences(String reason) throws RemoteException {
                        super.errorValidWordsOccurrences(reason);

                        errorResultOperation(reason);
                    }
                },1
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void wordHighestScoreList() {
        try {
            RemoteObjectContextProvider
            .server
            .wordHighestScore(
                new MonitorClientImpl() {
                    @Override
                    public void confirmWordHighestScore(String[] result) throws RemoteException {
                        super.confirmWordHighestScore(result);

                        redirectToAnalyticsPagination("paroleConValoreMaggiore", convertArrayIntoStringFormatted(result));
                    }

                    @Override
                    public void errorWordHighestScore(String reason) throws RemoteException {
                        super.errorWordHighestScore(reason);

                        errorResultOperation(reason);
                    }
                },1
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void averageRoundsList() {
        try {
            RemoteObjectContextProvider
            .server
            .averageRounds(
                new MonitorClientImpl() {
                    @Override
                    public void confirmAverageRounds(String[] result) throws RemoteException {
                        super.confirmAverageRounds(result);

                        updateLabelArray(convertArrayIntoStringFormatted(result));
                    }

                    @Override
                    public void errorAverageRounds(String reason) throws RemoteException {
                        super.errorAverageRounds(reason);

                        errorResultOperation(reason);
                    }
                }
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void minMaxRoundsList() {
        try {
            RemoteObjectContextProvider
            .server
            .averageRounds(
                new MonitorClientImpl() {
                    @Override
                    public void confirmAverageRounds(String[] result) throws RemoteException {
                        super.confirmAverageRounds(result);

                        updateLabelArray(convertArrayIntoStringFormatted(result));
                    }

                    @Override
                    public void errorAverageRounds(String reason) throws RemoteException {
                        super.errorAverageRounds(reason);

                        errorResultOperation(reason);
                    }
                }
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void charactersAvgOccurrenceList() {
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

                        updateLabelArray(resultString);
                    }

                    @Override
                    public void errorCharactersAvgOccurrence(String reason) throws RemoteException {
                        super.errorCharactersAvgOccurrence(reason);

                        errorResultOperation(reason);
                    }
                }
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void definitionRequestList() {
        try {
            RemoteObjectContextProvider
            .server
            .definitionRequest(
                new MonitorClientImpl() {
                    @Override
                    public void confirmDefinitionRequest(String[] result) throws RemoteException {
                        super.confirmDefinitionRequest(result);

                        redirectToAnalyticsPagination("paroleVisualizzateMaggiormente", convertArrayIntoStringFormatted(result));
                    }

                    @Override
                    public void errorDefinitionRequest(String reason) throws RemoteException {
                        super.errorDefinitionRequest(reason);

                        errorResultOperation(reason);
                    }
                }, 1
            );
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void gameDefinitionRequestList() {
        try {
            RemoteObjectContextProvider
            .server
            .gameDefinitionRequest(
                new MonitorClientImpl() {
                    @Override
                    public void confirmGameDefinitionRequest(String[] result) throws RemoteException {
                        super.confirmGameDefinitionRequest(result);

                        redirectToAnalyticsPagination("giochiConPiùParoleVisualizzateMaggiormente", convertArrayIntoStringFormatted(result));

                        updateLabelArray(convertArrayIntoStringFormatted(result));
                    }

                    @Override
                    public void errorGameDefinitionRequest(String reason) throws RemoteException {
                        super.errorGameDefinitionRequest(reason);

                        errorResultOperation(reason);
                    }
                }, 1
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

    public static void errorResultOperation(String messageFromServer) {
        updateLabelArray(ERROR_NO_DATA_TEXT);
        System.out.println(messageFromServer);
    }

    private static void updateLabelArray(String serverDataLabel) {
        //String degubString = convertArrayIntoStringFormatted(testStrings);
        //JOptionPane.showMessageDialog(null, degubString);
        JOptionPane.showMessageDialog(null, serverDataLabel);
    }

    private void addAllEventListeners() {
        homeButton.attachActionListenerToButton(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                redirectToHomeFrame();
            }
        });
    }

    private ActionListener getCorrectActionListener(int indexButton) {
        switch(indexButton) {
            case 0:
                return new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        scoreGameAndSession();
                    }
                };
            case 1:
                return new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        sessionsPlayed();
                    }
                };
            case 2:
                return new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        avgScoreGameAndSession();
                    }
                };
            case 3:
                return new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        proposedDuplicatedWords();
                    }
                };
            case 4:
                return new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        invalidProposedWords();
                    }
                };
            case 5:
                return new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        validWordsOccurrencesList();
                    }
                };
            case 6:
                return new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        wordHighestScoreList();
                    }
                };
            case 7:
                return new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        averageRoundsList();
                    }
                };
            case 8:
                return new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        minMaxRoundsList();
                    }
                };
            case 9:
                return new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        charactersAvgOccurrenceList();
                    }
                };
            case 10:
                return new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        definitionRequestList();
                    }
                };
            case 11:
                return new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        gameDefinitionRequestList();
                    }
                };
            default:
                return null;
        }
    }

    private void redirectToHomeFrame() {
        Home home = new Home();
        gridContainer.disposeFrame();
    }

    private void redirectToAnalyticsPagination(String sectionToLoad, String dataFromServer) {
        AnalyticsPagination analyticsPagination = new AnalyticsPagination(gridContainer, sectionToLoad, dataFromServer, 1);
    }
}
