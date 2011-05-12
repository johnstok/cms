package ccc.client.actions;

import ccc.api.core.Group;
import ccc.api.core.PagedCollection;
import ccc.client.core.Action;
import ccc.client.core.DefaultCallback;
import ccc.client.core.InternalServices;
import ccc.client.presenters.CreateUserPresenter;

/**
 * Callback to open the 'create user' dialog.
 *
 * @author Civic Computing Ltd.
 */
final class OpenCreateUserDialog
    extends
        DefaultCallback<PagedCollection<Group>> {

    /**
     * Constructor.
     */
    public OpenCreateUserDialog() {
        super(Action.UI_CONSTANTS.createUser());
    }

    /** {@inheritDoc} */
    @Override
    public void onSuccess(final PagedCollection<Group> groups) {
        new CreateUserPresenter(
            InternalServices.dialogs.createUser(
                groups.getElements()));
    }
}
