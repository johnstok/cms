package ccc.migration.ccc6.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * A SQL query that retrieves CCC6 folders with home page value set.
 *
 * @author Civic Computing Ltd.
 */
public final class HomepageSelector
    implements
        SqlQuery<Map<Integer, Integer>> {

    /** {@inheritDoc} */
    @Override
    public Map<Integer, Integer> handle(final ResultSet rs)
        throws SQLException {
        final Map<Integer, Integer> resultMap =
            new HashMap<Integer, Integer>();

        while (rs.next()) {
            final int contentId = rs.getInt("CONTENT_ID");
            final int homepageId = rs.getInt("HOMEPAGE");

            resultMap.put(Integer.valueOf(contentId),
                Integer.valueOf(homepageId));
        }
        return resultMap;
    }

    /** {@inheritDoc} */
    @Override
    public String getSql() {
        return
            "SELECT content_id, homepage "
            + "FROM c3_content "
            + "WHERE version_id = 0 "
            + "AND (status = 'PUBLISHED' OR status = 'NEW') "
            + "AND CONTENT_TYPE = 'FOLDER' "
            + "AND homepage is not null";
    }
}
