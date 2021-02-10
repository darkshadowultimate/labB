package com.insubria.it.g_components;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * The Button class is a UI element like a JButton
 */
public class Button extends GraphicComponent {
  /**
   * Static text used for buttons only
   */
  public static final String BACK_TO_HOME = "Torna alla Home";
  public static final String BACK_TO_LOGIN = "Torna al login";
  public static final String EXIT_GAME = "Esci dalla partita";
  /**
   * Used to center the button
   */
  private JPanel buttonPanel;
  /**
   * JButton UI element
   */
  private JButton button;

  /**
   * Constructor of the class (creates the UI element)
   *
   * @param text - Text displayed by the Button
   */
  public Button(String text) {
    // Check if the text is not empty
    if (text.length() > 0) {
      this.button = new JButton(text);
    } else {
      this.button = new JButton("undefined");
    }
    // Set background color
    this.button.setBackground(Color.WHITE);
    this.button.setOpaque(true);
    // Set padding and margin
    this.button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 5),
        BorderFactory.createMatteBorder(5, 10, 5, 10, Color.WHITE)));
    // Create JPanel and add JButton to JPanel
    this.buttonPanel = new JPanel();
    this.buttonPanel.add(this.button);
  }

  /**
   * Attach a ActionListener to the JButton
   *
   * @param actionListener - ActionListener specified in the class which is invoking this method
   */
  public void attachActionListenerToButton(ActionListener actionListener) {
    this.button.addActionListener(actionListener);
  }

  /**
   * Returns the JButton contained in this class
   */
  public JButton getButton() {
    return this.button;
  }

  /**
   * Returns the Component (Button)
   */
  public Component getGraphicComponent() {
    return this.buttonPanel;
  }
}