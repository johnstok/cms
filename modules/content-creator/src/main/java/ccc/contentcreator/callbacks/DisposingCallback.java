package ccc.contentcreator.callbacks;

import ccc.contentcreator.dialogs.Closeable;

import com.extjs.gxt.ui.client.widget.Window;

/**
 * A simple call-back that displays an alert on error or disposes the window on
 * success.
 *
 * @author Civic Computing Ltd.
 */
public final class DisposingCallback extends ErrorReportingCallback<Void> {

    private final Closeable _window;

    /**
     * Constructor.
     *
     * @param window The window that will closed.
     * @param action The action being performed when the error happened.
     */
    public DisposingCallback(final Window window, final String action) {
        super(action);
        _window = new Closeable() {
            @Override public void close() {
                window.close();
            }
        };
    }

    /**
     * Constructor.
     *
     * @param c The closeable UI component.
     * @param action The action performed by the callback.
     */
    public DisposingCallback(final Closeable c, final String action) {
        super(action);
        _window = c;
    }

    /** {@inheritDoc} */
    public void onSuccess(final Void arg0) {
        _window.close();
    }
}
