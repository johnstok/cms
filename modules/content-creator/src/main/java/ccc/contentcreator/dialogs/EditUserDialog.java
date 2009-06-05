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

import ccc.api.ID;
import ccc.api.UserDelta;
import ccc.api.Username;
import ccc.contentcreator.api.ActionNameConstants;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.IGlobals;
import ccc.contentcreator.client.UserTable;
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validator;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;


/**
 * Dialog for user editing.
 *
 * @author Civic Computing Ltd
 */
public class EditUserDialog extends AbstractEditDialog {
    private static final ActionNameConstants USER_ACTIONS =
        GWT.create(ActionNameConstants.class);

    private final TextField<String> _username = new TextField<String>();
    private final TextField<String> _email = new TextField<String>();
    private final TextArea          _roles = new TextArea();

    private final ID        _userId;
    private final UserDelta _userDTO;
    private final UserTable _userTable;

    /**
     * Constructor.
     *
     * @param userId The ID of the selected user.
     * @param userDTO The userDTO of the selected user.
     * @param userTable The user table.
     */
    public EditUserDialog(final ID userId,
                          final UserDelta userDTO,
                          final UserTable userTable) {
        super(Globals.uiConstants().editUser());

        _userId    = userId;
        _userDTO   = userDTO;
        _userTable = userTable;

        _username.setFieldLabel(constants().username());
        _username.setAllowBlank(false);
        _username.setMinLength(IGlobals.MIN_USER_NAME_LENGTH);
        _username.setId(constants().username());
        _username.setValue(_userDTO.getUsername().toString());
        addField(_username);

        _email.setFieldLabel(constants().email());
        _email.setAllowBlank(false);
        _email.setId(constants().email());
        _email.setValue(_userDTO.getEmail());
        addField(_email);

        _roles.setFieldLabel(_constants.roles());
        _roles.setId("resource-roles");
        _roles.setHeight(200);
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
                    .check(notEmpty(_username))
                    .check(notEmpty(_email))
                    .stopIfInError()
                    .check(minLength(_username, IGlobals.MIN_USER_NAME_LENGTH))
                    .check(notValidUserName(_username))
                    .check(notValidEmail(_email))
                    .check(uniqueUsername(_userDTO,
                        new Username(_username.getValue())))
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
                _userDTO.setUsername(new Username(_username.getValue()));
                _userDTO.setEmail(_email.getValue());

                final Set<String> validRoles = new HashSet<String>();
                final String[] roles =
                    _roles.getValue().split("\n|\r|\r\n");
                for (final String role : roles) {
                    final String cleanRole = role.trim();
                    if (cleanRole.length() > 0) {
                        validRoles.add(cleanRole);
                    }
                }
                _userDTO.setRoles(validRoles);


                commands().updateUser(
                    _userId,
                    _userDTO,
                    new ErrorReportingCallback<Void>(_constants.editUser()) {
                        public void onSuccess(final Void result) {
                            // TODO: Just update the edited row model data.
                            _userTable.refreshUsers();
                            close();
                        }
                    }
                );
            }
        };
    }

    /**
     * Factory method for username validators.
     *
     * @param userDTO The original user DTO.
     * @param username The username to check.
     * @return A new instance of the username validator.
     */
    private Validator uniqueUsername(final UserDelta userDTO,
                                     final Username username) {
        return new Validator() {
            public void validate(final Validate validate) {
                if (userDTO.getUsername().equals(username)) {
                    validate.next();
                } else {
                    queries().usernameExists(
                        username,
                        new ErrorReportingCallback<Boolean>(USER_ACTIONS.checkUniqueUsername()){
                            public void onSuccess(final Boolean exists) {
                                if (exists.booleanValue()) {
                                    validate.addMessage(
                                        _messages.userWithUsernameAlreadyExists(username)
                                    );
                                }
                                validate.next();
                            }
                        }
                    );
                }
            }
        };
    }
}
