package ccc.client.actions;

import ccc.api.core.Group;
import ccc.api.core.PagedCollection;
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
        super(OpenCreateUserAction.UI_CONSTANTS.createUser());
    }

    @Override
    public void onSuccess(final PagedCollection<Group> groups) {
        new CreateUserPresenter(
            InternalServices.DIALOGS.createUser(
                groups.getElements()));
    }
}
