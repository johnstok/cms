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
import java.util.List;
import java.util.UUID;

import ccc.domain.Folder;
import ccc.domain.LogEntry;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.persistence.IRepositoryFactory;
import ccc.rest.exceptions.UnauthorizedException;
import ccc.serialization.JsonImpl;
import ccc.types.CommandType;
import ccc.types.ResourceOrder;


/**
 * Command: updates a folder.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateFolderCommand
    extends
        UpdateResourceCommand<Void> {

    private final UUID _folderId;
    private final Folder _folder;
    private final ResourceOrder _order;
    private final UUID _indexPageId;
    private final List<UUID> _orderList;

    /**
     * Constructor.
     *
     * @param repoFactory The repository factory for this command.
     * @param folderId The folder to update.
     * @param order The new sort order.
     * @param indexPageId The index page.
     * @param orderList The manual order of the resources in the specified
     *  folder.
     */
    public UpdateFolderCommand(final IRepositoryFactory repoFactory,
                               final UUID folderId,
                               final ResourceOrder order,
                               final UUID indexPageId,
                               final List<UUID> orderList) {
        super(repoFactory);
        _folderId = folderId;
        _folder = getRepository().find(Folder.class, folderId);
        _order = order;
        _indexPageId = indexPageId;
        _orderList = orderList;
    }


    /** {@inheritDoc} */
    @Override
    public Void doExecute(final User actor,
                          final Date happenedOn) {

        final Folder f = getRepository().find(Folder.class, _folderId);
        f.confirmLock(actor);

        Page p = null;
        if (_indexPageId != null) {
            p = getRepository().find(Page.class, _indexPageId);
        }
        f.setIndexPage(p);
        f.setSortOrder(_order);

        if (_orderList != null && !_orderList.isEmpty()) {

            if (_orderList.size()!=f.getEntries().size()) {
                throw new RuntimeException("Wrong number of elements.");
            }

            for (int i=0; i<_orderList.size(); i++) {
                final UUID id = _orderList.get(i);
                final Resource r = f.getChild(id);
                if (null==r) {
                    throw new RuntimeException("Not a child of folder: "+id);
                }
                r.setIndexPosition(i);
            }
        }

        f.setDateChanged(happenedOn, actor);

        final LogEntry le =
            new LogEntry(
                actor,
                getType(),
                happenedOn,
                _folderId,
                new JsonImpl(f).getDetail());
        getAudit().record(le);

        return null;
    }


    /** {@inheritDoc} */
    @Override
    protected void authorize(final User actor) throws UnauthorizedException {
        if (!_folder.isWriteableBy(actor)) {
            throw new UnauthorizedException(_folderId, actor.getId());
        }
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.FOLDER_UPDATE; }
}
