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
package ccc.contentcreator.client;


import static ccc.contentcreator.client.Validations.*;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.dto.UserDTO;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;


/**
 * Dialog for user editing.
 *
 * @author Civic Computing Ltd
 */
public class EditUserDialog extends EditDialog {

    private final TextField<String> _username = new TextField<String>();
    private final TextField<String> _email = new TextField<String>();
    private UserDTO _userDTO = new UserDTO();

    /**
     * Constructor.
     *
     * @param userDTO The userDTO of the selected user.
     */
    public EditUserDialog(final UserDTO userDTO) {
        _userDTO = userDTO;
        setHeading(_constants.editUser());

        _username.setFieldLabel(_constants.username());
        _username.setAllowBlank(false);
        _username.setId(_constants.username());
        _username.setValue(_userDTO.getUsername());
        _panel.add(_username, new FormData("100%"));

        _email.setFieldLabel(_constants.email());
        _email.setAllowBlank(false);
        _email.setId(_constants.email());
        _email.setValue(_userDTO.getEmail());
        _panel.add(_email, new FormData("100%"));

        _panel.setId("UserPanel");
        _save.setId("userSave");
        _cancel.setId("userCancel");
    }

    /** {@inheritDoc} */
    @Override protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                Validate.callTo(updateUser())
                    .check(notEmpty(_username))
                    .check(notEmpty(_email))
                    // TODO: email format
                    .stopIfInError()
                    .check(uniqueUsername(_userDTO, _username.getValue()))
                    .callMethodOr(reportErrors());
            }
        };
    }


    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    private Runnable updateUser() {
        return new Runnable() {
            public void run() {
                _userDTO.setUsername(_username.getValue());
                _userDTO.setEmail(_email.getValue());

                Globals.resourceService().updateUser(
                    _userDTO,
                    new ErrorReportingCallback<Void>() {
                        public void onSuccess(final Void result) {
                            // refresh user grid
                            hide();
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
    private Validator uniqueUsername(final UserDTO userDTO, final String username) {
        return new Validator() {
            public void validate(final Validate validate) {
                if (userDTO.getUsername().equals(username)) {
                    validate.next();
                } else {
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

            }

        };
    }
}
