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
package ccc.contentcreator.actions;

import java.util.UUID;

import ccc.contentcreator.core.GwtJson;
import ccc.contentcreator.core.RemotingAction;
import ccc.rest.dto.PageDelta;

import com.google.gwt.http.client.RequestBuilder;


/**
 * Remote action for working copy updating.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateWorkingCopyAction
    extends
        RemotingAction {

    private final UUID _pageId;
    private final PageDelta _workingCopy;


    /**
     * Constructor.
     *
     * @param workingCopy The new working copy.
     * @param pageId The new page.
     */
    public UpdateWorkingCopyAction(final UUID pageId,
                                    final PageDelta workingCopy) {
        super(UI_CONSTANTS.saveDraft(), RequestBuilder.POST);
        _pageId = pageId;
        _workingCopy = workingCopy;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/pages/"+_pageId+"/wc";
    }


    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final GwtJson json = new GwtJson();
        _workingCopy.toJson(json);
        return json.toString();
    }
}
