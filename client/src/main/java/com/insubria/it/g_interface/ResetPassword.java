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

public class ResetPassword {
  // Constants
  private static final String TITLE_WINDOW = "Il Paroliere - Reset password";
  private static final String MAIN_TITLE = "<html>Hai dimenticato la password?<br/> La nuova password ti verrà inviata per e-mail";
  private static final String INSERT_EMAIL_TEXT = "Inserisci e-mail: ";
  private static final String RESET_BUTTON_TEXT = "Resetta password";
  private static final String ERROR_EMAIL_FIELD_TEXT = "L'email inserita non è valida";
  private static final String SUCCESS_RESET_EMAIL_TEXT = "La password è stata resettata con successo!\nControlla la posta per vedere la tua nuova password.";
  private static final int ROWS = 0;
  private static final int COLS = 1;
  // Variables
  private Label mainTitle;
  private InputLabel insertEmailInput;
  private Button resetButton, backToLogin;
  private GridFrame gridContainer;

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


  private void addAllEventListeners() {
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
    backToLogin.attachActionListenerToButton(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        redirectToLoginFrame();
      }
    });
  }

  private boolean checkEmail(String email) {
    return
      Validation.isFieldFilled(email) &&
      Validation.validateEmail(email);
  }

  private void redirectToLoginFrame() {
    LoginUtente loginUtente = new LoginUtente();
  }
}