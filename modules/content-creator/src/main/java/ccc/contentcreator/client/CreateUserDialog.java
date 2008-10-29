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
package ccc.contentcreator.client;


import static ccc.contentcreator.client.Validations.*;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.dialogs.EditDialog;
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
public class CreateUserDialog extends EditDialog {

    private final TextField<String> _username = new TextField<String>();
    private final TextField<String> _email = new TextField<String>();

    /**
     * Constructor.
     */
    public CreateUserDialog() {
        super(Globals.uiConstants().createUser());

        _username.setFieldLabel(_constants.username());
        _username.setAllowBlank(false);
        _username.setId(_constants.username());
        _panel.add(_username, new FormData("100%"));

        _email.setFieldLabel(_constants.email());
        _email.setAllowBlank(false);
        _email.setId(_constants.email());
        _panel.add(_email, new FormData("100%"));


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
                    // TODO: email format
                    .stopIfInError()
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
                                validate.addMessage( // TODO: I18n
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
                final UserDTO userDto = new UserDTO();
                userDto.setUsername(_username.getValue());
                userDto.setEmail(_email.getValue());

                Globals.resourceService().createUser(
                    userDto,
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
