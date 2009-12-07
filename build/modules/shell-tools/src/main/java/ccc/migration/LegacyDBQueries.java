/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: See subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.migration;

import static ccc.types.DBC.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ccc.migration.ccc6.handlers.AllUsersSelector;
import ccc.migration.ccc6.handlers.FileSelector;
import ccc.migration.ccc6.handlers.FlaggedSelector;
import ccc.migration.ccc6.handlers.HomepageSelector;
import ccc.migration.ccc6.handlers.IsMajorEditSelector;
import ccc.migration.ccc6.handlers.LogEntryUserSelector;
import ccc.migration.ccc6.handlers.ParagraphSelector;
import ccc.migration.ccc6.handlers.ParagraphVersionsSelector;
import ccc.migration.ccc6.handlers.ResourceRolesSelector;
import ccc.migration.ccc6.handlers.ResourceSelector;
import ccc.migration.ccc6.handlers.ShowInMainMenuSelector;
import ccc.migration.ccc6.handlers.SingleResourceSelector;
import ccc.migration.ccc6.handlers.StyleSheetSelector;
import ccc.migration.ccc6.handlers.TemplateFieldsSelector;
import ccc.migration.ccc6.handlers.UserCommentSelector;
import ccc.migration.ccc6.handlers.UserMetadataSelector;
import ccc.migration.ccc6.handlers.UserRolesSelector;
import ccc.rest.dto.UserDto;

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
     * Returns a resource with legacy ID.
     *
     * @param i The legacy ID.
     * @return Resource found with query.
     */
    public ResourceBean selectSingleResource(final int i) {
        final SingleResourceSelector selector = new SingleResourceSelector();
        return _db.select(selector, i);
    }

    /**
     * Returns list of all paragraphs with active version for specified
     * page. Sequences are not joined.
     *
     * @param pageId The page's id.
     * @param version The page's version.
     *
     * @return A list of paragraph beans.
     */
    public List<ParagraphBean> selectParagraphs(final int pageId,
                                                final int version) {
        final ParagraphSelector rsh = new ParagraphSelector();
        return _db.select(rsh, pageId, version);
    }

    /**
     * Determine all the paragraph versions for a page.
     *
     * @param pageId The page's id.
     * @return The paragraph versions, as a list of integers.
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
    public Map<Integer, UserDto> selectUsers() {
        final AllUsersSelector rsh = new AllUsersSelector(this);
        return _db.select(rsh);
    }

    /**
     * Sets metadata for the specified user.
     *
     * @param userId The user ID.
     *
     * @return The user's email address, as a string.
     */
    public Map<String, String> selectMetadataForUser(final int userId) {
        final UserMetadataSelector rsh = new UserMetadataSelector();
        return _db.select(rsh, Integer.valueOf(userId));
    }

    /**
     * Sets roles for the specified user.
     *
     * @param userId The user ID.
     *
     * @return The user's roles, as a set of strings.
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
        return _db.select(rsh, resourceId, resourceId);
    }

    /**
     * Returns the log entry for an action.
     *
     * @param contentId Content id
     * @param legacyVersion Version id
     * @param action Action
     * @return A log entry bean representing the action.
     */
    public LogEntryBean selectUserFromLog(final int contentId,
                                          final int legacyVersion,
                                          final String action) {
        final LogEntryUserSelector rsh = new LogEntryUserSelector();
        return _db.select(rsh, contentId, legacyVersion, action);
    }

    /**
     * Returns all files from the legacy database.
     *
     * @return The files as FileDeltas.
     */
    public Map<String, LegacyFile> selectFiles() {
        final FileSelector query = new FileSelector();
        return _db.select(query, "FILE");
    }

    /**
     * Returns all images from the legacy database.
     *
     * @return The images as FileDeltas.
     */
    public Map<String, LegacyFile> selectImages() {
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

    /**
     * Return map containing legacy id and home page id of the folders.
     *
     * @return The map of IDs.
     */
    public Map<Integer, Integer> homepages() {
        final HomepageSelector query = new HomepageSelector();
        return _db.select(query);
    }

    /**
     * Return user comment left at the page edit.
     *
     * @param contentId The resource's id.
     * @param version The page's version.
     * @return The user comment.
     */
    public String selectUserComment(final int contentId, final int version) {
        final UserCommentSelector query = new UserCommentSelector();
        return _db.select(query, contentId, version);
    }

    /**
     * Return value of the c3_content.is_major_edit.
     *
     * @param contentId The resource's id.
     * @param version The page's version.
     * @return The boolean value of isMajorEdit.
     */
    public Boolean selectIsMajorEdit(final int contentId, final int version) {
        final IsMajorEditSelector query = new IsMajorEditSelector();
        return _db.select(query, contentId, version);
    }

    /**
     * Return a set of field names of the template.
     *
     * @param templateName The template name.
     * @return Set of string containing field names.
     */
    public Set<String> selectTemplateFields(final String templateName) {
        final TemplateFieldsSelector query = new TemplateFieldsSelector();
        return _db.select(query, templateName);
    }
}
