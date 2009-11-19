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
import java.util.Set;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.commands.UpdatePageCommand;
import ccc.commands.UpdateWorkingCopyCommand;
import ccc.domain.CccCheckedException;
import ccc.domain.Page;
import ccc.domain.PageHelper;
import ccc.domain.User;
import ccc.rest.Pages;
import ccc.rest.RestException;
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
@Stateless(name=Pages.NAME)
@TransactionAttribute(REQUIRED)
@Remote(PagesExt.class)
@RolesAllowed({})
public class PagesEJB
    extends
        AbstractEJB
    implements
        PagesExt {


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
                                                 throws RestException {
        try {
            final User u = userForId(actorId);

            final Page p =
                commands().createPageCommand(
                    parentId,
                    delta,
                    ResourceName.escape(name),
                    title,
                    templateId,
                    comment,
                    majorChange)
                .execute(u, happenedOn);

            if (publish) {
                p.lock(u);
                commands().publishResource(p.id()).execute(u, happenedOn);
                p.unlock(u);
            }

            return mapResource(p);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR, API_USER})
    public ResourceSummary createPage(final PageDto page)
                                                 throws RestException {
        try {
            return createPage(
                page.getParentId(),
                page.getDelta(),
                page.getName(),
                false,
                page.getTemplateId(),
                page.getTitle(),
                currentUserId(),
                new Date(),
                page.getComment(),
                page.getMajorChange());

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void updatePage(final UUID pageId, final Json json)
                                                 throws RestException {
        try {
            final boolean majorEdit =
                json.getBool(JsonKeys.MAJOR_CHANGE).booleanValue();
            final String comment = json.getString(JsonKeys.COMMENT);
            final PageDelta delta = new PageDelta(json.getJson(JsonKeys.DELTA));

            updatePage(
                pageId,
                delta,
                comment,
                majorEdit,
                currentUserId(),
                new Date());

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
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
                                                 throws RestException {
        try {
            new UpdatePageCommand(
                getResources(),
                getAuditLog(),
                pageId,
                delta,
                comment,
                isMajorEdit)
            .execute(
                userForId(actorId),
                happenedOn);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void updateWorkingCopy(final UUID pageId,
                                  final PageDelta delta)
                                                 throws RestException {
        try {
            new UpdateWorkingCopyCommand(
                getResources(), getAuditLog()).execute(
                    currentUser(),
                    new Date(),
                    pageId,
                    delta);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public String validateFields(final Json json) {
        final String def = json.getString(JsonKeys.DEFINITION);
        final Set<Paragraph> p = new HashSet<Paragraph>();
        for (final Json j : json.getCollection(JsonKeys.PARAGRAPHS)) {
            p.add(new Paragraph(j));
        }
        return new PageHelper().validateFields(p, def);
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public PageDelta pageDelta(final UUID pageId) throws RestException {
        try {
            return
                deltaPage(getResources().find(Page.class, pageId));

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }
}
