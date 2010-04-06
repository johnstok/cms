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
import java.util.Set;
import java.util.UUID;

import ccc.domain.Page;
import ccc.domain.RevisionMetadata;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.rest.dto.PageDelta;
import ccc.types.CommandType;
import ccc.types.Paragraph;
import ccc.types.ResourceName;


/**
 * Command: create a new page.
 *
 * @author Civic Computing Ltd.
 */
class CreatePageCommand extends CreateResourceCommand<Page> {

    private final UUID _templateId;
    private final boolean _majorChange;
    private final String _comment;
    private final PageDelta _delta;
    private final ResourceName _name;
    private final String _title;
    private final UUID _parentFolder;


    /**
     * Constructor.
     *
     * @param repository The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     * @param parentFolder The folder in which the page will be created.
     * @param delta The contents of the new page.
     * @param name The new page's name.
     * @param templateId The new page's template.
     * @param comment Comment describing the change.
     * @param majorChange Is this a major change.
     * @param title The page's title.
     */
    public CreatePageCommand(final ResourceRepository repository,
                             final LogEntryRepository audit,
                             final UUID parentFolder,
                             final PageDelta delta,
                             final ResourceName name,
                             final String title,
                             final UUID templateId,
                             final String comment,
                             final boolean majorChange) {
        super(repository, audit);
        _parentFolder = parentFolder;
        _delta = delta;
        _name = name;
        _title = title;
        _templateId = templateId;
        _comment = comment;
        _majorChange = majorChange;
    }


    /** {@inheritDoc} */
    @Override
    public Page doExecute(final User actor,
                          final Date happenedOn) {

        final Template template =
            (null==_templateId)
                ? null
                : getRepository().find(Template.class, _templateId);

        final RevisionMetadata rm =
            new RevisionMetadata(happenedOn,
                actor,
                _majorChange,
                _comment == null || _comment.isEmpty() ? "Created." : _comment);

        final Set<Paragraph> paras = _delta.getParagraphs();
        final Page page =
            new Page(
                _name,
                _title,
                template,
                rm,
                paras.toArray(new Paragraph[paras.size()]));

        create(actor, happenedOn, _parentFolder, page);

        return page;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.PAGE_CREATE; }
}
