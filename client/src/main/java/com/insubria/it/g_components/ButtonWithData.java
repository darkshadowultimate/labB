package com.insubria.it.g_components;

/**
 * Extension of the Button class. This class allows to store data within it (used for ListGames)
 */
public class ButtonWithData extends Button {
    /**
     * data needed to be stored
     */
    private Object data;

    /**
     * Constructor of the class (creates the UI element)
     *
     * @param text - Text displayed by the Button
     * @param data - Data needed to be stored
     */
    public ButtonWithData(String text, Object data) {
        super(text);
        this.data = data;
    }

    /**
     * get data stored in this class
     */
    public Object getData() {
        return data;
    }
}
