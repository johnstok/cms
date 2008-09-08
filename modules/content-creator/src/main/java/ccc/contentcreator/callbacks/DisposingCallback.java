package ccc.contentcreator.callbacks;

import ccc.contentcreator.api.Application;
import ccc.contentcreator.dialogs.ApplicationDialog;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * A simple call-back that displays an alert on error or disposes the dialog on
 * success.
 *
 * @author Civic Computing Ltd.
 */
public final class DisposingCallback implements AsyncCallback<Void> {

    private final ApplicationDialog _dialog;
    private final Application       _app;

    /**
     * Constructor.
     *
     * @param app A reference to a valid application.
     * @param dialog The dialog that will hidden.
     */
    public DisposingCallback(final Application app,
                             final ApplicationDialog dialog) {
        _dialog = dialog;
        _app = app;
    }

    /** {@inheritDoc} */
    public void onFailure(final Throwable arg0) {
        // TODO: should be using a message, not a constant.
        _app.alert(
            _app.constants().error()
            + ": " //$NON-NLS-1$
            + arg0.getMessage());
    }

    /** {@inheritDoc} */
    public void onSuccess(final Void arg0) {
        _dialog.hide();
    }
}
