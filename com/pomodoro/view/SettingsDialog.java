package com.pomodoro.view;

import com.pomodoro.audio.AudioManager;
import com.pomodoro.model.TimerModel;
import com.pomodoro.view.themes.AppTheme;
import com.pomodoro.view.themes.ThemeManager;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class SettingsDialog extends JDialog {

    private final TimerModel model;
    private final ThemeManager themeManager;
    private final AudioManager audioManager;
    private JSpinner workSpinner, shortBreakSpinner, longBreakSpinner, sessionsSpinner;
    private JButton saveButton, cancelButton;
    private JPanel mainPanel, headerPanel, formPanel, buttonPanel;
    private JComboBox<String> musicChooser;

    public SettingsDialog(JFrame parent, TimerModel model, ThemeManager themeManager, AudioManager audioManager) {
        super(parent, "Settings", true);
        this.model = model;
        this.themeManager = themeManager;
        this.audioManager = audioManager;
        setupDialog();
        initComponents();
        layoutComponents();
        applyTheme();
    }

    private void setupDialog() {
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setUndecorated(true);
    }

    private void initComponents() {
        int initialWorkMins = model.getWorkDuration() / 60;
        int initialShortBreakMins = model.getShortBreakDuration() / 60;
        int initialLongBreakMins = model.getLongBreakDuration() / 60;
        int initialSessions = model.getSessionsBeforeLongBreak();

        workSpinner = createStyledSpinner(initialWorkMins, 1, 180);
        shortBreakSpinner = createStyledSpinner(initialShortBreakMins, 1, 60);
        longBreakSpinner = createStyledSpinner(initialLongBreakMins, 1, 90);
        sessionsSpinner = createStyledSpinner(initialSessions, 2, 10);

        String[] tracks = {"lofi1.mp3", "lofi2.mp3", "lofi3.mp3"};
        musicChooser = new JComboBox<>(tracks);

        saveButton = new JButton("Save & Close");
        cancelButton = new JButton("Cancel");
        saveButton.addActionListener(e -> saveSettings());
        cancelButton.addActionListener(e -> dispose());
    }

    private JSpinner createStyledSpinner(int value, int min, int max) {
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(value, min, max, 1);
        JSpinner spinner = new JSpinner(spinnerModel);
        spinner.setFont(new Font("Arial", Font.PLAIN, 16));
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
            textField.setHorizontalAlignment(JTextField.CENTER);
            textField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        }
        return spinner;
    }

    private void layoutComponents() {
        mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1), new EmptyBorder(15, 20, 15, 20)
        ));

        headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel headerLabel = new JLabel("Settings");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(headerLabel);

        formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        JPanel timerPanel = new JPanel(new GridBagLayout());
        timerPanel.setBorder(BorderFactory.createTitledBorder("Timer Durations"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 0;
        timerPanel.add(createFormLabel("Work (minutes)"), gbc);
        gbc.gridy = 1;
        timerPanel.add(createFormLabel("Short Break (minutes)"), gbc);
        gbc.gridy = 2;
        timerPanel.add(createFormLabel("Long Break (minutes)"), gbc);
        gbc.gridy = 3;
        timerPanel.add(createFormLabel("Sessions until Long Break"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridy = 0;
        timerPanel.add(workSpinner, gbc);
        gbc.gridy = 1;
        timerPanel.add(shortBreakSpinner, gbc);
        gbc.gridy = 2;
        timerPanel.add(longBreakSpinner, gbc);
        gbc.gridy = 3;
        timerPanel.add(sessionsSpinner, gbc);

        JPanel soundPanel = new JPanel(new GridBagLayout());
        soundPanel.setBorder(BorderFactory.createTitledBorder("Sound Options"));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        soundPanel.add(createFormLabel("Alarm Sound"), gbc);
        gbc.gridy = 1;
        soundPanel.add(createFormLabel("Background Music"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JButton chooseSoundButton = new JButton("Choose...");
        chooseSoundButton.addActionListener(e -> chooseSoundFile());
        soundPanel.add(chooseSoundButton, gbc);
        gbc.gridy = 1;
        soundPanel.add(musicChooser, gbc);

        formPanel.add(timerPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(soundPanel);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(getOwner());
    }

    private void chooseSoundFile() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Audio Files (MP3, WAV)", "mp3", "wav");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            audioManager.setCustomAlarmSound(file.getAbsolutePath());
            JOptionPane.showMessageDialog(this, "Alarm set to: " + file.getName());
        }
    }

    private void saveSettings() {
        model.setWorkDuration((Integer) workSpinner.getValue() * 60);
        model.setShortBreakDuration((Integer) shortBreakSpinner.getValue() * 60);
        model.setLongBreakDuration((Integer) longBreakSpinner.getValue() * 60);
        model.setSessionsBeforeLongBreak((Integer) sessionsSpinner.getValue());

        String selectedTrack = (String) musicChooser.getSelectedItem();
        audioManager.setBackgroundMusicTrack(selectedTrack);

        dispose();
    }

    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        return label;
    }

    private void styleDialogButton(JButton button, boolean isPrimary) {
        AppTheme theme = themeManager.getCurrentTheme();
        Color bgColor = isPrimary ? theme.getButtonPrimaryBg() : theme.getButtonSecondaryBg();
        Color fgColor = isPrimary ? theme.getButtonPrimaryFg() : theme.getButtonSecondaryFg();
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }

            public void mouseExited(MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }

    private void applyTheme() {
        AppTheme theme = themeManager.getCurrentTheme();
        Color bg = theme.getBackground();
        Color fg = theme.getForeground();
        Color borderC = theme.getBorder();

        mainPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(borderC, 1), new EmptyBorder(15, 20, 15, 20)));
        mainPanel.setBackground(bg);
        headerPanel.setBackground(bg);
        formPanel.setBackground(bg);
        buttonPanel.setBackground(bg);

        for (Component comp : formPanel.getComponents()) {
            if (comp instanceof JPanel panel) {
                panel.setBackground(bg);
                TitledBorder border = BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(borderC),
                        ((TitledBorder) panel.getBorder()).getTitle()
                );
                border.setTitleColor(fg);
                panel.setBorder(border);

                for (Component inner : panel.getComponents()) {
                    inner.setForeground(fg);
                    if (inner instanceof JButton button) {
                        styleDialogButton(button, false);
                    }
                    if (inner instanceof JSpinner spinner) {
                        JComponent editor = spinner.getEditor();
                        if (editor instanceof JSpinner.DefaultEditor) {
                            JTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
                            tf.setBackground(bg.brighter());
                            tf.setForeground(fg);
                        }
                    }
                }
            }
        }

        headerPanel.getComponent(0).setForeground(fg);
        styleDialogButton(saveButton, true);
        styleDialogButton(cancelButton, false);
        repaint();
    }
}
