package com.insubria.it.helpers;

import com.insubria.it.g_components.GridFrame;

public class FrameHandler {
    private static GridFrame mainGridContainer = null;
    private static GridFrame secondaryGridContainer = null;

    public static void showMainGridContainer(GridFrame gridFrame) {
        handleShowMainGridContainerProcess(gridFrame);
        mainGridContainer.showWindow();
    }

    public static void showMainGridContainerWithSizes(GridFrame gridFrame, int height, int width) {
        handleShowMainGridContainerProcess(gridFrame);
        mainGridContainer.showWindow(height, width);
    }

    public static void showSecondaryGridContainer(GridFrame gridFrame) {
        if(secondaryGridContainer != null) {
            secondaryGridContainer.disposeFrame();
        }
        secondaryGridContainer = gridFrame;
        secondaryGridContainer.showWindow();
    }

    public static void disposeSecondaryGridContainer() {
        if(secondaryGridContainer != null) {
            secondaryGridContainer.disposeFrame();
            secondaryGridContainer = null;
        }
    }

    private static void handleShowMainGridContainerProcess(GridFrame gridFrame) {
        if(mainGridContainer != null) {
            mainGridContainer.disposeFrame();
        }
        disposeSecondaryGridContainer();
        mainGridContainer = gridFrame;
    }
}
