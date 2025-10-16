package com.pomodoro.view;

import com.pomodoro.audio.AudioManager;
import com.pomodoro.controller.TimerController;
import com.pomodoro.model.TimerModel;
import com.pomodoro.view.themes.AppTheme;
import com.pomodoro.view.themes.Theme;
import com.pomodoro.view.themes.ThemeManager;
import javax.swing.*;

public class AppMenuBar extends JMenuBar {

    public AppMenuBar(JFrame parentFrame, TimerModel model, TimerController controller, ThemeManager themeManager, AudioManager audioManager) {
        JMenu settingsMenu = createSettingsMenu(parentFrame, model, controller, themeManager, audioManager);
        JMenu themeMenu = createThemeMenu(themeManager);

        add(settingsMenu);
        add(themeMenu);
    }

    private JMenu createSettingsMenu(JFrame parentFrame, TimerModel model, TimerController controller, ThemeManager themeManager, AudioManager audioManager) {
        JMenu settingsMenu = new JMenu("Settings");

        JMenuItem durationsItem = new JMenuItem("Timer & Sound...");
        durationsItem.addActionListener(e -> new SettingsDialog(parentFrame, model, themeManager, audioManager).setVisible(true));

        JCheckBoxMenuItem alwaysOnTopItem = new JCheckBoxMenuItem("Always on Top");
        alwaysOnTopItem.addActionListener(e -> parentFrame.setAlwaysOnTop(alwaysOnTopItem.isSelected()));

        JCheckBoxMenuItem enableMusicItem = new JCheckBoxMenuItem("Enable Lofi Music", true);
        enableMusicItem.addActionListener(e -> controller.handleToggleMusic(enableMusicItem.isSelected()));

        JMenuItem resetCyclesItem = new JMenuItem("Reset Cycles");
        resetCyclesItem.addActionListener(e -> controller.handleResetCycles());

        settingsMenu.add(durationsItem);
        settingsMenu.add(alwaysOnTopItem);
        settingsMenu.add(enableMusicItem);
        settingsMenu.addSeparator();
        settingsMenu.add(resetCyclesItem);
        return settingsMenu;
    }

    private JMenu createThemeMenu(ThemeManager themeManager) {
        JMenu themeMenu = new JMenu("Theme");
        ButtonGroup themeGroup = new ButtonGroup();
        JRadioButtonMenuItem lightThemeItem = new JRadioButtonMenuItem("Light Theme");
        JRadioButtonMenuItem darkThemeItem = new JRadioButtonMenuItem("Dark Theme");

        lightThemeItem.addActionListener(e -> themeManager.setCurrentTheme(Theme.LIGHT));
        darkThemeItem.addActionListener(e -> themeManager.setCurrentTheme(Theme.DARK));

        themeGroup.add(lightThemeItem);
        themeGroup.add(darkThemeItem);
        themeMenu.add(lightThemeItem);
        themeMenu.add(darkThemeItem);
        darkThemeItem.setSelected(true);
        return themeMenu;
    }

    public void applyTheme(AppTheme theme) {
        setBackground(theme.getMenuBarBg());
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, theme.getBorder()));
        for (int i = 0; i < getMenuCount(); i++) {
            getMenu(i).setForeground(theme.getForeground());
        }
    }
}