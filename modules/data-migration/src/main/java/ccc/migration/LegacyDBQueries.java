package ccc.migration;

import static ccc.commons.DBC.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ccc.migration.ccc6.handlers.AllUsersSelector;
import ccc.migration.ccc6.handlers.FileSelector;
import ccc.migration.ccc6.handlers.FlaggedSelector;
import ccc.migration.ccc6.handlers.LogEntryUserSelector;
import ccc.migration.ccc6.handlers.ParagraphSelector;
import ccc.migration.ccc6.handlers.ParagraphVersionsSelector;
import ccc.migration.ccc6.handlers.ResourceRolesSelector;
import ccc.migration.ccc6.handlers.ResourceSelector;
import ccc.migration.ccc6.handlers.ShowInMainMenuSelector;
import ccc.migration.ccc6.handlers.StyleSheetSelector;
import ccc.migration.ccc6.handlers.UserEmailSelector;
import ccc.migration.ccc6.handlers.UserRolesSelector;

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
    public List<ParagraphBean> selectParagraphs(final int pageId,
                                                final int version) {
        final ParagraphSelector rsh = new ParagraphSelector();
        return _db.select(rsh, pageId, version);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param pageId
     * @return
     */
    public List<Integer> selectParagraphVersions(final int pageId) {
        final ParagraphVersionsSelector rsh = new ParagraphVersionsSelector();
        return _db.select(rsh, pageId);
    }

    /**
     * Returns a list of users.
     * @param errors
     *
     * @return The list of users.
     */
    public Map<Integer, ExistingUser> selectUsers() {
        final AllUsersSelector rsh = new AllUsersSelector(this);
        return _db.select(rsh);
    }

    /**
     * Sets email for the specified user.
     *
     * @param userId The user ID.
     */
    public String selectEmailForUser(final int userId) {
        final UserEmailSelector rsh = new UserEmailSelector(userId);
        return _db.select(rsh, Integer.valueOf(userId));
    }

    /**
     * Sets roles for the specified user.
     *
     * @param userId The user ID.
     */
    public Set<String> selectRolesForUser(final int userId) {
        final UserRolesSelector rsh = new UserRolesSelector();
        return _db.select(rsh, Integer.valueOf(userId));
    }

    /**
     * Returns the roles for a specified resource.
     *
     * @param resourceId The resource ID.
     * @return The resource's roles.
     */
    public Collection<String> selectRolesForResource(final int resourceId) {
        final ResourceRolesSelector rsh = new ResourceRolesSelector();
        return _db.select(rsh, resourceId);
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
        final LogEntryUserSelector rsh = new LogEntryUserSelector();
        return _db.select(rsh, contentId, legacyVersion, action, comment);
    }

    /**
     * Returns all files from the legacy database.
     *
     * @return The files as FileDeltas.
     */
    public Map<String,LegacyFile> selectFiles() {
        final FileSelector query = new FileSelector();
        return _db.select(query, "FILE");
    }

    /**
     * Returns all images from the legacy database.
     *
     * @return The images as FileDeltas.
     */
    public Map<String,LegacyFile> selectImages() {
        final FileSelector query = new FileSelector();
        return _db.select(query, "IMAGE");
    }

    /**
     * Returns style sheet assigned to the resource.
     *
     * @param contentId Content id
     * @return The style sheet value stored in C3_SCRIPTS table
     */
    public String selectStyleSheet(final int contentId) {
        final StyleSheetSelector query = new StyleSheetSelector();
        return _db.select(query, contentId, contentId);
    }

    /**
     * Retrieve a list of all legacy pages that are shown in a menu.
     *
     * @return The list of page IDs as a set.
     */
    public Set<Integer> selectMenuItems() {
        final ShowInMainMenuSelector query = new ShowInMainMenuSelector();
        return _db.select(query);
    }

    /**
     * Return the flagged value for a resource.
     *
     * @param contentId The resource's id.
     * @return "Y" if the resource is flagged, any other value indicates false.
     */
    public String selectFlagged(final int contentId) {
        final FlaggedSelector query = new FlaggedSelector();
        return _db.select(query, contentId);
    }
}
