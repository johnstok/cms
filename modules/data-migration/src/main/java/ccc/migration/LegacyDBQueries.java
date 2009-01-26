package ccc.migration;

import static ccc.commons.DBC.*;

import java.util.List;
import java.util.Map;

import ccc.migration.ccc6.handlers.FileSelector;
import ccc.migration.ccc6.handlers.ParagraphSelector;
import ccc.migration.ccc6.handlers.ResourceSelector;
import ccc.migration.ccc6.handlers.UserEmailSelector;
import ccc.migration.ccc6.handlers.UserRolesSelector;
import ccc.migration.ccc6.handlers.UserSelector;
import ccc.migration.ccc6.handlers.UserSelector2;
import ccc.services.api.FileDelta;
import ccc.services.api.UserDelta;

/**
 * Queries for data migration.
 *
 * @author Civic Computing Ltd
 */
public class LegacyDBQueries {

    private final DB _db;

    /**
     * Constructor.
     *
     * @param db The database to query.
     */
    public LegacyDBQueries(final DB db) {
        require().notNull(db);
        _db = db;
    }

    /**
     * Returns list of all resources with active version for specified
     * parent.
     * @param i folder parent id
     *
     * @return list of resources found with query
     */
    public List<ResourceBean> selectResources(final int i) {
        final ResourceSelector rsh = new ResourceSelector();
        return _db.select(rsh, i);
    }

    /**
     * Returns list of all paragraphs with active version for specified
     * page. Sequences are not joined.
     *
     * @param pageId pageId
     * @return list of paragraphs found with query
     */
    public List<ParagraphBean> selectParagraphs(final int pageId) {
        final ParagraphSelector rsh = new ParagraphSelector();
        return _db.select(rsh, pageId);
    }

    /**
     * Returns a list of users.
     * @param errors
     *
     * @return The list of users.
     */
    public Map<Integer, UserDelta> selectUsers() {
        final UserSelector2 rsh = new UserSelector2(this);
        return _db.select(rsh);
    }

    /**
     * Sets email for the specified user.
     *
     * @param user The user.
     * @param userId The user ID.
     */
    public void selectEmailForUser(final UserDelta user, final int userId) {
        final UserEmailSelector rsh = new UserEmailSelector(userId, user);
        _db.select(rsh, userId);
    }

    /**
     * Sets roles for the specified user.
     *
     * @param user The user.
     * @param userId The user ID.
     */
    public void selectRolesForUser(final UserDelta user, final int userId) {
        final UserRolesSelector rsh = new UserRolesSelector(user);
        _db.select(rsh, userId);
    }

    /**
     * Returns legacy user id from c3_version_audit_log.
     *
     * @param contentId Content id
     * @param legacyVersion Version id
     * @param action Action
     * @param comment Version comment
     * @return User id as an Integer, null if no user id is found.
     */
    public Integer selectUserFromLog(final int contentId,
                                     final int legacyVersion,
                                     final String action,
                                     final String comment) {
        final UserSelector rsh = new UserSelector(legacyVersion, contentId);
        return _db.select(rsh, contentId, legacyVersion, action, comment);
    }

    /**
     * Returns all files from the legacy database.
     *
     * @return The list of files as FileDeltas.
     */
    public List<FileDelta> selectFiles() {
        final FileSelector query = new FileSelector();
        return _db.select(query, "FILE");
    }

    /**
     * Returns all images from the legacy database.
     *
     * @return The list of images as FileDeltas.
     */
    public List<FileDelta> selectImages() {
        final FileSelector query = new FileSelector();
        return _db.select(query, "IMAGE");
    }
}
