package ccc.migration.ccc6.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class ParagraphVersionsSelector
    implements
        SqlQuery<List<Integer>> {

    /** {@inheritDoc} */
    @Override public List<Integer> handle(final ResultSet rs) throws SQLException {
        final List<Integer> resultList = new ArrayList<Integer>();
        while (rs.next()) {
            final Integer version = rs.getInt("version_id");
            resultList.add(version);
        }
        return resultList;
    }

    /** {@inheritDoc} */
    @Override
    public String getSql() {
        return
            "select distinct version_id "
            + "from c3_paragraphs "
            + "where page_id=? "
            + "order by version_id asc";
    }
}