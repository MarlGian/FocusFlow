package com.pomodoro.model;

import javax.swing.Timer;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import com.pomodoro.audio.AudioManager;

public class TimerModel {

    private final AudioManager audioManager;
    private int workDuration = 25 * 60;
    private int shortBreakDuration = 5 * 60;
    private int longBreakDuration = 15 * 60;
    private static final int INTERVAL = 1000;
    private int sessionsBeforeLongBreak = 4;
    private int currentTimeInSeconds;
    private TimerState currentState;
    private Timer timer;
    private int completedWorkSessions;
    private PropertyChangeSupport support;

    public TimerModel(AudioManager audioManager) {
        this.audioManager = audioManager;
        this.support = new PropertyChangeSupport(this);
        this.completedWorkSessions = 0;
        this.currentState = TimerState.WORK;
        this.timer = new Timer(INTERVAL, e -> tick());
        this.timer.setRepeats(true);
        resetTimerToStateDuration();
    }

    public void resetCycle() {
        pauseTimer();

        TimerState oldState = this.currentState;
        int oldSessions = this.completedWorkSessions;

        this.currentState = TimerState.WORK;
        this.completedWorkSessions = 0;

        notifyChange("state", oldState, this.currentState);
        notifyChange("sessions", oldSessions, 0);

        resetTimerToStateDuration();
    }

    public void startTimer() {
        if (!timer.isRunning()) {
            timer.start();
            notifyChange("status", false, true);
        }
    }

    public void pauseTimer() {
        if (timer.isRunning()) {
            timer.stop();
            notifyChange("status", true, false);
        }
    }

    public void resetTimerToStateDuration() {
        pauseTimer();
        int newTime;
        switch (currentState) {
            case WORK ->
                newTime = workDuration;
            case SHORT_BREAK ->
                newTime = shortBreakDuration;
            case LONG_BREAK ->
                newTime = longBreakDuration;
            default ->
                newTime = workDuration;
        }
        setCurrentTimeInSeconds(newTime);
        notifyChange("state", null, this.currentState);
    }

    private void tick() {
        if (currentTimeInSeconds > 0) {
            int oldTime = this.currentTimeInSeconds;
            currentTimeInSeconds--;
            notifyChange("time", oldTime, currentTimeInSeconds);
        } else {
            handlePhaseEnd();
        }
    }

    private void handlePhaseEnd() {
        pauseTimer();
        audioManager.playAlarmSound();

        TimerState oldState = this.currentState;

        if (currentState == TimerState.WORK) {
            int oldSessions = completedWorkSessions;
            completedWorkSessions++;
            notifyChange("sessions", oldSessions, completedWorkSessions);
            currentState = (completedWorkSessions % sessionsBeforeLongBreak == 0)
                    ? TimerState.LONG_BREAK
                    : TimerState.SHORT_BREAK;
        } else {
            currentState = TimerState.WORK;
        }

        notifyChange("phaseEnd", oldState, this.currentState);

        notifyChange("state", oldState, this.currentState);
        resetTimerToStateDuration();
    }

    public void resetCompletedWorkSessions() {
        int oldSessions = this.completedWorkSessions;
        this.completedWorkSessions = 0;
        notifyChange("sessions", oldSessions, 0);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    private void notifyChange(String property, Object oldValue, Object newValue) {
        support.firePropertyChange(property, oldValue, newValue);
    }

    public int getCurrentTimeInSeconds() {
        return currentTimeInSeconds;
    }

    public TimerState getCurrentState() {
        return currentState;
    }

    public boolean timerIsRunning() {
        return timer.isRunning();
    }

    public int getWorkDuration() {
        return workDuration;
    }

    public int getShortBreakDuration() {
        return shortBreakDuration;
    }

    public int getLongBreakDuration() {
        return longBreakDuration;
    }

    public int getSessionsBeforeLongBreak() {
        return sessionsBeforeLongBreak;
    }

    public int getCompletedWorkSessions() {
        return completedWorkSessions;
    }

    public void setCurrentTimeInSeconds(int timeInSeconds) {
        int oldTime = this.currentTimeInSeconds;
        this.currentTimeInSeconds = timeInSeconds;
        support.firePropertyChange("time", oldTime, this.currentTimeInSeconds);
    }

    public void setWorkDuration(int workDurationInSeconds) {
        this.workDuration = workDurationInSeconds;
        if (!timerIsRunning() && currentState == TimerState.WORK) {
            setCurrentTimeInSeconds(workDurationInSeconds);
        }
    }

    public void setShortBreakDuration(int shortBreakDurationInSeconds) {
        this.shortBreakDuration = shortBreakDurationInSeconds;
        if (!timerIsRunning() && currentState == TimerState.SHORT_BREAK) {
            setCurrentTimeInSeconds(shortBreakDurationInSeconds);
        }
    }

    public void setLongBreakDuration(int longBreakDurationInSeconds) {
        this.longBreakDuration = longBreakDurationInSeconds;
        if (!timerIsRunning() && currentState == TimerState.LONG_BREAK) {
            setCurrentTimeInSeconds(longBreakDurationInSeconds);
        }
    }

    public void setSessionsBeforeLongBreak(int sessions) {
        this.sessionsBeforeLongBreak = sessions;
        notifyChange("sessions", null, completedWorkSessions);
    }
}
