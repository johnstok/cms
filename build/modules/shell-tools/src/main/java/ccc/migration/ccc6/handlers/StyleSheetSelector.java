package ccc.migration.ccc6.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

import ccc.api.DBC;

/**
 * A SQL query that retrieves CCC6 style sheet code for a resource.
 *
 * @author Civic Computing Ltd.
 */
public final class StyleSheetSelector
    implements
        SqlQuery<String> {

    /** {@inheritDoc} */
    @Override
    public String handle(final ResultSet rs) throws SQLException {
        if (rs.next()) {
            final String result = rs.getString("CODE");
            DBC.ensure().toBeFalse(rs.next());
            return result;
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String getSql() {
        return
            "SELECT code "
            + "FROM ("
            + "(SELECT code "
            + "FROM c3_pages, c3_scripts "
            + "WHERE c3_pages.page_id = ? "
            + "AND c3_pages.version_id = 0 "
            + "AND c3_pages.stylesheet = c3_scripts.id) "
            + "UNION ALL "
            + "(SELECT code "
            + "FROM  c3_folders, c3_scripts "
            + "WHERE c3_folders.folder_id = ? "
            + "AND c3_folders.version_id = 0 "
            + "AND c3_folders.stylesheet = c3_scripts.id))";

    }
}
