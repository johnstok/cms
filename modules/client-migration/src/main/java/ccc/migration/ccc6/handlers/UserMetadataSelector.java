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

/**
 * SQL query to select a user's metadata.
 *
 * @author Civic Computing Ltd.
 */
public final class UserMetadataSelector
    implements
        SqlQuery<Map<String, String>> {

    /** {@inheritDoc} */
    @Override
    public Map<String, String> handle(final ResultSet rs) throws SQLException {
        final Map<String, String> metamap = new HashMap<String, String>();
        while (rs.next()) {
            final String key = rs.getString("key1");
            final String value = rs.getString("value");
            if (key != null && !key.trim().equals("")) {
                metamap.put(key, value);
            }
        }
        return metamap;
    }

    /** {@inheritDoc} */
    @Override
    public String getSql() {
        return
        "SELECT udb.display_name key1, ud.attribute_value value "
        + "FROM user_data ud, user_data_attrib udb, users u "
        + "WHERE u.user_id = ud.user_id "
        + "AND ud.attribute_id=udb.attribute_id "
        + "AND u.user_id = ?";
    }
}
