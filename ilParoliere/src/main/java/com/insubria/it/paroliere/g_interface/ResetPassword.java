package com.insubria.it.paroliere.g_interface;

import com.insubria.it.paroliere.g_components.*;

public class ResetPassword {
  // Constants
  private static final String TITLE_WINDOW = "Il Paroliere - Reset password";
  private static final String MAIN_TITLE = "<html>Hai dimenticato la password?<br/> La nuova password ti verrà inviata per e-mail";
  private static final String INSERT_EMAIL_TEXT = "Inserisci e-mail: ";
  private static final String CONFIRM_BUTTON = "Resetta password";
  private static final int ROWS = 0;
  private static final int COLS = 1;
  // Variables
  private Label mainTitle;
  private InputLabel insertEmailInput;
  private Button confirmButton;
  private GridFrame gridContainer;

  public ResetPassword() {
    gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS);
    
    mainTitle = new Label(MAIN_TITLE);
    insertEmailInput = new InputLabel(INSERT_EMAIL_TEXT);
    confirmButton = new Button(CONFIRM_BUTTON);

    gridContainer.addToView(mainTitle);
    gridContainer.addToView(insertEmailInput);
    gridContainer.addToView(confirmButton);

    gridContainer.showWindow();
  }
}