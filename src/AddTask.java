
/**
 * Author: omteja04
 * Description: AddTask
 */

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.*;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class AddTask extends JFrame {
    private JLabel taskIdLabel;
    private JLabel taskDescriptionLabel;
    private JLabel dueDateLabel;
    private JLabel priorityLabel;
    private JLabel categoryLabel;
    private JLabel statusLabel;
    private JTextField statusField;
    private JTextField taskIdField;
    private JTextField taskDescriptionField;
    private JTextField dueDateField;
    @SuppressWarnings("rawtypes")
    private JComboBox priorityBox;
    private JComboBox categoryBox;
    // private JTextField categoryField;
    JButton addTaskButton;
    JButton clearButton;
    Font font = new Font("Arial", Font.BOLD, 12);

    AddTask() {
        setTitle("New Task");
        setSize(400, 300); // Adjusted size
        taskIdLabel = new JLabel("Task Id");
        taskIdField = new JTextField();
        taskIdField.setPreferredSize(new Dimension(200, 25)); // Set preferred size
        taskDescriptionLabel = new JLabel("Task Description");
        taskDescriptionField = new JTextField();
        taskDescriptionField.setPreferredSize(new Dimension(200, 25)); // Set preferred size
        dueDateLabel = new JLabel("Due Date (yyyy/mm/dd)");
        dueDateField = new JTextField();
        dueDateField.setPreferredSize(new Dimension(200, 25)); // Set preferred size
        priorityLabel = new JLabel("Priority");
        String[] priorities = { "1 - Low", "2 - Medium", "3 - High" };
        int[] associations = { 1, 2, 3 };
        priorityBox = new JComboBox<>(priorities);
        priorityBox.setFont(font);
        priorityBox.setSelectedIndex(-1);
        priorityBox.setPreferredSize(new Dimension(200, 25)); // Set preferred size
        categoryLabel = new JLabel("Category");
        // categoryField = new JTextField();
        String[] categories = { "Personal", "Study", "Work" };
        categoryBox = new JComboBox(categories);
        categoryBox.setFont(font);
        categoryBox.setSelectedIndex(-1);
        categoryBox.setPreferredSize(new Dimension(200, 25)); // Set preferred size
        statusLabel = new JLabel("Status");
        statusField = new JTextField("Pending");
        statusField.setPreferredSize(new Dimension(200, 25)); // Set preferred size
        addTaskButton = new JButton("Add Task");
        clearButton = new JButton("Clear");

        setLayout(new GridLayout(0, 2, 10, 10)); // Adjusted horizontal and vertical gaps
        add(taskIdLabel);
        add(taskIdField);
        add(taskDescriptionLabel);
        add(taskDescriptionField);
        add(dueDateLabel);
        add(dueDateField);
        add(priorityLabel);
        add(priorityBox);
        add(categoryLabel);
        add(categoryBox);
        add(statusLabel);
        add(statusField);
        setFontForTextFields();
        add(addTaskButton);
        add(clearButton);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(null, "Do You Want To Clear ?", "Submit",
                        JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    clearFields();
                }
            }
        });
        addTaskButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(null, "Do You Want To Submit ?", "Submit",
                        JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    try {
                        ArrayList<String> valueList = new ArrayList<>();
                        valueList.add(taskIdField.getText());
                        valueList.add(taskDescriptionField.getText());
                        valueList.add(dueDateField.getText());
                        Integer priority = associations[priorityBox.getSelectedIndex()];
                        valueList.add(priority.toString());
                        valueList.add((String) categoryBox.getSelectedItem());
                        valueList.add(statusField.getText());

                        AddTaskDB.addToDatabase(valueList);
                        TodoListHome.refreshTasks();
                        dispose();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "An error occurred while adding the task.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        setResizable(false);

    }

    Component[] components = getContentPane().getComponents();

    private void setFontForTextFields() {
        for (Component component : components) {
            if (component instanceof JTextField) {
                JTextField textField = (JTextField) component;
                textField.setFont(font);
            }
        }
    }

    private void clearFields() {
        Component[] components = getContentPane().getComponents();
        for (Component component : components) {
            if (component instanceof JTextField) {
                JTextField textField = (JTextField) component;
                textField.setText("");
            } else if (component instanceof JComboBox) {
                @SuppressWarnings("rawtypes")
                JComboBox comboBox = (JComboBox) component;
                comboBox.setSelectedIndex(-1);
            }
        }
    }

}