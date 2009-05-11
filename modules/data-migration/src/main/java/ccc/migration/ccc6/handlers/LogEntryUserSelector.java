
package ccc.migration.ccc6.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

import ccc.domain.CCCException;
import ccc.migration.MigrationException;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class LogEntryUserSelector
    implements
        SqlQuery<Integer> {

    /** {@inheritDoc} */
    @Override
    public Integer handle(final ResultSet rs) throws SQLException {
        if (rs.next()) {
            final Integer userId = Integer.valueOf(rs.getInt("user_id"));
            if (rs.next()) {
                final String contentId = rs.getString("content_id");
                throw new CCCException("Multiple users for id: "+contentId);
            }
            return userId;
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
            + "action = ? AND "
            + "version_comment like ?";
    }
}