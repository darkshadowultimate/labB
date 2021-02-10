package com.insubria.it.g_interface;

import com.insubria.it.context.RemoteObjectContextProvider;
import com.insubria.it.g_components.*;
import com.insubria.it.helpers.FrameHandler;
import com.insubria.it.helpers.Validation;
import com.insubria.it.serverImplClasses.PlayerCredentialsImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

/**
 * The ResetPassword class creates the ResetPassword frame to allow the user to reset his password.
 */
public class ResetPassword {
  /**
   * Static text that will be used with some UI components to communicate with the user
   */
  private static final String TITLE_WINDOW = "Il Paroliere - Reset password";
  private static final String MAIN_TITLE = "<html>Hai dimenticato la password?<br/> La nuova password ti verrà inviata per e-mail";
  private static final String INSERT_EMAIL_TEXT = "Inserisci e-mail: ";
  private static final String RESET_BUTTON_TEXT = "Resetta password";
  private static final String ERROR_EMAIL_FIELD_TEXT = "L'email inserita non è valida";
  private static final String SUCCESS_RESET_EMAIL_TEXT = "La password è stata resettata con successo!\nControlla la posta per vedere la tua nuova password.";
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
   * The email to insert in order to reset the account's password with the matching email
   */
  private InputLabel insertEmailInput;
  /**
   * resetButton - This button confirms the operation to reset the password
   * backToLogin - This button abort the reset password operation and sends the user back to login page
   */
  private Button resetButton, backToLogin;
  /**
   * Grid containers to handle UI elements visualization
   */
  private GridFrame gridContainer;

  /**
   * Constructor of the class (creates the frame and its visual components)
   */
  public ResetPassword() {
    gridContainer = new GridFrame(TITLE_WINDOW, ROWS, COLS);

    mainTitle = new Label(MAIN_TITLE);
    insertEmailInput = new InputLabel(INSERT_EMAIL_TEXT, false);
    resetButton = new Button(RESET_BUTTON_TEXT);
    backToLogin = new Button(Button.BACK_TO_LOGIN);

    addAllEventListeners();

    gridContainer.addToView(mainTitle);
    gridContainer.addToView(insertEmailInput);
    gridContainer.addToView(resetButton);
    gridContainer.addToView(backToLogin);

    FrameHandler.showMainGridContainer(gridContainer);
  }

  /**
   * This method defines and attaches all ActionListeners to the appropriate UI elements
   */
  private void addAllEventListeners() {
    // Reset the password by calling the remote "resetPlayerPassword" method
    resetButton.attachActionListenerToButton(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String email = insertEmailInput.getValueTextField();

        if(checkEmail(email)) {
          try {
            RemoteObjectContextProvider
            .server
            .resetPlayerPassword(
              email,
              new PlayerCredentialsImpl() {
                @Override
                public void confirmResetPlayerPassword() throws RemoteException {
                  super.confirmResetPlayerPassword();

                  JOptionPane.showMessageDialog(null, SUCCESS_RESET_EMAIL_TEXT);
                  redirectToLoginFrame();
                }

                @Override
                public void errorResetPlayerPassword(String reason) throws RemoteException {
                  super.errorResetPlayerPassword(reason);
                }
              }
            );
          } catch(RemoteException exc) {}
        } else {
          JOptionPane.showMessageDialog(null, ERROR_EMAIL_FIELD_TEXT);
        }
      }
    });
    // Sends the user back to login page
    backToLogin.attachActionListenerToButton(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        redirectToLoginFrame();
      }
    });
  }

  /**
   * This method returns true if the email is not empty and is valid; false otherwise
   *
   * @param email String object representing the email inserted by the user
   */
  private boolean checkEmail(String email) {
    return
      Validation.isFieldFilled(email) &&
      Validation.validateEmail(email);
  }

  /**
   * This method displays on screen the Login section
   */
  private void redirectToLoginFrame() {
    LoginUtente loginUtente = new LoginUtente();
  }
}