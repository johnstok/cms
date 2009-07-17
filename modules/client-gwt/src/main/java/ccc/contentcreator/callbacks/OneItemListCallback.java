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
package ccc.contentcreator.callbacks;

import java.util.Collections;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * This callback can be used to decorate an existing an existing callback,
 * allowing its result to instead be returned as the single element of a list.
 *
 * @param <T>
 * @author Civic Computing Ltd.
 */
public class OneItemListCallback<T> implements AsyncCallback<T> {

    private final AsyncCallback<List<T>> _callback;

    /**
     * Constructor.
     *
     * @param callback The callback to wrap.
     */
    public OneItemListCallback(final AsyncCallback<List<T>> callback) {
        _callback = callback;
    }

    /** {@inheritDoc} */
    public void onFailure(final Throwable arg0) {
        _callback.onFailure(arg0);
    }

    /** {@inheritDoc} */
    public void onSuccess(final T arg0) {
        _callback.onSuccess(Collections.singletonList(arg0));
    }

}
