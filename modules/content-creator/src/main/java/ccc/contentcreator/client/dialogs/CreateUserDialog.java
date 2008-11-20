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


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class CreateUserDialog extends AbstractEditDialog {

    private static final int LABEL_WIDTH = 150;
    private final TextField<String> _username = new TextField<String>();
    private final TextField<String> _password1 = new TextField<String>();
    private final TextField<String> _password2 = new TextField<String>();
    private final TextField<String> _email = new TextField<String>();

    /**
     * Constructor.
     */
    public CreateUserDialog() {
        super(Globals.uiConstants().createUser());

        setLabelWidth(LABEL_WIDTH); // Long labels, should fit to one line.

        _username.setFieldLabel(constants().username());
        _username.setAllowBlank(false);
        _username.setId(constants().username());
        addField(_username);

        _email.setFieldLabel(constants().email());
        _email.setAllowBlank(false);
        _email.setId(constants().email());
        addField(_email);

        _password1.setPassword(true);
        _password1.setFieldLabel(constants().password());
        _password1.setAllowBlank(false);
        _password1.setId(constants().password());
        addField(_password1);

        _password2.setPassword(true);
        _password2.setFieldLabel(constants().confirmPassword());
        _password2.setAllowBlank(false);
        _password2.setId(constants().confirmPassword());
        addField(_password2);


        setPanelId("UserPanel");
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
                        constants().passwordsDidNotMatch()
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
                            close();
                        }
                    }
                );
            }
        };
    }
}
