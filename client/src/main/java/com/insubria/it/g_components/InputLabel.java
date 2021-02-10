package com.insubria.it.g_components;

import java.awt.*;
import javax.swing.*;

/**
 * The InputLabel class is a UI element which contains Label and TextField/JPasswordField inside a JPanel
 */
public class InputLabel extends GraphicComponent {
  /**
   * This is like a container, but smaller and lighter.
   */
  private JPanel inputGroup;
  /**
   * Labels to communicate with the user what he's looking at
   */
  private Label label;
  /**
   * Input text where the user can insert data
   */
  private TextField textField = null;
  /**
   * Input text where the user can insert data, but for password
   */
  private JPasswordField passwordField = null;

  /**
   * Constructor of the class (creates the UI element)
   *
   * @param textLabel - Text displayed by the Button
   * @param isPassword - Boolean to indicate if this element must be use to insert a password or not
   */
  public InputLabel(String textLabel, boolean isPassword) {
    inputGroup = new JPanel();
    label = new Label(textLabel);

    if(isPassword) {
      passwordField = new JPasswordField();
      passwordField.setPreferredSize(new Dimension(150, 30));
    } else {
      textField = new TextField();
    }

    inputGroup.add(label.getLabel());
    inputGroup.add(isPassword ? passwordField : textField);
  }

  /**
   * Set the value of the TextField attribute
   *
   * @param valueInputField - Text displayed inside the TextField
   */
  public void setValueInputField(String valueInputField) { this.textField.setText(valueInputField); }

  /**
   * get the value of the TextField attribute
   */
  public String getValueTextField() {
    return passwordField != null ? new String(passwordField.getPassword()) : this.textField.getText();
  }

  /**
   * Returns the Component (Button)
   */
  public Component getGraphicComponent() {
    return this.inputGroup;
  }
}