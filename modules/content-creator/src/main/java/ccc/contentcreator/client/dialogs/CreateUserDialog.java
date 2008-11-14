/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.client.dialogs;


import static ccc.contentcreator.client.Validations.*;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.Validate;
import ccc.contentcreator.client.Validator;
import ccc.contentcreator.dto.UserDTO;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class CreateUserDialog extends AbstractEditDialog {

    private final TextField<String> _username = new TextField<String>();
    private final TextField<String> _password1 = new TextField<String>();
    private final TextField<String> _password2 = new TextField<String>();
    private final TextField<String> _email = new TextField<String>();

    /**
     * Constructor.
     */
    public CreateUserDialog() {
        super(Globals.uiConstants().createUser());

        _panel.setLabelWidth(150); // Long labels, should fit to one line.

        _username.setFieldLabel(_constants.username());
        _username.setAllowBlank(false);
        _username.setId(_constants.username());
        _panel.add(_username, new FormData("95%"));

        _email.setFieldLabel(_constants.email());
        _email.setAllowBlank(false);
        _email.setId(_constants.email());
        _panel.add(_email, new FormData("95%"));

        _password1.setPassword(true);
        _password1.setFieldLabel(_constants.password());
        _password1.setAllowBlank(false);
        _password1.setId(_constants.password());
        _panel.add(_password1, new FormData("95%"));

        _password2.setPassword(true);
        _password2.setFieldLabel(_constants.confirmPassword());
        _password2.setAllowBlank(false);
        _password2.setId(_constants.confirmPassword());
        _panel.add(_password2, new FormData("95%"));


        // TODO: Remove these set calls - set in super-class.
        _panel.setId("UserPanel");
        _save.setId("userSave");
        _cancel.setId("userCancel");
    }

    /** {@inheritDoc} */
    @Override protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                Validate.callTo(createUser())
                    .check(notEmpty(_username))
                    .check(notEmpty(_email))
                    .check(notEmpty(_password1))
                    .check(notEmpty(_password2))
                    // TODO: email format
                    .stopIfInError()
                    .check(matchingPasswords(
                        _password1.getValue(), _password2.getValue()))
                    .check(uniqueUsername(_username.getValue()))
                    .callMethodOr(reportErrors());
            }
        };
    }

    /**
     * Factory method for username validators.
     *
     * @param username The username to check.
     * @return A new instance of the username validator.
     */
    private Validator uniqueUsername(final String username) {
        return new Validator() {
            public void validate(final Validate validate) {
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
                        _constants.passwordsDidNotMatch()
                    );
                }
                validate.next();
            }
        };
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    private Runnable createUser() {
        return new Runnable() {
            public void run() {
                final UserDTO userDto = new UserDTO();
                userDto.setUsername(_username.getValue());
                userDto.setEmail(_email.getValue());

                Globals.resourceService().createUser(
                    userDto,
                    _password1.getValue(),
                    new ErrorReportingCallback<Void>() {
                        public void onSuccess(final Void result) {
                            // TODO: Refresh the main window.
                            hide();
                        }
                    }
                );
            }
        };
    }
}
