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
package ccc.contentcreator.actions;

import java.util.Map;

import ccc.contentcreator.client.GwtJson;
import ccc.types.ID;

import com.google.gwt.http.client.RequestBuilder;



/**
 * Remote action for metadata updating.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateMetadataAction_
    extends
        RemotingAction {

    private final ID _resourceId;
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
    public UpdateMetadataAction_(final ID resourceId,
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
