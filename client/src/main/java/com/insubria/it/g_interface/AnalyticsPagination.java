package com.insubria.it.g_interface;

import com.insubria.it.context.PlayerContextProvider;
import com.insubria.it.context.RemoteObjectContextProvider;
import com.insubria.it.g_components.Button;
import com.insubria.it.g_components.GridFrame;
import com.insubria.it.g_components.Label;
import com.insubria.it.serverImplClasses.MonitorClientImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class AnalyticsPagination {

    private static final String[] BUTTONS_TEXTS = {
            "Organizza Partita",
            "Visualizza Partite",
            "Modifica Profilo",
            "Analizza Statistiche"
    };
    private static final String TITLE_WINDOW = "Il Paroliere - Analitycs";
    private static final String BUTTON_BACK_HOME_TEXT = "Torna alla Home";
    private static final String BUTTON_BACK_ANALYTICS_TEXT = "Torna ad analytics";
    private static final String BUTTON_PREVIOUS_PAGE_TEXT = "Pagina Precedente";
    private static final String BUTTON_NEXT_PAGE_TEXT = "Pagina Successiva";
    private static final int COLS_MAIN_CONTAINER = 1;
    private static final int COLS_BUTTONS = 4;
    private static final int ROWS = 0;

    private String mainTitle = "Analytics - ";
    private String section = "";
    private int paginationNum = 0;
    private Button backToHome, backToAnalytics, nextPage, previousPage;
    private Label titlePage, data;
    private GridFrame gridButtonFrame, gridContainer, parentGridAnalytics;

    public AnalyticsPagination(GridFrame gridAnalyticsHome, String analyticsSection, String dataToShow, int page) {
        // The mainTitle needs to be placed inside the constructor to update its value after the user's profile update
        parentGridAnalytics = gridAnalyticsHome;
        section = analyticsSection;
        paginationNum = page;

        mainTitle += analyticsSection;

        gridButtonFrame = new GridFrame(ROWS, COLS_BUTTONS);

        gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS_MAIN_CONTAINER);

        titlePage = new Label(mainTitle);
        data = new Label(dataToShow);

        backToHome = new Button(BUTTON_BACK_HOME_TEXT);
        backToAnalytics = new Button(BUTTON_BACK_ANALYTICS_TEXT);
        previousPage = new Button(BUTTON_PREVIOUS_PAGE_TEXT);
        nextPage = new Button(BUTTON_NEXT_PAGE_TEXT);

        addAllEventListeners();

        gridContainer.addToView(titlePage);
        gridContainer.addToView(data);

        gridButtonFrame.addToView(backToHome);
        gridButtonFrame.addToView(backToAnalytics);
        gridButtonFrame.addToView(previousPage);
        gridButtonFrame.addToView(nextPage);

        gridContainer.addToView(gridButtonFrame);

        gridContainer.showWindow();
    }

    private void addAllEventListeners() {
        backToHome.attachActionListenerToButton(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                redirectToHomeFrame();
            }
        });
        backToAnalytics.attachActionListenerToButton(new ActionListener() {
            public void actionPerformed(ActionEvent me) {
                redirectToAnalyticsFrame();
            }
        });
        nextPage.attachActionListenerToButton(new ActionListener() {
            public void actionPerformed(ActionEvent me) {
                loadDataWithPaginationFromServer(section, paginationNum + 1);
            }
        });
        previousPage.attachActionListenerToButton(new ActionListener() {
            public void actionPerformed(ActionEvent me) {
                loadDataWithPaginationFromServer(section, paginationNum - 1);
            }
        });
    }

    private void loadDataWithPaginationFromServer(String type, final int pageNum) {
        try {
            switch(type) {
                case "occorrenzeParoleValide": {
                    RemoteObjectContextProvider
                    .server
                    .validWordsOccurrences(
                        new MonitorClientImpl() {
                            @Override
                            public void confirmValidWordsOccurrences(String[] result) throws RemoteException {
                                super.confirmValidWordsOccurrences(result);

                                loadNewPaginationFrame(Analytics.convertArrayIntoStringFormatted(result), pageNum);
                            }

                            @Override
                            public void errorValidWordsOccurrences(String reason) throws RemoteException {
                                super.errorValidWordsOccurrences(reason);

                                Analytics.errorResultOperation(reason);
                            }
                        }, pageNum
                    );
                }
                case "paroleConValoreMaggiore": {
                    RemoteObjectContextProvider
                    .server
                    .wordHighestScore(
                        new MonitorClientImpl() {
                            @Override
                            public void confirmWordHighestScore(String[] result) throws RemoteException {
                                super.confirmWordHighestScore(result);

                                loadNewPaginationFrame(Analytics.convertArrayIntoStringFormatted(result), pageNum);
                            }

                            @Override
                            public void errorWordHighestScore(String reason) throws RemoteException {
                                super.errorWordHighestScore(reason);

                                Analytics.errorResultOperation(reason);
                            }
                        },pageNum
                    );
                }
                case "paroleVisualizzateMaggiormente": {
                    RemoteObjectContextProvider
                    .server
                    .definitionRequest(
                        new MonitorClientImpl() {
                            @Override
                            public void confirmDefinitionRequest(String[] result) throws RemoteException {
                                super.confirmDefinitionRequest(result);

                                loadNewPaginationFrame(Analytics.convertArrayIntoStringFormatted(result), pageNum);
                            }

                            @Override
                            public void errorDefinitionRequest(String reason) throws RemoteException {
                                super.errorDefinitionRequest(reason);

                                Analytics.errorResultOperation(reason);
                            }
                        }, 1
                    );
                }
                case "giochiConPiùParoleVisualizzateMaggiormente": {
                    RemoteObjectContextProvider
                    .server
                    .gameDefinitionRequest(
                        new MonitorClientImpl() {
                            @Override
                            public void confirmGameDefinitionRequest(String[] result) throws RemoteException {
                                super.confirmGameDefinitionRequest(result);

                                loadNewPaginationFrame(Analytics.convertArrayIntoStringFormatted(result), pageNum);
                            }

                            @Override
                            public void errorGameDefinitionRequest(String reason) throws RemoteException {
                                super.errorGameDefinitionRequest(reason);

                                Analytics.errorResultOperation(reason);
                            }
                        }, 1
                    );
                }
            }
        } catch(RemoteException exc) {
            exc.printStackTrace();
        }
    }

    private void redirectToHomeFrame() {
        Home home = new Home();
        parentGridAnalytics.disposeFrame();
        gridContainer.disposeFrame();
    }

    private void redirectToAnalyticsFrame() {
        gridContainer.disposeFrame();
    }

    private void loadNewPaginationFrame(String dataFromServer, int pageNum) {
        AnalyticsPagination analyticsPagination = new AnalyticsPagination(parentGridAnalytics, section, dataFromServer, pageNum);
        gridContainer.disposeFrame();
    }
}
