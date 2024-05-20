/**
 * Author: omteja04
 * Description: AddTaskToHome
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AddTaskToHome {
    public static void loadTasksFromDatabase(JPanel tasksPanel, String status) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection != null) {
                String query = "SELECT * FROM tasks WHERE status = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, status);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        while (resultSet.next()) {
                            JPanel taskPanel = createTaskPanel(resultSet.getString("description"),
                                    resultSet.getString("status"),
                                    resultSet.getString("due_date"),
                                    resultSet.getString("id"),
                                    resultSet.getString("priority"),
                                    resultSet.getString("category"),
                                    tasksPanel);
                            tasksPanel.add(taskPanel);
                            tasksPanel.add(Box.createVerticalStrut(10));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Add logging or display error message
        }
    }

    public static JPanel createTaskPanel(String description, String status, String dueDate, String taskId,
            String priority, String category, JPanel tasksPanel) {
        JPanel taskPanel = new JPanel(new BorderLayout());
        taskPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel descriptionLabel = new JLabel(
                "Description: " + description + " (" + category + ") " + "(" + priority + ")");
        JLabel statusLabel = new JLabel("Status: " + status);
        JLabel dueDateLabel = new JLabel("Due Date: " + dueDate);

        JButton editButton = new JButton("Edit");
        JButton completeButton = new JButton("Complete");
        JButton deleteButton = new JButton("Delete");

        // Set color for status label based on status
        if (status.equals("Pending")) {
            statusLabel.setForeground(Color.RED);
        } else if (status.equals("Completed")) {
            statusLabel.setForeground(Color.GREEN);
        } // Add more conditions if needed for other status values

        editButton.addActionListener(
                new EditButtonActionListener(description, dueDate, taskId, descriptionLabel, dueDateLabel));
        completeButton.addActionListener(new CompleteButtonActionListener(taskId, statusLabel, taskPanel, tasksPanel));
        deleteButton.addActionListener(new DeleteButtonActionListener(taskPanel, tasksPanel, taskId));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(editButton);
        buttonPanel.add(completeButton);
        buttonPanel.add(deleteButton);

        taskPanel.add(descriptionLabel, BorderLayout.NORTH);
        taskPanel.add(statusLabel, BorderLayout.CENTER);
        taskPanel.add(dueDateLabel, BorderLayout.SOUTH);
        taskPanel.add(buttonPanel, BorderLayout.EAST);

        return taskPanel;
    }

    private static class EditButtonActionListener implements ActionListener {
        private String description;
        private String dueDate;
        private String taskId;
        private JLabel descriptionLabel;
        private JLabel dueDateLabel;

        public EditButtonActionListener(String description, String dueDate, String taskId, JLabel descriptionLabel,
                JLabel dueDateLabel) {
            this.description = description;
            this.dueDate = dueDate;
            this.taskId = taskId;
            this.descriptionLabel = descriptionLabel;
            this.dueDateLabel = dueDateLabel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String newDescription = JOptionPane.showInputDialog("Enter new description:", description);
            String newDueDate = JOptionPane.showInputDialog("Enter new due date (YYYY-MM-DD):", dueDate);

            // Update task details in the database
            try (Connection connection = DatabaseConnection.getConnection()) {
                if (connection != null) {
                    Statement statement = connection.createStatement();

                    if (newDescription != null && !newDescription.isEmpty()) {
                        String updateDescriptionQuery = String
                                .format("UPDATE tasks SET description = '%s' WHERE id = '%s'", newDescription, taskId);
                        statement.executeUpdate(updateDescriptionQuery);
                        descriptionLabel.setText("Description: " + newDescription);
                    }

                    if (newDueDate != null && !newDueDate.isEmpty()) {
                        String updateDueDateQuery = String.format("UPDATE tasks SET due_date = '%s' WHERE id = '%s'",
                                newDueDate, taskId);
                        statement.executeUpdate(updateDueDateQuery);
                        dueDateLabel.setText("Description: " + newDueDate);
                    }
                    statement.close();
                }
                TodoListHome.refreshTasks();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static class CompleteButtonActionListener implements ActionListener {
        private String taskId;
        private JLabel statusLabel;
        private JPanel taskPanel;
        private JPanel tasksPanel;

        public CompleteButtonActionListener(String taskId, JLabel statusLabel, JPanel taskPanel, JPanel tasksPanel) {
            this.taskId = taskId;
            this.statusLabel = statusLabel;
            this.taskPanel = taskPanel;
            this.tasksPanel = tasksPanel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Update the task status to "Completed" in the database
            try (Connection connection = DatabaseConnection.getConnection()) {
                if (connection != null) {
                    Statement statement = connection.createStatement();
                    String updateQuery = String.format("UPDATE tasks SET status = 'Completed' WHERE id = '%s'", taskId);
                    statement.executeUpdate(updateQuery);
                    statement.close();

                    // Change the color of statusLabel to green
                    statusLabel.setForeground(Color.GREEN);

                    // Remove the taskPanel from the tasksPanel
                    tasksPanel.remove(taskPanel);

                    // Reload tasks from the database
                    tasksPanel.removeAll();
                    loadTasksFromDatabase(tasksPanel, "Pending");
                    loadTasksFromDatabase(tasksPanel, "Completed");

                    // Refresh the UI
                    tasksPanel.revalidate();
                    tasksPanel.repaint();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static class DeleteButtonActionListener implements ActionListener {
        private JPanel taskPanel;
        private JPanel tasksPanel;
        private String taskId;

        public DeleteButtonActionListener(JPanel taskPanel, JPanel tasksPanel, String taskId) {
            this.taskPanel = taskPanel;
            this.tasksPanel = tasksPanel;
            this.taskId = taskId;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this task?",
                    "Confirmation", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                // Remove the taskPanel from the tasksPanel
                tasksPanel.remove(taskPanel);
                tasksPanel.revalidate();
                tasksPanel.repaint();

                // Delete the task from the database
                try (Connection connection = DatabaseConnection.getConnection()) {
                    if (connection != null) {
                        Statement statement = connection.createStatement();
                        String deleteQuery = String.format("DELETE FROM tasks WHERE id = '%s'", taskId);
                        statement.executeUpdate(deleteQuery);
                        statement.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }
}
