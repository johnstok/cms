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

import static ccc.api.DBC.*;

import java.sql.ResultSet;
import java.sql.SQLException;



/**
 * A SQL query that retrieves 'is major edit' information.
 *
 * @author Civic Computing Ltd.
 */
public class IsMajorEditSelector implements SqlQuery<Boolean> {

    /** {@inheritDoc} */
    @Override
    public Boolean handle(final ResultSet rs) throws SQLException {
        if (rs.next()) {
            final String isMajorEdit = rs.getString("IS_MAJOR_EDIT");
            require().toBeFalse(rs.next());
            if (isMajorEdit != null && isMajorEdit.equalsIgnoreCase("Y")) {
                return Boolean.valueOf(true);
            }
        }
        return Boolean.valueOf(false);

    }

    /** {@inheritDoc} */
    @Override
    public String getSql() {
        return "SELECT is_major_edit "
                + "FROM c3_content "
                + "WHERE content_id = ? and version_id = ?";
    }

}
