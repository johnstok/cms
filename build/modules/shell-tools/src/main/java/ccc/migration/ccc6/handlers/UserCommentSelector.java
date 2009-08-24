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

import static ccc.types.DBC.*;

import java.sql.ResultSet;
import java.sql.SQLException;



/**
 * A SQL query that retrieves user comment of the page update.
 *
 * @author Civic Computing Ltd.
 */
public class UserCommentSelector implements SqlQuery<String> {

    /** {@inheritDoc} */
    @Override
    public String handle(final ResultSet rs) throws SQLException {
        if (rs.next()) {
            final String userComment = rs.getString("USER_COMMENT");
            require().toBeFalse(rs.next());
            return userComment;
        }
        return "";

    }

    /** {@inheritDoc} */
    @Override
    public String getSql() {
        return "SELECT user_comment "
                + "FROM c3_content "
                + "WHERE content_id = ? and version_id = ?";
    }

}
