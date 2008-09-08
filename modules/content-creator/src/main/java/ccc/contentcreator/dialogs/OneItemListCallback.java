/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.dialogs;

import java.util.Collections;
import java.util.List;

import ccc.contentcreator.dto.FolderDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class OneItemListCallback implements AsyncCallback<FolderDTO> {

    private final AsyncCallback<List<FolderDTO>> _callback;

    /**
     * Constructor.
     *
     * @param callback The callback to wrap.
     */
    public OneItemListCallback(final AsyncCallback<List<FolderDTO>> callback) {
        _callback = callback;
    }

    /** {@inheritDoc} */
    public void onFailure(final Throwable arg0) {
        _callback.onFailure(arg0);
    }

    /** {@inheritDoc} */
    public void onSuccess(final FolderDTO arg0) {
        _callback.onSuccess(Collections.singletonList(arg0));
    }

}
