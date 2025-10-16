package com.pomodoro.view.themes;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ThemeManager {

    private AppTheme currentTheme;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public Theme getCurrentThemeEnum() {
        return this.currentTheme.getThemeEnum();

    }

    public ThemeManager() {
        this.currentTheme = new DarkTheme();
    }

    public AppTheme getCurrentTheme() {
        return currentTheme;
    }

    public void setCurrentTheme(Theme theme) {
        AppTheme oldTheme = this.currentTheme;
        if (theme == Theme.LIGHT) {
            this.currentTheme = new LightTheme();
        } else {
            this.currentTheme = new DarkTheme();
        }
        support.firePropertyChange("currentTheme", oldTheme, this.currentTheme);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }
}
