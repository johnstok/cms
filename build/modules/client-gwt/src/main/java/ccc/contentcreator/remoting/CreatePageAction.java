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
package ccc.contentcreator.remoting;

import java.util.UUID;

import ccc.contentcreator.core.GwtJson;
import ccc.contentcreator.core.RemotingAction;
import ccc.rest.dto.PageDelta;
import ccc.rest.dto.ResourceSummary;
import ccc.serialization.JsonKeys;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;


/**
 * Create a page.
 *
 * @author Civic Computing Ltd.
 */
public abstract class CreatePageAction
    extends
        RemotingAction {

    private final UUID _parentFolder;
    private final PageDelta _page;
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
    public CreatePageAction(final UUID parentFolder,
                             final PageDelta page,
                             final String name,
                             final UUID template,
                             final String title,
                             final String comment,
                             final boolean majorChange) {
        super(GLOBALS.uiConstants().createPage(), RequestBuilder.POST);
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
    protected String getPath() {
        return "/pages";
    }


    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final ResourceSummary rs = parseResourceSummary(response);
        execute(rs);
    }


    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final GwtJson json = new GwtJson();
        json.set(JsonKeys.PARENT_ID, _parentFolder);
        json.set(JsonKeys.DELTA, _page);
        json.set(JsonKeys.NAME, _name);
        json.set(JsonKeys.TEMPLATE_ID, _template);
        json.set(JsonKeys.TITLE, _title);
        json.set(JsonKeys.COMMENT, _comment);
        json.set(JsonKeys.MAJOR_CHANGE, _majorChange);
        return json.toString();
    }


    /**
     * Handle the summary returned when a page is successfully created.
     *
     * @param rs The page summary.
     */
    protected abstract void execute(ResourceSummary rs);
}
