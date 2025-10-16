package com.pomodoro.view;

import com.pomodoro.model.TimerModel;
import com.pomodoro.model.TimerState;
import com.pomodoro.view.themes.AppTheme;
import javax.swing.*;
import java.awt.*;

public class TimerDisplayPanel extends JPanel {
    private JLabel timerLabel;
    private JLabel stateLabel;
    private JLabel cycleLabel;

    public TimerDisplayPanel() {
        setLayout(new BorderLayout(0, 10));
        setOpaque(false);

        stateLabel = new JLabel("WORK", SwingConstants.CENTER);
        stateLabel.setFont(new Font("Arial", Font.BOLD, 28));

        timerLabel = new JLabel("25:00", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Monospaced", Font.BOLD, 100));

        cycleLabel = new JLabel("Cycle: 0 / 4", SwingConstants.CENTER);
        cycleLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        add(stateLabel, BorderLayout.NORTH);
        add(timerLabel, BorderLayout.CENTER);
        add(cycleLabel, BorderLayout.SOUTH);
    }

    public void updateDisplay(TimerModel model) {
        int seconds = model.getCurrentTimeInSeconds();
        timerLabel.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));
        stateLabel.setText(model.getCurrentState().toString().replace("_", " "));

        int completed = model.getCompletedWorkSessions();
        int total = model.getSessionsBeforeLongBreak();
        int currentCycle = completed > 0 && completed % total == 0 ? total : completed % total;
        cycleLabel.setText(String.format("Cycle: %d / %d", currentCycle, total));
    }

    public void applyTheme(AppTheme theme, TimerState state) {
        stateLabel.setForeground(theme.getForeground());
        cycleLabel.setForeground(theme.getForeground());

        switch (state) {
            case WORK -> timerLabel.setForeground(theme.getAccentWork());
            case SHORT_BREAK -> timerLabel.setForeground(theme.getAccentShortBreak());
            case LONG_BREAK -> timerLabel.setForeground(theme.getAccentLongBreak());
        }
    }

    public Color getTimerLabelColor() {
        return timerLabel.getForeground();
    }
}