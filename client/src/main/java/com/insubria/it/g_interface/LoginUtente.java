package com.insubria.it.g_interface;

import java.awt.event.*;
import javax.swing.JOptionPane;
import java.rmi.RemoteException;

import com.insubria.it.helpers.FrameHandler;
import com.insubria.it.helpers.Validation;
import com.insubria.it.serverImplClasses.PlayerCredentialsImpl;
import com.insubria.it.context.*;
import com.insubria.it.g_components.*;

/**
 * The LoginUtente class creates the login frame to allow the user to:
 * - Login into the Home section with his credentials
 * - Create a new account
 * - Reset the password
 */
public class LoginUtente {
    /**
     * Static text that will be used with some UI components to communicate with the user
     */
    private static final String TITLE_WINDOW = "Form login user";
    private static final String TITLE_TOP = "Inserire Credenziali";
    private static final String LABEL_EMAIL = "E-mail";
    private static final String LABEL_PSWD = "Password";
    private static final String BUTTON_SUBMIT_TEXT = "ACCEDI";
    private static final String LINK_FORGOT_PASSWORD = "Reset Password";
    private static final String LINK_REGISTRATION = "Registrati";
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
    private Label credTitle, regLink, pswdLink;
    /**
     * Inputs to login
     */
    private InputLabel email, password;
    /**
     * Button to send data to the server
     */
    private Button submitButton;
    /**
     * Grid containers to handle UI elements visualization
     */
    private GridFrame gridFrame;

    /**
     * Constructor of the class (creates the frame and its visual components)
     */
    public LoginUtente() {
        gridFrame = new GridFrame(TITLE_WINDOW, ROWS, COLS);

        credTitle = new Label(TITLE_TOP);
        email = new InputLabel(LABEL_EMAIL, false);
        password = new InputLabel(LABEL_PSWD, true);
        submitButton = new Button(BUTTON_SUBMIT_TEXT);
        pswdLink = new Label(LINK_FORGOT_PASSWORD);
        regLink = new Label(LINK_REGISTRATION);

        this.addAllEventListeners(email, password);
        // Title at the top of the page
        gridFrame.addToView(credTitle);
        // Email and Password fields
        gridFrame.addToView(email);
        gridFrame.addToView(password);
        // Submit button
        gridFrame.addToView(submitButton);
        // Forgot password link
        gridFrame.addToView(pswdLink);
        // Registration link
        gridFrame.addToView(regLink);

        FrameHandler.showMainGridContainer(gridFrame);
    }

    /**
     * This method defines and attaches all ActionListeners to the appropriate UI elements
     *
     * @param email - The reference to InputLabel object where the email is contained
     * @param password - The reference to InputLabel object where the password is contained
     */
    private void addAllEventListeners(final InputLabel email, final InputLabel password) {
        submitButton.attachActionListenerToButton(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final String emailValue = email.getValueTextField();
                String passwordValue = password.getValueTextField();

                if (checkFormFields(emailValue, passwordValue)) {
                    System.out.println("Fields form valid");
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

                                    PlayerContextProvider.setPlayerInfo(name, surname, username, emailValue);
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
                } else {
                    System.out.println("Fields empty of invalid email!");
                }
            }
        });
        pswdLink.attachMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                redirectToResetPasswordFrame();
            }
        });
        regLink.attachMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                redirectToRegistrationFrame();
            }
        });
    }

    /**
     * This method returns true both email and password are not empty strings and if the email is valid; false otherwise.
     *
     * @param email - String object which contains the email inserted
     * @param password - String object which contains the password inserted
     */
    private boolean checkFormFields(String email, String password) {
        return Validation.isFieldFilled(email) && Validation.isFieldFilled(password) && Validation.validateEmail(email);
    }

    /**
     * This method displays on screen the Home section
     */
    private void redirectToHomeFrame() {
        Home home = new Home();
    }

    /**
     * This method displays on screen the ResetPassword section
     */
    private void redirectToResetPasswordFrame() {
        ResetPassword resetPassword = new ResetPassword();
    }

    /**
     * This method displays on screen the Registration section
     */
    private void redirectToRegistrationFrame() {
        UserRegistration registration = new UserRegistration(false);
    }

    /**
     * This method displays the errMessage argument using a JOptionPane
     *
     * @param errMessage String object which represents the error message to display
     */
    private void showError(String errMessage) {
        JOptionPane.showMessageDialog(null, errMessage);
    }
}