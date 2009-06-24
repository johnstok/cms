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
import ccc.api.Paragraph;
import ccc.domain.Page;
import ccc.domain.PageHelper;
import ccc.domain.RemoteExceptionSupport;
import ccc.domain.ResourceName;
import ccc.domain.RevisionMetadata;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;


/**
 * Command: create a new page.
 *
 * @author Civic Computing Ltd.
 */
public class CreatePageCommand extends CreateResourceCommand {

    private final PageHelper _pageHelper = new PageHelper();

    /**
     * Constructor.
     *
     * @param dao The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     */
    public CreatePageCommand(final Dao dao, final AuditLog audit) {
        super(dao, audit);
    }


    /**
     * Create a page.
     *
     * @param parentFolder The folder in which the page will be created.
     * @param delta The contents of the new page.
     * @param publish Should the new page be published.
     * @param name The new page's name.
     * @param templateId The new page's template.
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @throws RemoteExceptionSupport If the command fails.
     *
     *  @return The new page.
     */
    public Page execute(final User actor,
                        final Date happenedOn,
                        final UUID parentFolder,
                        final PageDelta delta,
                        final boolean publish,
                        final ResourceName name,
                        final UUID templateId) throws RemoteExceptionSupport {

        final Template template =
            (null==templateId)
                ? null
                : getDao().find(Template.class, templateId);

        final RevisionMetadata rm =
            new RevisionMetadata(happenedOn, actor, true, "Created.");

        final Page page =
            new Page(
                name,
                delta.getTitle(),
                template,
                rm,
                delta.getParagraphs().toArray(new Paragraph[0]));

        if (publish) {
            page.publish(actor);
        }

        create(actor, happenedOn, parentFolder, page);

        return page;
    }
}
