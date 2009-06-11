package ccc.migration.ccc6.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ccc.migration.ParagraphBean;

/**
 * A SQL query to return the paragraphs for a page.
 *
 * @author Civic Computing Ltd.
 */
public final class ParagraphSelector
    implements
        SqlQuery<List<ParagraphBean>> {

    /** {@inheritDoc} */
    @Override public List<ParagraphBean> handle(final ResultSet rs)
    throws SQLException {
        final List<ParagraphBean> resultList = new ArrayList<ParagraphBean>();
        while (rs.next()) {
            final String key = rs.getString("para_type");
            final String text = rs.getString("text");
            resultList.add(new ParagraphBean(key, text));
        }
        return resultList;
    }

    /** {@inheritDoc} */
    @Override
    public String getSql() {
        return
            "SELECT * FROM  c3_paragraphs "
            + "WHERE c3_paragraphs.page_id = ? "
            + "AND version_id = ? ORDER BY seq";
    }
}
