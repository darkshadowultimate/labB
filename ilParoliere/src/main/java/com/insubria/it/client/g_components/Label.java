package com.insubria.it.client.g_components;

import java.awt.*;
import java.awt.Component;
import javax.swing.*;

public class Label extends GraphicComponent {
  private JLabel label;

  public Label(String text) {
    this.label = new JLabel(text.length() > 0 ? text : "undefined", SwingConstants.CENTER);
  }

  public JLabel getLabel() {
    return this.label;
  }

  public Component getGraphicComponent() {
    return this.label;
  }
}