package com.insubria.it.g_interface;

import java.awt.event.*;
import java.rmi.RemoteException;
import javax.swing.JOptionPane;

import com.insubria.it.context.PlayerContextProvider;
import com.insubria.it.context.RemoteObjectContextProvider;
import com.insubria.it.g_components.*;
import com.insubria.it.helpers.Validation;
import com.insubria.it.serverImplClasses.PlayerCredentialsImpl;

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
  private static final String CHECK_FORM_ERROR_TEXT = "Tutti i campi devono essere compilati e le passwords devono coincidere";
  private static final String REGISTRATION_ERROR_TEXT = "Ops... Sembra che sia stato un problema durante la registrazione...";
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
    for (int i = 0; i < LABELS_TEXTS.length; i++) {
      inputLabels[i] = new InputLabel(LABELS_TEXTS[i]);
    }

    submitButton = new Button(CONFIRM_BUTTON_TEXT);
    cancelButton = new Button(CANCEL_BUTTON_TEXT);

    addAllEventListeners();
    // add all elements to container
    gridFrame.addToView(titleRegistration);
    for (int i = 0; i < LABELS_TEXTS.length; i++) {
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
        if(checkFormFields(inputLabels)) {
          final String name = inputLabels[0].getValueTextField();
          final String surname = inputLabels[1].getValueTextField();
          final String username = inputLabels[2].getValueTextField();
          String email = inputLabels[3].getValueTextField();
          String password = inputLabels[4].getValueTextField();


          try {
            RemoteObjectContextProvider
            .server
            .createPlayerAccount(
              name,
              surname,
              username,
              email,
              password,
              new PlayerCredentialsImpl() {
                @Override
                public void confirmPlayerRegistration() throws RemoteException {
                  super.confirmPlayerRegistration();

                  PlayerContextProvider.setPlayerInfo(name, surname, username);

                  redirectConfirmCodeFrame();
                }

                @Override
                public void errorPlayerRegistration(String reason) throws RemoteException {
                  super.errorPlayerRegistration(reason);
                  JOptionPane.showMessageDialog(null, REGISTRATION_ERROR_TEXT);
                }
              }
            );
          } catch(RemoteException exc) {}
        } else {
          JOptionPane.showMessageDialog(null, CHECK_FORM_ERROR_TEXT);
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
  /*
   * private void updatePlayerInfo() {
   * PlayerContextProvider.setEmail(email.getValueTextField()); }
   */

  private boolean checkFormFields(InputLabel[] inputFields) {

    for (int i = 0; i < LABELS_TEXTS.length; i++) {
      if(!Validation.isFieldFilled(inputFields[i].getValueTextField())) {
        return false;
      }
    }

    String email = inputFields[3].getValueTextField();

    if(!Validation.validateEmail(email)) {
      return false;
    }

    String password = inputFields[LABELS_TEXTS.length - 2].getValueTextField();
    String confirmPassword = inputFields[LABELS_TEXTS.length - 1].getValueTextField();

    return
      password.equals(confirmPassword) &&
      password.length() > 3;
  }

  private void redirectConfirmCodeFrame() {
    ConfirmCode confirmCode = new ConfirmCode();
    gridFrame.disposeFrame();
  }

  private void redirectToLoginFrame() {
    LoginUtente login = new LoginUtente();
    gridFrame.disposeFrame();
  }
}