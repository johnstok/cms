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

import ccc.api.core.Resource;
import ccc.api.exceptions.UnauthorizedException;
import ccc.api.exceptions.WorkingCopyNotSupportedException;
import ccc.api.types.CommandType;
import ccc.domain.RevisionMetadata;
import ccc.domain.UserEntity;
import ccc.domain.WorkingCopySupport;
import ccc.persistence.IRepositoryFactory;


/**
 * Command: apply the current working copy.
 *
 * @author Civic Computing Ltd.
 */
public class ApplyWorkingCopyCommand
    extends
        UpdateResourceCommand<Resource> {

    private final UUID _id;
    private final String _comment;
    private final boolean _isMajorEdit;
    private final WorkingCopySupport<?, ?, ?> _r;

    /**
     * Constructor.
     *
     * @param repoFactory The repository factory for this command.
     * @param id The resource's id.
     * @param comment The comment for the page edit.
     * @param isMajorEdit A boolean for major edit.
     */
    public ApplyWorkingCopyCommand(final IRepositoryFactory repoFactory,
                                   final UUID id,
                                   final String comment,
                                   final boolean isMajorEdit) {
        super(repoFactory);
        _id = id;
        _comment = comment;
        _isMajorEdit = isMajorEdit;
        _r = getRepository().findWcAware(_id);
    }


    /** {@inheritDoc} */
    @Override
    public Resource doExecute(final UserEntity actor,
                              final Date happenedOn) {


        if (null==_r) { throw new WorkingCopyNotSupportedException(_id); }

        _r.confirmLock(actor);

        final RevisionMetadata rm =
            new RevisionMetadata(happenedOn, actor, _isMajorEdit, _comment);
        _r.applyWorkingCopy(rm);

        update(_r, actor, happenedOn);

        return _r.forCurrentRevision();
    }


    @Override
    protected void authorize(final UserEntity actor) {
        if (!_r.isWriteableBy(actor)) {
            throw new UnauthorizedException(_id, actor.getId());
        }
    }

    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.RESOURCE_APPLY_WC; }
}
