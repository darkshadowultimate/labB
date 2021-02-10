package com.insubria.it.g_components;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.Component;
import javax.swing.*;

/**
 * The Label class is a UI element like a JLabel
 */
public class Label extends GraphicComponent {
  private JLabel label;

  /**
   * Constructor of the class (creates the UI element)
   *
   * @param text - Text displayed by the Label
   */
  public Label(String text) {
    String labelText = text == null || text.length() < 1 ? "" : text;
    this.label = new JLabel(labelText, SwingConstants.CENTER);
  }

  /**
   * Attach a MouseListener to the JLabel
   *
   * @param event - MouseAdapter specified in the class which is invoking this method
   */
  public void attachMouseListener(MouseAdapter event) {
    this.label.addMouseListener(event);
  }

  /**
   * Returns the JLabel contained in this class
   */
  public JLabel getLabel() {
    return this.label;
  }

  /**
   * Returns the text contained in the JLabel of this class
   */
  public String getLabelText() {
    return this.label.getText();
  }

  /**
   * Returns the Component (Label)
   */
  public Component getGraphicComponent() {
    return this.label;
  }

  /**
   * Set the value of the Label
   */
  public void setLabelValue(String value) { label.setText(value); }
}