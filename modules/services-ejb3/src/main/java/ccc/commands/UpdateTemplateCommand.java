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

import ccc.api.dto.TemplateDelta;
import ccc.domain.RevisionMetadata;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.persistence.IRepositoryFactory;
import ccc.types.CommandType;


/**
 * Command: update a template.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateTemplateCommand
    extends
        UpdateResourceCommand<Void> {

    private final UUID _templateId;
    private final TemplateDelta _delta;


    /**
     * Constructor.
     *
     * @param repoFactory The repository factory for this command.
     * @param templateId The id of the template to update.
     * @param delta The changes to the template.
     */
    public UpdateTemplateCommand(final IRepositoryFactory repoFactory,
                                 final UUID templateId,
                                 final TemplateDelta delta) {
        super(repoFactory);
        _templateId = templateId;
        _delta = delta;
    }


    /** {@inheritDoc} */
    @Override
    public Void doExecute(final User actor,
                          final Date happenedOn) {

        final Template template =
            getRepository().find(Template.class, _templateId);
        template.confirmLock(actor);

        final RevisionMetadata rm =
            new RevisionMetadata(happenedOn, actor, true, "Updated.");

        template.update(_delta, rm);

        update(template, actor, happenedOn);

        return null;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.TEMPLATE_UPDATE; }
}
