package ccc.migration.ccc6.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import ccc.migration.LegacyDBQueries;
import ccc.services.api.UserDelta;

/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class UserSelector2
    implements
        SqlQuery<Map<Integer, UserDelta>> {

    private static Logger log =
        Logger.getLogger(UserSelector2.class);

    /** legacyDBQueries : UserSelector2. */
    private final LegacyDBQueries legacyDBQueries;

    /**
     * Constructor.
     *
     * @param legacyDBQueries
     */
    public UserSelector2(final LegacyDBQueries legacyDBQueries) {
        this.legacyDBQueries = legacyDBQueries;
    }

    /** {@inheritDoc} */
    @Override public Map<Integer, UserDelta> handle(final ResultSet rs)
                                                           throws SQLException {
        final Map<Integer, UserDelta> resultList =
            new HashMap<Integer, UserDelta>();
        while (rs.next()) {
            final String userName = rs.getString("user_name");
            final String password = rs.getString("user_passwd");
            final int userId = rs.getInt("user_id");
            try {
                final UserDelta user = new UserDelta();
                user._username = userName;
                user._password = password;
                legacyDBQueries.selectEmailForUser(user, userId);
                legacyDBQueries.selectRolesForUser(user, userId);
                resultList.put(userId, user);
            } catch (final Exception e) {
                log.error(e.getMessage());
            }
        }
        return resultList;
    }

    /** {@inheritDoc} */
    @Override
    public String getSql() {
        return "SELECT user_id, user_name, user_passwd, name FROM users";
    }
}
