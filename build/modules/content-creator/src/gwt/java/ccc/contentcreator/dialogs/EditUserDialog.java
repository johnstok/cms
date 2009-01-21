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

import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.UserTable;
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validator;
import ccc.services.api.UserDelta;
import ccc.services.api.UserSummary;

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

    private UserDelta _userDTO = new UserDelta();
    private CheckBoxGroup _checkGroup = new CheckBoxGroup();
    private final UserTable _userTable;

    private static final String ROLE = "role";

    /**
     * Constructor.
     *
     * @param userDTO The userDTO of the selected user.
     * @param userTable The user table.
     */
    public EditUserDialog(final UserDelta userDTO, final UserTable userTable) {
        super(Globals.uiConstants().editUser());

        _userTable = userTable;
        _userDTO = userDTO;

        _username.setFieldLabel(constants().username());
        _username.setAllowBlank(false);
        _username.setMinLength(Globals.MIN_USER_NAME_LENGTH);
        _username.setId(constants().username());
        _username.setValue(_userDTO._username);
        addField(_username);

        _email.setFieldLabel(constants().email());
        _email.setAllowBlank(false);
        _email.setId(constants().email());
        _email.setValue(_userDTO._email);
        addField(_email);

        _password1.setPassword(true);
        _password1.setFieldLabel(constants().password());
        _password1.setId(constants().password());
        addField(_password1);

        _password2.setPassword(true);
        _password2.setFieldLabel(constants().confirmPassword());
        _password2.setId(constants().confirmPassword());
        addField(_password2);

        final Set<String> userRoles = _userDTO._roles;

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
                    .stopIfInError()
                    .check(minLength(_username, Globals.MIN_USER_NAME_LENGTH))
                    .check(notValidResourceName(_username))
                    .check(notValidEmail(_email))
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
                _userDTO._username = _username.getValue();
                _userDTO._email = _email.getValue();
                final Set<String> roles = new HashSet<String>();
                for (final CheckBox box : _checkGroup.getValues()) {
                    roles.add((String) box.getData(ROLE));
                }
                _userDTO._roles = roles;

                String password = null;
                final String pw1 = _password1.getValue();
                if (null != pw1 && !pw1.trim().equals("")) {
                    password = pw1;
                }
                _userDTO._password = password;

                commands().updateUser(
                    _userDTO,
                    new ErrorReportingCallback<UserSummary>() {
                        public void onSuccess(final UserSummary result) {
                            // TODO: Update the row with result
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
                                     final String username) {
        return new Validator() {
            public void validate(final Validate validate) {
                if (userDTO._username.equals(username)) {
                    validate.next();
                } else {
                    queries().usernameExists(
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
