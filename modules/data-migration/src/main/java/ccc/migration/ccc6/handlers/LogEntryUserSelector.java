
package ccc.migration.ccc6.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import ccc.migration.LogEntryBean;
import ccc.migration.MigrationException;


/**
 * TODO: Add Description for this type.
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
        throw new MigrationException("User missing.");
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