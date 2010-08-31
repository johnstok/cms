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

import ccc.api.core.Page;
import ccc.api.types.CommandType;
import ccc.domain.PageEntity;
import ccc.domain.UserEntity;
import ccc.persistence.IRepositoryFactory;


/**
 * Command: updates the working copy for a page.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateWCCommand2
    extends
        Command<Page> {

    private final UUID _resourceId;
    private final Page _delta;


    /**
     * Constructor.
     *
     * @param repoFactory The repository factory for this command.
     * @param resourceId  The page's id.
     * @param delta       The page delta to store in the page.
     */
    public UpdateWCCommand2(final IRepositoryFactory repoFactory,
                            final UUID resourceId,
                            final Page delta) {
        super(repoFactory);
        _resourceId = resourceId;
        _delta = delta;
    }


    /** {@inheritDoc} */
    @Override
    protected Page doExecute(final UserEntity actor, final Date happenedOn) {
        final PageEntity r =
            getRepository().find(PageEntity.class, _resourceId);
        r.confirmLock(actor);

        r.setOrUpdateWorkingCopy(_delta);

        auditResourceCommand(actor, happenedOn, r);

        return r.forCurrentRevision();
    }

    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.RESOURCE_UPDATE_WC; }
}
