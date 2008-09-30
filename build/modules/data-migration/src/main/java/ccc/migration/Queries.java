package ccc.migration;

import static ccc.commons.DBC.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;

import ccc.domain.CreatorRoles;
import ccc.domain.User;

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
        final List<ResourceBean> resultList = new ArrayList<ResourceBean>();
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
        } catch (final SQLException e) {
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
        final List<ParagraphBean> resultList = new ArrayList<ParagraphBean>();

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
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
        }
        return resultList;
    }

    /**
     * Returns a list of users.
     * @param errors
     *
     * @return The list of users.
     */
    public List<User> selectUsers() {
        ResultSet rs = null;
        PreparedStatement ps = null;
        final List<User> resultList = new ArrayList<User>();

        try {
            ps = _connection.prepareStatement(
                "SELECT user_id, user_name, user_passwd, name FROM users");
            rs = ps.executeQuery();

            while (rs.next()) {
                final String userName = rs.getString("user_name");
                final int userId = rs.getInt("user_id");
                try {
                    final User user = new User(userName);
                    selectEmailForUser(user, userId);
                    selectRolesForUser(user, userId);
                    resultList.add(user);
                } catch (final Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
        }
        return resultList;
    }

    /**
     * Sets email for the specified user.
     *
     * @param user The user.
     * @param userId The user ID.
     */
    public void selectEmailForUser(final User user, final int userId) {
        ResultSet rs = null;
        PreparedStatement ps = null;

        try {
            ps = _connection.prepareStatement(
                "SELECT users.user_id, user_data.attribute_value " +
                "FROM users, user_data, user_data_attrib " +
                "WHERE users.user_id = user_data.user_id " +
                "AND user_data.attribute_id = user_data_attrib.attribute_id " +
                "AND user_data_attrib.display_name = 'Email' " +
                "AND users.user_id = ?");
            ps.setInt(1, userId);
            rs = ps.executeQuery();

            if (rs.next()) {
                final String email = rs.getString("attribute_value");
                user.email(email);
                require().toBeFalse(rs.next());
            } else {
                System.err.println("user "+userId+" has no email.");
            }
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
        }
    }

    public void selectRolesForUser(final User user, final int userId) {
        ResultSet rs = null;
        PreparedStatement ps = null;

        try {
            ps = _connection.prepareStatement(
                "SELECT DISTINCT users.user_id, " +
                    "profiles.application_name, " +
                    "profiles.profile_name " +
                "FROM users, user_profiles, profiles " +
                "WHERE users.user_id = user_profiles.user_id " +
                "AND user_profiles.profile_id= profiles.profile_id " +
                "AND users.user_id = ?");
            ps.setInt(1, userId);
            rs = ps.executeQuery();

            while (rs.next()) {
                final String profile = rs.getString("profile_name");
                if ("Writer".equalsIgnoreCase(profile) ||
                        "Editor".equalsIgnoreCase(profile)) {
                    user.addRole(CreatorRoles.CONTENT_CREATOR);
                }
                else if ("Total Control".equalsIgnoreCase(profile)) {
                    user.addRole(CreatorRoles.SITE_BUILDER);
                    user.addRole(CreatorRoles.CONTENT_CREATOR);
                    user.addRole(CreatorRoles.ADMINISTRATOR);
                }
                else if ("Administrator".equalsIgnoreCase(profile)) {
                    user.addRole(CreatorRoles.ADMINISTRATOR);
                    user.addRole(CreatorRoles.CONTENT_CREATOR);
                }
            }

        } catch (final SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
        }
    }


}
