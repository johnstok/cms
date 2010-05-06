/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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

import java.util.UUID;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.api.core.PagedCollection;
import ccc.api.core.ResourceSummary;
import ccc.api.core.Template;
import ccc.api.core.Templates;
import ccc.api.exceptions.EntityNotFoundException;
import ccc.commands.UpdateTemplateCommand;
import ccc.domain.TemplateEntity;
import ccc.persistence.ResourceRepository;


/**
 * EJB implementation of the {@link Templates} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=Templates.NAME)
@TransactionAttribute(REQUIRED)
@Local(Templates.class)
@RolesAllowed({})
public final class TemplatesEJB
    extends
        AbstractEJB
    implements
        Templates {


    /**
     * Constructor.
     */
    public TemplatesEJB() { super(); }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(TEMPLATE_READ)
    public Boolean templateNameExists(final String templateName) {
        try {
            getRepoFactory()
                .createResourceRepository()
                .template(templateName);
            return Boolean.TRUE;
        } catch (final EntityNotFoundException e) {
            return Boolean.FALSE;
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(TEMPLATE_READ)
    public PagedCollection<Template> templates(final int pageNo,
                                               final int pageSize) {
        final ResourceRepository repo =
            getRepoFactory().createResourceRepository();

        return new PagedCollection<Template>(repo.templateCount(),
            TemplateEntity.mapTemplates(repo.templates(pageNo, pageSize)));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(TEMPLATE_CREATE)
    public ResourceSummary createTemplate(final Template template) {
        return
            execute(commands().createTemplateCommand(template))
            .mapResource();
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(TEMPLATE_UPDATE)
    public void updateTemplate(final UUID templateId,
                               final Template delta) {
        execute(
            new UpdateTemplateCommand(
                getRepoFactory(),
                templateId,
                delta));
    }




    /* ====================================================================
     * UNSAFE METHODS.
     * ================================================================== */

    /** {@inheritDoc} */
    @Override
    @PermitAll
    public Template templateDelta(final UUID templateId) {
        checkPermission(TEMPLATE_READ);
        return
            getRepoFactory()
                .createResourceRepository()
                .find(TemplateEntity.class, templateId).createSnapshot();
    }
}
