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
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import ccc.api.types.CommandType;
import ccc.domain.ResourceEntity;
import ccc.domain.UserEntity;
import ccc.persistence.IRepositoryFactory;


/**
 * Command: update resource metadata.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateResourceMetadataCommand
    extends
        Command<Void> {

    private final UUID                _id;
    private final String              _title;
    private final String              _description;
    private final Set<String>         _tags;
    private final Map<String, String> _metadata;


    /**
     * Constructor.
     *
     * @param repoFactory The repository factory for this command.
     * @param id The resource to update.
     * @param metadata The new metadata to set.
     * @param title The new title for the resource.
     * @param description The new description for the resource.
     * @param tags The new tags for the resource.
     */
    public UpdateResourceMetadataCommand(final IRepositoryFactory repoFactory,
                                         final UUID id,
                                         final String title,
                                         final String description,
                                         final Set<String> tags,
                                         final Map<String, String> metadata) {
        super(repoFactory);
        _id = id;
        _title = title;
        _description = description;
        _tags = tags;
        _metadata = metadata;
    }


    /** {@inheritDoc} */
    @Override
    protected Void doExecute(final UserEntity actor, final Date happenedOn) {
        final ResourceEntity r =
            getRepository().find(ResourceEntity.class, _id);
        r.confirmLock(actor);

        r.setTitle(_title);
        r.setDescription(_description);
        r.setTags(_tags);

        r.clearMetadata();
        for (final Map.Entry<String, String> metadatum: _metadata.entrySet()) {
            r.addMetadatum(metadatum.getKey(), metadatum.getValue());
        }

        auditResourceCommand(actor, happenedOn, r);

        return null;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() {
        return CommandType.RESOURCE_UPDATE_METADATA;
    }
}
