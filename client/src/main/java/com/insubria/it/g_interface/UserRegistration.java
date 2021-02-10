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

/**
 * The UserRegistration class creates the UserRegistration frame to allow the user
 * to create a new account or (if logged in), to update the info of the account
 */
public class UserRegistration {
  /**
   * Static text that will be used with some UI components to communicate with the user
   */
  private static final String[] LABELS_TEXTS = {
    "Nome",
    "Cognome",
    "Nickname",
    "E-mail"
  };
  private static final String TITLE_WINDOW_REGISTRATION = "Registrazione utente";
  private static final String TITLE_WINDOW_UPDATE_PROFILE = "Aggiornamento profilo utente";
  private static final String MAIN_LABEL_REGISTRATION = "Registrazione";
  private static final String PASSWORD_LABEL_TEXT = "Password";
  private static final String CONFIRM_PASSWORD_LABEL_TEXT = "Conferma password";
  private static final String MAIN_LABEL_UPDATE_PROFILE = "Aggionarmento profilo utente";
  private static final String OLD_PASSWORD_LABEL_TEXT = "Vecchia password";
  private static final String NEW_PASSWORD_LABEL_TEXT = "Nuova password";
  private static final String CONFIRM_BUTTON_TEXT = "CONFERMA";
  private static final String CHECK_FORM_ERROR_TEXT = "Tutti i campi devono essere compilati e le passwords devono coincidere";
  private static final String REGISTRATION_ERROR_TEXT = "Ops... Sembra che sia stato un problema durante la registrazione...";
  private static final String UPDATE_PROFILE_ERROR_TEXT = "Ops... Sembra che sia stato un problema durante l'aggiornamento del profilo...";
  private static final String UPDATE_PROFILE_SUCCESS_TEXT = "Il profilo è stato aggiornato con successo!";
  /**
   * Rows for the grid container (0 stands for: unlimited number of rows)
   */
  private static final int ROWS = 0;
  /**
   * Columns for the grid container
   */
  private static final int COLS = 1;
  private static final int COLS_BUTTONS = 2;

  /**
   * inputLabels - all the fields for the information to insert (except the password fields)
   */
  private InputLabel[] inputLabels = new InputLabel[LABELS_TEXTS.length];
  /**
   * password field (for registration and profile updating)
   */
  private InputLabel registrPassword, registrConfirmPassword, oldPasswordInput, newPassword;
  /**
   * Labels to communicate with the user what he's looking at
   */
  private Label titleRegistration;
  /**
   * submitButton - confirm the operation
   * cancel - redirect the user to the Login or (if logged in), to the Home
   */
  private Button submitButton, cancelButton;
  /**
   * Grid containers to handle UI elements visualization
   */
  private GridFrame gridFrame, gridButtons;

  /**
   * Constructor of the class (creates the frame and its visual components)
   *
   * @param isUserLogged - boolean to understand if the user is logged in
   */
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

    submitButton = new Button(CONFIRM_BUTTON_TEXT);
    cancelButton = new Button(isUserLogged ? Button.BACK_TO_HOME : Button.BACK_TO_LOGIN);

    if(isUserLogged) {
      addAllEventListenersChangeUserProfile();
    } else {
      addAllEventListenersRegistration();
    }

    // add all elements to container
    gridFrame.addToView(titleRegistration);

    for (int i = 0; i < LABELS_TEXTS.length; i++) {
      inputLabels[i] = new InputLabel(LABELS_TEXTS[i], false);
      if(isUserLogged) {
        inputLabels[i].setValueInputField(getValueForInputField(i));
      }

      gridFrame.addToView(inputLabels[i]);
    }

    if(isUserLogged) {
      oldPasswordInput = new InputLabel(OLD_PASSWORD_LABEL_TEXT, true);
      newPassword = new InputLabel(NEW_PASSWORD_LABEL_TEXT, true);

      gridFrame.addToView(oldPasswordInput);
      gridFrame.addToView(newPassword);
    } else {
      registrPassword = new InputLabel(PASSWORD_LABEL_TEXT, true);
      registrConfirmPassword = new InputLabel(CONFIRM_PASSWORD_LABEL_TEXT, true);

      gridFrame.addToView(registrPassword);
      gridFrame.addToView(registrConfirmPassword);
    }

    gridButtons.addToView(submitButton);
    gridButtons.addToView(cancelButton);

    gridFrame.addToView(gridButtons);

    FrameHandler.showMainGridContainer(gridFrame);
  }

  /**
   * This method returns the right value of the right input field
   *
   * @param indexInputField - index to identify the correct input field
   */
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

  /**
   * This method defines and attaches all ActionListeners to the appropriate UI elements (user NOT logged in)
   */
  private void addAllEventListenersRegistration() {
    submitButton.attachActionListenerToButton(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // this is until I create the integration with server side
        if(checkFormFields(inputLabels, false)) {
          final String name = inputLabels[0].getValueTextField();
          final String surname = inputLabels[1].getValueTextField();
          final String username = inputLabels[2].getValueTextField();
          final String email = inputLabels[3].getValueTextField();
          String password = registrPassword.getValueTextField();

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

  /**
   * This method defines and attaches all ActionListeners to the appropriate UI elements (user logged in)
   */
  private void addAllEventListenersChangeUserProfile() {
    submitButton.attachActionListenerToButton(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // this is until I create the integration with server side
        if(checkFormFields(inputLabels, true)) {
          final String name = inputLabels[0].getValueTextField();
          final String surname = inputLabels[1].getValueTextField();
          final String username = inputLabels[2].getValueTextField();
          final String email = inputLabels[3].getValueTextField();
          final String password = newPassword.getValueTextField();
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

  /**
   * Returns true if all the input fields are valid; otherwise false
   */
  private boolean checkFormFields(InputLabel[] inputFields, boolean isUserLogged) {

    for (int i = 0; i < LABELS_TEXTS.length; i++) {
      if(!Validation.isFieldFilled(inputFields[i].getValueTextField())) {
        return false;
      }
    }

    String email = inputFields[3].getValueTextField();

    if(!Validation.validateEmail(email)) {
      return false;
    }

    if(isUserLogged) {
      String oldPasswordString = oldPasswordInput.getValueTextField();
      String newPasswordString = newPassword.getValueTextField();

      return Validation.isFieldFilled(oldPasswordString) && Validation.isFieldFilled(newPasswordString);
    } else {
      String password = registrPassword.getValueTextField();
      String confirmPassword = registrConfirmPassword.getValueTextField();

      return
        Validation.isFieldFilled(password) &&
        Validation.isFieldFilled(confirmPassword) &&
        password.length() > 3 &&
        password.equals(confirmPassword);
    }
  }

  /**
   * Update the static fields of the PlayerContextProvider class, to keep track of them across the application
   */
  private void updatePlayerContextProvider(String name, String surname, String username, String email) {
    PlayerContextProvider.setPlayerInfo(name, surname, username, email);
  }

  /**
   * This method displays on screen the ConfirmCode section
   */
  private void redirectConfirmCodeFrame() {
    ConfirmCode confirmCode = new ConfirmCode();
  }

  /**
   * This method displays on screen the LoginUtente section
   */
  private void redirectToLoginFrame() {
    LoginUtente login = new LoginUtente();
  }

  /**
   * This method displays on screen the Home section
   */
  private void redirectToHomeFrame() {
    Home home = new Home();
  }
}