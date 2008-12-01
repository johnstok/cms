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

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.domain.Folder;
import ccc.services.QueryManagerLocal;
import ccc.services.api.FolderSummary;
import ccc.services.api.LogEntrySummary;
import ccc.services.api.Queries;
import ccc.services.api.ResourceSummary;
import ccc.services.api.TemplateSummary;
import ccc.services.api.UserSummary;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name="PublicQueries")
@TransactionAttribute(REQUIRED)
@Remote(Queries.class)
public class QueriesEJB
    extends
        ModelTransalation
    implements
        Queries {

    @EJB(name="QueryManager", beanInterface=QueryManagerLocal.class)
    private QueryManagerLocal _qm;

    /**
     * Constructor.
     */
    @SuppressWarnings("unused") private QueriesEJB() { super(); }

    /** {@inheritDoc} */
    @Override
    public String getAbsolutePath(final String resourceId) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> getChildren(final String folderId) {
        final Folder f = _qm.find(Folder.class, folderId);
        return mapResources(f.entries());
    }

    /** {@inheritDoc} */
    @Override
    public Collection<FolderSummary> getFolderChildren(final String folderId) {
        final Folder f = _qm.find(Folder.class, folderId);
        return mapFolders(f.folders());
    }

    /** {@inheritDoc} */
    @Override
    public TemplateSummary getTemplateForResource(final String resourceId) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public Collection<LogEntrySummary> history(final String resourceId) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public Collection<UserSummary> listUsers() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public Collection<UserSummary> listUsersWithEmail(final String email) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public Collection<UserSummary> listUsersWithRole(final String role) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public Collection<UserSummary> listUsersWithUsername(
                                                    final String username) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> locked() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> lockedByCurrentUser() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public UserSummary loggedInUser() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public boolean nameExistsInFolder(final String folderId,
                                      final String name) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public boolean nameExistsInParentFolder(final String id,
                                            final String name) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary resource(final String resourceId) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public Collection<FolderSummary> roots() {
        return mapFolders(_qm.list("roots", Folder.class));
    }

    /** {@inheritDoc} */
    @Override
    public boolean templateNameExists(final String templateName) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public Collection<TemplateSummary> templates() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public boolean usernameExists(final String username) {
        throw new UnsupportedOperationException("Method not implemented.");
    }
}
