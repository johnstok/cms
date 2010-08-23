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

import static ccc.api.types.Permission.*;
import static javax.ejb.TransactionAttributeType.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.api.core.Page;
import ccc.api.core.PageCriteria;
import ccc.api.core.PagedCollection;
import ccc.api.core.Pages;
import ccc.api.core.ResourceSummary;
import ccc.api.exceptions.EntityNotFoundException;
import ccc.commands.UpdatePageCommand;
import ccc.commands.UpdateWorkingCopyCommand;
import ccc.commons.Exceptions;
import ccc.domain.PageEntity;
import ccc.domain.PageHelper;
import ccc.domain.ResourceEntity;
import ccc.domain.TemplateEntity;
import ccc.domain.UserEntity;


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
    @PermitAll
    public ResourceSummary create(final Page page) {
        checkPermission(PAGE_CREATE);
        return
            execute(
                commands().createPageCommand(
                    page.getParent(),
                    page))
                .mapResource();
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(PAGE_UPDATE)
    public void update(final UUID pageId, final Page delta) {
            execute(
                new UpdatePageCommand(
                    getRepoFactory(),
                    pageId,
                    delta));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(PAGE_UPDATE)
    public void updateWorkingCopy(final UUID pageId,
                                  final Page delta) {
        new UpdateWorkingCopyCommand(getRepoFactory())
            .execute(
                currentUser(),
                new Date(),
                pageId,
                delta);
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(PAGE_UPDATE)
    public String validate(final Page page) {
        final TemplateEntity t =
            getRepoFactory().createResourceRepository().find(
                TemplateEntity.class, page.getTemplate());
        return
            new PageHelper().validateFields(
                page.getParagraphs(), t.getDefinition());
    }


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public Page retrieve(final UUID pageId) {
        checkPermission(RESOURCE_READ);

        return
            getRepoFactory()
                .createResourceRepository()
                .find(PageEntity.class, pageId).forCurrentRevision();
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(PAGE_UPDATE)
    public Page retrieveWorkingCopy(final UUID pageId) {
        return
            getRepoFactory()
                .createResourceRepository()
                .find(PageEntity.class, pageId).deltaPage();
    }


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public PagedCollection<ResourceSummary> list(final PageCriteria criteria,
                                                 final int pageNo,
                                                 final int pageSize) {
        checkPermission(RESOURCE_READ);

        UserEntity u = null;
        try {
            u = currentUser();
        } catch (final EntityNotFoundException e) {
            Exceptions.swallow(e); // Leave user as NULL.
        }

        final List<ResourceSummary> list = ResourceEntity.mapResources(
            filterAccessibleTo(
                u,
                getRepoFactory()
                    .createResourceRepository()
                    .list(criteria, pageNo, pageSize)));

        final long count =
            getRepoFactory()
                .createResourceRepository()
                .totalCount(criteria);

        return
            new PagedCollection<ResourceSummary>(
                count, ResourceSummary.class, list);
    }
}
