package com.pomodoro.controller;

import com.pomodoro.model.TimerModel;
import com.pomodoro.view.PomodoroFrame;
import com.pomodoro.audio.AudioManager;
import com.pomodoro.settings.SettingsManager;
import com.pomodoro.view.themes.ThemeManager;

public class TimerController {

    private final TimerModel model;
    private final PomodoroFrame view;
    private final AudioManager audioManager;
    private final ThemeManager themeManager;
    private final SettingsManager settingsManager;

    public TimerController(TimerModel model, PomodoroFrame view, AudioManager audioManager, ThemeManager themeManager, SettingsManager settingsManager) {
        this.model = model;
        this.view = view;
        this.audioManager = audioManager;
        this.themeManager = themeManager;
        this.settingsManager = settingsManager;
    }

    public void handleStartPause() {
        if (model.timerIsRunning()) {
            model.pauseTimer();
            audioManager.stopBackgroundMusic();
            view.setTaskFieldLock(false);
        } else {
            model.startTimer();
            audioManager.playBackgroundMusic();
            view.setTaskFieldLock(true);
        }
    }

    public void handleReset() {
        model.resetCycle();
        audioManager.stopBackgroundMusic();
        view.setTaskFieldLock(false);
    }

    public void handleQuit() {
        settingsManager.saveSettings(model, audioManager, themeManager);
        audioManager.stopBackgroundMusic();
        System.exit(0);
    }

    public void handleResetCycles() {
        model.resetCompletedWorkSessions();
    }

    public void handleToggleMusic(boolean isEnabled) {
        audioManager.setMusicEnabled(isEnabled);
    }
}
