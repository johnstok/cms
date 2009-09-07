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
package ccc.services.ejb3;

import static ccc.types.CreatorRoles.*;
import static javax.ejb.TransactionAttributeType.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBContext;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ccc.commands.CreatePageCommand;
import ccc.commands.PublishCommand;
import ccc.commands.UpdatePageCommand;
import ccc.commands.UpdateWorkingCopyCommand;
import ccc.domain.CccCheckedException;
import ccc.domain.Page;
import ccc.domain.PageHelper;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.LogEntryRepositoryImpl;
import ccc.persistence.ResourceRepositoryImpl;
import ccc.persistence.UserRepositoryImpl;
import ccc.persistence.jpa.JpaRepository;
import ccc.rest.CommandFailedException;
import ccc.rest.dto.PageDelta;
import ccc.rest.dto.PageDto;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.extensions.PagesExt;
import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.types.Paragraph;
import ccc.types.ResourceName;


/**
 * EJB implementation of the {@link PagesExt} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=PagesExt.NAME)
@TransactionAttribute(REQUIRES_NEW)
@Remote(PagesExt.class)
@RolesAllowed({})
public class PagesEJB extends
BaseCommands
implements
    PagesExt {

    @javax.annotation.Resource private EJBContext _context;
    @PersistenceContext private EntityManager _em;
    private LogEntryRepository           _audit;


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR})
    public ResourceSummary createPage(final UUID parentId,
                                      final PageDelta delta,
                                      final String name,
                                      final boolean publish,
                                      final UUID templateId,
                                      final String title,
                                      final UUID actorId,
                                      final Date happenedOn,
                                      final String comment,
                                      final boolean majorChange)
                                                 throws CommandFailedException {
        try {
            final User u = userForId(actorId);

            final Page p = new CreatePageCommand(_bdao, _audit).execute(
                u,
                happenedOn,
                parentId,
                delta,
                ResourceName.escape(name),
                title,
                templateId,
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
    @RolesAllowed({CONTENT_CREATOR})
    public ResourceSummary createPage(final PageDto page)
                                                 throws CommandFailedException {
        return createPage(
            page.getParentId(),
            page.getDelta(),
            page.getName(),
            false,
            page.getTemplateId(),
            page.getTitle(),
            loggedInUserId(_context),
            new Date(),
            page.getComment(),
            page.getMajorChange());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void updatePage(final UUID pageId, final Json json)
                                                 throws CommandFailedException {
        final boolean majorEdit =
            json.getBool(JsonKeys.MAJOR_CHANGE).booleanValue();
        final String comment = json.getString(JsonKeys.COMMENT);
        final PageDelta delta = new PageDelta(json.getJson(JsonKeys.DELTA));

        updatePage(
            pageId,
            delta,
            comment,
            majorEdit,
            loggedInUserId(_context),
            new Date());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR})
    public void updatePage(final UUID pageId,
                           final PageDelta delta,
                           final String comment,
                           final boolean isMajorEdit,
                           final UUID actorId,
                           final Date happenedOn)
                                                 throws CommandFailedException {
        try {
            new UpdatePageCommand(_bdao, _audit).execute(
                userForId(actorId),
                happenedOn,
                pageId,
                delta,
                comment,
                isMajorEdit);

        } catch (final CccCheckedException e) {
            throw fail(_context, e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void updateWorkingCopy(final UUID pageId,
                                  final PageDelta delta)
                                                 throws CommandFailedException {
        try {
            new UpdateWorkingCopyCommand(_bdao, _audit).execute(
                loggedInUser(_context),
                new Date(),
                pageId,
                delta);

        } catch (final CccCheckedException e) {
            throw fail(_context, e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public List<String> validateFields(final Json json) {
        final String def = json.getString("definition");
        final Set<Paragraph> p = new HashSet<Paragraph>();
        for (final Json j : json.getCollection("paragraphs")) {
            p.add(new Paragraph(j));
        }
        return new PageHelper().validateFields(p, def);
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public PageDelta pageDelta(final UUID pageId) {
        return
            deltaPage(_resources.find(Page.class, pageId));
    }



    /* ==============
     * Helper methods
     * ============== */

    @PostConstruct @SuppressWarnings("unused")
    private void configureCoreData() {
        _bdao = new JpaRepository(_em);
        _audit = new LogEntryRepositoryImpl(_bdao);
        _users = new UserRepositoryImpl(_bdao);
        _resources = new ResourceRepositoryImpl(_bdao);
    }

}
