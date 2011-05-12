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

import ccc.api.core.Template;
import ccc.api.exceptions.InvalidException;
import ccc.api.exceptions.UnauthorizedException;
import ccc.api.types.CommandType;
import ccc.domain.RevisionMetadata;
import ccc.domain.TemplateEntity;
import ccc.domain.TemplateValidator;
import ccc.domain.UserEntity;
import ccc.persistence.IRepositoryFactory;


/**
 * Command: update a template.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateTemplateCommand
    extends
        UpdateResourceCommand<Template> {

    private final UUID _templateId;
    private final Template _delta;
    private final TemplateEntity _template;

    /**
     * Constructor.
     *
     * @param repoFactory The repository factory for this command.
     * @param templateId The id of the template to update.
     * @param delta The changes to the template.
     */
    public UpdateTemplateCommand(final IRepositoryFactory repoFactory,
                                 final UUID templateId,
                                 final Template delta) {
        super(repoFactory);
        _templateId = templateId;
        _delta = delta;
        _template = getRepository().find(TemplateEntity.class, _templateId);
    }


    /** {@inheritDoc} */
    @Override
    public Template doExecute(final UserEntity actor,
                              final Date happenedOn) {

        _template.confirmLock(actor);

        final RevisionMetadata rm =
            new RevisionMetadata(happenedOn, actor, true, "Updated.");

        _template.update(_delta, rm);

        update(_template, actor, happenedOn);

        return _template.forCurrentRevision();
    }


    @Override
    protected void authorize(final UserEntity actor) {
        if (!_template.isWriteableBy(actor)) {
            throw new UnauthorizedException(_templateId, actor.getId());
        }
    }


    /** {@inheritDoc} */
    @Override
    protected void validate() {
        final TemplateValidator templateValidator = new TemplateValidator();
        final String result =
            templateValidator.validate(_delta.getDefinition());
        if(null != result) {
            throw new InvalidException("Invalid template definition: "+result);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.TEMPLATE_UPDATE; }

}
