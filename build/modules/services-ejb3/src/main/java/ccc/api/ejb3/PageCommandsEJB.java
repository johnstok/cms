/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBContext;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ccc.api.PageDelta;
import ccc.api.ResourceSummary;
import ccc.commands.ClearWorkingCopyCommand;
import ccc.commands.CreatePageCommand;
import ccc.commands.PublishCommand;
import ccc.commands.UpdatePageCommand;
import ccc.commands.UpdateWorkingCopyCommand;
import ccc.domain.CccCheckedException;
import ccc.domain.CommandFailedException;
import ccc.domain.Page;
import ccc.domain.User;
import ccc.persistence.AuditLog;
import ccc.persistence.PageCommands;
import ccc.persistence.UserLookup;
import ccc.persistence.jpa.JpaRepository;
import ccc.services.impl.AuditLogImpl;
import ccc.types.ID;
import ccc.types.ResourceName;


/**
 * EJB implementation of the {@link PageCommands} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=PageCommands.NAME)
@TransactionAttribute(REQUIRES_NEW)
@Remote(PageCommands.class)
@RolesAllowed({}) // "ADMINISTRATOR", "CONTENT_CREATOR", "SITE_BUILDER"
public class PageCommandsEJB extends
BaseCommands
implements
    PageCommands {

    @javax.annotation.Resource private EJBContext _context;
    @PersistenceContext private EntityManager _em;

    private AuditLog           _audit;

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"ADMINISTRATOR"})
    public ResourceSummary createPage(final ID parentId,
                                      final PageDelta delta,
                                      final String name,
                                      final boolean publish,
                                      final ID templateId,
                                      final String title,
                                      final ID actorId,
                                      final Date happenedOn,
                                      final String comment,
                                      final boolean majorChange)
                                                 throws CommandFailedException {
        try {
            final User u = userForId(actorId);

            final Page p = new CreatePageCommand(_bdao, _audit).execute(
                u,
                happenedOn,
                toUUID(parentId),
                delta,
                ResourceName.escape(name),
                title,
                toUUID(templateId),
                comment,
                majorChange);

            if (publish) {
                p.lock(u);
                new PublishCommand(_audit).execute(happenedOn, u, p);
                p.unlock(u);
            }

            return mapResource(p);

        } catch (final CccCheckedException e) {
            throw fail(_context ,e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
    public ResourceSummary createPage(final ID parentId,
                                      final PageDelta delta,
                                      final String name,
                                      final boolean publish,
                                      final ID templateId,
                                      final String title,
                                      final String comment,
                                      final boolean majorChange)
                                                 throws CommandFailedException {
        return createPage(
            parentId,
            delta,
            name,
            publish,
            templateId,
            title,
            loggedInUserId(_context),
            new Date(),
            comment,
            majorChange);
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
    public void updatePage(final ID pageId,
                           final PageDelta delta,
                           final String comment,
                           final boolean isMajorEdit)
                                                 throws CommandFailedException {
        updatePage(
            pageId, delta, comment, isMajorEdit, loggedInUserId(_context), new Date());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"ADMINISTRATOR"})
    public void updatePage(final ID pageId,
                           final PageDelta delta,
                           final String comment,
                           final boolean isMajorEdit,
                           final ID actorId,
                           final Date happenedOn)
                                                 throws CommandFailedException {
        try {
            new UpdatePageCommand(_bdao, _audit).execute(
                userForId(actorId),
                happenedOn,
                toUUID(pageId),
                delta,
                comment,
                isMajorEdit);

        } catch (final CccCheckedException e) {
            throw fail(_context, e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
    public void updateWorkingCopy(final ID pageId,
                                  final PageDelta delta)
                                                 throws CommandFailedException {
        try {
            new UpdateWorkingCopyCommand(_bdao, _audit).execute(
                loggedInUser(_context),
                new Date(),
                toUUID(pageId),
                delta);

        } catch (final CccCheckedException e) {
            throw fail(_context, e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"CONTENT_CREATOR"})
    public void clearWorkingCopy(final ID resourceId)
                                                 throws CommandFailedException {
        try {
            new ClearWorkingCopyCommand(_bdao, _audit).execute(
                loggedInUser(_context), new Date(), toUUID(resourceId));

        } catch (final CccCheckedException e) {
            throw fail(_context, e);
        }
    }

    /* ==============
     * Helper methods
     * ============== */
    @PostConstruct @SuppressWarnings("unused")
    private void configureCoreData() {
        _bdao = new JpaRepository(_em);
        _audit = new AuditLogImpl(_bdao);
        _userLookup = new UserLookup(_bdao);
    }

}
