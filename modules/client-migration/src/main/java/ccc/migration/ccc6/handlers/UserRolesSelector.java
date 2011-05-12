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
package ccc.migration.ccc6.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * SQL query to select user roles.
 *
 * @author Civic Computing Ltd.
 */
public final class UserRolesSelector
    implements
        SqlQuery<Set<String>> {

    /** {@inheritDoc} */
    @Override public Set<String> handle(final ResultSet rs)
    throws SQLException {
        final Set<String> roles = new HashSet<String>();
        while (rs.next()) {
            final String profile = rs.getString("profile_name");
            if ("Writer".equalsIgnoreCase(profile)
                    || "Editor".equalsIgnoreCase(profile)) {
                roles.add("CONTENT_CREATOR");
            } else if ("Total Control".equalsIgnoreCase(profile)) {
                roles.add("SITE_BUILDER");
                roles.add("CONTENT_CREATOR");
                roles.add("ADMINISTRATOR");
            } else if ("Administrator".equalsIgnoreCase(profile)) {
                roles.add("ADMINISTRATOR");
                roles.add("CONTENT_CREATOR");
            } else {
                roles.add(profile);
            }
        }
        return roles;
    }

    /** {@inheritDoc} */
    @Override
    public String getSql() {
        return
            "SELECT DISTINCT users.user_id, "
            + "profiles.application_name, "
            + "profiles.profile_name "
            + "FROM users, user_profiles, profiles "
            + "WHERE users.user_id = user_profiles.user_id "
            + "AND user_profiles.profile_id= profiles.profile_id "
            + "AND users.user_id = ?";
    }
}
