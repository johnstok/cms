package ccc.migration;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import ccc.commons.jee.JNDI;
import ccc.services.ResourceManager;

/**
 * Entry class for the migration application.
 *
 */
public final class App {

    private App() { /* NO-OP */ }

    /**
     * Entry point for this application.
     *
     * @param args String array of application arguments.
     */
    public static void main(final String[] args) {

        // Establish a queries instance to communicate with the legacy DB.
        Connection connection = getConnection();
        Queries queries = new Queries(connection);
        
        // Migrations migrations = consoleMigrations();
        MigrationsEJB migrations =
            new MigrationsEJB(
                JNDI.<ResourceManager>get("ResourceManagerEJB/remote"),
                queries);

        migrations.migrate();
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
            String url =
                "jdbc:oracle:thin:@"
                + serverName + ":"
                + portNumber + ":"
                + sid;
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
    
//  /**
//  * TODO: Add a description of this method.
//  *
//  * @param rootFolder
//  */
// private static void prettyPrint(Folder rootFolder) {
//     int indent = 0;
//     System.out.println(rootFolder.name());
//     prettyPrint(rootFolder.entries(), indent+2);
// }
//
// /**
//  * TODO: Add a description of this method.
//  *
//  * @param entries
//  * @param i
//  */
// private static void prettyPrint(List<Resource> entries, int i) {
//     for (Resource entry : entries) {
//         for (int a=0 ; a < i ; a++) {
//             System.out.print(" ");
//         }
//         System.out.println(entry.name());
//         if (entry.type() == ResourceType.FOLDER) {
//             prettyPrint(entry.asFolder().entries(), i+2);
//         }
//     }
// }
}
