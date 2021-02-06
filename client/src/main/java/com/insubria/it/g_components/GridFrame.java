package com.insubria.it.g_components;

import java.awt.*;
import javax.swing.*;

public class GridFrame extends GraphicComponent {
  private JFrame window;
  private Container container;

  public GridFrame(String windowTitle, int rows, int cols) {
    this.window = new JFrame(windowTitle);
    // Setting up grid container
    this.container = this.window.getContentPane();
    this.container.setLayout(new GridLayout(rows, cols));
  }

  public GridFrame(int rows, int cols) {
    this.window = new JFrame();
    // Setting up grid container
    this.container = this.window.getContentPane();
    this.container.setLayout(new GridLayout(rows, cols));
  }

  // Add a component to the 'container' field
  public void addToView(GraphicComponent component) {
    // check the instance of 'component' and call 'getGraphicComponent' method to
    // retrive a JComponent
    // if none of the instances match, a log message is displayed (debug)
    if (component instanceof Button) {
      this.container.add(((Button) component).getGraphicComponent());
    } else if (component instanceof Label) {
      this.container.add(((Label) component).getGraphicComponent());
    } else if (component instanceof InputLabel) {
      this.container.add(((InputLabel) component).getGraphicComponent());
    } else if (component instanceof GridFrame) {
      this.container.add(((GridFrame) component).getGraphicComponent());
    } else {
      System.out.println("Element of class " + component.getClass().getName() + " not added to the container");
    }
  }

  public void addToView(Component comp) {
    this.container.add(comp);
  }

  public void removeFromView(GraphicComponent componentToRemove) {
    container.remove(componentToRemove);
  }

  public void showWindow(int height, int width) {
    this.window.setSize(width, height);
    this.setWindowOptions();
  }

  // Configure the JFrame's options and open the window with the components inside
  // the 'container' field
  public void showWindow() {
    this.window.setSize(600, 400);
    this.setWindowOptions();
  }

  private void setWindowOptions() {
    // position the frame in the center of the screen
    this.window.setLocationRelativeTo(null);
    this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.window.setVisible(true);
  }

  public void disposeFrame() {
    this.window.dispose();
  }

  public Component getGraphicComponent() {
    return this.container;
  }
}