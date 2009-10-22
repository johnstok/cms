package ccc.migration;

import static ccc.types.DBC.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.dbutils.DbUtils;

import ccc.domain.CCCException;
import ccc.domain.User;

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
     * @param username The username for the new user.
     * @param email The email for the new user.
     * @param password The password for the new user.
     *
     * @return UUID of the created user.
     */
    public UUID insertMigrationUser(final String username,
                                    final String email,
                                    final String password) {
        final UUID uid = UUID.randomUUID();
        final byte[] hash = User.hash(password, uid.toString());

        PreparedStatement ps = null;

        try {
            // insert user
            ps = _connection.prepareStatement(
                "INSERT INTO users (id, email, username, vn, hash, name) "
                + "VALUES (?,?,?,?,?,?)");
            ps.setString(1, uid.toString());
            ps.setString(2, email);
            ps.setString(3, username);
            ps.setInt(4, 0);
            ps.setBytes(5, hash);
            ps.setString(6, username);
            ps.executeUpdate();

            // insert role
            ps = _connection.prepareStatement(
                "INSERT INTO user_roles (user_id, role) "
                + "VALUES (?, 'ADMINISTRATOR')");
            ps.setString(1, uid.toString());
            ps.executeUpdate();
            ps = _connection.prepareStatement(
                "INSERT INTO user_roles (user_id, role) "
                + "VALUES (?, 'CONTENT_CREATOR')");
            ps.setString(1, uid.toString());
            ps.executeUpdate();
            ps = _connection.prepareStatement(
                "INSERT INTO user_roles (user_id, role) "
                + "VALUES (?, 'SITE_BUILDER')");
            ps.setString(1, uid.toString());
            ps.executeUpdate();

            _connection.commit();
        } catch (final SQLException e) {
            throw new CCCException(e);
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
            User.hash(""+new Date().getTime(), pwId.toString());

        PreparedStatement ps = null;

        try {
            // update password
            ps = _connection.prepareStatement(
                "UPDATE users SET hash=?, vn=1 WHERE user_id = ?");
            ps.setBytes(1, hash);
            ps.setString(2, muid.toString());
            ps.executeUpdate();
            _connection.commit();

        } catch (final SQLException e) {
            throw new CCCException(e);
        } finally {
            DbUtils.closeQuietly(ps);
        }
    }
}
