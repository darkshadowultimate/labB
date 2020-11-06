package com.insubria.it.client.g_interface;

import com.insubria.it.client.g_components.*;

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
}