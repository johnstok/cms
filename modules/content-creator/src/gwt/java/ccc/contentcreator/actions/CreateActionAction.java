package ccc.contentcreator.actions;

import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.CreateActionDialog;

/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class CreateActionAction
    implements
        Action {

    private SingleSelectionModel _ssm;

    /**
     * Constructor.
     *
     * @param ssm The selection model.
     */
    public CreateActionAction(final SingleSelectionModel ssm) {
        _ssm = ssm;
    }

    /** {@inheritDoc} */
    public void execute() {
        new CreateActionDialog(_ssm.tableSelection().getId()).show();
    }
}
