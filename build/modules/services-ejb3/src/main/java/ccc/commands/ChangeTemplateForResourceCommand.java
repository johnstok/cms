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

import ccc.api.types.CommandType;
import ccc.domain.ResourceEntity;
import ccc.domain.TemplateEntity;
import ccc.domain.UserEntity;
import ccc.persistence.IRepositoryFactory;


/**
 * Command: change the template for a resource.
 *
 * @author Civic Computing Ltd.
 */
public class ChangeTemplateForResourceCommand
    extends
        Command<Void> {

    private final UUID _resourceId;
    private final UUID _templateId;


    /**
     * Constructor.
     *
     * @param repoFactory The repository factory for this command.
     * @param resourceId The id of the resource to change.
     * @param templateId The id of template to set (NULL is allowed).
     */
    public ChangeTemplateForResourceCommand(
                                        final IRepositoryFactory repoFactory,
                                        final UUID resourceId,
                                        final UUID templateId) {
        super(repoFactory);
        _resourceId = resourceId;
        _templateId = templateId;
    }


    /** {@inheritDoc} */
    @Override
    protected Void doExecute(final UserEntity actor, final Date happenedOn) {
        final ResourceEntity r =
            getRepository().find(ResourceEntity.class, _resourceId);
        r.confirmLock(actor);

        final TemplateEntity t =
            (null==_templateId)
                ? null
                : getRepository().find(TemplateEntity.class, _templateId);

        r.setTemplate(t);

        auditResourceCommand(actor, happenedOn, r);

        return null;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() {
        return CommandType.RESOURCE_CHANGE_TEMPLATE;
    }
}
