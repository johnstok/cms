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
package ccc.contentcreator.dialogs;


import static ccc.contentcreator.validation.Validations.matchingPasswords;
import static ccc.contentcreator.validation.Validations.minLength;
import static ccc.contentcreator.validation.Validations.notEmpty;
import static ccc.contentcreator.validation.Validations.notValidEmail;
import static ccc.contentcreator.validation.Validations.notValidUserName;
import static ccc.contentcreator.validation.Validations.passwordStrength;
import static ccc.contentcreator.validation.Validations.reportErrors;

import java.util.HashSet;

import ccc.api.UserDelta;
import ccc.api.UserSummary;
import ccc.api.Username;
import ccc.contentcreator.api.ActionNameConstants;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validator;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;


/**
 * Dialog for user creation.
 *
 * @author Civic Computing Ltd
 */
public class CreateUserDialog extends AbstractEditDialog {
    private static final ActionNameConstants USER_ACTIONS =
        GWT.create(ActionNameConstants.class);

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
        _username.setMinLength(Globals.MIN_USER_NAME_LENGTH);
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
                    .stopIfInError()
                    .check(minLength(_username, Globals.MIN_USER_NAME_LENGTH))
                    .check(notValidUserName(_username))
                    .check(notValidEmail(_email))
                    .check(matchingPasswords(
                        _password1.getValue(), _password2.getValue()))
                    .check(passwordStrength(_password1.getValue()))
                    .check(uniqueUsername(new Username(_username.getValue())))
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
    private Validator uniqueUsername(final Username username) {
        return new Validator() {
            public void validate(final Validate validate) {
                Globals.queriesService().usernameExists(
                    username,
                    new ErrorReportingCallback<Boolean>(USER_ACTIONS.checkUniqueUsername()){
                        public void onSuccess(final Boolean exists) {
                            if (exists) {
                                validate.addMessage(
                                    _messages.userWithUsernameAlreadyExists(username)
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
     * TODO: Add a description of this method.
     *
     * @return
     */
    private Runnable createUser() {
        return new Runnable() {
            public void run() {
                Globals.commandService().createUser(
                    new UserDelta(
                        _email.getValue(),
                        new Username(_username.getValue()),
                        new HashSet<String>()),
                    _password1.getValue(),
                    new ErrorReportingCallback<UserSummary>(_constants.createUser()) {
                        public void onSuccess(final UserSummary result) {
                            // TODO: Refresh the main window.
                            close();
                        }
                    }
                );
            }
        };
    }
}
