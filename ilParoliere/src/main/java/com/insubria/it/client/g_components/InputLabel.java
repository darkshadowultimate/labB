package com.insubria.it.client.g_components;

import java.awt.*;
import javax.swing.*;

public class InputLabel extends GraphicComponent {
  private JPanel inputGroup;
  private Label label;
  private TextField textField;

  public InputLabel(String textLabel) {
    inputGroup = new JPanel();
    label = new Label(textLabel);
    textField = new TextField();

    inputGroup.add(label.getLabel());
    inputGroup.add(textField.getTextField());
  }

  public Component getGraphicComponent() {
    return this.inputGroup;
  }
}