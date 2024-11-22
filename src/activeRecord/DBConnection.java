package activeRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static Connection connection;
    private static String url = "jdbc:mysql://localhost/testpersonne";
    private static String username = "root";
    private static String password = "password";

    private DBConnection() throws SQLException {
        Properties prop = new Properties();
        prop.setProperty("user", username);
        prop.setProperty("password", password);
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost/testpersonne";
        connection = DriverManager.getConnection(url,prop);
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, username, password);
        }
        return connection;
    }

    public static void setNomDB(String nomDB) throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        url = "jdbc:mysql://localhost/" + nomDB;
        connection = DriverManager.getConnection(url, username, password);
    }
}
