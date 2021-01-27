package com.insubria.it;

import java.awt.*;
import java.net.SocketPermission;
import java.rmi.RemoteException;
import java.util.ArrayList;

import com.insubria.it.context.RemoteObjectContextProvider;
import com.insubria.it.g_interface.*;
import com.insubria.it.serverImplClasses.PlayerCredentialsImpl;

public class Main {
		public static final String[][] matrixLettersGamePlay = { { "A", "B", "C", "D" }, { "A", "B", "C", "D" },
				{ "A", "B", "C", "D" }, { "A", "B", "C", "D" } };

		public static final String[] wordsToAnalyse = { "Lavorare", "Bagno", "Panino", "Occhiali", "Verde", "Mela",
				"Argento" };

		public static void main(String[] args) {
				RemoteObjectContextProvider.setServerRemoteObject(args);

				//SocketPermission perm = new SocketPermission("localhost:1024-", "accept,connect,listen,resolve");

				LoginUtente loginUtente = new LoginUtente();
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
				// ResetPasswordStatus resetPasswordStatus = new ResetPasswordStatus("Errore:
				// e-mail non trovata");

				/*
				 * ArrayList<String> words = new ArrayList<String>();
				 *
				 * for(String word: wordsToAnalyse) { words.add(word); }
				 *
				 * WordsAnalysis wordsAnalysis = new WordsAnalysis(words);
				 */
		}
}