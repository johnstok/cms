package ccc.migration;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import oracle.jdbc.pool.OracleDataSource;

import org.apache.commons.dbutils.DbUtils;

/**
 * Entry class for the migration application.
 *
 */
public final class App {

    private static final long MILLISECS_PER_SEC = 1000;

    private App() { /* NO-OP */ }

    /**
     * Entry point for this application.
     *
     * @param args String array of application arguments.
     */
    public static void main(final String[] args) {
        final long startTime = new Date().getTime();

        // Establish a queries instance to communicate with the legacy DB.
        final Connection connection = getConnection();
        final Queries queries = new Queries(connection);

        // Migrations migrations = consoleMigrations();
        final Migrations migrations =
            new Migrations(queries);

        migrations.migrate();
        DbUtils.closeQuietly(connection);

        final long elapsedTime = new Date().getTime() - startTime;
        System.out.println(
            "Migration finished in "
            + elapsedTime/MILLISECS_PER_SEC
            + " secs");
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
            final String username = "ccc_migration";
            final String password = "d3ccc_migration";

            final OracleDataSource ods = new OracleDataSource();
            final Properties props = new Properties();
            props.put("user", username);
            props.put("password", password);
            props.put(
                "oracle.jdbc.FreeMemoryOnEnterImplicitCache", Boolean.TRUE);
            ods.setConnectionProperties(props);
            ods.setURL(url);
            connection = ods.getConnection();

        } catch (final ClassNotFoundException e) {
            throw new MigrationException(e);
        } catch (final SQLException e) {
            throw new MigrationException(e);
        }
        return connection;
    }
}
