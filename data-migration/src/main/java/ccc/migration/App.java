package ccc.migration;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import ccc.commons.jee.JNDI;
import ccc.services.ResourceManager;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Connection connection = getConnection();
        Queries queries = new Queries(connection);
        ResultSet rs = queries.selectFolders();
//        Migrations migrations = consoleMigrations();
        Migrations migrations = new MigrationsEJB(JNDI.<ResourceManager>get("ResourceManagerEJB/remote"));
        migrations.migrateFolders(rs);
    }

    private static MigrationsLogger consoleMigrations() {

        return new MigrationsLogger(new Console() {

            @Override
            public void print(String input) {
                System.out.println(input);
            }
            
        });
    }
    
    private static Connection getConnection() {
        Connection connection = null;
        try {
            // Load the JDBC driver
            String driverName = "oracle.jdbc.driver.OracleDriver";
            Class.forName(driverName);
        
            // Create a connection to the database
            String serverName = "poseidon";
            String portNumber = "1521";
            String sid = "DEV";
            String url = "jdbc:oracle:thin:@" + serverName + ":" + portNumber + ":" + sid;
            String username = "asb_pepez";
            String password = "d3asb_pepez";
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            throw new MigrationException(e);
        } catch (SQLException e) {
            throw new MigrationException(e);
        }
        return connection;
    }
}
