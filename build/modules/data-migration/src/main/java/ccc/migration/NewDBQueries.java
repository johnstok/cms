package ccc.migration;

import static ccc.commons.DBC.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.dbutils.DbUtils;

import ccc.domain.Password;

/**
 * Queries for data migration.
 *
 * @author Civic Computing Ltd
 */
public class NewDBQueries {

    private final Connection _connection;

    /**
     * Constructor.
     *
     * @param conn Connection
     */
    public NewDBQueries(final Connection conn) {
        require().notNull(conn);
        _connection = conn;
    }

    /**
     * Creates migration user.
     *
     * @return UUID of the created user.
     */
    public UUID insertMigrationUser(final String username,
                                    final String email,
                                    final String password) {
        final UUID uid = UUID.randomUUID();
        final UUID pwId = UUID.randomUUID();
        final byte[] hash = Password.hash(password, pwId.toString());

        PreparedStatement ps = null;

        try {
            // insert user
            ps = _connection.prepareStatement(
                "INSERT INTO USER ( _ID, _EMAIL , _USERNAME , _VERSION ) "
                + "VALUES (?,?,?,0);");
            ps.setString(1, uid.toString());
            ps.setString(2, email);
            ps.setString(3, username);
            ps.executeUpdate();

            // insert role
            ps = _connection.prepareStatement(
                "INSERT INTO USER__ROLES ( ID, ROLE) "
            + "VALUES (?, 'ADMINISTRATOR');");
            ps.setString(1, uid.toString());
            ps.executeUpdate();

            // insert password
            ps = _connection.prepareStatement(
                "INSERT INTO PASSWORD ( _ID, _VERSION, _HASH, _USER) "
            + "VALUES (?, 0, ?, ?);");
            ps.setString(1, pwId.toString());
            ps.setBytes(2, hash);
            ps.setString(3, uid.toString());
            ps.executeUpdate();
            _connection.commit();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DbUtils.closeQuietly(ps);
        }
        return uid;
    }

    /**
     * Change migration user password.
     *
     * @param muid UUID of the migration user;
     */
    public void changeMigrationUserPw(final UUID muid) {

        final UUID pwId = UUID.randomUUID();
        final byte[] hash =
            Password.hash(""+new Date().getTime(), pwId.toString());

        PreparedStatement ps = null;

        try {
            // update password
            ps = _connection.prepareStatement(
                "UPDATE PASSWORD SET _HASH=?, _VERSION=1 WHERE _USER = ?;");
            ps.setBytes(1, hash);
            ps.setString(2, muid.toString());
            ps.executeUpdate();
            _connection.commit();

        } catch (final SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DbUtils.closeQuietly(ps);
        }
    }
}
