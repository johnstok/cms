package ccc.migration.ccc6.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ccc.services.api.FileDelta;

/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class FileSelector
    implements
        SqlQuery<List<FileDelta>> {

    /** {@inheritDoc} */
    @Override
    public List<FileDelta> handle(final ResultSet rs) throws SQLException {
        final List<FileDelta> results = new ArrayList<FileDelta>();

        while (rs.next()) {
            final FileDelta file =
                new FileDelta(
                    null,
                    rs.getString("object_name"),
                    rs.getString("object_title"),
                    rs.getString("classification"));
            results.add(file);
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
