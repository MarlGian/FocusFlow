package com.pomodoro.app;

import com.pomodoro.audio.AudioManager;
import com.pomodoro.controller.TimerController;
import com.pomodoro.model.TimerModel;
import com.pomodoro.settings.SettingsManager;
import com.pomodoro.view.PomodoroFrame;
import com.pomodoro.view.themes.ThemeManager;
import javax.swing.SwingUtilities;

public class PomodoroApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AudioManager audioManager = new AudioManager();
            ThemeManager themeManager = new ThemeManager();
            SettingsManager settingsManager = new SettingsManager();

            TimerModel model = new TimerModel(audioManager);

            settingsManager.loadSettings(model, audioManager, themeManager);

            PomodoroFrame view = new PomodoroFrame(model, themeManager, audioManager);

            TimerController controller = new TimerController(model, view, audioManager, themeManager, settingsManager);

            view.setController(controller);

            view.initialize();

            view.setVisible(true);
        });
    }
}
