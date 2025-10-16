package com.pomodoro.settings;

import com.pomodoro.audio.AudioManager;
import com.pomodoro.model.TimerModel;
import com.pomodoro.view.themes.Theme;
import com.pomodoro.view.themes.ThemeManager;
import java.io.*;
import java.util.Properties;

public class SettingsManager {

    private final String configFilePath;
    private final Properties properties;

    public SettingsManager() {
        String homeDir = System.getProperty("user.home");
        this.configFilePath = homeDir + File.separator + ".focusflow_settings.properties";
        this.properties = new Properties();
    }

    public void saveSettings(TimerModel model, AudioManager audio, ThemeManager theme) {
        System.out.println("Saving settings to: " + configFilePath);
        try (OutputStream output = new FileOutputStream(configFilePath)) {
            properties.setProperty("workDuration", String.valueOf(model.getWorkDuration()));
            properties.setProperty("shortBreakDuration", String.valueOf(model.getShortBreakDuration()));
            properties.setProperty("longBreakDuration", String.valueOf(model.getLongBreakDuration()));
            properties.setProperty("sessionsBeforeLongBreak", String.valueOf(model.getSessionsBeforeLongBreak()));

            properties.setProperty("musicEnabled", String.valueOf(audio.isMusicEnabled()));
            properties.setProperty("currentLofiTrack", audio.getCurrentLofiTrack());
            if (audio.getCustomAlarmPath() != null) {
                properties.setProperty("customAlarmPath", audio.getCustomAlarmPath());
            }

            properties.setProperty("currentTheme", theme.getCurrentThemeEnum().name());

            properties.store(output, "Focus Flow Settings");
        } catch (IOException e) {
            System.err.println("Error saving settings.");
            e.printStackTrace();
        }
    }

    public void loadSettings(TimerModel model, AudioManager audio, ThemeManager theme) {
        File configFile = new File(configFilePath);
        if (!configFile.exists()) {
            System.out.println("No settings file found. Using defaults.");
            return;
        }

        System.out.println("Loading settings from: " + configFilePath);
        try (InputStream input = new FileInputStream(configFile)) {
            properties.load(input);

            model.setWorkDuration(Integer.parseInt(properties.getProperty("workDuration", "1500")));
            model.setShortBreakDuration(Integer.parseInt(properties.getProperty("shortBreakDuration", "300")));
            model.setLongBreakDuration(Integer.parseInt(properties.getProperty("longBreakDuration", "900")));
            model.setSessionsBeforeLongBreak(Integer.parseInt(properties.getProperty("sessionsBeforeLongBreak", "4")));
            model.resetTimerToStateDuration();

            audio.setMusicEnabled(Boolean.parseBoolean(properties.getProperty("musicEnabled", "true")));
            audio.setBackgroundMusicTrack(properties.getProperty("currentLofiTrack", "lofi1.mp3"));
            audio.setCustomAlarmSound(properties.getProperty("customAlarmPath"));

            String themeName = properties.getProperty("currentTheme", "DARK");
            theme.setCurrentTheme(Theme.valueOf(themeName));

        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading settings. Using defaults.");
            e.printStackTrace();
        }
    }
}
