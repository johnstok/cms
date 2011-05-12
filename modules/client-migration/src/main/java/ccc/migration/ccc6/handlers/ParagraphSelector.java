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

import ccc.migration.ParagraphBean;

/**
 * A SQL query to return the paragraphs for a page.
 *
 * @author Civic Computing Ltd.
 */
public final class ParagraphSelector
    implements
        SqlQuery<List<ParagraphBean>> {

    /** {@inheritDoc} */
    @Override public List<ParagraphBean> handle(final ResultSet rs)
    throws SQLException {
        final List<ParagraphBean> resultList = new ArrayList<ParagraphBean>();
        while (rs.next()) {
            final String key = rs.getString("para_type");
            final String text = rs.getString("text");
            resultList.add(new ParagraphBean(key, text));
        }
        return resultList;
    }

    /** {@inheritDoc} */
    @Override
    public String getSql() {
        return
            "SELECT * FROM  c3_paragraphs "
            + "WHERE c3_paragraphs.page_id = ? "
            + "AND version_id = ? ORDER BY seq";
    }
}
