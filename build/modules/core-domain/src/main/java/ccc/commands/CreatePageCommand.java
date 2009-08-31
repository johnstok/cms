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

import ccc.api.PageDelta;
import ccc.domain.Page;
import ccc.domain.CccCheckedException;
import ccc.domain.RevisionMetadata;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.persistence.AuditLog;
import ccc.persistence.Repository;
import ccc.types.Paragraph;
import ccc.types.ResourceName;


/**
 * Command: create a new page.
 *
 * @author Civic Computing Ltd.
 */
public class CreatePageCommand extends CreateResourceCommand {

    /**
     * Constructor.
     *
     * @param repository The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     */
    public CreatePageCommand(final Repository repository, final AuditLog audit) {
        super(repository, audit);
    }


    /**
     * Create a page.
     *
     * @param parentFolder The folder in which the page will be created.
     * @param delta The contents of the new page.
     * @param name The new page's name.
     * @param templateId The new page's template.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     * @param comment Comment describing the change.
     * @param isMajorEdit Is this a major change.
     *
     * @throws CccCheckedException If the command fails.
     *
     *  @return The new page.
     */
    public Page execute(final User actor,
                        final Date happenedOn,
                        final UUID parentFolder,
                        final PageDelta delta,
                        final ResourceName name,
                        final String title,
                        final UUID templateId,
                        final String comment,
                        final boolean majorChange) throws CccCheckedException {

        final Template template =
            (null==templateId)
                ? null
                : getDao().find(Template.class, templateId);

        final RevisionMetadata rm =
            new RevisionMetadata(happenedOn,
                actor,
                majorChange,
                comment == null || comment.isEmpty() ? "Created." : comment);

        final Page page =
            new Page(
                name,
                title,
                template,
                rm,
                delta.getParagraphs().toArray(new Paragraph[0]));

        create(actor, happenedOn, parentFolder, page);

        return page;
    }
}
