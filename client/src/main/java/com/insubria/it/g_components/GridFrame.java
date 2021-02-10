package com.insubria.it.g_components;

import java.awt.*;
import javax.swing.*;

/**
 * The GridFrame class is a Container element to organize the visualization of UI elements
 */
public class GridFrame extends GraphicComponent {
  /**
   * The window that the user will see
   */
  private JFrame window;
  /**
   * The container which organize the visualization of UI elements
   */
  private Container container;

  /**
   * Constructor of the class (creates the window with title)
   *
   * @param windowTitle - Text displayed as title on the Frame
   * @param rows - rows contained in the container
   * @param cols - columns contained in the container
   */
  public GridFrame(String windowTitle, int rows, int cols) {
    this.window = new JFrame(windowTitle);
    // Setting up grid container
    this.container = this.window.getContentPane();
    this.container.setLayout(new GridLayout(rows, cols));
  }

  /**
   * Constructor of the class (used to create the container and not the window)
   *
   * @param rows - rows contained in the container
   * @param cols - columns contained in the container
   */
  public GridFrame(int rows, int cols) {
    this.window = new JFrame();
    // Setting up grid container
    this.container = this.window.getContentPane();
    this.container.setLayout(new GridLayout(rows, cols));
  }

  /**
   * Add component to the container
   *
   * @param component - a UI element which extends GraphicComponent
   */
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

  /**
   * Add component to the container
   *
   * @param comp - a UI element which doesn't extends GraphicComponent
   */
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

  /**
   * Show the window to the user
   */
  // Configure the JFrame's options and open the window with the components inside
  // the 'container' field
  public void showWindow() {
    this.window.setSize(600, 400);
    this.setWindowOptions();
  }

  /**
   * Set configurations for JFrame
   */
  private void setWindowOptions() {
    // position the frame in the center of the screen
    this.window.setLocationRelativeTo(null);
    this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.window.setVisible(true);
  }

  /**
   * Remove the window to the user
   */
  public void disposeFrame() {
    this.window.dispose();
  }

  /**
   * Returns the Component
   */
  public Component getGraphicComponent() {
    return this.container;
  }
}