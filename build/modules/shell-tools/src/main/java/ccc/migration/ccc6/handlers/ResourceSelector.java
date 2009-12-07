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
import java.util.Date;
import java.util.List;

import ccc.migration.ResourceBean;

/**
 * A SQL query that retrieves CCC6 resources.
 *
 * @author Civic Computing Ltd.
 */
public final class ResourceSelector
    implements
        SqlQuery<List<ResourceBean>> {

    /** {@inheritDoc} */
    @Override
    public List<ResourceBean> handle(final ResultSet rs) throws SQLException {
        final List<ResourceBean> resultList =
            new ArrayList<ResourceBean>();

        while (rs.next()) {
            final int contentId = rs.getInt("CONTENT_ID");
            final String type = rs.getString("CONTENT_TYPE");
            final String name = rs.getString("NAME");
            final String title = rs.getString("INDEX_TITLE");
            final String displayTemplate = rs.getString("PAGE");
            final boolean published =
                "PUBLISHED".equals(rs.getString("STATUS"));
            final int legacyVersion = rs.getInt("VERSION_ID");
            final boolean isSecure = (null!=rs.getString("permission_name"));
            final String useInIndex = rs.getString("USE_IN_INDEX");
            final String description = rs.getString("DESCRIPTION");
            final Date expiryDate = rs.getDate("EXPIRY_DATE");

            resultList.add(new ResourceBean(contentId,
                                            type,
                                            name,
                                            displayTemplate,
                                            published,
                                            legacyVersion,
                                            isSecure,
                                            title,
                                            useInIndex,
                                            description,
                                            expiryDate));
        }
        return resultList;
    }

    /** {@inheritDoc} */
    @Override
    public String getSql() {
        return
            "SELECT content_id, content_type, name, index_title, page, "
            + " status,version_id, permission_name, use_in_index, description, "
            + " embargo_date, expiry_date "
            + "FROM c3_content, c3_display_templates "
            + "WHERE c3_content.parent_id = ? "
            + "AND version_id = 0 "
            + "AND (status = 'PUBLISHED' OR status = 'NEW') "
            + "AND c3_content.display_template_id = "
            +   "c3_display_templates.template_id(+) "
            + "ORDER BY SORT_ORDER";
    }
}
