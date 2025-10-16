package com.pomodoro.view.themes;

import java.awt.Color;

public interface AppTheme {

    Theme getThemeEnum();

    Color getBackground();

    Color getForeground();

    Color getAccentWork();

    Color getAccentShortBreak();

    Color getAccentLongBreak();

    Color getButtonPrimaryBg();

    Color getButtonPrimaryFg();

    Color getButtonSecondaryBg();

    Color getButtonSecondaryFg();

    Color getMenuBarBg();

    Color getBorder();
}
