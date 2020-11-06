package com.insubria.it.client.g_components;

import java.awt.*;
import javax.swing.*;

public class Button extends GraphicComponent {
  private JPanel buttonPanel;
  private JButton button;

  public Button(String text) {
    // Check if the text is not empty
    if(text.length() > 0) {
      this.button = new JButton(text);
    } else {
      this.button = new JButton("undefined");
    }
    // Set background color
    this.button.setBackground(Color.WHITE);
    this.button.setOpaque(true);
    // Set padding and margin
    this.button.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.WHITE, 5),
        BorderFactory.createMatteBorder​(5, 10, 5, 10, Color.WHITE)));
    // Create JPanel and add JButton to JPanel
    this.buttonPanel = new JPanel();
    this.buttonPanel.add(this.button);
  }

  public JButton getButton() {
    return this.button;
  }
  public Component getGraphicComponent() {
    return this.buttonPanel;
  }
}