/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.dialogs;


import static ccc.contentcreator.validation.Validations.*;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.UserTable;
import ccc.contentcreator.validation.Validate;
import ccc.services.api.UserDelta;
import ccc.services.api.UserSummary;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextField;


/**
 * Dialog for user password editing.
 *
 * @author Civic Computing Ltd
 */
public class EditUserPwDialog extends AbstractEditDialog {

    private final TextField<String> _username = new TextField<String>();
    private final TextField<String> _password1 = new TextField<String>();
    private final TextField<String> _password2 = new TextField<String>();

    private final UserDelta _userDTO;
    private final UserTable _userTable;

    /**
     * Constructor.
     *
     * @param userDTO The userDTO of the selected user.
     * @param userTable The user table.
     */
    public EditUserPwDialog(final UserDelta userDTO,
                            final UserTable userTable) {
        super(Globals.uiConstants().editUserPw());

        _userTable = userTable;
        _userDTO = userDTO;

        _username.setFieldLabel(constants().username());
        _username.setReadOnly(true);
        _username.setId(constants().username());
        _username.setValue(_userDTO.getUsername());
        addField(_username);

        _password1.setPassword(true);
        _password1.setFieldLabel(constants().password());
        _password1.setId(constants().password());
        addField(_password1);

        _password2.setPassword(true);
        _password2.setFieldLabel(constants().confirmPassword());
        _password2.setId(constants().confirmPassword());
        addField(_password2);

        setPanelId("UserPwPanel");
    }

    /** {@inheritDoc} */
    @Override protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                Validate.callTo(updateUser())
                    .check(notEmpty(_username))
                    .stopIfInError()
                    .check(minLength(_username, Globals.MIN_USER_NAME_LENGTH))
                    .check(notValidResourceName(_username))
                    .check(matchingPasswords(
                        _password1.getValue(), _password2.getValue()))
                    .check(passwordStrength(_password1.getValue()))
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

                String password = null;
                final String pw1 = _password1.getValue();
                if (null != pw1 && !pw1.trim().equals("")) {
                    password = pw1;
                }
                _userDTO.setPassword(password);

                commands().updateUser(
                    _userDTO,
                    new ErrorReportingCallback<UserSummary>() {
                        public void onSuccess(final UserSummary result) {
                            _userTable.refreshUsers();
                            close();
                        }
                    }
                );
            }
        };
    }
}
