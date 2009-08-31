package ccc.contentcreator.actions;

import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.CreateAliasDialog;
import ccc.rest.ResourceSummary;

/**
 * Create an alias.
 *
 * @author Civic Computing Ltd.
 */
public final class OpenCreateAliasAction
    implements
        Action {

    private final SingleSelectionModel _selectionModel;
    private ResourceSummary _root;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     * @param root The root of current resource tree.
     */
    public OpenCreateAliasAction(final SingleSelectionModel selectionModel,
                             final ResourceSummary root) {
        _selectionModel = selectionModel;
        _root = root;
    }

    /** {@inheritDoc} */
    public void execute() {
        new CreateAliasDialog(_selectionModel, _root).show();
    }
}
