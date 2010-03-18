/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.services.ejb3;

import static ccc.types.Permission.*;
import static javax.ejb.TransactionAttributeType.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.commands.UpdatePageCommand;
import ccc.commands.UpdateWorkingCopyCommand;
import ccc.domain.CccCheckedException;
import ccc.domain.Page;
import ccc.domain.PageHelper;
import ccc.rest.Pages;
import ccc.rest.RestException;
import ccc.rest.dto.PageDelta;
import ccc.rest.dto.PageDto;
import ccc.rest.dto.ResourceSummary;
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
@Local(Pages.class)
@RolesAllowed({})
public class PagesEJB
    extends
        AbstractEJB
    implements
        Pages {


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(PAGE_CREATE)
    public ResourceSummary createPage(final PageDto page)
                                                 throws RestException {
        return
            execute(
                commands().createPageCommand(
                    page.getParentId(),
                    page.getDelta(),
                    ResourceName.escape(page.getName()),
                    page.getTitle(),
                    page.getTemplateId(),
                    page.getComment(),
                    page.getMajorChange()))
                .mapResource();
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(PAGE_UPDATE)
    public void updatePage(final UUID pageId, final Json json)
                                                 throws RestException {
            final boolean majorEdit =
                json.getBool(JsonKeys.MAJOR_CHANGE).booleanValue();
            final String comment = json.getString(JsonKeys.COMMENT);
            final PageDelta delta = new PageDelta(json.getJson(JsonKeys.DELTA));

            execute(
                new UpdatePageCommand(
                    getRepoFactory(),
                    pageId,
                    delta,
                    comment,
                    majorEdit));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(PAGE_UPDATE)
    public void updateWorkingCopy(final UUID pageId,
                                  final PageDelta delta)
                                                 throws RestException {
        try {
            new UpdateWorkingCopyCommand(getRepoFactory())
                .execute(
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
    @RolesAllowed(PAGE_UPDATE)
    public String validateFields(final Json json) {
        final String def = json.getString(JsonKeys.DEFINITION);
        final Set<Paragraph> p = new HashSet<Paragraph>();
        for (final Json j : json.getCollection(JsonKeys.PARAGRAPHS)) {
            p.add(new Paragraph(j));
        }
        return new PageHelper().validateFields(p, def);
    }

    /* ====================================================================
     * UNSAFE METHODS.
     * ================================================================== */

    /** {@inheritDoc} */
    @Override
    @PermitAll
    public PageDelta pageDelta(final UUID pageId) throws RestException {
        try {
            return
                getRepoFactory()
                    .createResourceRepository()
                    .find(Page.class, pageId).deltaPage();

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }
}
