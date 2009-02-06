package ccc.contentcreator.actions;

import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.CreateAliasDialog;
import ccc.services.api.ResourceSummary;

import com.extjs.gxt.ui.client.data.ModelData;

/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class CreateAliasAction
    implements
        Action {

    private final SingleSelectionModel _selectionModel;
    private ResourceSummary _root;

    /**
     * Constructor.
     *
     * @param selectionModel
     * @param root
     */
    public CreateAliasAction(final SingleSelectionModel selectionModel,
                             final ResourceSummary root) {
        _selectionModel = selectionModel;
        _root = root;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ModelData item =
            _selectionModel.getSelectedModel();
        new CreateAliasDialog(item, _root).show();
    }
}
