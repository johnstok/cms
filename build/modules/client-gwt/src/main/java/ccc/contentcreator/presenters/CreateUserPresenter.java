/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.presenters;

import static ccc.contentcreator.validation.Validations.*;
import ccc.contentcreator.actions.remote.CreateUserAction;
import ccc.contentcreator.actions.remote.UniqueUsernameAction;
import ccc.contentcreator.core.EditController;
import ccc.contentcreator.core.IGlobals;
import ccc.contentcreator.i18n.UIMessages;
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validator;
import ccc.contentcreator.views.CreateUser;
import ccc.rest.dto.UserDto;
import ccc.types.Username;

import com.google.gwt.http.client.Response;

/**
 * A controller for user creation.
 *
 * @author Civic Computing Ltd.
 */
public class CreateUserPresenter implements EditController {

    private final UIMessages _messages;
    private final CreateUser _dialog;
    private final IGlobals   _globals;

    /**
     * Constructor.
     *
     * @param dialog The create user dialog this controller will manage.
     * @param globals The globals factory for this controller.
     */
    public CreateUserPresenter(final CreateUser dialog,
                                final IGlobals globals) {
        _dialog      = dialog;
        _globals     = globals;
        _messages    = _globals.uiMessages();
    }


    /**
     * Accessor.
     *
     * @return Returns the dialog.
     */
    CreateUser getDialog() {
        return _dialog;
    }


    /**
     * Accessor.
     *
     * @return Returns the messages.
     */
    UIMessages getMessages() {
        return _messages;
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
                                getMessages().userWithUsernameAlreadyExists(
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

                final UserDto d = new UserDto();
                d.setEmail(getDialog().getEmail().getValue());
                d.setUsername(
                    new Username(getDialog().getUsername().getValue()));
                d.setName(getDialog().getName().getValue());
                d.setPassword(getDialog().getPassword1().getValue());

                new CreateUserAction(d){
                    @Override protected void onOK(final Response response) {
                        // TODO: Refresh the main window.
                        getDialog().close();
                    }
                }.execute();
            }
        };
    }
}
