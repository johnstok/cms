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
package ccc.api.ejb3;

import static javax.ejb.TransactionAttributeType.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ccc.api.ActionSummary;
import ccc.api.AliasDelta;
import ccc.api.Duration;
import ccc.api.FileDelta;
import ccc.api.FileSummary;
import ccc.api.ID;
import ccc.api.LogEntrySummary;
import ccc.api.PageDelta;
import ccc.api.ResourceSummary;
import ccc.api.TemplateDelta;
import ccc.api.TemplateSummary;
import ccc.api.UserSummary;
import ccc.domain.Alias;
import ccc.domain.File;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.Resource;
import ccc.domain.ResourceOrder;
import ccc.domain.ResourcePath;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.entities.ResourceName;
import ccc.persistence.jpa.BaseDao;
import ccc.services.ActionDao;
import ccc.services.ModelTranslation;
import ccc.services.Queries;
import ccc.services.QueryNames;
import ccc.services.ResourceDao;
import ccc.services.UserLookup;
import ccc.services.UserManager;
import ccc.services.impl.ResourceDaoImpl;
import ccc.services.impl.UserManagerImpl;
import ccc.types.Username;


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

    @EJB(name=ActionDao.NAME)      private ActionDao       _actions;
    @PersistenceContext            private EntityManager   _em;
    @javax.annotation.Resource     private EJBContext      _context;

    private UserManager _users;
    private ResourceDao _resources;
    private UserLookup  _userLookup;
    private BaseDao     _bdao;

    /**
     * Constructor.
     */
    public QueriesEJB() { super(); }

    /** {@inheritDoc} */
    @Override
    public String getAbsolutePath(final ID resourceId) {
        return
            _resources.find(Resource.class, toUUID(resourceId))
                      .absolutePath()
                      .toString();
    }

    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> getChildren(final ID folderId) {
        final Folder f =
            _resources.find(Folder.class, toUUID(folderId));
        return mapResources(f != null ? f.entries() : new ArrayList<Resource>());
    }

    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> getChildrenManualOrder(final ID folderId) {
        final Folder f =
            _resources.find(Folder.class, toUUID(folderId));
        if (f != null) {
            f.sortOrder(ResourceOrder.MANUAL);
            return mapResources(f.entries());
        }
        return mapResources(new ArrayList<Resource>());
    }

    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> getFolderChildren(final ID folderId) {
        final Folder f =
            _resources.find(Folder.class, toUUID(folderId));
        return mapResources(f != null ? f.folders() : new ArrayList<Folder>());
    }

    /** {@inheritDoc} */
    @Override
    public Collection<LogEntrySummary> history(final ID resourceId) {
        return mapLogEntries(_resources.history(toUUID(resourceId)));
    }

    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> locked() {
        return mapResources(_resources.locked());
    }

    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> lockedByCurrentUser() {
        return mapResources(_resources.lockedByUser(currentUser()));
    }

    /** {@inheritDoc} */
    @Override
    public boolean nameExistsInFolder(final ID folderId, final String name) {
        // TODO handle null folderId? (for root folders)
        return
        _resources.find(Folder.class, toUUID(folderId))
                  .hasEntryWithName(new ResourceName(name));
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary resource(final ID resourceId) {
        return
            mapResource(_resources.find(Resource.class, toUUID(resourceId)));
    }

    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> roots() {
        return mapResources(_resources.list(QueryNames.ROOTS, Folder.class));
    }

    /** {@inheritDoc} */
    @Override
    public boolean templateNameExists(final String templateName) {
        return null!=_resources.find(
            QueryNames.TEMPLATE_BY_NAME,
            Template.class, new ResourceName(templateName));


    }

    /** {@inheritDoc} */
    @Override
    public Collection<TemplateSummary> templates() {
        return mapTemplates(_resources.list(
            QueryNames.ALL_TEMPLATES, Template.class));
    }



    /*
     * USER METHODS
     */
    /** {@inheritDoc} */
    @Override
    public boolean usernameExists(final Username username) {
        return _users.usernameExists(username.toString());
    }

    /** {@inheritDoc} */
    @Override
    public UserSummary loggedInUser() {
        return mapUser(currentUser());
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
    @Override public TemplateDelta templateDelta(final ID templateId) {
        return
            deltaTemplate(_resources.find(Template.class, toUUID(templateId)));
    }

    /** {@inheritDoc} */
    @Override
    public UserSummary userDelta(final ID userId) {
        return
            deltaUser(_users.find(toUUID(userId)));
    }

    /** {@inheritDoc} */
    @Override
    public AliasDelta aliasDelta(final ID aliasId) {
        return
            deltaAlias(_resources.find(Alias.class, toUUID(aliasId)));
    }

    /** {@inheritDoc} */
    @Override
    public PageDelta pageDelta(final ID pageId) {
        return
            deltaPage(_resources.find(Page.class, toUUID(pageId)));
    }

    /** {@inheritDoc} */
    @Override
    public FileDelta fileDelta(final ID fileId) {
        return
            deltaFile(_resources.find(File.class, toUUID(fileId)));
    }

    /** {@inheritDoc} */
    @Override
    public Collection<FileSummary> getAllContentImages() {
        final List<File> list = new ArrayList<File>();
        for (final File file : _bdao.list(QueryNames.ALL_IMAGES, File.class)) {
            if (PredefinedResourceNames.CONTENT.equals(
                file.root().name().toString())) {
                list.add(file);
            }
        }
        return mapFiles(list);
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, String> metadata(final ID resourceId) {
        final Resource r =
            _resources.find(Resource.class, toUUID(resourceId));
        return r.metadata();
    }

    /** {@inheritDoc} */
    @Override
    public Collection<ActionSummary> listPendingActions() {
        return mapActions(_actions.pending());
    }

    /** {@inheritDoc} */
    @Override
    public Collection<ActionSummary> listCompletedActions() {
        return mapActions(_actions.executed());
    }

    /** {@inheritDoc} */
    @Override
    public Collection<String> roles(final ID resourceId) {
        final Resource r =
            _resources.find(Resource.class, toUUID(resourceId));
        return r.roles();
    }

    /** {@inheritDoc} */
    @Override
    public Duration cacheDuration(final ID resourceId) {
        final Resource r =
            _resources.find(Resource.class, toUUID(resourceId));
        return r.cache();
    }

    @PostConstruct @SuppressWarnings("unused")
    private void configureCoreData() {
        _bdao = new BaseDao(_em);
        _resources = new ResourceDaoImpl(_bdao);
        _userLookup = new UserLookup(_bdao);
        _users = new UserManagerImpl(_bdao);
    }

    /** {@inheritDoc} */
    @Override
    public TemplateSummary computeTemplate(final ID resourceId) {
        final Resource r =
            _resources.find(Resource.class, toUUID(resourceId));
        return mapTemplate(r.computeTemplate(null));
    }

    private User currentUser() {
        return _userLookup.loggedInUser(_context.getCallerPrincipal());
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary resourceForPath(final String rootPath) {
        final ResourcePath rp = new ResourcePath(rootPath);
        return mapResource(
            _resources.lookup(rp.top().toString(), rp.removeTop()));
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary resourceForLegacyId(final String legacyId) {
        return mapResource(_resources.lookupWithLegacyId(legacyId));
    }
}
