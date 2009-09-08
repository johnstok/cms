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
import java.util.Date;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.commands.CreateTemplateCommand;
import ccc.commands.UpdateTemplateCommand;
import ccc.domain.CccCheckedException;
import ccc.domain.Template;
import ccc.persistence.QueryNames;
import ccc.rest.CommandFailedException;
import ccc.rest.Templates;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.TemplateDelta;
import ccc.rest.dto.TemplateDto;
import ccc.rest.dto.TemplateSummary;
import ccc.types.ResourceName;


/**
 * EJB implementation of the {@link Templates} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=Templates.NAME)
@TransactionAttribute(REQUIRED)
@Remote(Templates.class)
@RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
public final class TemplatesEJB
    extends
        BaseCommands
    implements
        Templates {


    /**
     * Constructor.
     */
    public TemplatesEJB() { super(); }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public boolean templateNameExists(final String templateName) {
        return null!=_resources.find(
            QueryNames.TEMPLATE_BY_NAME,
            Template.class, new ResourceName(templateName));


    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public Collection<TemplateSummary> templates() {
        return mapTemplates(_resources.list(
            QueryNames.ALL_TEMPLATES, Template.class));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public ResourceSummary createTemplate(final TemplateDto template)
                                                 throws CommandFailedException {
        try {
            return mapResource(
                new CreateTemplateCommand(_bdao, _audit).execute(
                    currentUser(),
                    new Date(),
                    template.getParentId(),
                    template.getDelta(),
                    template.getTitle(),
                    template.getDescription(),
                    new ResourceName(template.getName())));

        } catch (final CccCheckedException e) {
            throw fail(e);
        }

    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void updateTemplate(final UUID templateId,
                               final TemplateDelta delta)
                                                 throws CommandFailedException {
        try {
            new UpdateTemplateCommand(_bdao, _audit).execute(
                currentUser(), new Date(), templateId, delta);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public TemplateDelta templateDelta(final UUID templateId) {
        return
            deltaTemplate(_resources.find(Template.class, templateId));
    }
}
