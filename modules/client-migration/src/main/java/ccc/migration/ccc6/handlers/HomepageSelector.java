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
 * A SQL query that retrieves CCC6 folders with home page value set.
 *
 * @author Civic Computing Ltd.
 */
public final class HomepageSelector
    implements
        SqlQuery<Map<Integer, Integer>> {

    /** {@inheritDoc} */
    @Override
    public Map<Integer, Integer> handle(final ResultSet rs)
        throws SQLException {
        final Map<Integer, Integer> resultMap =
            new HashMap<Integer, Integer>();

        while (rs.next()) {
            final int contentId = rs.getInt("CONTENT_ID");
            final int homepageId = rs.getInt("HOMEPAGE");

            resultMap.put(Integer.valueOf(contentId),
                Integer.valueOf(homepageId));
        }
        return resultMap;
    }

    /** {@inheritDoc} */
    @Override
    public String getSql() {
        return
            "SELECT content_id, homepage "
            + "FROM c3_content "
            + "WHERE version_id = 0 "
            + "AND (status = 'PUBLISHED' OR status = 'NEW') "
            + "AND CONTENT_TYPE = 'FOLDER' "
            + "AND homepage is not null";
    }
}
