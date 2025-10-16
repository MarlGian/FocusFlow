package com.pomodoro.view;

import com.pomodoro.model.TimerModel;
import com.pomodoro.controller.TimerController;
import com.pomodoro.model.TimerState;
import com.pomodoro.view.themes.AppTheme;
import com.pomodoro.view.themes.ThemeManager;
import com.pomodoro.audio.AudioManager;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;

public class PomodoroFrame extends JFrame implements PropertyChangeListener {

    private final TimerModel model;
    private final ThemeManager themeManager;
    private final AudioManager audioManager;
    private TimerController controller;
    private TrayIcon trayIcon;

    private TimerDisplayPanel timerDisplayPanel;
    private TaskPanel taskPanel;
    private ControlPanel controlPanel;
    private AppMenuBar menuBar;

    public PomodoroFrame(TimerModel model, ThemeManager themeManager, AudioManager audioManager) {
        this.model = model;
        this.themeManager = themeManager;
        this.audioManager = audioManager;
        this.model.addPropertyChangeListener(this);
        this.themeManager.addPropertyChangeListener(this);
    }

    public void initialize() {
        setupFrame();
        initComponents();
        layoutComponents();
        setupSystemTray();
        updateDisplay();
    }

    public void setTaskFieldLock(boolean isLocked) {
        taskPanel.setLock(isLocked, themeManager.getCurrentTheme());
    }

    private void setupFrame() {
        setTitle("Focus Flow by @MarlGian");
        try {
            URL iconURL = getClass().getResource("/resources/icon.png");
            if (iconURL != null) {
                setIconImage(new ImageIcon(iconURL).getImage());
            }
        } catch (Exception e) {
            System.out.println("ERROR: Could not load icon.png.");
        }

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(false);
            }
        });
    }

    private void initComponents() {
        timerDisplayPanel = new TimerDisplayPanel();
        taskPanel = new TaskPanel();
        controlPanel = new ControlPanel(controller);
        menuBar = new AppMenuBar(this, model, controller, themeManager, audioManager);
        setJMenuBar(menuBar);
    }

    private void layoutComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 30, 10, 30));

        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(timerDisplayPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(taskPanel);
        mainPanel.add(Box.createVerticalGlue());

        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(500, 480));
        pack();
        setLocationRelativeTo(null);
    }

    private void updateDisplay() {
        timerDisplayPanel.updateDisplay(model);
        controlPanel.updateDisplay(model.timerIsRunning());
        taskPanel.setLock(model.timerIsRunning(), themeManager.getCurrentTheme());
        updateTheme();
        updateTrayIcon();
    }

    private void updateTrayIcon() {
        if (trayIcon != null) {
            String state = model.getCurrentState().toString().replace("_", " ");
            String time = String.format("%02d:%02d", model.getCurrentTimeInSeconds() / 60, model.getCurrentTimeInSeconds() % 60);
            trayIcon.setToolTip(String.format("%s - %s", state, time));
        }
    }

    private void updateTheme() {
        AppTheme theme = themeManager.getCurrentTheme();
        getContentPane().setBackground(theme.getBackground());

        timerDisplayPanel.applyTheme(theme, model.getCurrentState());
        taskPanel.applyTheme(theme);
        controlPanel.applyTheme(theme, model.timerIsRunning(), timerDisplayPanel.getTimerLabelColor());
        menuBar.applyTheme(theme);

        JPanel mainPanel = (JPanel) getContentPane().getComponent(0);
        mainPanel.setBackground(theme.getBackground());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("phaseEnd".equals(evt.getPropertyName())) {
            showNotification((TimerState) evt.getOldValue(), (TimerState) evt.getNewValue());
        }
        SwingUtilities.invokeLater(this::updateDisplay);
    }

    private void showNotification(TimerState oldState, TimerState newState) {
        if (trayIcon == null) {
            return;
        }
        String title = (oldState == TimerState.WORK) ? "Work Session Complete!" : "Break's Over!";
        String message = (oldState == TimerState.WORK) ? "Time for a " + newState.toString().replace("_", " ").toLowerCase() + "." : "Time to get back to work.";
        trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
    }

    public void setController(TimerController controller) {
        this.controller = controller;
    }

    private void setupSystemTray() {
        if (!SystemTray.isSupported()) {
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            return;
        }
        final PopupMenu popup = new PopupMenu();
        trayIcon = new TrayIcon(getIconImage(), getTitle());
        trayIcon.setImageAutoSize(true);
        MenuItem showItem = new MenuItem("Show App");
        MenuItem quitItem = new MenuItem("Quit");
        showItem.addActionListener(e -> {
            setVisible(true);
            setState(JFrame.NORMAL);
        });
        quitItem.addActionListener(e -> controller.handleQuit());
        popup.add(showItem);
        popup.addSeparator();
        popup.add(quitItem);
        trayIcon.setPopupMenu(popup);
        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    setVisible(true);
                    setState(JFrame.NORMAL);
                }
            }
        });
        try {
            SystemTray.getSystemTray().add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
    }
}
