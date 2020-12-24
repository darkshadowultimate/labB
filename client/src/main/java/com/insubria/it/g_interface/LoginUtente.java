package com.insubria.it.g_interface;

import java.awt.event.*;
import javax.swing.JOptionPane;
import java.rmi.RemoteException;

import com.insubria.it.helpers.Validation;
import com.insubria.it.serverImplClasses.PlayerCredentialsImpl;
import com.insubria.it.context.*;
import com.insubria.it.g_components.*;

public class LoginUtente {
    private static final String TITLE_WINDOW = "Form login user";
    private static final String TITLE_TOP = "Inserire Credenziali";
    private static final String LABEL_EMAIL = "E-mail";
    private static final String LABEL_PSWD = "Password";
    private static final String BUTTON_SUBMIT_TEXT = "ACCEDI";
    private static final String LINK_REGISTRATION = "Registrati";
    private static final int ROWS = 0;
    private static final int COLS = 1;

    private Label credTitle, regLink;
    private InputLabel email, password;
    private Button submitButton;
    private GridFrame gridFrame;

    public LoginUtente() {
        gridFrame = new GridFrame(TITLE_WINDOW, ROWS, COLS);

        credTitle = new Label(TITLE_TOP);
        email = new InputLabel(LABEL_EMAIL);
        password = new InputLabel(LABEL_PSWD);
        submitButton = new Button(BUTTON_SUBMIT_TEXT);
        regLink = new Label(LINK_REGISTRATION);

        this.addAllEventListeners(email, password);
        // Title at the top of the page
        gridFrame.addToView(credTitle);
        // Email and Password fields
        gridFrame.addToView(email);
        gridFrame.addToView(password);
        // Submit button
        gridFrame.addToView(submitButton);
        // Registration link
        gridFrame.addToView(regLink);

        gridFrame.showWindow();
    }

    private void addAllEventListeners(final InputLabel email, final InputLabel password) {
        submitButton.attachActionListenerToButton(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String emailValue = email.getValueTextField();
                String passwordValue = password.getValueTextField();

                if (checkFormFields(emailValue, passwordValue)) {
                    System.out.println("Fields form valid");
                    System.out.println(RemoteObjectContextProvider.server);
                    try {
                        RemoteObjectContextProvider
                        .server
                        .loginPlayerAccount(
                            emailValue,
                            passwordValue,
                            new PlayerCredentialsImpl() {
                                @Override
                                public void confirmLoginPlayerAccount(String name, String surname, String username) throws RemoteException {
                                    super.confirmLoginPlayerAccount(name, surname, username);

                                    gridFrame.disposeFrame();
                                    redirectToHomeFrame();
                                }

                                @Override
                                public void errorLoginPlayerAccount(String reason) throws RemoteException {
                                    super.errorLoginPlayerAccount(reason);

                                    showError("There was a problem during the login...\nCheck your credentials");
                                }
                            }
                        );
                    } catch (RemoteException exec) {
                        exec.printStackTrace();
                    }
                    /*
                     * try { RemoteObjectContextProvider.server.loginPlayerAccount(emailValue,
                     * passwordValue, new PlayerCredentialsImpl()); } catch (RemoteException exec) {
                     * exec.printStackTrace(); }
                     */
                } else {
                    System.out.println("Fields empty of invalid email!");
                }
            }
        });
        regLink.attachMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                redirectToRegistrationFrame();
            }
        });
    }

    private boolean checkFormFields(String email, String password) {
        return Validation.isFieldEmpty(email) && Validation.isFieldEmpty(password) && Validation.validateEmail(email);
    }

    // update the PlayerContextProvider fields value
    private void updatePlayerInfo() {
        PlayerContextProvider.setEmail(email.getValueTextField());
    }

    private void redirectToHomeFrame() {
        Home home = new Home();
        gridFrame.disposeFrame();
    }

    private void showError(String errMessage) {
        JOptionPane.showMessageDialog(null, errMessage);
    }

    private void redirectToRegistrationFrame() {
        UserRegistration registration = new UserRegistration();
        gridFrame.disposeFrame();
    }
}