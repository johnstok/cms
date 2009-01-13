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

import ccc.contentcreator.client.Globals;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * A callback extension that reports failure with a dialog.
 *
 * @param <T> The type of the result.
 *
 * @author Civic Computing Ltd.
 */
public abstract class ErrorReportingCallback<T> implements AsyncCallback<T> {

    private final Exception _e = new Exception("Async call failed.");

    /** {@inheritDoc} */
    public final void onFailure(final Throwable caught) {
        _e.initCause(caught);
        Globals.unexpectedError(_e);
    }
}
