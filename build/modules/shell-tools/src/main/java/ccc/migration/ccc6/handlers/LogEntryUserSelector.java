
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
