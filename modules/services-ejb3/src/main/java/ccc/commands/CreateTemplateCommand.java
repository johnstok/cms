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
package ccc.commands;

import java.util.Date;

import ccc.api.dto.TemplateDto;
import ccc.api.types.CommandType;
import ccc.domain.RevisionMetadata;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;


/**
 * Command: create a new template.
 *
 * @author Civic Computing Ltd.
 */
class CreateTemplateCommand extends CreateResourceCommand<Template> {

    private final TemplateDto _template;


    /**
     * Constructor.
     *
     * @param repository The DAO used for CRUD operations, etc.
     * @param audit      The audit log to record business actions.
     * @param template   The template to create.
     */
    public CreateTemplateCommand(final ResourceRepository repository,
                                 final LogEntryRepository audit,
                                 final TemplateDto template) {
        super(repository, audit);
        _template = template;
    }

    /** {@inheritDoc} */
    @Override
    public Template doExecute(final User actor,
                              final Date happenedOn) {
        final RevisionMetadata rm =
            new RevisionMetadata(happenedOn, actor, true, "Created.");

        final Template t =
            new Template(
                _template.getName(),
                _template.getTitle(),
                _template.getDescription(),
                _template.getBody(),
                _template.getDefinition(),
                _template.getMimeType(),
                rm);

        create(actor, happenedOn, _template.getParent(), t);

        return t;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.TEMPLATE_CREATE; }
}
