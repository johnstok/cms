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

import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import ccc.api.core.Page;
import ccc.api.exceptions.UnauthorizedException;
import ccc.api.types.CommandType;
import ccc.domain.PageEntity;
import ccc.domain.RevisionMetadata;
import ccc.domain.UserEntity;
import ccc.messaging.Producer;
import ccc.persistence.IRepositoryFactory;


/**
 * Command: updates a page with the specified delta.
 *
 * @author Civic Computing Ltd.
 */
public class UpdatePageCommand
    extends
        UpdateResourceCommand<Page> {

    private final UUID      _id;
    private final Page _delta;
    private final PageEntity _page;
    private final Producer   _producer;


    /**
     * Constructor.
     *
     * @param repoFactory The repository factory for this command.
     * @param id The id of the page to update.
     * @param delta The changes to the page.
     */
    public UpdatePageCommand(final IRepositoryFactory repoFactory,
                             final Producer producer,
                             final UUID id,
                             final Page delta) {
        super(repoFactory);
        _producer  = producer;
        _id = id;
        _delta = delta;
        _page = getRepository().find(PageEntity.class, _id);
    }


    /** {@inheritDoc} */
    @Override
    public Page doExecute(final UserEntity actor, final Date happenedOn) {

        _page.confirmLock(actor);

        final RevisionMetadata rm =
            new RevisionMetadata(
                happenedOn,
                actor,
                _delta.isMajorChange(),
                _delta.getComment());

        _page.setOrUpdateWorkingCopy(_delta);
        _page.applyWorkingCopy(rm);

        update(_page, actor, happenedOn);

        _producer.broadcastMessage(
            CommandType.SEARCH_INDEX_RESOURCE,
            Collections.singletonMap("resource", _page.getId().toString()));

        return _page.forCurrentRevision();
    }


    @Override
    protected void authorize(final UserEntity actor) {
        if (!_page.isWriteableBy(actor)) {
            throw new UnauthorizedException(_id, actor.getId());
        }
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.PAGE_UPDATE; }
}
