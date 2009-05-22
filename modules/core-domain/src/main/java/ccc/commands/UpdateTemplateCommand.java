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
import ccc.domain.LockMismatchException;
import ccc.domain.Template;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;


/**
 * Command: update a template.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateTemplateCommand extends UpdateResourceCommand {

    /**
     * Constructor.
     *
     * @param dao The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     */
    public UpdateTemplateCommand(final Dao dao, final AuditLog audit) {
        super(dao, audit);
    }

    /**
     * Update a template.
     *
     * @param templateId The id of the template to update.
     * @param delta The changes to the template.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @throws LockMismatchException If the resource is locked by another user.
     * @throws UnlockedException If the resource is unlocked.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID templateId,
                        final TemplateDelta delta)
                               throws UnlockedException, LockMismatchException {

        final Template template = getDao().find(Template.class, templateId);
        template.confirmLock(actor);

        template.title(delta.getTitle());
        template.description(delta.getDescription());
        template.definition(delta.getDefinition());
        template.body(delta.getBody());
        template.mimeType(delta.getMimeType());

        update(template, null, false, actor, happenedOn);
    }
}
