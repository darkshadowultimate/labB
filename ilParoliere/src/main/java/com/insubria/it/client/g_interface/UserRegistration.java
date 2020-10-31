package com.insubria.it.client.g_interface;

import com.insubria.it.client.g_components.*;

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
  private static final int ROWS = 0;
  private static final int COLS = 1;
  // Arrays variables
  private InputLabel[] inputLabels = new InputLabel[LABELS_TEXTS.length];
  // Single variables
  private Label titleRegistration;
  private Button submitButton;
  private GridFrame gridFrame;

  public UserRegistration() {
    gridFrame = new GridFrame(TITLE_WINDOW, ROWS, COLS);

    titleRegistration = new Label("Registrazione");

    // initialize labels and textfields (inside JPanels)
    for(int i = 0; i < LABELS_TEXTS.length; i++) {
      inputLabels[i] = new InputLabel(LABELS_TEXTS[i]);
    }

    submitButton = new Button("CONFERMA");

    // add all elements to container
    gridFrame.addToView(titleRegistration);
    for(int i = 0; i < LABELS_TEXTS.length; i++) {
      gridFrame.addToView(inputLabels[i]);
    }
    gridFrame.addToView(submitButton);

    gridFrame.showWindow();
  }
}