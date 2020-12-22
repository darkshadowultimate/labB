package com.insubria.it.g_interface;

import com.insubria.it.g_components.*;

public class ResetPasswordStatus {
  // Constants
  private static final String TITLE_WINDOW = "Il Paroliere - Reset password";
  private static final String CONFIRM_BUTTON = "OK";
  private static final int ROWS = 0;
  private static final int COLS = 1;
  // Variables
  private Label mainTitle;
  private Button confirmButton;
  private GridFrame gridContainer;

  public ResetPasswordStatus(String messageStatus) {
    gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS);

    mainTitle = new Label(messageStatus);
    confirmButton = new Button(CONFIRM_BUTTON);

    gridContainer.addToView(mainTitle);
    gridContainer.addToView(confirmButton);

    gridContainer.showWindow();
  }
}