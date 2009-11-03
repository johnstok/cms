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
package ccc.commands;

import java.util.Date;
import java.util.UUID;

import ccc.domain.CccCheckedException;
import ccc.domain.RevisionMetadata;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.rest.dto.TemplateDelta;
import ccc.types.CommandType;
import ccc.types.ResourceName;


/**
 * Command: create a new template.
 *
 * @author Civic Computing Ltd.
 */
class CreateTemplateCommand extends CreateResourceCommand<Template> {


    private final UUID _parentFolder;
    private final TemplateDelta _delta;
    private final String _title;
    private final String _description;
    private final ResourceName _name;


    /**
     * Constructor.
     *
     * @param repository The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     * @param parentFolder The folder in which the template will be created.
     * @param delta The template's details.
     * @param name The name of the template.
     * @param title The template's title.
     * @param description The template's description.
     */
    public CreateTemplateCommand(final ResourceRepository repository,
                                 final LogEntryRepository audit,
                                 final UUID parentFolder,
                                 final TemplateDelta delta,
                                 final String title,
                                 final String description,
                                 final ResourceName name) {
        super(repository, audit);
        _parentFolder = parentFolder;
        _delta = delta;
        _title = title;
        _description = description;
        _name = name;
    }

    /** {@inheritDoc} */
    @Override
    public Template doExecute(final User actor,
                              final Date happenedOn)
                                                throws CccCheckedException {
        final RevisionMetadata rm =
            new RevisionMetadata(happenedOn, actor, true, "Created.");

        final Template t =
            new Template(
                _name,
                _title,
                _description,
                _delta.getBody(),
                _delta.getDefinition(),
                _delta.getMimeType(),
                rm);

        create(actor, happenedOn, _parentFolder, t);

        return t;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.TEMPLATE_CREATE; }
}
