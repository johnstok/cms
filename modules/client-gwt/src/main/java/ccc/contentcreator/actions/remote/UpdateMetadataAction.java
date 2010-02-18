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
package ccc.contentcreator.actions.remote;

import java.util.Map;
import java.util.UUID;

import ccc.contentcreator.core.GwtJson;
import ccc.contentcreator.core.RemotingAction;

import com.google.gwt.http.client.RequestBuilder;



/**
 * Remote action for metadata updating.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateMetadataAction
    extends
        RemotingAction {

    private final UUID _resourceId;
    private final String _title;
    private final String _description;
    private final String _tags;
    private final Map<String, String> _metadata;


    /**
     * Constructor.
     *
     * @param metadata Key value pairs.
     * @param tags Tags for a resource.
     * @param description The resource's description.
     * @param title The resource's title.
     * @param resourceId The resource's id.
     */
    public UpdateMetadataAction(final UUID resourceId,
                                 final String title,
                                 final String description,
                                 final String tags,
                                 final Map<String, String> metadata) {
        super(UI_CONSTANTS.updateTags(), RequestBuilder.POST);
        _resourceId = resourceId;
        _title = title;
        _description = description;
        _tags = tags;
        _metadata = metadata;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/resources/"+_resourceId+"/metadata";
    }


    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final GwtJson json = new GwtJson();
        json.set("title", _title);
        json.set("description", _description);
        json.set("tags", _tags);
        json.set("metadata", _metadata);
        return json.toString();
    }
}
