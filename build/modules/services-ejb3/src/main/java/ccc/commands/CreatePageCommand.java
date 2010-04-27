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

import ccc.api.core.PageDto;
import ccc.api.types.CommandType;
import ccc.api.types.Paragraph;
import ccc.domain.PageEntity;
import ccc.domain.RevisionMetadata;
import ccc.domain.TemplateEntity;
import ccc.domain.UserEntity;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;


/**
 * Command: create a new page.
 *
 * @author Civic Computing Ltd.
 */
class CreatePageCommand extends CreateResourceCommand<PageEntity> {

    private final PageDto _page;
    private final UUID _parentFolder;


    /**
     * Constructor.
     *
     * @param repository The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     * @param parentFolder The folder in which the page will be created.
     * @param page The new page to create.
     */
    public CreatePageCommand(final ResourceRepository repository,
                             final LogEntryRepository audit,
                             final UUID parentFolder,
                             final PageDto page) {
        super(repository, audit);
        _parentFolder = parentFolder;
        _page = page;
    }


    /** {@inheritDoc} */
    @Override
    public PageEntity doExecute(final UserEntity actor,
                          final Date happenedOn) {

        final TemplateEntity template =
            (null==_page.getTemplate())
                ? null
                : getRepository().find(TemplateEntity.class, _page.getTemplate());

        final RevisionMetadata rm =
            new RevisionMetadata(happenedOn,
                actor,
                _page.getMajorChange(),
                (_page.getComment() == null || _page.getComment().isEmpty())
                    ? "Created."
                    : _page.getComment());

        final Set<Paragraph> paras = _page.getParagraphs();
        final PageEntity page =
            new PageEntity(
                _page.getName(),
                _page.getTitle(),
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
