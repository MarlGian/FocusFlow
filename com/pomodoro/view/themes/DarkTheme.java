package com.pomodoro.view.themes;

import java.awt.Color;

public class DarkTheme implements AppTheme {

    @Override
    public Color getBackground() {
        return new Color(40, 44, 52);
    }

    @Override
    public Color getForeground() {
        return new Color(220, 220, 220);
    }

    @Override
    public Color getAccentWork() {
        return new Color(211, 78, 78);
    }

    @Override
    public Color getAccentShortBreak() {
        return new Color(70, 150, 70);
    }

    @Override
    public Color getAccentLongBreak() {
        return new Color(60, 120, 180);
    }

    @Override
    public Color getButtonPrimaryBg() {
        return new Color(230, 230, 230);
    }

    @Override
    public Color getButtonPrimaryFg() {
        return new Color(40, 44, 52);
    }

    @Override
    public Color getButtonSecondaryBg() {
        return new Color(60, 66, 76);
    }

    @Override
    public Color getButtonSecondaryFg() {
        return new Color(220, 220, 220);
    }

    @Override
    public Color getMenuBarBg() {
        return new Color(50, 54, 62);
    }

    @Override
    public Color getBorder() {
        return new Color(70, 74, 82);
    }
    // Add this method inside your DarkTheme.java class

    @Override
    public Theme getThemeEnum() {
        return Theme.DARK;
    }
}
