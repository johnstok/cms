package ccc.migration.ccc6.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Selects the set of pages that are visible in menus.
 *
 * @author Civic Computing Ltd.
 */
public final class ShowInMainMenuSelector
    implements
        SqlQuery<Set<Integer>> {

    /** {@inheritDoc} */
    @Override
    public Set<Integer> handle(final ResultSet rs) throws SQLException {
        final Set<Integer> resultList = new HashSet<Integer>();

        while (rs.next()) {
            final Integer id = Integer.valueOf(rs.getInt(1));
            resultList.add(id);
        }

        return resultList;
    }

    /** {@inheritDoc} */
    @Override
    public String getSql() {
        return
            "select distinct content_id from menu_data";
    }
}
