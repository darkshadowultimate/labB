package com.insubria.it.helpers;

import com.insubria.it.g_components.GridFrame;

public class FrameHandler {
    private static GridFrame mainGridContainer;
    private static GridFrame secondaryGridContainer;

    public static void setMainGridContainer(GridFrame mainGridContainer) {
        FrameHandler.mainGridContainer = mainGridContainer;
    }

    public static void setSecondaryGridContainer(GridFrame secondaryGridContainer) {
        FrameHandler.secondaryGridContainer = secondaryGridContainer;
    }

    public static void redirectToNewFrame(GridFrame newGridFrame) {}
}
