package com.insubria.it.paroliere.g_interface;


import java.awt.event.*;

import com.insubria.it.paroliere.g_components.*;

public class UserRegistration {
  // Constants
  private static final String[] LABELS_TEXTS = { 
    "Nome", 
    "Cognome", 
    "Nickname", 
    "E-mail", 
    "Password", 
    "Conferma password" 
  };
  private static final String TITLE_WINDOW = "Registrazione utente";
  private static final String CONFIRM_BUTTON_TEXT = "CONFERMA";
  private static final String CANCEL_BUTTON_TEXT = "ANNULLA";
  private static final int ROWS = 0;
  private static final int COLS = 1;
  private static final int COLS_BUTTONS = 2;
  // Arrays variables
  private InputLabel[] inputLabels = new InputLabel[LABELS_TEXTS.length];
  // Single variables
  private Label titleRegistration;
  private Button submitButton, cancelButton;
  private GridFrame gridFrame, gridButtons;

  public UserRegistration() {
    gridFrame = new GridFrame(TITLE_WINDOW, ROWS, COLS);
    gridButtons = new GridFrame(ROWS, COLS_BUTTONS);

    titleRegistration = new Label("Registrazione");

    // initialize labels and textfields (inside JPanels)
    for(int i = 0; i < LABELS_TEXTS.length; i++) {
      inputLabels[i] = new InputLabel(LABELS_TEXTS[i]);
    }

    submitButton = new Button(CONFIRM_BUTTON_TEXT);
    cancelButton = new Button(CANCEL_BUTTON_TEXT);

    addAllEventListeners();
    // add all elements to container
    gridFrame.addToView(titleRegistration);
    for(int i = 0; i < LABELS_TEXTS.length; i++) {
      gridFrame.addToView(inputLabels[i]);
    }
    gridButtons.addToView(submitButton);
    gridButtons.addToView(cancelButton);

    gridFrame.addToView(gridButtons);

    gridFrame.showWindow();
  }

  private void addAllEventListeners() {
    submitButton.attachActionListenerToButton(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            // this is until I create the integration with server side
            boolean isUserAuthorized = true;
            if(isUserAuthorized) {
                redirectToHomeFrame();
            } else {
                // display an error message
            }
        }
    });
    cancelButton.attachActionListenerToButton(new ActionListener() {
        public void actionPerformed(ActionEvent me) { 
          redirectToLoginFrame();
        }
    });
  }

  // update the PlayerContextProvider fields value
  /* private void updatePlayerInfo() {
    PlayerContextProvider.setEmail(email.getValueTextField());
  } */

  private void redirectToHomeFrame() {
    Home home = new Home();
    gridFrame.disposeFrame();
  }

  private void redirectToLoginFrame() {
    LoginUtente login = new LoginUtente();
    gridFrame.disposeFrame();
  }
}