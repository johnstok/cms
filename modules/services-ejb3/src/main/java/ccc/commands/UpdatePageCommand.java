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
import ccc.domain.RevisionMetadata;
import ccc.domain.UserEntity;
import ccc.persistence.IRepositoryFactory;


/**
 * Command: updates a page with the specified delta.
 *
 * @author Civic Computing Ltd.
 */
public class UpdatePageCommand
    extends
        UpdateResourceCommand<Void> {

    private final UUID      _id;
    private final Page _delta;

    /**
     * Constructor.
     *
     * @param repoFactory The repository factory for this command.
     * @param id The id of the page to update.
     * @param delta The changes to the page.
     * @param comment Comment describing the change.
     * @param isMajorEdit Is this a major change.
     */
    public UpdatePageCommand(final IRepositoryFactory repoFactory,
                             final UUID id,
                             final Page delta) {
        super(repoFactory);
        _id = id;
        _delta = delta;
    }


    /** {@inheritDoc} */
    @Override
    public Void doExecute(final UserEntity actor, final Date happenedOn) {

        final PageEntity page = getRepository().find(PageEntity.class, _id);
        page.confirmLock(actor);

        final RevisionMetadata rm =
            new RevisionMetadata(
                happenedOn,
                actor,
                _delta.getMajorChange(),
                _delta.getComment());

        page.setOrUpdateWorkingCopy(_delta);
        page.applyWorkingCopy(rm);

        update(page, actor, happenedOn);

        return null;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.PAGE_UPDATE; }
}
