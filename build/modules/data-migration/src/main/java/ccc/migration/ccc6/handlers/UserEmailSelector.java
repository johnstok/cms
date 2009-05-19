package ccc.migration.ccc6.handlers;

import static ccc.api.DBC.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import ccc.migration.MigrationException;

/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class UserEmailSelector
    implements
        SqlQuery<String> {

    /** userId : int. */
    private final int       _userId;

    /**
     * Constructor.
     *
     * @param userId
     * @param user
     */
    public UserEmailSelector(final int userId) {
        _userId = userId;
    }

    /** {@inheritDoc} */
    @Override public String handle(final ResultSet rs) throws SQLException {
        if (rs.next()) {
            final String email = rs.getString("attribute_value");
            require().toBeFalse(rs.next());
            return email;
        } else {
            throw new MigrationException("User "+_userId+" has no email.");
        }
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