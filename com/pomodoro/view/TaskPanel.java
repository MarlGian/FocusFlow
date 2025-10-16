package com.pomodoro.view;

import com.pomodoro.view.themes.AppTheme;
import javax.swing.*;
import java.awt.*;

public class TaskPanel extends JPanel {

    private JTextField taskField;

    public TaskPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        taskField = new JTextField("What are you working on?");
        taskField.setFont(new Font("Arial", Font.ITALIC, 16));
        taskField.setHorizontalAlignment(JTextField.CENTER);

        taskField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (taskField.getText().equals("What are you working on?")) {
                    taskField.setText("");
                    taskField.setFont(new Font("Arial", Font.PLAIN, 16));
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (taskField.getText().isEmpty()) {
                    taskField.setText("What are you working on?");
                    taskField.setFont(new Font("Arial", Font.ITALIC, 16));
                }
            }
        });

        add(taskField, BorderLayout.CENTER);
    }

    public void setLock(boolean isLocked, AppTheme theme) {
        taskField.setEditable(!isLocked);
        taskField.setFocusable(!isLocked);
        if (isLocked) {
            taskField.setBackground(theme.getBackground());
            taskField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        } else {
            taskField.setBackground(theme.getBackground().brighter());
            taskField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(theme.getBorder()),
                    BorderFactory.createEmptyBorder(4, 4, 4, 4)
            ));
        }
    }

    public void applyTheme(AppTheme theme) {
        taskField.setForeground(theme.getForeground());
    }
}
