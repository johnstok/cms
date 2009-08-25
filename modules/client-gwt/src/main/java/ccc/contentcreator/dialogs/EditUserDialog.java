/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev: 467 $
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.dialogs;


import static ccc.contentcreator.validation.Validations.*;

import java.util.HashSet;
import java.util.Set;

import ccc.api.UserSummary;
import ccc.contentcreator.actions.UpdateUserAction_;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.contentcreator.client.UserTable;
import ccc.contentcreator.validation.Validate;
import ccc.types.ID;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.http.client.Response;


/**
 * Dialog for user editing.
 *
 * @author Civic Computing Ltd
 */
public class EditUserDialog extends AbstractEditDialog {

    private final TextField<String> _username = new TextField<String>();
    private final TextField<String> _email = new TextField<String>();
    private final TextArea          _roles = new TextArea();

    private final ID          _userId;
    private final UserSummary _userDTO;
    private final UserTable   _userTable;

    /** ROLE_HEIGHT : int. */
    private static final int ROLE_HEIGHT = 200;

    /**
     * Constructor.
     *
     * @param userId The ID of the selected user.
     * @param userDTO The userDTO of the selected user.
     * @param userTable The user table.
     */
    public EditUserDialog(final ID userId,
                          final UserSummary userDTO,
                          final UserTable userTable) {
        super(new IGlobalsImpl().uiConstants().editUser(), new IGlobalsImpl());

        _userId    = userId;
        _userDTO   = userDTO;
        _userTable = userTable;

        _username.setFieldLabel(constants().username());
        _username.setReadOnly(true);
        _username.setId("username");
        _username.setValue(_userDTO.getUsername().toString());
        addField(_username);

        _email.setFieldLabel(constants().email());
        _email.setAllowBlank(false);
        _email.setId("useremail");
        _email.setValue(_userDTO.getEmail());
        addField(_email);

        _roles.setFieldLabel(_constants.roles());
        _roles.setId("resource-roles");
        _roles.setHeight(ROLE_HEIGHT);
        final StringBuilder rolesString = new StringBuilder();
        for (final String role : _userDTO.getRoles()) {
            rolesString.append(role);
            rolesString.append('\n');
        }
        _roles.setValue(rolesString.toString());
        addField(_roles);


        setPanelId("UserPanel");
    }

    /** {@inheritDoc} */
    @Override protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                Validate.callTo(updateUser())
                    .check(notEmpty(_email))
                    .stopIfInError()
                    .check(notValidEmail(_email))
                    .callMethodOr(reportErrors());
            }
        };
    }


    /**
     * Updates the edited user and calls user list refreshing.
     *
     * @return
     */
    private Runnable updateUser() {
        return new Runnable() {
            public void run() {
                _userDTO.setEmail(_email.getValue());

                final Set<String> validRoles = new HashSet<String>();
                final String roleValue = _roles.getValue();
                final String[] roles =
                    (null==roleValue)
                        ? new String[]{}
                        : roleValue.split("\n|\r|\r\n");
                for (final String role : roles) {
                    final String cleanRole = role.trim();
                    if (cleanRole.length() > 0) {
                        validRoles.add(cleanRole);
                    }
                }
                _userDTO.setRoles(validRoles);

                new UpdateUserAction_(
                    _userId,
                    _userDTO
                ){
                    /** {@inheritDoc} */
                    @Override protected void onNoContent(final Response response) {
                        // TODO: Just update the edited row model data.
                        _userTable.refreshUsers();
                        hide();
                    }

                }.execute();
            }
        };
    }
}
