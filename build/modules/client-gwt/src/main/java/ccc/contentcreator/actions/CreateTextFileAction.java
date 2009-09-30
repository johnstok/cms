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

import ccc.contentcreator.client.GwtJson;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.TextFileDto;
import ccc.serialization.JsonKeys;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class CreateTextFileAction  extends
        RemotingAction {


    private TextFileDto _dto;
    /**
     * Constructor.
     *
     * @param dto Text file DTO.
     */
    public CreateTextFileAction(final TextFileDto dto) {
        super(GLOBALS.uiConstants().createTextFile(), RequestBuilder.POST);

        _dto = dto;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {

        return "/files";
    }

    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final GwtJson json = new GwtJson();
        json.set(JsonKeys.PARENT_ID, _dto.getParentId());
        json.set(JsonKeys.NAME, _dto.getName());
        json.set(JsonKeys.MIME_TYPE, _dto.getMimeType());
        json.set(JsonKeys.MAJOR_CHANGE, _dto.isMajorRevision());
        json.set(JsonKeys.COMMENT, _dto.getRevisionComment());
        json.set(JsonKeys.DATA, _dto.getContent());
        return json.toString();
    }


    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final ResourceSummary rs = parseResourceSummary(response);
        execute(rs);
    }

    /**
     * Handle the result of a successful call.
     *
     * @param folder The folder returned.
     */
    protected abstract void execute(ResourceSummary folder);

}
