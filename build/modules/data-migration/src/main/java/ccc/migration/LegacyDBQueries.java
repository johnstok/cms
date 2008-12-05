package ccc.migration;

import static ccc.commons.DBC.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import ccc.commons.EmailAddress;
import ccc.domain.CreatorRoles;
import ccc.domain.User;

/**
 * Queries for data migration.
 *
 * @author Civic Computing Ltd
 */
public class LegacyDBQueries {

    private static Logger log =
        Logger.getLogger(LegacyDBQueries.class);

    private final Connection _connection;

    /**
     * Constructor.
     *
     * @param conn Connection
     */
    public LegacyDBQueries(final Connection conn) {
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
                "SELECT content_id, content_type, name, page, "
                + "status, version_id "
                + "FROM "
                + "c3_content, c3_display_templates WHERE "
                + "c3_content.parent_id = ? AND version_id = 0 AND "
                + "(status = 'PUBLISHED' OR status = 'NEW') AND "
                + "c3_content.display_template_id = "
                + "c3_display_templates.template_id(+)");
            ps.setInt(1, i);
            rs = ps.executeQuery();

            while (rs.next()) {
                final int contentId = rs.getInt("CONTENT_ID");
                final String type = rs.getString("CONTENT_TYPE");
                final String name = rs.getString("NAME");
                final String displayTemplate = rs.getString("PAGE");
                final boolean published =
                    "PUBLISHED".equals(rs.getString("STATUS"));
                final int legacyVersion = rs.getInt("VERSION_ID");

                resultList.add(new ResourceBean(contentId,
                                                type,
                                                name,
                                                displayTemplate,
                                                published,
                                                legacyVersion));
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
                "SELECT * FROM  c3_paragraphs "
                + "WHERE c3_paragraphs.page_id = ? "
                + "AND version_id = 0 ORDER BY seq");
            ps.setInt(1, pageId);
            rs = ps.executeQuery();

            // populate map
            while (rs.next()) {
                final String key = rs.getString("para_type");
                final String text = rs.getString("text");
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
    public List<UserBean> selectUsers() {
        ResultSet rs = null;
        PreparedStatement ps = null;
        final List<UserBean> resultList = new ArrayList<UserBean>();

        try {
            ps = _connection.prepareStatement(
                "SELECT user_id, user_name, user_passwd, name FROM users");
            rs = ps.executeQuery();

            while (rs.next()) {
                final String userName = rs.getString("user_name");
                final String password = rs.getString("user_passwd");
                final int userId = rs.getInt("user_id");
                try {
                    final User user = new User(userName);
                    selectEmailForUser(user, userId);
                    selectRolesForUser(user, userId);
                    final UserBean mu = new UserBean(user, password, userId);
                    resultList.add(mu);
                } catch (final Exception e) {
                    log.error(e.getMessage());
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
                "SELECT users.user_id, user_data.attribute_value "
                + "FROM users, user_data, user_data_attrib "
                + "WHERE users.user_id = user_data.user_id "
                + "AND user_data.attribute_id = user_data_attrib.attribute_id "
                + "AND user_data_attrib.display_name = 'Email' "
                + "AND users.user_id = ?");
            ps.setInt(1, userId);
            rs = ps.executeQuery();

            if (rs.next()) {
                final String email = rs.getString("attribute_value");
                user.email(new EmailAddress(email));
                require().toBeFalse(rs.next());
            } else {
                throw new RuntimeException("User "+userId+" has no email.");
            }
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
        }
    }

    /**
     * Sets roles for the specified user.
     *
     * @param user The user.
     * @param userId The user ID.
     */
    public void selectRolesForUser(final User user, final int userId) {
        ResultSet rs = null;
        PreparedStatement ps = null;

        try {
            ps = _connection.prepareStatement(
                "SELECT DISTINCT users.user_id, "
                    + "profiles.application_name, "
                    + "profiles.profile_name "
                + "FROM users, user_profiles, profiles "
                + "WHERE users.user_id = user_profiles.user_id "
                + "AND user_profiles.profile_id= profiles.profile_id "
                + "AND users.user_id = ?");
            ps.setInt(1, userId);
            rs = ps.executeQuery();

            while (rs.next()) {
                final String profile = rs.getString("profile_name");
                if ("Writer".equalsIgnoreCase(profile)
                        || "Editor".equalsIgnoreCase(profile)) {
                    user.addRole(CreatorRoles.CONTENT_CREATOR);
                } else if ("Total Control".equalsIgnoreCase(profile)) {
                    user.addRole(CreatorRoles.SITE_BUILDER);
                    user.addRole(CreatorRoles.CONTENT_CREATOR);
                    user.addRole(CreatorRoles.ADMINISTRATOR);
                } else if ("Administrator".equalsIgnoreCase(profile)) {
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

    /**
     * Returns legacy user id from c3_version_audit_log.
     *
     * @param contentId Content id
     * @param legacyVersion Version id
     * @param action Action
     * @param versionComment Version comment
     * @return User id as an Integer, null if no user id is found.
     */
    public Integer selectUserFromLog(final int contentId,
                                     final int legacyVersion,
                                     final String action,
                                     final String versionComment) {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Integer userId = null;

        try {
            ps = _connection.prepareStatement(
                "SELECT user_id FROM c3_version_audit_log "
                + "WHERE content_id = ? AND "
                + "version_id = ? AND "
                + "action = ? AND "
                + "version_comment = ?");
            ps.setInt(1, contentId);
            ps.setInt(2, legacyVersion);
            ps.setString(3, action);
            ps.setString(4, versionComment);
            rs = ps.executeQuery();

            if (rs.next()) {
                log.debug("FOUND "+contentId +" "+legacyVersion);
                userId = rs.getInt("user_id");
                require().toBeFalse(rs.next());
            } else {
                log.error("User Id NOT FOUND with content_id: "+contentId
                    +" version_id"+legacyVersion);
            }
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
        }
        return userId;
    }
}
