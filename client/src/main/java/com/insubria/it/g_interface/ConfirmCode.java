package com.insubria.it.g_interface;


import java.awt.event.*;
import java.rmi.RemoteException;

import com.insubria.it.context.RemoteObjectContextProvider;
import com.insubria.it.g_components.*;
import com.insubria.it.helpers.FrameHandler;
import com.insubria.it.serverImplClasses.PlayerCredentialsImpl;

import javax.swing.*;

public class ConfirmCode {
  // Constants
  private static final String TITLE_WINDOW = "Il Paroliere - Codice conferma e-mail";
  private static final String MAIN_TITLE = "Ti abbiamo inviato un codice conferma per e-mail.";
  private static final String EMPTY_FIELD_ERROR_TEXT = "Il codice non può essere vuoto";
  private static final String INVALID_CODE_ERROR_TEXT = "Il codice inserito non è valido";
  private static final String INSERT_CONFIRM_CODE_TEXT = "Inserisci codice conferma: ";
  private static final String CONFIRM_BUTTON = "Conferma";
  private static final String CANCEL_BUTTON = "Annulla";
  private static final int ROWS = 0;
  private static final int COLS = 1;
  // Variables
  private Label mainTitle;
  private InputLabel insertConfirmCodeInput;
  private Button confirmButton, cancelButton;
  private GridFrame gridContainer;

  public ConfirmCode() {
    gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS);

    mainTitle = new Label(MAIN_TITLE);
    insertConfirmCodeInput = new InputLabel(INSERT_CONFIRM_CODE_TEXT, false);
    confirmButton = new Button(CONFIRM_BUTTON);
    cancelButton = new Button(CANCEL_BUTTON);

    addAllEventListeners();

    gridContainer.addToView(mainTitle);
    gridContainer.addToView(insertConfirmCodeInput);
    gridContainer.addToView(confirmButton);
    gridContainer.addToView(cancelButton);

    FrameHandler.showMainGridContainer(gridContainer);
  }

  private void addAllEventListeners() {
    confirmButton.attachActionListenerToButton(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String confirmCode = insertConfirmCodeInput.getValueTextField();

        if(confirmCode.length() > 0) {
          try {
            RemoteObjectContextProvider
            .server
            .confirmPlayerAccount(
              confirmCode,
              new PlayerCredentialsImpl() {
                @Override
                public void confirmCodeConfirmation() throws RemoteException {
                  super.confirmPlayerRegistration();
                  redirectHomeFrame();
                }

                @Override
                public void errorCodeConfirmation(String reason) throws RemoteException {
                  super.errorPlayerRegistration(reason);
                  JOptionPane.showMessageDialog(null, INVALID_CODE_ERROR_TEXT);
                }
              }
            );
          } catch(RemoteException exc) {}
        } else {
          JOptionPane.showMessageDialog(null, EMPTY_FIELD_ERROR_TEXT);
        }
      }
    });
    cancelButton.attachActionListenerToButton(new ActionListener() {
      public void actionPerformed(ActionEvent me) {
        redirectRegistrationFrame();
      }
    });
  }

  private void redirectHomeFrame() {
    Home home = new Home();
  }

  private void redirectRegistrationFrame() {
    UserRegistration userRegistration = new UserRegistration(false);
  }
}