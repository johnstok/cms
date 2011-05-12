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
import java.util.ArrayList;
import java.util.List;

/**
 * A SQL query that return the version numbers of the paragraphs for a page.
 *
 * @author Civic Computing Ltd.
 */
public final class ParagraphVersionsSelector
    implements
        SqlQuery<List<Integer>> {

    /** {@inheritDoc} */
    @Override public List<Integer> handle(final ResultSet rs)
    throws SQLException {
        final List<Integer> resultList = new ArrayList<Integer>();
        while (rs.next()) {
            final Integer version = Integer.valueOf(rs.getInt("version_id"));
            resultList.add(version);
        }
        return resultList;
    }

    /** {@inheritDoc} */
    @Override
    public String getSql() {
        return
            "select distinct version_id "
            + "from c3_paragraphs "
            + "where page_id=? "
            + "order by version_id asc";
    }
}
