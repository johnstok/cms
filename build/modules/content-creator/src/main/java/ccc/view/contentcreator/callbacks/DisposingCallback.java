package ccc.view.contentcreator.callbacks;

import ccc.view.contentcreator.client.GwtApp;
import ccc.view.contentcreator.dialogs.AppDialog;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * A simple call-back that displays an error or disposes the dialog.
 *
 * @author Civic Computing Ltd.
 */
public final class DisposingCallback implements AsyncCallback<Void> {

    private final AppDialog _dialog;
    private final GwtApp    _app;

    /**
     * Constructor.
     *
     * @param dialog The dialog that will hidden.
     */
    public DisposingCallback(final GwtApp app, final AppDialog dialog) {
        _dialog = dialog;
        _app = app;
    }

    /** {@inheritDoc} */
    public void onFailure(final Throwable arg0) {
        _app.alert("Error: "+arg0.getMessage());
    }

    /** {@inheritDoc} */
    public void onSuccess(final Void arg0) {
        _dialog.hide();
    }
}
