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

import ccc.api.PageDelta;
import ccc.contentcreator.client.GwtJson;
import ccc.serialization.JsonKeys;
import ccc.types.ID;

import com.google.gwt.http.client.RequestBuilder;


/**
 * Remote action for page updating.
 *
 * @author Civic Computing Ltd.
 */
public class UpdatePageAction
    extends
        RemotingAction {

    private final ID _pageId;
    private final PageDelta _details;
    private final String _comment;
    private final boolean _majorChange;


    /**
     * Constructor.
     * @param majorChange Is this update a major change.
     * @param comment A comment describing the update.
     * @param details Details of the update.
     * @param pageId The id of the page to update.
     */
    public UpdatePageAction(final ID pageId,
                             final PageDelta details,
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
