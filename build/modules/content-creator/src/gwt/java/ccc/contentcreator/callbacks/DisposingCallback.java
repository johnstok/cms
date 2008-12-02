package ccc.contentcreator.callbacks;

import com.extjs.gxt.ui.client.widget.Window;

/**
 * A simple call-back that displays an alert on error or disposes the window on
 * success.
 *
 * @author Civic Computing Ltd.
 */
public final class DisposingCallback extends ErrorReportingCallback<Void> {

    private final Window _window;

    /**
     * Constructor.
     *
     * @param window The window that will hidden.
     */
    public DisposingCallback(final Window window) {
        _window = window;
    }

    /** {@inheritDoc} */
    public void onSuccess(final Void arg0) {
        _window.close();
    }
}
