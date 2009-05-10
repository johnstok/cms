package ccc.migration.ccc6.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import ccc.services.api.FileDelta;

/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class FileSelector
    implements
        SqlQuery<Map<String,FileDelta>> {

    /** {@inheritDoc} */
    @Override
    public Map<String,FileDelta> handle(final ResultSet rs) throws SQLException {
        final Map<String,FileDelta> results = new HashMap<String,FileDelta>();

        while (rs.next()) {
            final FileDelta file =
                new FileDelta(
                    rs.getString("object_title"),
                    rs.getString("classification"),
                    null,
                    -1);
            results.put(rs.getString("object_name"), file);
        }

        return results;
    }

    /** {@inheritDoc} */
    @Override
    public String getSql() {
        return
        "SELECT object_name, object_title, classification "
        + "FROM c3_file_objects "
        + "WHERE application_name='CCC' AND object_type= ?";
    }
}
