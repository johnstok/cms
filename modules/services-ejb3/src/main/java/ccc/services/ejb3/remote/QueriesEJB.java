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

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ccc.domain.Alias;
import ccc.domain.CreatorRoles;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.services.AssetManagerLocal;
import ccc.services.QueryManagerLocal;
import ccc.services.ResourceDAOLocal;
import ccc.services.UserManagerLocal;
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

    @PersistenceContext(unitName = "ccc-persistence")
    private EntityManager _entityManager;

    @EJB(name="QueryManager", beanInterface=QueryManagerLocal.class)
    private QueryManagerLocal _qm;
    @EJB(name="UserManager", beanInterface=UserManagerLocal.class)
    private UserManagerLocal _users;
    @EJB(name="AssetManager", beanInterface=AssetManagerLocal.class)
    private AssetManagerLocal _assets;
    @EJB(name="ResourceDAO", beanInterface=ResourceDAOLocal.class)
    private ResourceDAOLocal _resources;

    /**
     * Constructor.
     */
    @SuppressWarnings("unused") private QueriesEJB() { super(); }

    /** {@inheritDoc} */
    @Override
    public String getAbsolutePath(final String resourceId) {
        return _qm.find(Resource.class, resourceId).absolutePath().toString();
    }

    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> getChildren(final String folderId) {
        final Folder f = _qm.find(Folder.class, folderId);
        return mapResources(f.entries());
    }

    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> getFolderChildren(final String folderId) {
        final Folder f = _qm.find(Folder.class, folderId);
        return mapFolders(f.folders());
    }

    /** {@inheritDoc} */
    @Override
    public TemplateDelta getTemplateForResource(final String resourceId) {
        return delta(
            _qm.find(Resource.class, resourceId).displayTemplateName());
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
        return
            _qm.find(Folder.class, folderId)
            .hasEntryWithName(new ResourceName(name));
    }

    /** {@inheritDoc} */
    @Override
    public boolean nameExistsInParentFolder(final String id,
                                            final String name) {
        return
            _qm.find(Folder.class, id).parent()
            .hasEntryWithName(new ResourceName(name));
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary resource(final String resourceId) {
        return map(_qm.find(Resource.class, resourceId));
    }

    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> roots() {
        final List<Folder> roots = _qm.list("roots", Folder.class);
        return mapFolders(roots);
    }

    /** {@inheritDoc} */
    @Override
    public boolean templateNameExists(final String templateName) {
        final List<Template> templates =
            _qm.list("templateByName",
                     Template.class,
                     new ResourceName(templateName));
        return templates.size() > 0;

    }

    /** {@inheritDoc} */
    @Override
    public Collection<TemplateDelta> templates() {
        return deltaTemplates(_assets.lookupTemplates());
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
        return delta(_qm.find(Template.class, templateId));
    }

    /** {@inheritDoc} */
    @Override
    public UserDelta userDelta(final String userId) {
        return delta(_qm.find(User.class, userId));
    }

    /** {@inheritDoc} */
    @Override
    public AliasDelta aliasDelta(final String aliasId) {
        return delta(_qm.find(Alias.class, aliasId));
    }

    /** {@inheritDoc} */
    @Override
    public PageDelta pageDelta(final String pageId) {
        return delta(_qm.find(Page.class, pageId));
    }

    /** {@inheritDoc} */
    @Override
    public ResourceDelta folderDelta(final String folderId) {
        return delta(_qm.find(Folder.class, folderId));
    }
}
