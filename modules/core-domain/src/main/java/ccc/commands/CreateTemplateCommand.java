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

import ccc.api.TemplateDelta;
import ccc.domain.CccCheckedException;
import ccc.domain.RevisionMetadata;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Repository;
import ccc.types.ResourceName;


/**
 * Command: create a new template.
 *
 * @author Civic Computing Ltd.
 */
public class CreateTemplateCommand extends CreateResourceCommand {


    /**
     * Constructor.
     *
     * @param repository The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     */
    public CreateTemplateCommand(final Repository repository, final AuditLog audit) {
        super(repository, audit);
    }

    /**
     * Create the template.
     *
     * @param parentFolder The folder in which the template will be created.
     * @param delta The template's details.
     * @param name The name of the template.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @throws CccCheckedException If the command fails.
     *
     *  @return The new template.
     */
    public Template execute(final User actor,
                            final Date happenedOn,
                            final UUID parentFolder,
                            final TemplateDelta delta,
                            final String title,
                            final String description,
                            final ResourceName name)
                                                throws CccCheckedException {
        final RevisionMetadata rm =
            new RevisionMetadata(happenedOn, actor, true, "Created.");

        final Template t =
            new Template(
                name,
                title,
                description,
                delta.getBody(),
                delta.getDefinition(),
                delta.getMimeType(),
                rm);

        create(actor, happenedOn, parentFolder, t);

        return t;
    }
}
