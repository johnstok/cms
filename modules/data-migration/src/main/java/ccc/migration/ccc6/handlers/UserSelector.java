
package ccc.migration.ccc6.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

import ccc.commons.DBC;
import ccc.migration.MigrationException;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class UserSelector
    implements
        SqlQuery<Integer> {

    /** {@inheritDoc} */
    @Override
    public Integer handle(final ResultSet rs) throws SQLException {
        if (rs.next()) {
            final Integer userId = rs.getInt("user_id");
             DBC.ensure().toBeFalse(rs.next());
             return userId;
        }
        throw new MigrationException("User missing.");
    }

    /** {@inheritDoc} */
    @Override
    public String getSql() {
        return
            "SELECT user_id FROM c3_version_audit_log "
            + "WHERE content_id = ? AND "
            + "version_id = ? AND "
            + "action = ? AND "
            + "version_comment like ?";
    }
}