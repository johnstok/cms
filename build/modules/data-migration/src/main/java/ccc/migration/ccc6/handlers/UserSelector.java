
package ccc.migration.ccc6.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class UserSelector
    implements
        SqlQuery<Integer> {

    static Logger log =
        Logger.getLogger(UserSelector.class);

    /** legacyVersion : int. */
    private final int legacyVersion;
    /** contentId : int. */
    private final int contentId;

    /**
     * Constructor.
     *
     * @param legacyVersion
     * @param contentId
     */
    public UserSelector(final int legacyVersion, final int contentId) {
        this.legacyVersion = legacyVersion;
        this.contentId = contentId;
    }

    /** {@inheritDoc} */
    @Override
    public Integer handle(final ResultSet rs) throws SQLException {

        Integer userId = null;
        if (rs.next()) {
            log.debug("FOUND " + contentId + " " + legacyVersion);
            userId = rs.getInt("user_id");
            // require().toBeFalse(rs.next());
            if (rs.next()) {
                log.warn("More than one user found for " + contentId + " v." + legacyVersion);
            }
        } else {
            log.error("User Id NOT FOUND with content_id: " + contentId + " version_id" + legacyVersion);
        }
        return userId;
    }

    /** {@inheritDoc} */
    @Override
    public String getSql() {
        return
            "SELECT user_id FROM c3_version_audit_log "
            + "WHERE content_id = ? AND "
            + "version_id = ? AND "
            + "action = ? AND "
            + "version_comment = ?";
    }
}