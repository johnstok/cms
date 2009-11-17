/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
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

import ccc.domain.CccCheckedException;
import ccc.domain.RevisionMetadata;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.rest.dto.TemplateDelta;
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
     * @param repository The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     * @param templateId The id of the template to update.
     * @param delta The changes to the template.
     */
    public UpdateTemplateCommand(final ResourceRepository repository,
                                 final LogEntryRepository audit,
                                 final UUID templateId,
                                 final TemplateDelta delta) {
        super(repository, audit, null);
        _templateId = templateId;
        _delta = delta;
    }


    /** {@inheritDoc} */
    @Override
    public Void doExecute(final User actor,
                          final Date happenedOn) throws CccCheckedException {

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
