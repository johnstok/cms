/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.services.ejb3.remote;

import static javax.ejb.TransactionAttributeType.*;

import java.util.Collection;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.domain.Alias;
import ccc.domain.CreatorRoles;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.services.FolderDao;
import ccc.services.ResourceDao;
import ccc.services.TemplateDao;
import ccc.services.UserManager;
import ccc.services.api.AliasDelta;
import ccc.services.api.LogEntrySummary;
import ccc.services.api.PageDelta;
import ccc.services.api.Queries;
import ccc.services.api.ResourceDelta;
import ccc.services.api.ResourceSummary;
import ccc.services.api.TemplateDelta;
import ccc.services.api.UserDelta;
import ccc.services.api.UserSummary;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name="PublicQueries")
@TransactionAttribute(REQUIRED)
@Remote(Queries.class)
public final class QueriesEJB
    extends
        ModelTranslation
    implements
        Queries {

    @EJB(name="TemplateDao")    private TemplateDao     _templates;
    @EJB(name="FolderDao")      private FolderDao       _folders;
    @EJB(name="UserManager")    private UserManager      _users;
    @EJB(name="ResourceDao")    private ResourceDao _resources;

    /**
     * Constructor.
     */
    @SuppressWarnings("unused") public QueriesEJB() { super(); }

    /** {@inheritDoc} */
    @Override
    public String getAbsolutePath(final String resourceId) {
        return
            _resources.find(Resource.class, UUID.fromString(resourceId))
                      .absolutePath()
                      .toString();
    }

    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> getChildren(final String folderId) {
        final Folder f =
            _resources.find(Folder.class, UUID.fromString(folderId));
        return mapResources(f.entries());
    }

    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> getFolderChildren(
                                                        final String folderId) {
        final Folder f =
            _resources.find(Folder.class, UUID.fromString(folderId));
        return mapFolders(f.folders());
    }

    /** {@inheritDoc} */
    @Override
    public TemplateDelta getTemplateForResource(final String resourceId) {
        return delta(
            _resources.find(Resource.class, UUID.fromString(resourceId))
                      .computeTemplate(null));
    }

    /** {@inheritDoc} */
    @Override
    public Collection<LogEntrySummary> history(final String resourceId) {
        return mapLogEntries(_resources.history(resourceId));
    }

    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> locked() {
        return mapResources(_resources.locked());
    }

    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> lockedByCurrentUser() {
        return mapResources(_resources.lockedByCurrentUser());
    }

    /** {@inheritDoc} */
    @Override
    public boolean nameExistsInFolder(final String folderId,
                                      final String name) {
        // TODO handle null folderId? (for root folders)
        return
        _resources.find(Folder.class, UUID.fromString(folderId))
                  .hasEntryWithName(new ResourceName(name));
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary resource(final String resourceId) {
        return
            map(_resources.find(Resource.class, UUID.fromString(resourceId)));
    }

    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> roots() {
        return mapFolders(_folders.roots());
    }

    /** {@inheritDoc} */
    @Override
    public boolean templateNameExists(final String templateName) {
        return _templates.nameExists(new ResourceName(templateName));


    }

    /** {@inheritDoc} */
    @Override
    public Collection<TemplateDelta> templates() {
        return deltaTemplates(_templates.allTemplates());
    }

    /*
     * USER METHODS
     */
    /** {@inheritDoc} */
    @Override
    public boolean usernameExists(final String username) {
        return _users.usernameExists(username);
    }

    /** {@inheritDoc} */
    @Override
    public UserSummary loggedInUser() {
        return map(_users.loggedInUser());
    }

    /** {@inheritDoc} */
    @Override
    public Collection<UserSummary> listUsers() {
        return mapUsers(_users.listUsers());
    }

    /** {@inheritDoc} */
    @Override
    public Collection<UserSummary> listUsersWithEmail(final String email) {
        return mapUsers(_users.listUsersWithEmail(email));
    }

    /** {@inheritDoc} */
    @Override
    public Collection<UserSummary> listUsersWithRole(final String role) {
        return mapUsers(_users.listUsersWithRole(CreatorRoles.valueOf(role)));
    }

    /** {@inheritDoc} */
    @Override
    public Collection<UserSummary> listUsersWithUsername(
                                                    final String username) {
        return mapUsers(_users.listUsersWithUsername(username));
    }

    /** {@inheritDoc} */
    @Override public TemplateDelta templateDelta(final String templateId) {
        return
            delta(_resources.find(Template.class, UUID.fromString(templateId)));
    }

    /** {@inheritDoc} */
    @Override
    public UserDelta userDelta(final String userId) {
        return
            delta(_resources.find(User.class, UUID.fromString(userId)));
    }

    /** {@inheritDoc} */
    @Override
    public AliasDelta aliasDelta(final String aliasId) {
        return
            delta(_resources.find(Alias.class, UUID.fromString(aliasId)));
    }

    /** {@inheritDoc} */
    @Override
    public PageDelta pageDelta(final String pageId) {
        return
            delta(_resources.find(Page.class, UUID.fromString(pageId)));
    }

    /** {@inheritDoc} */
    @Override
    public ResourceDelta folderDelta(final String folderId) {
        return
            delta(_resources.find(Folder.class, UUID.fromString(folderId)));
    }

    /** {@inheritDoc} */
    @Override
    public ResourceDelta resourceDelta(final String resourceId) {
        return
        delta(_resources.find(Resource.class, UUID.fromString(resourceId)));
    }
}
