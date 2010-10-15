/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.commands.anonymous;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import ccc.api.types.CommandType;
import ccc.api.types.ResourceType;
import ccc.domain.FolderEntity;
import ccc.domain.ResourceEntity;
import ccc.messaging.Producer;
import ccc.persistence.DataRepository;
import ccc.persistence.ResourceRepository;
import ccc.persistence.SettingsRepository;
import ccc.search.SearchHelper;


/**
 * Command: index a resource and all its children.
 *
 * @author Civic Computing Ltd.
 */
class IndexRecursiveCommand
    implements
        AnonymousCommand {
    private static final Logger LOG =
        Logger.getLogger(IndexRecursiveCommand.class);


    private final ResourceRepository _resRepo;
    private final DataRepository     _dataRepo;
    private final SettingsRepository _settingsRepo;
    private final UUID               _resourceId;
    private final Producer           _broadcast;


    /**
     * Constructor.
     *
     * @param resRepo      The resource repository.
     * @param dataRepo     The data repository.
     * @param settingsRepo The settings repository.
     * @param params       The parameters for the command.
     * @param broadcast    The publisher to broadcast additional re-indexes.
     */
    public IndexRecursiveCommand(final ResourceRepository resRepo,
                                 final DataRepository dataRepo,
                                 final SettingsRepository settingsRepo,
                                 final Producer broadcast,
                                 final Map<String, String> params) {
        _resRepo = resRepo;
        _dataRepo = dataRepo;
        _settingsRepo = settingsRepo;
        _resourceId = UUID.fromString(params.get("resource"));
        _broadcast = broadcast;
    }


    /** {@inheritDoc} */
    @Override
    public void execute() {
        final SearchHelper search =
            new SearchHelper(_resRepo, _dataRepo, _settingsRepo);

        final ResourceEntity r =
            _resRepo.find(ResourceEntity.class, _resourceId);

        if (null==r) {
            LOG.warn("Ignored request to index missing resource: "+_resourceId);

        } else {
            search.index(r);

            if (ResourceType.FOLDER==r.getType()) {
                final FolderEntity f =
                    _resRepo.find(FolderEntity.class, _resourceId);

                for (final ResourceEntity e : f.getEntries()) {
                    _broadcast.broadcastMessage(
                        CommandType.SEARCH_INDEX_RESOURCE,
                        Collections.singletonMap(
                            "resource", e.getId().toString()));
                }
            }
        }
    }
}
