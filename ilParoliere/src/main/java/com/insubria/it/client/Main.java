package com.insubria.it.client;

import com.insubria.it.client.g_interface.*;
import java.util.ArrayList;

public class Main {

    public static final String[][] matrixLettersGamePlay = {
        { "A", "B", "C", "D" },
        { "A", "B", "C", "D" },
        { "A", "B", "C", "D" },
        { "A", "B", "C", "D" }
    };

    public static final String[] wordsToAnalyse = {
        "Lavorare",
        "Bagno",
        "Panino",
        "Occhiali",
        "Verde",
        "Mela",
        "Argento"
    };

    public static void main(String[] args) {
        // LoginUtente loginUtente = new LoginUtente();
        // UserRegistration registration = new UserRegistration();
        // Home home = new Home();
        // CreateNewGame createNewGame = new CreateNewGame();
        // WaitingPlayers waitingPlayers = new WaitingPlayers();
        // ListGames listGames = new ListGames();
        // WaitingStartGame waitingStartGame = new WaitingStartGame();
        // GameWinner gameWinner = new GameWinner();
        // NoWinners noWinners = new NoWinners();
        // GamePlay gamePlay = new GamePlay("La mia partita", matrixLettersGamePlay);
        // ConfirmCode confirmCode = new ConfirmCode();
        // ResetPassword resetPassword = new ResetPassword();
        // ResetPasswordStatus resetPasswordStatus = new ResetPasswordStatus("Errore: e-mail non trovata");
        
        ArrayList<String> words = new ArrayList<String>();

        for(String word: wordsToAnalyse) {
            words.add(word);
        }

        WordsAnalysis wordsAnalysis = new WordsAnalysis(words);
    }
}