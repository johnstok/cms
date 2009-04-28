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


import static ccc.contentcreator.validation.Validations.*;

import java.util.HashSet;

import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validator;
import ccc.services.api.UserDelta;
import ccc.services.api.UserSummary;

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
                    .check(notValidResourceName(_username))
                    .check(notValidEmail(_email))
                    .check(matchingPasswords(
                        _password1.getValue(), _password2.getValue()))
                    .check(passwordStrength(_password1.getValue()))
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
                Globals.queriesService().usernameExists(
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
     * TODO: Add a description of this method.
     *
     * @return
     */
    private Runnable createUser() {
        return new Runnable() {
            public void run() {

                final UserDelta delta = new UserDelta();
                delta._username = _username.getValue();
                delta._email = _email.getValue();
                delta._password = _password1.getValue();
                delta._roles = new HashSet<String>();

                Globals.commandService().createUser(
                    delta,
                    new ErrorReportingCallback<UserSummary>() {
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
