package ccc.client.callbacks;

import ccc.api.core.PagedCollection;
import ccc.api.core.Revision;
import ccc.client.core.DefaultCallback;
import ccc.client.core.InternalServices;
import ccc.client.core.SingleSelectionModel;

/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
final class ViewHistoryCallback
    extends
        DefaultCallback<PagedCollection<Revision>> {

    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param actionName
     */
    private ViewHistoryCallback(final String actionName,
                                final SingleSelectionModel selectionModel) {
        super(actionName);
        _selectionModel = selectionModel;
    }

    @Override
    public void onSuccess(final PagedCollection<Revision> rsCollection) {
        InternalServices.DIALOGS.viewHistory(
            rsCollection.getElements(),
            _selectionModel.tableSelection().getType(),
            _selectionModel)
        .show();
    }
}