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
import ccc.persistence.Repository;
import ccc.rest.dto.TemplateDelta;


/**
 * Command: update a template.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateTemplateCommand extends UpdateResourceCommand {

    /**
     * Constructor.
     *
     * @param repository The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     */
    public UpdateTemplateCommand(final Repository repository,
                                 final LogEntryRepository audit) {
        super(repository, audit);
    }

    /**
     * Update a template.
     *
     * @param templateId The id of the template to update.
     * @param delta The changes to the template.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @throws CccCheckedException If the command fails.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID templateId,
                        final TemplateDelta delta) throws CccCheckedException {

        final Template template = getDao().find(Template.class, templateId);
        template.confirmLock(actor);

        final RevisionMetadata rm =
            new RevisionMetadata(happenedOn, actor, true, "Created.");

        template.update(delta, rm);

        update(template, actor, happenedOn);
    }
}
