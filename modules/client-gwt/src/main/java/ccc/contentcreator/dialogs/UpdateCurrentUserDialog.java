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
import ccc.contentcreator.actions.UpdateCurrentUserAction;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.contentcreator.validation.Validate;
import ccc.rest.UserSummary;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.http.client.Response;


/**
 * Dialog for current user's details updating.
 *
 * @author Civic Computing Ltd
 */
public class UpdateCurrentUserDialog extends AbstractEditDialog {

    private final TextField<String> _username = new TextField<String>();
    private final TextField<String> _email = new TextField<String>();
    private final TextField<String> _password1 = new TextField<String>();
    private final TextField<String> _password2 = new TextField<String>();

    private final UserSummary _user;

    /**
     * Constructor.
     *
     */
    public UpdateCurrentUserDialog() {
        super(new IGlobalsImpl().uiConstants().editUser(), new IGlobalsImpl());
        _user    = new IGlobalsImpl().currentUser();

        _username.setFieldLabel(constants().username());
        _username.setReadOnly(true);
        _username.setId("username");
        _username.setValue(_user.getUsername().toString());
        addField(_username);

        _email.setFieldLabel(constants().email());
        _email.setAllowBlank(false);
        _email.setId("useremail");
        _email.setValue(_user.getEmail());
        addField(_email);

        _password1.setPassword(true);
        _password1.setFieldLabel(constants().newPassword());
        _password1.setId("user_password");
        addField(_password1);

        _password2.setPassword(true);
        _password2.setFieldLabel(constants().confirmNewPassword());
        _password2.setId("user_confirmPassword");
        addField(_password2);

        setPanelId("UserPanel");
    }

    /** {@inheritDoc} */
    @Override protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                if (_password1.getValue() != null
                    && _password2.getValue() != null) {
                    Validate.callTo(updateUser())
                    .check(notEmpty(_email))
                    .stopIfInError()
                    .check(notValidEmail(_email))
                    .check(passwordStrength(_password1.getValue()))
                    .check(matchingPasswords(
                        _password1.getValue(), _password2.getValue()))
                        .callMethodOr(reportErrors());
                } else {
                    Validate.callTo(updateUser())
                    .check(notEmpty(_email))
                    .stopIfInError()
                    .check(notValidEmail(_email))
                        .callMethodOr(reportErrors());
                }
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

                new UpdateCurrentUserAction(
                    _user.getId(),
                    _email.getValue(),
                    _password1.getValue()
                ){
                    /** {@inheritDoc} */
                    @Override protected void onNoContent(final Response response) {
                        final UserSummary user = new UserSummary(
                            _email.getValue(),
                            _user.getId(),
                            _user.getUsername(),
                            _user.getRoles());
                        GLOBALS.currentUser(user);
                        hide();
                    }

                }.execute();
            }
        };
    }
}
