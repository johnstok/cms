package ccc.client.callbacks;

import ccc.api.core.PagedCollection;
import ccc.api.core.Revision;
import ccc.client.core.DefaultCallback;
import ccc.client.core.InternalServices;
import ccc.client.core.SingleSelectionModel;

/**
 * Callback handler for resource history dialog opening.
 *
 * @author Civic Computing Ltd.
 */
public class ViewHistoryCallback
    extends
        DefaultCallback<PagedCollection<Revision>> {

    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param actionName The name of the action.
     * @param selectionModel The selection model.
     */
    public ViewHistoryCallback(final String actionName,
                               final SingleSelectionModel selectionModel) {
        super(actionName);
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    @Override
    public void onSuccess(final PagedCollection<Revision> rsCollection) {
        InternalServices.dialogs.viewHistory(
            rsCollection.getElements(),
            _selectionModel.tableSelection().getType(),
            _selectionModel)
        .show();
    }
}
