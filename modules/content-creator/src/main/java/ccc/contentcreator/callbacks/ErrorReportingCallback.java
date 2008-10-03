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

import ccc.contentcreator.api.Application;
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


    /**
     * Constructor.
     *
     * @param application The application instance for use by this callback.
     */
    public ErrorReportingCallback(final Application application) {
        this();
    }

    /**
     * Constructor.
     */
    public ErrorReportingCallback() { super(); }

    /**
     * {@inheritDoc}
     */
    public final void onFailure(final Throwable caught) {
        // TODO: should be using a message, not a constant.
        Globals.alert(
            Globals.uiConstants().error()
            + ": " //$NON-NLS-1$
            + caught.getMessage());
    }
}
