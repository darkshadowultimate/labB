package com.insubria.it.g_components;

public class ButtonWithData extends Button {
    private Object data;

    public ButtonWithData(String text, Object data) {
        super(text);
        this.data = data;
    }

    public Object getData() {
        return data;
    }
}
