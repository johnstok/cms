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
import ccc.api.Username;
import ccc.contentcreator.actions.CreateUserAction_;
import ccc.contentcreator.actions.UniqueUsernameAction;
import ccc.contentcreator.api.ActionNameConstants;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.api.UIMessages;
import ccc.contentcreator.client.EditController;
import ccc.contentcreator.client.IGlobals;
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validator;
import ccc.contentcreator.views.ICreateUserDialog;

import com.google.gwt.http.client.Response;

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
                new UniqueUsernameAction(username){
                    @Override
                    protected void execute(final boolean usernameExists) {
                        if (usernameExists) {
                            validate.addMessage(
                                _messages.userWithUsernameAlreadyExists(
                                    username)
                            );
                        }
                        validate.next();
                    }
                }.execute();
            }
        };
    }


    private Runnable createUser() {
        return new Runnable() {
            public void run() {
                final UserDelta d = new UserDelta(
                    _dialog.getEmail().getValue(),
                    new Username(_dialog.getUsername().getValue()),
                    new HashSet<String>(),
                    new HashMap<String, String>());
                final String p = _dialog.getPassword1().getValue();

                new CreateUserAction_(d, p){
                    @Override protected void onOK(final Response response) {
                        // TODO: Refresh the main window.
                        _dialog.close();
                    }
                }.execute();
            }
        };
    }
}
