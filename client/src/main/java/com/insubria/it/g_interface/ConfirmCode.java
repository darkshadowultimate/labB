package com.insubria.it.g_interface;


import java.awt.event.*;
import java.rmi.RemoteException;

import com.insubria.it.context.RemoteObjectContextProvider;
import com.insubria.it.g_components.*;
import com.insubria.it.helpers.FrameHandler;
import com.insubria.it.serverImplClasses.PlayerCredentialsImpl;

import javax.swing.*;

/**
 * The ConfirmCode class creates the ConfirmCode frame to allow the user
 * to inserted the confirmation code sent to his email in order to terminate the account registration
 */
public class ConfirmCode {
  /**
   * Static text that will be used with some UI components to communicate with the user
   */
  private static final String TITLE_WINDOW = "Il Paroliere - Codice conferma e-mail";
  private static final String MAIN_TITLE = "Ti abbiamo inviato un codice conferma per e-mail.";
  private static final String EMPTY_FIELD_ERROR_TEXT = "Il codice non può essere vuoto";
  private static final String INVALID_CODE_ERROR_TEXT = "Il codice inserito non è valido";
  private static final String INSERT_CONFIRM_CODE_TEXT = "Inserisci codice conferma: ";
  private static final String CONFIRM_BUTTON = "Conferma";
  private static final String CANCEL_BUTTON = "Annulla";
  /**
   * Rows for the grid container (0 stands for: unlimited number of rows)
   */
  private static final int ROWS = 0;
  /**
   * Columns for the grid container (only one element for row)
   */
  private static final int COLS = 1;

  /**
   * Labels to communicate with the user what he's looking at
   */
  private Label mainTitle;
  /**
   * Confirmation code sent by email
   */
  private InputLabel insertConfirmCodeInput;
  /**
   * confirmButton - Send the verification code to the server to terminate (or not) the account registration
   * cancelButton - Go back to login page
   */
  private Button confirmButton, cancelButton;
  /**
   * Grid containers to handle UI elements visualization
   */
  private GridFrame gridContainer;

  /**
   * Constructor of the class (creates the frame and its visual components)
   */
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

  /**
   * This method defines and attaches all ActionListeners to the appropriate UI elements
   */
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

  /**
   * This method displays on screen the Home section
   */
  private void redirectHomeFrame() {
    Home home = new Home();
  }

  /**
   * This method displays on screen the Registration section
   */
  private void redirectRegistrationFrame() {
    UserRegistration userRegistration = new UserRegistration(false);
  }
}