package ccc.migration.ccc6.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

import ccc.api.DBC;

/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class FlaggedSelector
    implements
        SqlQuery<String> {

    /** {@inheritDoc} */
    @Override
    public String handle(final ResultSet rs) throws SQLException {
        if (rs.next()) {
            final String result = rs.getString("news_flag");
            DBC.ensure().toBeFalse(rs.next());
            return result;
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String getSql() {
        return
            "SELECT news_flag "
          + "FROM c3_pages "
          + "WHERE page_id=? "
          + "AND version_id=0";
    }
}
