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
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ccc.domain.Alias;
import ccc.domain.File;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.Template;
import ccc.services.ActionDao;
import ccc.services.AuditLog;
import ccc.services.DataManager;
import ccc.services.FolderDao;
import ccc.services.ResourceDao;
import ccc.services.TemplateDao;
import ccc.services.UserManager;
import ccc.services.api.ActionSummary;
import ccc.services.api.AliasDelta;
import ccc.services.api.FileDelta;
import ccc.services.api.FileSummary;
import ccc.services.api.LogEntrySummary;
import ccc.services.api.PageDelta;
import ccc.services.api.Queries;
import ccc.services.api.ResourceDelta;
import ccc.services.api.ResourceSummary;
import ccc.services.api.TemplateDelta;
import ccc.services.api.UserDelta;
import ccc.services.api.UserSummary;
import ccc.services.ejb3.local.AuditLogEJB;
import ccc.services.ejb3.local.ResourceDaoImpl;
import ccc.services.ejb3.support.BaseDao;
import ccc.services.ejb3.support.Dao;
import ccc.services.support.ModelTranslation;


/**
 * EJB implementation of the {@link Queries} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=Queries.NAME)
@TransactionAttribute(REQUIRED)
@Remote(Queries.class)
@RolesAllowed({"ADMINISTRATOR", "CONTENT_CREATOR", "SITE_BUILDER"})
public final class QueriesEJB
    extends
        ModelTranslation
    implements
        Queries {

    @EJB(name=TemplateDao.NAME)    private TemplateDao     _templates;
    @EJB(name=FolderDao.NAME)      private FolderDao       _folders;
    @EJB(name=UserManager.NAME)    private UserManager     _users;
    @EJB(name=DataManager.NAME)    private DataManager     _datas;
    @EJB(name=ActionDao.NAME)      private ActionDao       _actions;
    @PersistenceContext private EntityManager _em;
    private ResourceDao     _resources;

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
        return mapUsers(_users.listUsersWithRole(role));
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
            delta(_users.find(UUID.fromString(userId)));
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

    /** {@inheritDoc} */
    @Override
    public FileDelta fileDelta(final String fileId) {
        return
            delta(_resources.find(File.class, UUID.fromString(fileId)));
    }

    /** {@inheritDoc} */
    @Override
    public Collection<FileSummary> getAllImages() {
        final List<File> list = _datas.findImages();
        return mapFiles(list);
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, String> metadata(final String resourceId) {
        final Resource r =
            _resources.find(Resource.class, UUID.fromString(resourceId));
        return r.metadata();
    }

    /** {@inheritDoc} */
    @Override
    public PageDelta workingCopyDelta(final String pageId) {
        final Page p = _resources.find(Page.class, UUID.fromString(pageId));
        return workingCopyDelta(p);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<ActionSummary> listPendingActions() {
        return map(_actions.pending());
    }

    /** {@inheritDoc} */
    @Override
    public Collection<ActionSummary> listCompletedActions() {
        return map(_actions.executed());
    }

    /** {@inheritDoc} */
    @Override
    public Collection<String> roles(final String resourceId) {
        final Resource r =
            _resources.find(Resource.class, UUID.fromString(resourceId));
        return r.roles();
    }


    @PostConstruct @SuppressWarnings("unused")
    private void configureCoreData() {
        final Dao bdao = new BaseDao(_em);
        final AuditLog audit = new AuditLogEJB(_em);
        _resources = new ResourceDaoImpl(_users, audit, bdao);
    }
}
