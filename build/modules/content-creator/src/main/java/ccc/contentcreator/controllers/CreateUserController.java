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
package ccc.contentcreator.controllers;

import static ccc.contentcreator.validation.Validations.*;

import java.util.HashMap;
import java.util.HashSet;

import ccc.api.UserDelta;
import ccc.api.UserSummary;
import ccc.api.Username;
import ccc.contentcreator.api.ActionNameConstants;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.api.UIMessages;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.EditController;
import ccc.contentcreator.client.IGlobals;
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validator;
import ccc.contentcreator.views.ICreateUserDialog;

/**
 * A controller for user creation.
 *
 * @author Civic Computing Ltd.
 */
public class CreateUserController implements EditController {

    private final UIConstants         _constants;
    private final UIMessages          _messages;
    private final ActionNameConstants _userActions;
    private final ICreateUserDialog   _dialog;
    private final IGlobals            _globals;

    /**
     * Constructor.
     *
     * @param dialog The create user dialog this controller will manage.
     * @param globals The globals factory for this controller.
     */
    public CreateUserController(final ICreateUserDialog dialog,
                                final IGlobals globals) {
        _dialog      = dialog;
        _globals     = globals;
        _constants   = _globals.uiConstants();
        _messages    = _globals.uiMessages();
        _userActions = _globals.userActions();
    }


    /** {@inheritDoc} */
    @Override
    public void submit() {
        Validate.callTo(createUser())
            .check(notEmpty(_dialog.getUsername()))
            .check(notEmpty(_dialog.getEmail()))
            .check(notEmpty(_dialog.getPassword1()))
            .check(notEmpty(_dialog.getPassword2()))
            .stopIfInError()
            .check(minLength(
                _dialog.getUsername(), IGlobals.MIN_USER_NAME_LENGTH))
            .check(notValidUserName(_dialog.getUsername()))
            .check(notValidEmail(_dialog.getEmail()))
            .check(matchingPasswords(
                _dialog.getPassword1().getValue(),
                _dialog.getPassword2().getValue()))
            .check(passwordStrength(_dialog.getPassword1().getValue()))
            .check(uniqueUsername(
                new Username(_dialog.getUsername().getValue())))
            .callMethodOr(reportErrors());
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
                _globals.queriesService().usernameExists(
                    username,
                    new ErrorReportingCallback<Boolean>(
                        _userActions.checkUniqueUsername()){
                        public void onSuccess(final Boolean exists) {
                            if (exists.booleanValue()) {
                                validate.addMessage(
                                    _messages.userWithUsernameAlreadyExists(
                                        username)
                                );
                            }
                            validate.next();
                        }
                    }
                );
            }

        };
    }


    private Runnable createUser() {
        return new Runnable() {
            public void run() {
                _globals.commandService().createUser(
                    new UserDelta(
                        _dialog.getEmail().getValue(),
                        new Username(_dialog.getUsername().getValue()),
                        new HashSet<String>(),
                        new HashMap<String, String>()),
                        _dialog.getPassword1().getValue(),
                        new ErrorReportingCallback<UserSummary>(
                            _constants.createUser()) {
                        public void onSuccess(final UserSummary result) {
                            // TODO: Refresh the main window.
                            _dialog.close();
                        }
                    }
                );
            }
        };
    }
}
