/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: See subversion log.
 *-----------------------------------------------------------------------------
 */
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
