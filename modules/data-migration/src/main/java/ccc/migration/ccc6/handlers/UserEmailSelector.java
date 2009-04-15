package ccc.migration.ccc6.handlers;

import static ccc.commons.DBC.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import ccc.migration.MigrationException;
import ccc.services.api.UserDelta;

/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class UserEmailSelector
    implements
        SqlQuery<Void> {

    /** userId : int. */
    private final int       _userId;
    /** user : UserDelta. */
    private final UserDelta _user;

    /**
     * Constructor.
     *
     * @param userId
     * @param user
     */
    public UserEmailSelector(final int userId, final UserDelta user) {

        _userId = userId;
        _user = user;
    }

    /** {@inheritDoc} */
    @Override public Void handle(final ResultSet rs) throws SQLException {
        if (rs.next()) {
            _user._email = rs.getString("attribute_value");
            require().toBeFalse(rs.next());
        } else {
            throw new MigrationException("User "+_userId+" has no email.");
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String getSql() {
        return
            "SELECT users.user_id, user_data.attribute_value "
            + "FROM users, user_data, user_data_attrib "
            + "WHERE users.user_id = user_data.user_id "
            + "AND user_data.attribute_id = user_data_attrib.attribute_id "
            + "AND user_data_attrib.display_name = 'Email' "
            + "AND users.user_id = ?";
    }
}