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
import java.util.Set;
import java.util.UUID;

import ccc.domain.CccCheckedException;
import ccc.domain.Page;
import ccc.domain.RevisionMetadata;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.rest.dto.PageDelta;
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
    public CreatePageCommand(final ResourceRepository repository,
                             final LogEntryRepository audit) {
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
     * @param majorChange Is this a major change.
     * @param title The page's title.
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

        final Set<Paragraph> paras = delta.getParagraphs();
        final Page page =
            new Page(
                name,
                title,
                template,
                rm,
                paras.toArray(new Paragraph[paras.size()]));

        create(actor, happenedOn, parentFolder, page);

        return page;
    }
}
