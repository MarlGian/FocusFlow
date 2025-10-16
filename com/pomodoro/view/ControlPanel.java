package com.pomodoro.view;

import com.pomodoro.controller.TimerController;
import com.pomodoro.view.themes.AppTheme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ControlPanel extends JPanel {

    private JButton startPauseButton;
    private JButton resetButton;

    public ControlPanel(TimerController controller) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 40, 10));
        setBorder(new EmptyBorder(0, 0, 20, 0));
        setOpaque(false);
        startPauseButton = new JButton("START");
        resetButton = new JButton("RESET");

        startPauseButton.addActionListener(e -> controller.handleStartPause());
        resetButton.addActionListener(e -> controller.handleReset());

        add(startPauseButton);
        add(resetButton);
    }

    public void updateDisplay(boolean isRunning) {
        startPauseButton.setText(isRunning ? "PAUSE" : "START");
    }

    public void applyTheme(AppTheme theme, boolean isRunning, Color timerColor) {
        styleButton(resetButton, theme, false, null);
        if (isRunning) {
            styleButton(startPauseButton, theme, true, timerColor);
        } else {
            styleButton(startPauseButton, theme, true, null);
        }
    }

    private void styleButton(JButton button, AppTheme theme, boolean isPrimary, Color bgColorOverride) {
        Color bgColor = isPrimary ? theme.getButtonPrimaryBg() : theme.getButtonSecondaryBg();
        Color fgColor = isPrimary ? theme.getButtonPrimaryFg() : theme.getButtonSecondaryFg();

        if (bgColorOverride != null) {
            bgColor = bgColorOverride;
            fgColor = theme.getBackground();
        }

        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setForeground(fgColor);
        button.setBackground(bgColor);
        button.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));

        for (MouseListener ml : button.getMouseListeners()) {
            if (ml instanceof MouseAdapter) {
                button.removeMouseListener(ml);
            }
        }

        final Color finalBgColor = bgColor;
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(finalBgColor.brighter());
            }

            public void mouseExited(MouseEvent evt) {
                button.setBackground(finalBgColor);
            }
        });
    }
}
