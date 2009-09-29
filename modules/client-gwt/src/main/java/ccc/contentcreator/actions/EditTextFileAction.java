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
import ccc.rest.dto.TextFileDelta;

import com.google.gwt.http.client.RequestBuilder;


/**
 * Action for updating the text file's content.
 *
 * @author Civic Computing Ltd.
 */
public class EditTextFileAction
    extends
        RemotingAction {

    private final TextFileDelta _dto;

    /**
     * Constructor.
     *
     * @param dto The dto of the file.
     */
    public EditTextFileAction(final TextFileDelta dto) {
        super(UI_CONSTANTS.updateTextFile(), RequestBuilder.POST);
        _dto = dto;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/files/"+_dto.getId();
    }

    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final GwtJson json = new GwtJson();
        _dto.toJson(json);
        return json.toString();
    }
}
