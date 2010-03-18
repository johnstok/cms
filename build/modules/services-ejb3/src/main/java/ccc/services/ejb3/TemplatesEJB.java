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

import static ccc.types.Permission.*;
import static javax.ejb.TransactionAttributeType.*;

import java.util.Collection;
import java.util.UUID;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.commands.UpdateTemplateCommand;
import ccc.domain.CccCheckedException;
import ccc.domain.EntityNotFoundException;
import ccc.domain.Template;
import ccc.rest.RestException;
import ccc.rest.Templates;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.TemplateDelta;
import ccc.rest.dto.TemplateDto;
import ccc.rest.dto.TemplateSummary;


/**
 * EJB implementation of the {@link Templates} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=Templates.NAME)
@TransactionAttribute(REQUIRED)
@Remote(Templates.class)
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
    public Collection<TemplateSummary> templates() {
        return
            Template.mapTemplates(
                getRepoFactory()
                    .createResourceRepository()
                    .templates());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(TEMPLATE_CREATE)
    public ResourceSummary createTemplate(final TemplateDto template)
                                                 throws RestException {
        return
            execute(commands().createTemplateCommand(template))
            .mapResource();
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed(TEMPLATE_UPDATE)
    public void updateTemplate(final UUID templateId,
                               final TemplateDelta delta)
                                                 throws RestException {
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
    public TemplateDelta templateDelta(final UUID templateId)
    throws RestException {
        try {
            return
                getRepoFactory()
                    .createResourceRepository()
                    .find(Template.class, templateId).deltaTemplate();

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }
}
