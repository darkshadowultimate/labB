package com.insubria.it.client.g_components;

import java.awt.*;
import javax.swing.*;

public class TextField {
  private JTextField textField;

  public TextField() {
    super();
    textField = new JTextField();
    textField.setPreferredSize(new Dimension(150, 30));
  }

  public JTextField getTextField() {
    return this.textField;
  }

  public void setDimensions(int x, int y) {
    this.textField.setPreferredSize(new Dimension(x, y));
  }
}