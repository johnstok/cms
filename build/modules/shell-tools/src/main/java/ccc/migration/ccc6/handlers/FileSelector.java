package ccc.migration.ccc6.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import ccc.migration.LegacyFile;

/**
 * SQL query used to select file records from CCC6.
 *
 * @author Civic Computing Ltd.
 */
public final class FileSelector
    implements
        SqlQuery<Map<String, LegacyFile>> {

    /** {@inheritDoc} */
    @Override
    public Map<String, LegacyFile> handle(final ResultSet rs)
    throws SQLException {
        final Map<String, LegacyFile> results =
            new HashMap<String, LegacyFile>();

        while (rs.next()) {
            final LegacyFile file =
                new LegacyFile(
                    rs.getString("object_title"),
                    rs.getString("classification"),
                    rs.getDate("last_update"));
            results.put(rs.getString("object_name"), file);
        }

        return results;
    }

    /** {@inheritDoc} */
    @Override
    public String getSql() {
        return
        "SELECT object_name, object_title, classification, last_update "
        + "FROM c3_file_objects "
        + "WHERE application_name='CCC' AND object_type= ?";
    }
}
