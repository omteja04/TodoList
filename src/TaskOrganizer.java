/**
 * Author: omteja04
 * Description: TaskOrganizer
 */

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TaskOrganizer {
    public static void organizeByPriority(JPanel tasksPanel) {
        // Clear existing tasks
        tasksPanel.removeAll();

        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection != null) {
                try (Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery("SELECT * FROM tasks ORDER BY priority DESC")) {

                    while (resultSet.next()) {
                        JPanel taskPanel = createTaskPanel(resultSet.getString("description"),
                                resultSet.getString("status"),
                                resultSet.getString("due_date"),
                                resultSet.getString(
                                        "id"),
                                resultSet.getString("priority"),
                                resultSet.getString("category"),
                                tasksPanel);
                        tasksPanel.add(taskPanel);
                        tasksPanel.add(Box.createVerticalStrut(10));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Refresh tasks panel
        tasksPanel.revalidate();
        tasksPanel.repaint();
    }

    public static void organizeByDueDate(JPanel tasksPanel) {
        // Clear existing tasks
        tasksPanel.removeAll();

        // Load tasks from the database sorted by due date
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection != null) {
                try (Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery("SELECT * FROM tasks ORDER BY due_date")) {

                    while (resultSet.next()) {
                        JPanel taskPanel = createTaskPanel(resultSet.getString("description"),
                                resultSet.getString("status"),
                                resultSet.getString("due_date"),
                                resultSet.getString(
                                        "id"),
                                resultSet.getString("priority"),
                                resultSet.getString("category"),
                                tasksPanel);
                        tasksPanel.add(taskPanel);
                        tasksPanel.add(Box.createVerticalStrut(10));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Refresh tasks panel
        tasksPanel.revalidate();
        tasksPanel.repaint();
    }

    public static void organizeByCategory(JPanel tasksPanel) {
        // Clear existing tasks
        tasksPanel.removeAll();

        // Load tasks from the database sorted by category
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection != null) {
                try (Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery("SELECT * FROM tasks ORDER BY category")) {

                    while (resultSet.next()) {
                        JPanel taskPanel = createTaskPanel(resultSet.getString("description"),
                                resultSet.getString("status"),
                                resultSet.getString("due_date"),
                                resultSet.getString(
                                        "id"),
                                resultSet.getString("priority"),
                                resultSet.getString("category"),
                                tasksPanel);
                        tasksPanel.add(taskPanel);
                        tasksPanel.add(Box.createVerticalStrut(10));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Refresh tasks panel
        tasksPanel.revalidate();
        tasksPanel.repaint();
    }

    private static JPanel createTaskPanel(String description, String status, String dueDate, String taskId,
            String priority, String category, JPanel tasksPanel) {

        return AddTaskToHome.createTaskPanel(description, status, dueDate, taskId, priority, category, tasksPanel);
    }
}
