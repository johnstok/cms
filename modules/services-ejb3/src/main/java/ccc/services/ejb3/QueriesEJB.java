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
package ccc.services.ejb3;

import static ccc.types.CreatorRoles.*;
import static javax.ejb.TransactionAttributeType.*;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBContext;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ccc.domain.Alias;
import ccc.domain.Resource;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.persistence.QueryNames;
import ccc.persistence.ResourceRepository;
import ccc.persistence.ResourceRepositoryImpl;
import ccc.persistence.UserRepository;
import ccc.persistence.UserRepositoryImpl;
import ccc.persistence.jpa.JpaRepository;
import ccc.rest.Queries;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.RevisionDto;
import ccc.rest.dto.TemplateDelta;
import ccc.rest.dto.TemplateSummary;
import ccc.types.Duration;
import ccc.types.ResourceName;
import ccc.types.ResourcePath;


/**
 * EJB implementation of the {@link Queries} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=Queries.NAME)
@TransactionAttribute(REQUIRED)
@Remote(Queries.class)
@RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
public final class QueriesEJB
    extends
        ModelTranslation
    implements
        Queries {

    @PersistenceContext            private EntityManager   _em;
    @javax.annotation.Resource     private EJBContext      _context;

    private UserRepository _users;
    private ResourceRepository _resources;
    private JpaRepository     _bdao;

    /**
     * Constructor.
     */
    public QueriesEJB() { super(); }

    /** {@inheritDoc} */
    @Override
    public String getAbsolutePath(final UUID resourceId) {
        return
            _resources.find(Resource.class, resourceId)
                      .absolutePath()
                      .toString();
    }

    /** {@inheritDoc} */
    @Override
    public Collection<RevisionDto> history(final UUID resourceId) {
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
        return mapResources(_resources.lockedByUser(currentUser()));
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary resource(final UUID resourceId) {
        return
            mapResource(_resources.find(Resource.class, resourceId));
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

    /** {@inheritDoc} */
    @Override public TemplateDelta templateDelta(final UUID templateId) {
        return
            deltaTemplate(_resources.find(Template.class, templateId));
    }

    /** {@inheritDoc} */
    @Override
    public String aliasTargetName(final UUID aliasId) {
        final Alias alias = _resources.find(Alias.class, aliasId);
        if (alias != null) {
            return alias.target().name().toString();
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, String> metadata(final UUID resourceId) {
        final Resource r =
            _resources.find(Resource.class, resourceId);
        return r.metadata();
    }

    /** {@inheritDoc} */
    @Override
    public Collection<String> roles(final UUID resourceId) {
        final Resource r =
            _resources.find(Resource.class, resourceId);
        return r.roles();
    }

    /** {@inheritDoc} */
    @Override
    public Duration cacheDuration(final UUID resourceId) {
        final Resource r =
            _resources.find(Resource.class, resourceId);
        return r.cache();
    }

    @PostConstruct @SuppressWarnings("unused")
    private void configureCoreData() {
        _bdao = new JpaRepository(_em);
        _resources = new ResourceRepositoryImpl(_bdao);
        _users = new UserRepositoryImpl(_bdao);
    }

    /** {@inheritDoc} */
    @Override
    public TemplateSummary computeTemplate(final UUID resourceId) {
        final Resource r =
            _resources.find(Resource.class, resourceId);
        return mapTemplate(r.computeTemplate(null));
    }

    private User currentUser() {
        return _users.loggedInUser(_context.getCallerPrincipal());
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
