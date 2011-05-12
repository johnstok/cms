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
import java.util.Date;

import ccc.migration.LogEntryBean;


/**
 * A SQL query to return the user associated with a specified CCC6 log entry.
 *
 * @author Civic Computing Ltd.
 */
public final class LogEntryUserSelector
    implements
        SqlQuery<LogEntryBean> {

    /** {@inheritDoc} */
    @Override
    public LogEntryBean handle(final ResultSet rs) throws SQLException {
        if (rs.next()) {
            final int  userId     = rs.getInt("USER_ID");
            final Date happenedOn = rs.getDate("ACTION_DATE");

            return new LogEntryBean(userId, happenedOn);

            // Ignore further records - choose the first.
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String getSql() {
        return
            "SELECT * FROM c3_version_audit_log "
            + "WHERE content_id = ? AND "
            + "version_id = ? AND "
            + "action = ?";
    }
}
