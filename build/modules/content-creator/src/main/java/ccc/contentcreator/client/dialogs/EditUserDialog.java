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
package ccc.contentcreator.client.dialogs;


import static ccc.contentcreator.client.Validations.*;

import java.util.HashSet;
import java.util.Set;

import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.UserTable;
import ccc.contentcreator.client.Validate;
import ccc.contentcreator.client.Validator;
import ccc.contentcreator.dto.UserDTO;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.TextField;


/**
 * Dialog for user editing.
 *
 * @author Civic Computing Ltd
 */
public class EditUserDialog extends AbstractEditDialog {

    private final TextField<String> _username = new TextField<String>();
    private final TextField<String> _email = new TextField<String>();
    private final TextField<String> _password1 = new TextField<String>();
    private final TextField<String> _password2 = new TextField<String>();

    private UserDTO _userDTO = new UserDTO();
    private CheckBoxGroup _checkGroup = new CheckBoxGroup();
    private final UserTable _userTable;

    private static final String ROLE = "role";

    /**
     * Constructor.
     *
     * @param userDTO The userDTO of the selected user.
     * @param userTable The user table.
     */
    public EditUserDialog(final UserDTO userDTO, final UserTable userTable) {
        super(Globals.uiConstants().editUser());

        _userTable = userTable;
        _userDTO = userDTO;

        _username.setFieldLabel(constants().username());
        _username.setAllowBlank(false);
        _username.setId(constants().username());
        _username.setValue(_userDTO.getUsername());
        addField(_username);

        _email.setFieldLabel(constants().email());
        _email.setAllowBlank(false);
        _email.setId(constants().email());
        _email.setValue(_userDTO.getEmail());
        addField(_email);

        _password1.setPassword(true);
        _password1.setFieldLabel(constants().password());
        _password1.setId(constants().password());
        addField(_password1);

        _password2.setPassword(true);
        _password2.setFieldLabel(constants().confirmPassword());
        _password2.setId(constants().confirmPassword());
        addField(_password2);

        final Set<String> userRoles = _userDTO.getRoles();

        final CheckBox check1 = new CheckBox();
        check1.setBoxLabel(constants().contentCreator());
        check1.setId(constants().contentCreator());
        check1.setData(ROLE , "CONTENT_CREATOR");
        if (userRoles.contains("CONTENT_CREATOR")) {
            check1.setValue(true);
        }

        final CheckBox check2 = new CheckBox();
        check2.setBoxLabel(constants().siteBuilder());
        check2.setId(constants().siteBuilder());
        check2.setData(ROLE, "SITE_BUILDER");
        if (userRoles.contains("SITE_BUILDER")) {
            check2.setValue(true);
        }

        final CheckBox check3 = new CheckBox();
        check3.setBoxLabel(constants().administrator());
        check3.setId(constants().administrator());
        check3.setData(ROLE, "ADMINISTRATOR");
        if (userRoles.contains("ADMINISTRATOR")) {
            check3.setValue(true);
        }

        _checkGroup.setFieldLabel(constants().roles());
        _checkGroup.setId(constants().roles());
        _checkGroup.add(check1);
        _checkGroup.add(check2);
        _checkGroup.add(check3);
        addField(_checkGroup);

        setPanelId("UserPanel");
    }

    /** {@inheritDoc} */
    @Override protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                Validate.callTo(updateUser())
                    .check(notEmpty(_username))
                    .check(notEmpty(_email))
                    // TODO: email format
                    // TODO: username format
                    .stopIfInError()
                    .check(matchingPasswords(
                        _password1.getValue(), _password2.getValue()))
                    .check(uniqueUsername(_userDTO, _username.getValue()))
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
                _userDTO.setUsername(_username.getValue());
                _userDTO.setEmail(_email.getValue());
                final Set<String> roles = new HashSet<String>();
                for (final CheckBox box : _checkGroup.getValues()) {
                    roles.add((String) box.getData(ROLE));
                }
                _userDTO.setRoles(roles);

                String password = null;
                if (null != _password1.getValue()
                        && _password1.getValue().trim().equals("")) {
                    password = _password1.getValue();
                }

                Globals.resourceService().updateUser(
                    _userDTO,
                    password,
                    new ErrorReportingCallback<Void>() {
                        public void onSuccess(final Void result) {
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
    private Validator uniqueUsername(
                                     final UserDTO userDTO,
                                     final String username) {
        return new Validator() {
            public void validate(final Validate validate) {
                if (userDTO.getUsername().equals(username)) {
                    validate.next();
                } else {
                    Globals.resourceService().usernameExists(
                        username,
                        new ErrorReportingCallback<Boolean>(){
                            public void onSuccess(final Boolean exists) {
                                if (exists) {
                                    // TODO: I18n
                                    validate.addMessage(
                                        "A user with username '"
                                        + username
                                        + "' already exists."
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


    /**
     * Factory method for password validators.
     *
     * @param pw1 The password to check.
     * @param pw2 The password to check.
     * @return A new instance of the password validator.
     */
    private Validator matchingPasswords(final String pw1, final String pw2) {
        return new Validator() {
            public void validate(final Validate validate) {
                if (pw1 != null && pw2 != null && !pw1.equals(pw2)) {
                    validate.addMessage(
                        constants().passwordsDidNotMatch()
                    );
                }
                validate.next();
            }
        };
    }
}
