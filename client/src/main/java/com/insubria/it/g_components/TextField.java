package com.insubria.it.g_components;

import java.awt.*;
import javax.swing.*;

public class TextField extends JTextField {
  public TextField() {
    super();
    setPreferredSize(new Dimension(150, 30));
  }

  public void setDimensions(int x, int y) {
    setPreferredSize(new Dimension(x, y));
  }
}