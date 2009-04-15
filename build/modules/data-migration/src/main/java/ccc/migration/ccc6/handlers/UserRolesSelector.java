package ccc.migration.ccc6.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

import ccc.services.api.UserDelta;

/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class UserRolesSelector
    implements
        SqlQuery<Void> {

    /** user : UserDelta. */
    private final UserDelta _user;

    /**
     * Constructor.
     *
     * @param user
     */
    public UserRolesSelector(final UserDelta user) {

        _user = user;
    }

    /** {@inheritDoc} */
    @Override public Void handle(final ResultSet rs) throws SQLException {
        while (rs.next()) {
            final String profile = rs.getString("profile_name");
            if ("Writer".equalsIgnoreCase(profile)
                    || "Editor".equalsIgnoreCase(profile)) {
                _user._roles.add("CONTENT_CREATOR");
            } else if ("Total Control".equalsIgnoreCase(profile)) {
                _user._roles.add("SITE_BUILDER");
                _user._roles.add("CONTENT_CREATOR");
                _user._roles.add("ADMINISTRATOR");
            } else if ("Administrator".equalsIgnoreCase(profile)) {
                _user._roles.add("ADMINISTRATOR");
                _user._roles.add("CONTENT_CREATOR");
            } else {
                _user._roles.add(profile);
            }
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String getSql() {
        return
            "SELECT DISTINCT users.user_id, "
            + "profiles.application_name, "
            + "profiles.profile_name "
            + "FROM users, user_profiles, profiles "
            + "WHERE users.user_id = user_profiles.user_id "
            + "AND user_profiles.profile_id= profiles.profile_id "
            + "AND users.user_id = ?";
    }
}