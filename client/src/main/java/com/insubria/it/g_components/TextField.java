package com.insubria.it.g_components;

import java.awt.*;
import javax.swing.*;

/**
 * The TextField class is a UI element like a JTextField
 */
public class TextField extends JTextField {
  /**
   * Constructor of the class (creates the UI element)
   */
  public TextField() {
    super();
    setPreferredSize(new Dimension(150, 30));
  }

  /**
   * Set the dimensions of the TextField
   *
   * @param x - dimension x
   * @param y - dimension y
   */
  public void setDimensions(int x, int y) {
    setPreferredSize(new Dimension(x, y));
  }
}