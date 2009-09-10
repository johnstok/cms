/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev: 1744 $
 * Modified by   $Author: petteri $
 * Modified on   $Date: 2009-08-28 16:17:04 +0100 (Fri, 28 Aug 2009) $
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.actions;

import java.util.Map;
import java.util.UUID;

import ccc.contentcreator.client.GwtJson;

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