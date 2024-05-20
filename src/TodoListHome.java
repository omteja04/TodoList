/**
 * Author: omteja04
 * Description: TodoListHome
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TodoListHome extends JFrame implements ActionListener {

    private JPanel mainPanel;
    private static JPanel tasksPanel;
    private JPanel buttonsPanel;
    private JScrollPane scrollPane;
    private JButton addTaskButton;
    private JComboBox<String> organizeComboBox;
    private static final String[] ORGANIZE_OPTIONS = { "Default Organization","By Due Date", "By Priority", "By Category" };

    TodoListHome() {
        setTitle("Todo List App");
        setSize(600, 500);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        tasksPanel = new JPanel();
        tasksPanel.setLayout(new BoxLayout(tasksPanel, BoxLayout.Y_AXIS));
        AddTaskToHome.loadTasksFromDatabase(tasksPanel,"Pending");
        AddTaskToHome.loadTasksFromDatabase(tasksPanel,"Completed");
        scrollPane = new JScrollPane(tasksPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        addTaskButton = new JButton("Add Task");
        addTaskButton.addActionListener(this);
        organizeComboBox = new JComboBox<>(ORGANIZE_OPTIONS);
        organizeComboBox.addActionListener(this);

        buttonsPanel = new JPanel();
        buttonsPanel.add(addTaskButton);
        buttonsPanel.add(organizeComboBox);

        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        this.add(mainPanel);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addTaskButton) {
            new AddTask();
        } else if (e.getSource() == organizeComboBox) {
            String selectedOption = (String) organizeComboBox.getSelectedItem();
            organizeTasks(selectedOption);
        }
    }

    private void organizeTasks(String selectedOption) {
        tasksPanel.removeAll();
        if (selectedOption.equals("By Due Date")) {
            TaskOrganizer.organizeByDueDate(tasksPanel);
        } else if (selectedOption.equals("By Priority")) {
            TaskOrganizer.organizeByPriority(tasksPanel);
        } else if (selectedOption.equals("By Category")) {
            TaskOrganizer.organizeByCategory(tasksPanel);
        }
        else if(selectedOption.equals("Default Organization")){
            refreshTasks();
        }
        tasksPanel.revalidate();
        tasksPanel.repaint();
    }

    public static void refreshTasks() {
        tasksPanel.removeAll();
        AddTaskToHome.loadTasksFromDatabase(tasksPanel,"Pending");
        AddTaskToHome.loadTasksFromDatabase(tasksPanel,"Completed");
        tasksPanel.revalidate();
        tasksPanel.repaint();
    }
}
