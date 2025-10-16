package com.pomodoro.view.themes;

import java.awt.Color;

public class LightTheme implements AppTheme {

    @Override
    public Color getBackground() {
        return new Color(245, 245, 245);
    }

    @Override
    public Color getForeground() {
        return new Color(30, 30, 30);
    }

    @Override
    public Color getAccentWork() {
        return new Color(205, 60, 60);
    }

    @Override
    public Color getAccentShortBreak() {
        return new Color(50, 140, 50);
    }

    @Override
    public Color getAccentLongBreak() {
        return new Color(50, 110, 170);
    }

    @Override
    public Color getButtonPrimaryBg() {
        return new Color(50, 50, 50);
    }

    @Override
    public Color getButtonPrimaryFg() {
        return new Color(245, 245, 245);
    }

    @Override
    public Color getButtonSecondaryBg() {
        return new Color(210, 210, 210);
    }

    @Override
    public Color getButtonSecondaryFg() {
        return new Color(50, 50, 50);
    }

    @Override
    public Color getMenuBarBg() {
        return new Color(230, 230, 230);
    }

    @Override
    public Color getBorder() {
        return new Color(200, 200, 200);
    }
    // Add this method inside your LightTheme.java class

    @Override
    public Theme getThemeEnum() {
        return Theme.LIGHT;
    }
}
