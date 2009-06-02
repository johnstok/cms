package ccc.migration.ccc6.handlers;

import static ccc.api.DBC.*;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * SQL query to select a user's email.
 *
 * @author Civic Computing Ltd.
 */
public final class UserEmailSelector
    implements
        SqlQuery<String> {

    /** {@inheritDoc} */
    @Override public String handle(final ResultSet rs) throws SQLException {
        if (rs.next()) {
            final String email = rs.getString("attribute_value");
            require().toBeFalse(rs.next());
            return email;
        }
        return "";
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
