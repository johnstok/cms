package ccc.contentcreator.actions;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.CreateAliasDialog;
import ccc.services.api.ResourceSummary;

/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class CreateAliasAction
    implements
        Action {

    private final SingleSelectionModel<ResourceSummaryModelData> _selectionModel;
    private ResourceSummary _root;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     * @param root The root of current resource tree.
     */
    public CreateAliasAction(final SingleSelectionModel<ResourceSummaryModelData> selectionModel,
                             final ResourceSummary root) {
        _selectionModel = selectionModel;
        _root = root;
    }

    /** {@inheritDoc} */
    public void execute() {
        new CreateAliasDialog(_selectionModel, _root).show();
    }
}
