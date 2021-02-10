package com.insubria.it.g_components;

import java.awt.*;

/**
 * GraphicComponent is an abstract class which represents a graphic component like: button, textfield, label...
 */
public abstract class GraphicComponent extends Component {
  /**
   * method to retrieve the JComponent that the class represents
   */
  public abstract Component getGraphicComponent();
}