package com.insubria.it.g_components;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.Component;
import javax.swing.*;

public class Label extends GraphicComponent {
  private JLabel label;

  public Label(String text) {
    String labelText = text == null || text.length() < 1 ? "" : text;
    this.label = new JLabel(labelText, SwingConstants.CENTER);
  }

  public void attachMouseListener(MouseAdapter event) {
    this.label.addMouseListener(event);
  }

  public JLabel getLabel() {
    return this.label;
  }

  public String getLabelText() {
    return this.label.getText();
  }

  public Component getGraphicComponent() {
    return this.label;
  }

  public void setLabelValue(String value) { label.setText(value); }
}