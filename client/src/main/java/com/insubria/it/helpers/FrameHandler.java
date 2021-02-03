package com.insubria.it.helpers;

import com.insubria.it.g_components.GridFrame;

public class FrameHandler {
    private static GridFrame mainGridContainer = null;
    private static GridFrame secondaryGridContainer = null;

    public static void showMainGridContainer(GridFrame gridFrame) {
        if(mainGridContainer != null) {
            mainGridContainer.disposeFrame();
        }
        mainGridContainer = gridFrame;
        mainGridContainer.showWindow();
    }

    public static void showMainGridContainerWithSizes(GridFrame gridFrame, int height, int width) {
        if(mainGridContainer != null) {
            mainGridContainer.disposeFrame();
        }
        if(secondaryGridContainer != null) {
            secondaryGridContainer.disposeFrame();
        }
        mainGridContainer = gridFrame;
        secondaryGridContainer = null;
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
        secondaryGridContainer.disposeFrame();
        secondaryGridContainer = null;
    }
}
