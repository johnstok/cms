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
