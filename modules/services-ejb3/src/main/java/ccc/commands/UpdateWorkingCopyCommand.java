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

import ccc.api.exceptions.UnauthorizedException;
import ccc.api.exceptions.WorkingCopyNotSupportedException;
import ccc.api.types.CommandType;
import ccc.domain.ResourceEntity;
import ccc.domain.UserEntity;
import ccc.domain.WorkingCopySupport;
import ccc.persistence.IRepositoryFactory;


/**
 * Command: updates the working copy for a page.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateWorkingCopyCommand
    extends
        Command<Void> {

    private final UUID _resourceId;
    private final long _revisionNo;
    private final ResourceEntity _r;

    /**
     * Constructor.
     *
     * @param repoFactory The repository factory for this command.
     * @param resourceId  The page's id.
     * @param revisionNo  The revision that the working copy will be created
     *  from.
     */
    public UpdateWorkingCopyCommand(final IRepositoryFactory repoFactory,
                                    final UUID resourceId,
                                    final long revisionNo) {
        super(repoFactory);
        _resourceId = resourceId;
        _revisionNo = revisionNo;
        _r = getRepository().find(ResourceEntity.class, _resourceId);
    }


    /** {@inheritDoc} */
    @Override
    protected Void doExecute(final UserEntity actor, final Date happenedOn) {
        _r.confirmLock(actor);

        if (_r instanceof WorkingCopySupport<?, ?, ?>) {
            final WorkingCopySupport<?, ?, ?> wcAware =
                (WorkingCopySupport<?, ?, ?>) _r;
            wcAware.setWorkingCopyFromRevision((int) _revisionNo);

            auditResourceCommand(actor, happenedOn, _r);

            return null;
        }

        throw new WorkingCopyNotSupportedException(_r.getId());
    }


    @Override
    protected void authorize(final UserEntity actor) {
        if (!_r.isWriteableBy(actor)) {
            throw new UnauthorizedException(_resourceId, actor.getId());
        }
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.RESOURCE_UPDATE_WC; }
}
