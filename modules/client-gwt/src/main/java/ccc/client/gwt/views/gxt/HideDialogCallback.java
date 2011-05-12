package ccc.client.gwt.views.gxt;

import ccc.api.types.DBC;
import ccc.client.core.DefaultCallback;
import ccc.client.core.LegacyView;

/**
 * Callback that hides a dialog.
 *
 * @param <T> The return type the callback will handle.
 *
 * @author Civic Computing Ltd.
 */
public final class HideDialogCallback<T>
    extends
        DefaultCallback<T> {

    private final LegacyView _view;

    /**
     * Constructor.
     *
     * @param actionName The action being performed.
     * @param view       The view to dispose.
     */
    public HideDialogCallback(final String actionName,
                              final LegacyView view) {
        super(actionName);
        _view = DBC.require().notNull(view);
    }

    /** {@inheritDoc} */
    @Override public void onSuccess(final T result) {
        _view.hide();
    }
}
