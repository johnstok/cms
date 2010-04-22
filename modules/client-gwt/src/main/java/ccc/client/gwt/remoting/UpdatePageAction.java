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
import ccc.client.gwt.core.GwtJson;
import ccc.client.gwt.core.RemotingAction;
import ccc.plugins.s11n.JsonKeys;

import com.google.gwt.http.client.RequestBuilder;


/**
 * Remote action for page updating.
 *
 * @author Civic Computing Ltd.
 */
public class UpdatePageAction
    extends
        RemotingAction {

    private final UUID _pageId;
    private final PageDto _details;
    private final String _comment;
    private final boolean _majorChange;


    /**
     * Constructor.
     * @param majorChange Is this update a major change.
     * @param comment A comment describing the update.
     * @param details Details of the update.
     * @param pageId The id of the page to update.
     */
    // FIXME: Use the fields from PageDto instead.
    public UpdatePageAction(final UUID pageId,
                             final PageDto details,
                             final String comment,
                             final boolean majorChange) {
        super(UI_CONSTANTS.updateContent(), RequestBuilder.POST);
        _pageId = pageId;
        _details = details;
        _comment = comment;
        _majorChange = majorChange;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/pages/"+_pageId;
    }


    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final GwtJson json = new GwtJson();
        json.set(JsonKeys.MAJOR_CHANGE, _majorChange);
        json.set(JsonKeys.COMMENT, _comment);
        json.set(JsonKeys.DELTA, _details);
        return json.toString();
    }
}
