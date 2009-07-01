/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
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
        return "SELECT unique p.para_type "
        + "FROM c3_content c, c3_paragraphs p, c3_display_templates dt "
        + "WHERE c.content_id = p.page_id "
        + "AND c.display_template_id is not null "
        + "AND dt.template_id = c.display_template_id "
        + "AND dt.page = ?";
    }
}
