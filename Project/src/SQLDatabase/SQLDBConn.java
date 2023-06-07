package SQLDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class and methods to get and close a connection with the
 * SQL database to query and send information.
 */
public class SQLDBConn {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    private static String password = "Passw0rd!"; // Password
    private static Connection conn = null;
    public static Connection connection(){
        try {
            Class.forName(driver); // Locate Driver
            conn = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
            System.out.println("Connection successful!");

        }
        catch (SQLException e) {
            e.printStackTrace();

        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }
    public static Connection getConnection()
    {
        return conn;
    }
    public static void closeConnection(){
        try{
            conn.close();
        }
        catch (Exception e){
            //do nothing
            }
    }
}
