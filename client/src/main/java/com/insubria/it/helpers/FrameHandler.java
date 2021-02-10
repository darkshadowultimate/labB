package com.insubria.it.helpers;

import com.insubria.it.g_components.GridFrame;

import javax.swing.*;

/**
 * The FrameHandler class handles the operations of the active frames
 * (opening frames and closing them). There can be at max:
 * - Two frames opened at the same time (the main and secondary for importance)
 * - JOptionPane (the user is visualizing some text on it)
 *
 * The goal of this class is to avoid unexpected UI frames behaviors by centralizing
 * the handling of every frame instead of letting the single classes to handle their own frame
 */
public class FrameHandler {
    /**
     * The frame which have top priority over any other frames
     * (if the program is running there's 1 mainGridContainer; otherwise the program is closed)
     */
    private static GridFrame mainGridContainer = null;
    /**
     * The frame which have low priority over the mainGridContainer
     * (if the program is running, there can be 0 or 1 secondaryGridContainer)
     */
    private static GridFrame secondaryGridContainer = null;


    /**
     * This method closes the secondaryGridFrame and JOptionPane active
     * and substitute the current mainGridContainer with the one passed as parameter.
     * Then it display the frame.
     *
     * @param gridFrame - The reference to GridFrame object which represent the frame just created
     */
    public static void showMainGridContainer(GridFrame gridFrame) {
        handleShowMainGridContainerProcess(gridFrame);
        mainGridContainer.showWindow();
    }

    /**
     * This method closes the secondaryGridFrame and JOptionPane active
     * and substitute the current mainGridContainer with the one passed as parameter.
     * Then it display the frame.
     * The sizes of the mainGridContainer are specified in the parameters.
     *
     * @param gridFrame - The reference to GridFrame object which represent the frame just created
     * @param height - Height of the frame
     * @param width - Width of the frame
     */
    public static void showMainGridContainerWithSizes(GridFrame gridFrame, int height, int width) {
        handleShowMainGridContainerProcess(gridFrame);
        mainGridContainer.showWindow(height, width);
    }

    /**
     * This method closes the JOptionPane active
     * and substitute the current secondaryGridContainer with the one passed as parameter.
     *
     * @param gridFrame - The reference to GridFrame object which represent the frame just created
     */
    public static void showSecondaryGridContainer(GridFrame gridFrame) {
        if(secondaryGridContainer != null) {
            secondaryGridContainer.disposeFrame();
        }
        JOptionPane.getRootFrame().dispose();
        secondaryGridContainer = gridFrame;
        secondaryGridContainer.showWindow();
    }

    /**
     * This method closes the secondaryGridFrame and JOptionPane active
     */
    public static void disposeSecondaryGridContainer() {
        if(secondaryGridContainer != null) {
            secondaryGridContainer.disposeFrame();
            secondaryGridContainer = null;
        }
        JOptionPane.getRootFrame().dispose();
    }

    /**
     * This method closes the secondaryGridFrame and JOptionPane active
     * and substitute the current mainGridContainer with the one passed as parameter.
     *
     * @param gridFrame - The reference to GridFrame object which represent the frame just created
     */
    private static void handleShowMainGridContainerProcess(GridFrame gridFrame) {
        if(mainGridContainer != null) {
            mainGridContainer.disposeFrame();
        }
        disposeSecondaryGridContainer();
        JOptionPane.getRootFrame().dispose();
        mainGridContainer = gridFrame;
    }
}
