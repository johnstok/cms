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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.migration.ccc6.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;


/**
 * A SQL query that retrieves CCC6 possible paragraph names of a template.
 *
 * @author Civic Computing Ltd.
 */
public class TemplateFieldsSelector implements SqlQuery<Set<String>> {


    /** {@inheritDoc} */
    @Override
    public Set<String> handle(final ResultSet rs) throws SQLException {
        final Set<String> fields = new HashSet<String>();
        while (rs.next()) {
            final String field = rs.getString("PARA_TYPE");
            if (field != null) {
                fields.add(field);
            }
        }
        return fields;
    }

    /** {@inheritDoc} */
    @Override
    public String getSql() {
        return "SELECT distinct p.para_type "
        + "FROM c3_content c, c3_paragraphs p, c3_display_templates dt "
        + "WHERE c.content_id = p.page_id "
        + "AND c.display_template_id is not null "
        + "AND dt.template_id = c.display_template_id "
        + "AND dt.page = ?";
    }
}
