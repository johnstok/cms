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

import java.util.HashSet;
import java.util.Set;

import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.dialogs.EditDialog;
import ccc.contentcreator.dto.UserDTO;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
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
    private CheckBoxGroup _checkGroup = new CheckBoxGroup();
    private final UserTable _userTable;

    private static final String ROLE = "role";

    /**
     * Constructor.
     *
     * @param userDTO The userDTO of the selected user.
     * @param userTable The user table.
     */
    public EditUserDialog(final UserDTO userDTO, final UserTable userTable) {
        super(Globals.uiConstants().editUser());

        _userTable = userTable;
        _userDTO = userDTO;

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

        final Set<String> userRoles = _userDTO.getRoles();

        final CheckBox check1 = new CheckBox();
        check1.setBoxLabel(_constants.contentCreator());
        check1.setId(_constants.contentCreator());
        check1.setData(ROLE , "CONTENT_CREATOR");
        if (userRoles.contains("CONTENT_CREATOR")) {
            check1.setValue(true);
        }

        final CheckBox check2 = new CheckBox();
        check2.setBoxLabel(_constants.siteBuilder());
        check2.setId(_constants.siteBuilder());
        check2.setData(ROLE, "SITE_BUILDER");
        if (userRoles.contains("SITE_BUILDER")) {
            check2.setValue(true);
        }

        final CheckBox check3 = new CheckBox();
        check3.setBoxLabel(_constants.administrator());
        check3.setId(_constants.administrator());
        check3.setData(ROLE, "ADMINISTRATOR");
        if (userRoles.contains("ADMINISTRATOR")) {
            check3.setValue(true);
        }

        _checkGroup.setFieldLabel(_constants.roles());
        _checkGroup.setId(_constants.roles());
        _checkGroup.add(check1);
        _checkGroup.add(check2);
        _checkGroup.add(check3);
        _panel.add(_checkGroup, new FormData("100%"));

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
                    // TODO: username format
                    .stopIfInError()
                    .check(uniqueUsername(_userDTO, _username.getValue()))
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
                _userDTO.setEmail(_email.getValue());
                final Set<String> roles = new HashSet<String>();
                for (final CheckBox box : _checkGroup.getValues()) {
                    roles.add((String) box.getData(ROLE));
                }
                _userDTO.setRoles(roles);

                Globals.resourceService().updateUser(
                    _userDTO,
                    new ErrorReportingCallback<Void>() {
                        public void onSuccess(final Void result) {
                            _userTable.refreshUsers();
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
    private Validator uniqueUsername(
                                     final UserDTO userDTO,
                                     final String username) {
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
