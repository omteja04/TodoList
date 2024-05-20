
/**
 * Author: omteja04
 * Description: DatabaseConnection
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class DatabaseConnection {
    private static String url = "jdbc:mysql://localhost:3306/todo_list";
    private static String username = "omteja";
    private static String password = "teja1234";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Connection Failed", "Oops!",
                    JOptionPane.ERROR_MESSAGE);
        }
        return connection;
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
                JOptionPane.showMessageDialog(null, "Not Closed", "Oops!",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
