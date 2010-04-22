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
package ccc.client.gwt.remoting;

import java.util.UUID;

import ccc.api.dto.PageDto;
import ccc.client.gwt.binding.ResourceSummaryModelData;
import ccc.client.gwt.core.RemotingAction;
import ccc.client.gwt.core.Request;


/**
 * Create a page.
 *
 * @author Civic Computing Ltd.
 */
public final class CreatePageAction
    extends
        RemotingAction {

    private final UUID _parentFolder;
    private final PageDto _page;
    private final String _name;
    private final UUID _template;
    private final String _title;
    private final String _comment;
    private final boolean _majorChange;


    /**
     * Constructor.
     *
     * @param parentFolder The folder where the page will be created.
     * @param page The page's content.
     * @param name The page's name.
     * @param template The page's template.
     * @param title The page's title.
     * @param comment A comment describing the update.
     * @param majorChange Is this update a major change.
     *
     */
    // FIXME: Use the properties from PageDto instead.
    public CreatePageAction(final UUID parentFolder,
                             final PageDto page,
                             final String name,
                             final UUID template,
                             final String title,
                             final String comment,
                             final boolean majorChange) {
        _parentFolder = parentFolder;
        _page = page;
        _name = name;
        _template = template;
        _title = title;
        _comment = comment;
        _majorChange = majorChange;
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest() {
        return ResourceSummaryModelData.createPage(
            _parentFolder,
            _page,
            _name,
            _template,
            _title,
            _comment,
            _majorChange);
    }
}
