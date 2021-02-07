package com.insubria.it.g_components;

import java.awt.*;
import javax.swing.*;

public class InputLabel extends GraphicComponent {
  private JPanel inputGroup;
  private Label label;
  private TextField textField = null;
  private JPasswordField passwordField = null;

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

  public void setValueInputField(String valueInputField) { this.textField.setText(valueInputField); }

  public String getValueTextField() {
    return passwordField != null ? new String(passwordField.getPassword()) : this.textField.getText();
  }

  public Component getGraphicComponent() {
    return this.inputGroup;
  }
}