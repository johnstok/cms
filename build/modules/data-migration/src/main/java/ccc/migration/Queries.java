package ccc.migration;

import static ccc.commons.DBC.require;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;

/**
 * Queries for data migration.
 *
 * @author Civic Computing Ltd
 */
public class Queries {
    private final Connection _connection;

    /**
     * Constructor.
     *
     * @param conn Connection
     */
    public Queries(final Connection conn) {
        require().notNull(conn);
        _connection = conn;
    }

    /**
     * Returns list of all resources with active version for specified
     * parent.
     * @param i folder parent id
     *
     * @return list of resources found with query
     */
    public List<ResourceBean> selectResources(final int i) {
        ResultSet rs = null;
        PreparedStatement ps = null;
        List<ResourceBean> resultList = new ArrayList<ResourceBean>();
        try {
            ps = _connection.prepareStatement(
                "SELECT CONTENT_ID, CONTENT_TYPE, NAME, PAGE FROM "
                + "C3_CONTENT, C3_DISPLAY_TEMPLATES WHERE "
                + "C3_CONTENT.PARENT_ID = ? AND VERSION_ID = 0 AND "
                + "STATUS = 'PUBLISHED' AND "
                + "C3_CONTENT.DISPLAY_TEMPLATE_ID = "
                + "C3_DISPLAY_TEMPLATES.TEMPLATE_ID(+)");
            ps.setInt(1, i);
            rs = ps.executeQuery();

            while (rs.next()) {
                final int contentId = rs.getInt("CONTENT_ID");
                final String type = rs.getString("CONTENT_TYPE");
                final String name = rs.getString("NAME");
                final String displayTemplate = rs.getString("PAGE");

                resultList.add(new ResourceBean(
                    contentId, type, name, displayTemplate));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
        }
        return resultList;
    }

    /**
     * Returns list of all paragraphs with active version for specified
     * page. Sequences are not joined.
     *
     * @param pageId pageId
     * @return list of paragraphs found with query
     */
    public List<ParagraphBean> selectParagraphs(final int pageId) {
        ResultSet rs = null;
        PreparedStatement ps = null;
        List<ParagraphBean> resultList = new ArrayList<ParagraphBean>();

        try {
            ps = _connection.prepareStatement(
                "SELECT * FROM  C3_PARAGRAPHS "
                + "WHERE C3_PARAGRAPHS.PAGE_ID = ? "
                + "AND VERSION_ID = 0 ORDER BY SEQ");
            ps.setInt(1, pageId);
            rs = ps.executeQuery();

            // populate map
            while (rs.next()) {
                final String key = rs.getString("PARA_TYPE");
                final String text = rs.getString("TEXT");
                resultList.add(new ParagraphBean(key, text));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
        }
        return resultList;
    }
}
