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
import ccc.domain.Template;
import ccc.domain.User;
import ccc.persistence.QueryNames;
import ccc.persistence.ResourceRepository;
import ccc.persistence.ResourceRepositoryImpl;
import ccc.persistence.UserRepository;
import ccc.persistence.UserRepositoryImpl;
import ccc.persistence.jpa.JpaRepository;
import ccc.rest.Queries;
import ccc.rest.dto.TemplateDelta;
import ccc.rest.dto.TemplateSummary;
import ccc.types.ResourceName;


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

    @PostConstruct @SuppressWarnings("unused")
    private void configureCoreData() {
        _bdao = new JpaRepository(_em);
        _resources = new ResourceRepositoryImpl(_bdao);
        _users = new UserRepositoryImpl(_bdao);
    }

    private User currentUser() {
        return _users.loggedInUser(_context.getCallerPrincipal());
    }
}
