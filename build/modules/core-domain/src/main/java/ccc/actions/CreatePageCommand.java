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
package ccc.actions;

import java.util.Date;
import java.util.UUID;

import ccc.domain.Page;
import ccc.domain.PageHelper;
import ccc.domain.ResourceName;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;
import ccc.services.api.PageDelta;


/**
 * TODO: Add Description for this type.
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
     * Create the page.
     *
     * @param actor
     * @param happenedOn
     * @param parentFolder
     * @param delta
     * @param publish
     * @param name
     * @param templateId
     */
    public Page execute(final User actor,
                        final Date happenedOn,
                        final UUID parentFolder,
                        final PageDelta delta,
                        final boolean publish,
                        final ResourceName name,
                        final UUID templateId) {

        final Page page = new Page(name, delta.getTitle());

        if (publish) {
            page.publish(actor);
        }

        if (templateId != null) {
            final Template template =
                _dao.find(Template.class, templateId);
            page.template(template);
        }

        _pageHelper.assignParagraphs(page, delta);

        final Template template = page.computeTemplate(null);

        if (template != null) {
            _pageHelper.validateFieldsForPage(
                page.paragraphs(), template.definition());

        }

        create(actor, happenedOn, parentFolder, page);

        return page;
    }
}
