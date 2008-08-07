package ccc.migration;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.dbutils.DbUtils;

import ccc.commons.JNDI;
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
        final Long startTime = new Date().getTime();
        // Establish a queries instance to communicate with the legacy DB.
        final Connection connection = getConnection();
        final Queries queries = new Queries(connection);

        // Migrations migrations = consoleMigrations();
        final MigrationsEJB migrations =
            new MigrationsEJB(
                new JNDI().<ResourceManager>get("ResourceManagerEJB/remote"),
                queries);

        migrations.migrate();
        DbUtils.closeQuietly(connection);
        final Long elapsedTime = new Date().getTime() - startTime;

        System.out.println("Migration finished in "+elapsedTime/1000+" seconds");
    }

    private static Connection getConnection() {
        Connection connection = null;

        // TODO 24 Jul 2008 petteri: Read from properties
        try {
            // Load the JDBC driver
            final String driverName = "oracle.jdbc.driver.OracleDriver";
            Class.forName(driverName);

            // Create a connection to the database
            final String serverName = "poseidon";
            final String portNumber = "1521";
            final String sid = "DEV";
            final String url =
                "jdbc:oracle:thin:@"
                + serverName + ":"
                + portNumber + ":"
                + sid;
            final String username = "asb_pepez";
            final String password = "d3asb_pepez";
            connection = DriverManager.getConnection(url, username, password);
        } catch (final ClassNotFoundException e) {
            throw new MigrationException(e);
        } catch (final SQLException e) {
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
