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

import static ccc.api.types.Permission.TEMPLATE_CREATE;
import static ccc.api.types.Permission.TEMPLATE_READ;
import static ccc.api.types.Permission.TEMPLATE_UPDATE;
import static javax.ejb.TransactionAttributeType.REQUIRED;

import java.util.UUID;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.api.core.PagedCollection;
import ccc.api.core.Template;
import ccc.api.synchronous.Templates;
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
    public Boolean templateNameExists(final String templateName) {
        checkPermission(TEMPLATE_READ);

        final TemplateEntity template = getRepoFactory()
        .createResourceRepository()
        .template(templateName);
        return template != null;
    }


    /** {@inheritDoc} */
    @Override
    public PagedCollection<Template> query(final int pageNo,
                                               final int pageSize) {
        checkPermission(TEMPLATE_READ);

        final ResourceRepository repo =
            getRepoFactory().createResourceRepository();

        return
            new PagedCollection<Template>(
                repo.templateCount(),
                Template.class,
                TemplateEntity.mapTemplates(repo.templates(pageNo, pageSize)));
    }


    /** {@inheritDoc} */
    @Override
    public Template create(final Template template) {
        checkPermission(TEMPLATE_CREATE);

        return
            execute(commands().createTemplateCommand(template))
            .forCurrentRevision();
    }


    /** {@inheritDoc} */
    @Override
    public Template update(final UUID templateId,
                               final Template delta) {
        checkPermission(TEMPLATE_UPDATE);

        return
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
    public Template retrieve(final UUID templateId) {
        checkPermission(TEMPLATE_READ);
        return
            getRepoFactory()
                .createResourceRepository()
                .find(TemplateEntity.class, templateId).forCurrentRevision();
    }


    /** {@inheritDoc} */
    @Override
    public Template retrieveRevision(final UUID templateId,
                                     final int revision) {
        checkPermission(TEMPLATE_READ);
        return
        getRepoFactory()
            .createResourceRepository()
            .find(TemplateEntity.class, templateId)
                .forSpecificRevision(revision);
    }
}
