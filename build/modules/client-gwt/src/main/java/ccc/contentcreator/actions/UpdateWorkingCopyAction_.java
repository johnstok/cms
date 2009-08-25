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
import ccc.types.ID;

import com.google.gwt.http.client.RequestBuilder;


/**
 * Remote action for working copy updating.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateWorkingCopyAction_
    extends
        RemotingAction {

    private final ID _pageId;
    private final PageDelta _workingCopy;


    /**
     * Constructor.
     *
     * @param workingCopy The new working copy.
     * @param pageId The new page.
     */
    public UpdateWorkingCopyAction_(final ID pageId,
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
