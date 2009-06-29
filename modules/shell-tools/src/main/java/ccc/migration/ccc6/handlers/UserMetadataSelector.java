package ccc.migration.ccc6.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * SQL query to select a user's metadata.
 *
 * @author Civic Computing Ltd.
 */
public final class UserMetadataSelector
    implements
        SqlQuery<Map<String, String>> {

    /** {@inheritDoc} */
    @Override
    public Map<String, String> handle(final ResultSet rs) throws SQLException {
        final Map<String, String> metamap = new HashMap<String, String>();
        while (rs.next()) {
            final String key = rs.getString("key");
            final String value = rs.getString("value");
            if (key != null && !key.trim().equals("")) {
                metamap.put(key, value);
            }
        }
        return metamap;
    }

    /** {@inheritDoc} */
    @Override
    public String getSql() {
        return
        "SELECT udb.display_name key, ud.attribute_value value "
        + "FROM user_data ud, user_data_attrib udb, users u "
        + "WHERE u.user_id = ud.user_id "
        + "AND ud.attribute_id=udb.attribute_id "
        + "AND u.user_id = ?";
    }
}
