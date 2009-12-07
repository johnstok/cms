/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
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
