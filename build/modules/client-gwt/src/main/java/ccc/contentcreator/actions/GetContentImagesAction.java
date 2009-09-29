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

import java.util.ArrayList;
import java.util.Collection;

import ccc.contentcreator.client.GwtJson;
import ccc.rest.dto.FileDto;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;


/**
 * Get a list of all images from the CCC server.
 *
 * @author Civic Computing Ltd.
 */
public abstract class GetContentImagesAction
    extends
        RemotingAction {

    /**
     * Constructor.
     *
     * @param actionName Local-specific name for the action.
     */
    public GetContentImagesAction(final String actionName) {
        super(actionName);
    }

    /** {@inheritDoc} */
    @Override protected String getPath() { return "/files/images"; }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final JSONArray result = JSONParser.parse(response.getText()).isArray();
        final Collection<FileDto> files = new ArrayList<FileDto>();
        for (int i=0; i<result.size(); i++) {
            files.add(new FileDto(new GwtJson(result.get(i).isObject())));
        }
        execute(files);
    }

    /**
     * Handle the data returned from the server.
     *
     * @param images The available images.
     */
    protected abstract void execute(Collection<FileDto> images);
}
