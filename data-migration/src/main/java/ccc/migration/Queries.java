package ccc.migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static ccc.commons.jee.DBC.*;

/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class Queries {
    private final Connection connection;

    public Queries(Connection conn) {
        require().notNull(conn);
        connection = conn;
    }

    /**
     * Returns result set of all resources with active version for specified
     * parent.
     * @param i folder parent id
     *
     * @return
     */
    public ResultSet selectResources(int i) {
        try {
            PreparedStatement ps;
            ps = connection.prepareStatement(
                "SELECT * FROM  C3_CONTENT WHERE C3_CONTENT.PARENT_ID = ? " +
                "AND VERSION_ID = 0 AND STATUS = 'PUBLISHED'");
            ps.setInt(1, i);
            return ps.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns result set of all paragraphs with active version for specified
     * page. Sequences are not joined.
     * 
     * @param i
     * @return
     */
    public ResultSet selectParagraphs(int pageId) {
        try {
            PreparedStatement ps;
            ps = connection.prepareStatement(
                "SELECT * FROM  C3_PARAGRAPHS WHERE C3_PARAGRAPHS.PAGE_ID = ? " +
                "AND VERSION_ID = 0 ORDER BY SEQ");
            ps.setInt(1, pageId);
            return ps.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
