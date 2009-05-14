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

import ccc.domain.ResourceExistsException;
import ccc.domain.ResourceName;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;
import ccc.services.api.TemplateDelta;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class CreateTemplateCommand extends CreateResourceCommand {


    /**
     * Constructor.
     *
     * @param dao The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     */
    public CreateTemplateCommand(final Dao dao, final AuditLog audit) {
        super(dao, audit);
    }

    /**
     * Create the template.
     *
     * @param actor
     * @param happenedOn
     * @param parentFolder
     * @param delta
     * @param name
     * @throws ResourceExistsException
     */
    public Template execute(final User actor,
                            final Date happenedOn,
                            final UUID parentFolder,
                            final TemplateDelta delta,
                            final ResourceName name)
                                                throws ResourceExistsException {
        final Template t = new Template(
            name,
            delta.getTitle(),
            delta.getDescription(),
            delta.getBody(),
            delta.getDefinition());

        create(actor, happenedOn, parentFolder, t);

        return t;
    }
}
