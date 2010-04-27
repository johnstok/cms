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

import ccc.api.exceptions.WorkingCopyNotSupportedException;
import ccc.api.types.CommandType;
import ccc.domain.ResourceEntity;
import ccc.domain.RevisionMetadata;
import ccc.domain.UserEntity;
import ccc.domain.WCAware;
import ccc.persistence.IRepositoryFactory;


/**
 * Command: apply the current working copy.
 *
 * @author Civic Computing Ltd.
 */
public class ApplyWorkingCopyCommand
    extends
        UpdateResourceCommand<Void> {

    private final UUID _id;
    private final String _comment;
    private final boolean _isMajorEdit;


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
    }


    /** {@inheritDoc} */
    @Override
    public Void doExecute(final UserEntity actor,
                          final Date happenedOn) {

        final ResourceEntity r = getRepository().find(ResourceEntity.class, _id);
        r.confirmLock(actor);

        if (r instanceof WCAware<?>) {
            final WCAware<?> wcAware = (WCAware<?>) r;
            final RevisionMetadata rm =
                new RevisionMetadata(happenedOn, actor, _isMajorEdit, _comment);
            wcAware.applyWorkingCopy(rm);
        } else {
            throw new WorkingCopyNotSupportedException(r.getId());
        }

        update(r, actor, happenedOn);

        return null;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.RESOURCE_APPLY_WC; }
}
