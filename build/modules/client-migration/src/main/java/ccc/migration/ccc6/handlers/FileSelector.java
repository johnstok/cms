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

import ccc.migration.LegacyFile;

/**
 * SQL query used to select file records from CCC6.
 *
 * @author Civic Computing Ltd.
 */
public final class FileSelector
    implements
        SqlQuery<Map<String, LegacyFile>> {

    /** {@inheritDoc} */
    @Override
    public Map<String, LegacyFile> handle(final ResultSet rs)
    throws SQLException {
        final Map<String, LegacyFile> results =
            new HashMap<String, LegacyFile>();

        while (rs.next()) {
            final LegacyFile file =
                new LegacyFile(
                    rs.getString("object_title"),
                    rs.getString("classification"),
                    rs.getDate("last_update"));
            results.put(rs.getString("object_name"), file);
        }

        return results;
    }

    /** {@inheritDoc} */
    @Override
    public String getSql() {
        return
        "SELECT object_name, object_title, classification, last_update "
        + "FROM c3_file_objects "
        + "WHERE application_name='CCC' AND object_type= ?";
    }
}
