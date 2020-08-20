package com.github.perschola.utils;

public enum  AnsiColor {
    WHITE("\u001B[37m");

    private final String color;

    AnsiColor(String ansiColor)
    {
        this.color=ansiColor;
    }
    public String getColor()
    {
        return  color;
    }
}
