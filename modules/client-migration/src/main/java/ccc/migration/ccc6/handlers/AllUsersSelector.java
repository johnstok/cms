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
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import ccc.api.core.UserDto;
import ccc.api.types.Username;
import ccc.migration.LegacyDBQueries;
import ccc.migration.MigrationException;

/**
 * A SQL query to export all users from CCC6.
 *
 * @author Civic Computing Ltd.
 */
public final class AllUsersSelector
    implements
        SqlQuery<Map<Integer, UserDto>> {

    private static Logger log = Logger.getLogger(AllUsersSelector.class);

    private final LegacyDBQueries _legacyDBQueries;

    /**
     * Constructor.
     *
     * @param legacyDBQueries A query class to perform sub-queries.
     */
    public AllUsersSelector(final LegacyDBQueries legacyDBQueries) {
        _legacyDBQueries = legacyDBQueries;
    }

    /** {@inheritDoc} */
    @Override public Map<Integer, UserDto> handle(final ResultSet rs)
                                                           throws SQLException {
        final Map<Integer, UserDto> resultList =
            new HashMap<Integer, UserDto>();
        while (rs.next()) {
            final String userName = rs.getString("user_name");
            final String password = rs.getString("user_passwd");
            final String name = rs.getString("name");
            final int userId = rs.getInt("user_id");
            try {
                final Map<String, String> metamap =
                    _legacyDBQueries.selectMetadataForUser(userId);
                metamap.put("legacyId", ""+userId);

                String email = "";
                if (metamap.containsKey("Email address")) {
                    email = metamap.get("Email address");
                } else if (metamap.containsKey("Email")) {
                    email = metamap.get("Email");
                }

                final UserDto user = new UserDto();
                user.setEmail(email);
                user.setUsername(new Username(userName));
                user.setName(name);
                user.setMetadata(metamap);
                user.setPassword(password);

                resultList.put(Integer.valueOf(userId), user);

            } catch (final MigrationException e) {
                log.warn("Error selecting user: "+e.getMessage());
            }
        }
        return resultList;
    }

    /** {@inheritDoc} */
    @Override
    public String getSql() {
        return "SELECT user_id, user_name, user_passwd, name FROM users";
    }
}
