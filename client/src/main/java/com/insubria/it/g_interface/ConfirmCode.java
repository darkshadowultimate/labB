package com.insubria.it.g_interface;

import com.insubria.it.g_components.*;

public class ConfirmCode {
  // Constants
  private static final String TITLE_WINDOW = "Il Paroliere - Codice conferma e-mail";
  private static final String MAIN_TITLE = "Ti abbiamo inviato un codice conferma per e-mail.";
  private static final String INSERT_CONFIRM_CODE_TEXT = "Inserisci codice conferma: ";
  private static final String CONFIRM_BUTTON = "Conferma";
  private static final int ROWS = 0;
  private static final int COLS = 1;
  // Variables
  private Label mainTitle;
  private InputLabel insertConfirmCodeInput;
  private Button confirmButton;
  private GridFrame gridContainer;

  public ConfirmCode() {
    gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS);

    mainTitle = new Label(MAIN_TITLE);
    insertConfirmCodeInput = new InputLabel(INSERT_CONFIRM_CODE_TEXT);
    confirmButton = new Button(CONFIRM_BUTTON);

    gridContainer.addToView(mainTitle);
    gridContainer.addToView(insertConfirmCodeInput);
    gridContainer.addToView(confirmButton);

    gridContainer.showWindow();
  }
}