package com.insubria.it.g_interface;

import java.awt.event.*;
import java.rmi.RemoteException;
import javax.swing.JOptionPane;

import com.insubria.it.context.PlayerContextProvider;
import com.insubria.it.context.RemoteObjectContextProvider;
import com.insubria.it.g_components.*;
import com.insubria.it.helpers.FrameHandler;
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
  private static final String TITLE_WINDOW_REGISTRATION = "Registrazione utente";
  private static final String TITLE_WINDOW_UPDATE_PROFILE = "Aggiornamento profilo utente";
  private static final String MAIN_LABEL_REGISTRATION = "Registrazione";
  private static final String MAIN_LABEL_UPDATE_PROFILE = "Aggionarmento profilo utente";
  private static final String OLD_PASSWORD_LABEL_TEXT = "Vecchia password";
  private static final String NEW_PASSWORD_LABEL_TEXT = "Nuova password";
  private static final String CONFIRM_BUTTON_TEXT = "CONFERMA";
  private static final String CANCEL_BUTTON_TEXT = "ANNULLA";
  private static final String CHECK_FORM_ERROR_TEXT = "Tutti i campi devono essere compilati e le passwords devono coincidere";
  private static final String REGISTRATION_ERROR_TEXT = "Ops... Sembra che sia stato un problema durante la registrazione...";
  private static final String UPDATE_PROFILE_ERROR_TEXT = "Ops... Sembra che sia stato un problema durante l'aggiornamento del profilo...";
  private static final String UPDATE_PROFILE_SUCCESS_TEXT = "Il profilo è stato aggiornato con successo!";
  private static final int ROWS = 0;
  private static final int COLS = 1;
  private static final int COLS_BUTTONS = 2;
  // Arrays variables
  private InputLabel[] inputLabels = new InputLabel[LABELS_TEXTS.length];
  private InputLabel oldPasswordInput;
  // Single variables
  private Label titleRegistration;
  private Button submitButton, cancelButton;
  private GridFrame gridFrame, gridButtons;

  public UserRegistration(boolean isUserLogged) {
    gridFrame = new GridFrame(
      isUserLogged
        ? TITLE_WINDOW_UPDATE_PROFILE
        : TITLE_WINDOW_REGISTRATION,
      ROWS,
      COLS
    );
    gridButtons = new GridFrame(ROWS, COLS_BUTTONS);

    titleRegistration = new Label(isUserLogged ? MAIN_LABEL_UPDATE_PROFILE : MAIN_LABEL_REGISTRATION);

    // initialize labels and textfields (inside JPanels)
    for (int i = 0; i < LABELS_TEXTS.length; i++) {
      inputLabels[i] = new InputLabel(LABELS_TEXTS[i]);
      if(isUserLogged) {
        inputLabels[i].setValueInputField(getValueForInputField(i));
      }
    }

    if(isUserLogged) {
      inputLabels[4] = new InputLabel(NEW_PASSWORD_LABEL_TEXT);
      oldPasswordInput = new InputLabel(OLD_PASSWORD_LABEL_TEXT);
    }

    submitButton = new Button(CONFIRM_BUTTON_TEXT);
    cancelButton = new Button(CANCEL_BUTTON_TEXT);

    if(isUserLogged) {
      addAllEventListenersChangeUserProfile();
    } else {
      addAllEventListenersRegistration();
    }

    // add all elements to container
    gridFrame.addToView(titleRegistration);
    for (int i = 0; i < LABELS_TEXTS.length; i++) {
      if(isUserLogged && i == 4) {
        gridFrame.addToView(oldPasswordInput);
      }
      gridFrame.addToView(inputLabels[i]);
    }
    gridButtons.addToView(submitButton);
    gridButtons.addToView(cancelButton);

    gridFrame.addToView(gridButtons);

    FrameHandler.showMainGridContainer(gridFrame);
  }

  private String getValueForInputField(int indexInputField) {
    switch(indexInputField) {
      case 0:
        return PlayerContextProvider.getNamePlayer();
      case 1:
        return PlayerContextProvider.getSurnamePlayer();
      case 2:
        return PlayerContextProvider.getUsernamePlayer();
      case 3:
        return PlayerContextProvider.getEmailPlayer();
      default:
        return "";
    }
  }

  private void addAllEventListenersRegistration() {
    submitButton.attachActionListenerToButton(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // this is until I create the integration with server side
        if(checkFormFields(inputLabels)) {
          final String name = inputLabels[0].getValueTextField();
          final String surname = inputLabels[1].getValueTextField();
          final String username = inputLabels[2].getValueTextField();
          final String email = inputLabels[3].getValueTextField();
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

                  PlayerContextProvider.setPlayerInfo(name, surname, username, email);

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

  private void addAllEventListenersChangeUserProfile() {
    submitButton.attachActionListenerToButton(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // this is until I create the integration with server side
        if(checkFormFields(inputLabels)) {
          final String name = inputLabels[0].getValueTextField();
          final String surname = inputLabels[1].getValueTextField();
          final String username = inputLabels[2].getValueTextField();
          final String email = inputLabels[3].getValueTextField();
          final String password = inputLabels[4].getValueTextField();
          final String oldPassword = oldPasswordInput.getValueTextField();

          try {
            RemoteObjectContextProvider
            .server
            .changePlayerData(
              email,
              name,
              surname,
              username,
              password,
              oldPassword,
              new PlayerCredentialsImpl() {
                @Override
                public void confirmChangePlayerData() throws RemoteException {
                  super.confirmChangePlayerData();

                  updatePlayerContextProvider(name, surname, username, email);
                  JOptionPane.showMessageDialog(null, UPDATE_PROFILE_SUCCESS_TEXT);
                  redirectToHomeFrame();
                }

                @Override
                public void errorChangePlayerData(String reason) throws RemoteException {
                  super.errorChangePlayerData(reason);

                  JOptionPane.showMessageDialog(null, UPDATE_PROFILE_ERROR_TEXT);
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
        redirectToHomeFrame();
      }
    });
  }

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

  private void updatePlayerContextProvider(String name, String surname, String username, String email) {
    PlayerContextProvider.setPlayerInfo(name, surname, username, email);
  }

  private void redirectConfirmCodeFrame() {
    ConfirmCode confirmCode = new ConfirmCode();
  }

  private void redirectToLoginFrame() {
    LoginUtente login = new LoginUtente();
  }

  private void redirectToHomeFrame() {
    Home home = new Home();
  }
}