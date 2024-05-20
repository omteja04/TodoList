
/**
 * Author: omteja04
 * Description: AddTaskDB
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class AddTaskDB {

    public static void addToDatabase(ArrayList<String> valueList) {
        Connection connection = DatabaseConnection.getConnection();

        if (connection == null) {
            JOptionPane.showMessageDialog(null, "Database connection is null");
            return;
        }
        StringBuilder query = new StringBuilder("INSERT INTO ");
        query.append("tasks ").append(" VALUES (");
        for (int i = 0; i < valueList.size(); i++) {
            query.append("?, ");
        }
        query.deleteCharAt(query.length() - 2); // Remove the last comma and space
        query.append(");");
        try (PreparedStatement statement = connection.prepareStatement(query.toString())) {
            for (int i = 0; i < valueList.size(); i++) {
                statement.setString(i + 1, valueList.get(i));
            }
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "Add task successfully");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add task");
            }
        } catch (SQLException e) {

            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error occurred " + e.getMessage());
        }

    }

}
