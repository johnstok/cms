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
import java.util.UUID;

import ccc.api.dto.PageDto;
import ccc.api.exceptions.WorkingCopyNotSupportedException;
import ccc.api.types.CommandType;
import ccc.domain.LogEntry;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.domain.WorkingCopySupport;
import ccc.persistence.IRepositoryFactory;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.plugins.s11n.json.JsonImpl;


/**
 * Command: updates the working copy for a page.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateWorkingCopyCommand {

    private final ResourceRepository      _repository;
    private final LogEntryRepository _audit;

    /**
     * Constructor.
     *
     * @param repoFactory The repository factory for this command.
     */
    public UpdateWorkingCopyCommand(final IRepositoryFactory repoFactory) {
        _repository = repoFactory.createResourceRepository();
        _audit = repoFactory.createLogEntryRepo();
    }

    /**
     * Updates the working copy.
     *
     * @param delta The page delta to store in the page.
     * @param resourceId The page's id.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID resourceId,
                        final PageDto delta) {
        final Page r = _repository.find(Page.class, resourceId);
        r.confirmLock(actor);

        r.setOrUpdateWorkingCopy(delta);

        _audit.record(
            new LogEntry(
                actor,
                CommandType.RESOURCE_UPDATE_WC,
                happenedOn,
                resourceId,
                new JsonImpl(r).getDetail()));
    }

    /**
     * Updates the working copy.
     *
     * @param resourceId The page's id.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     * @param revisionNo The revision that the working copy will be created
     *  from.
     */
    public void execute(final User actor,
                        final Date happenedOn,
                        final UUID resourceId,
                        final long revisionNo) {
        final Resource r =
            _repository.find(Resource.class, resourceId);
        r.confirmLock(actor);

        if (r instanceof WorkingCopySupport<?, ?, ?>) {
            final WorkingCopySupport<?, ?, ?> wcAware =
                (WorkingCopySupport<?, ?, ?>) r;
            wcAware.setWorkingCopyFromRevision((int) revisionNo);

            _audit.record(
                new LogEntry(
                    actor,
                    CommandType.RESOURCE_UPDATE_WC,
                    happenedOn,
                    resourceId,
                    new JsonImpl(r).getDetail()));
        } else {
            throw new WorkingCopyNotSupportedException(r.getId());
        }
    }
}
