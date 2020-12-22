package com.insubria.it.helpers;

import java.lang.reflect.Constructor;

import com.insubria.it.g_components.GridFrame;
import com.insubria.it.g_interface.*;

public class FrameTransitions {
  public static Object currentFrame = new LoginUtente();

  /*
   * private static getInstanceFrameFromName(String nameInstance) {
   * switch(nameInstance) { case "" } }
   */

  /*
   * public static void moveToNextFrame(String nameClassFrame, GridFrame
   * gridFrame) { Class className = Class.forName(nameClassFrame); Constructor<?>
   * constr = className.getConstructor(String.class); currentFrame =
   * constr.newInstance(); gridFrame.disposeFrame(); }
   */
}
