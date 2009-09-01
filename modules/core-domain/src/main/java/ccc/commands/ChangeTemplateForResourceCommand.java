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

import ccc.domain.LockMismatchException;
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.Template;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.Repository;
import ccc.serialization.JsonImpl;
import ccc.types.CommandType;


/**
 * Command: change the template for a resource.
 *
 * @author Civic Computing Ltd.
 */
public class ChangeTemplateForResourceCommand {

    private final Repository      _repository;
    private final LogEntryRepository _audit;

    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public ChangeTemplateForResourceCommand(final Repository repository,
                                              final LogEntryRepository audit) {
        _repository = repository;
        _audit = audit;
    }

    /**
     * Change the template for the specified resource.
     *
     * @param resourceId The id of the resource to change.
     * @param templateId The id of template to set (NULL is allowed).
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @throws LockMismatchException If the resource is locked by another user.
     * @throws UnlockedException If the resource is unlocked.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID resourceId,
                        final UUID templateId)
      throws UnlockedException, LockMismatchException {
        final Resource r = _repository.find(Resource.class, resourceId);
        r.confirmLock(actor);

        final Template t =
            (null==templateId)
                ? null
                : _repository.find(Template.class, templateId);

        r.template(t);

        final LogEntry le =
            new LogEntry(
                actor,
                CommandType.RESOURCE_CHANGE_TEMPLATE,
                happenedOn,
                resourceId,
                new JsonImpl(r).getDetail());
        _audit.record(le);
    }

}
