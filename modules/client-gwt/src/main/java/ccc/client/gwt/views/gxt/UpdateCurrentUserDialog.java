/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
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
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.views.gxt;


import static ccc.client.core.InternalServices.validator;
import ccc.api.core.User;
import ccc.client.actions.UpdateCurrentUserAction;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.ValidationResult;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextField;


/**
 * Dialog for current user's details updating.
 *
 * @author Civic Computing Ltd
 */
public class UpdateCurrentUserDialog extends AbstractEditDialog {

    private final TextField<String> _username = new TextField<String>();
    private final TextField<String> _name = new TextField<String>();
    private final TextField<String> _email = new TextField<String>();
    private final TextField<String> _password1 = new TextField<String>();
    private final TextField<String> _password2 = new TextField<String>();

    private final User _user;

    /**
     * Constructor.
     *
     */
    public UpdateCurrentUserDialog() {
        super(I18n.uiConstants.editUser(), InternalServices.globals);
        _user = InternalServices.globals.currentUser();

        _username.setFieldLabel(constants().username());
        _username.setReadOnly(true);
        _username.setValue(_user.getUsername().toString());
        addField(_username);

        _name.setFieldLabel(constants().fullName());
        _name.setAllowBlank(false);
        _name.setValue(_user.getName());
        addField(_name);

        _email.setFieldLabel(constants().email());
        _email.setAllowBlank(false);
        _email.setValue(_user.getEmail());
        addField(_email);

        _password1.setPassword(true);
        _password1.setFieldLabel(constants().newPassword());
        addField(_password1);

        _password2.setPassword(true);
        _password2.setFieldLabel(constants().confirmNewPassword());
        addField(_password2);
    }

    /** {@inheritDoc} */
    @Override protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {

                final ValidationResult vr = new ValidationResult();
                vr.addError(
                    validator.notEmpty(
                        _email.getValue(), _email.getFieldLabel()));
                vr.addError(
                    validator.notEmpty(
                        _name.getValue(), _name.getFieldLabel()));
                vr.addError(
                    validator.notValidEmail(
                        _email.getValue(), _email.getFieldLabel()));

                if (null!=_password1.getValue()
                    || null!=_password2.getValue()) {
                    vr.addError(
                        validator.passwordStrength(_password1.getValue()));
                    vr.addError(
                        validator.matchingPasswords(
                            _password1.getValue(), _password2.getValue()));
                }

                if (!vr.isValid()) {
                    InternalServices.window.alert(vr.getErrorText());
                    return;
                }

                updateUser();
            }
        };
    }


    /**
     * Updates the edited user and calls user list refreshing.
     *
     * @return
     */
    private void updateUser() {
        final User user = new User();
        user.setId(_user.getId());
        user.setUsername(_user.getUsername());
        user.setGroups(_user.getGroups());
        user.setMetadata(_user.getMetadata());
        user.setPassword(_password1.getValue());
        user.setEmail(_email.getValue());
        user.setName(_name.getValue());

        new UpdateCurrentUserAction(user){
            /** {@inheritDoc} */
            @Override protected void onSuccess(final User newUser) {
                InternalServices.globals.currentUser(newUser);
                hide();
            }

        }.execute();
    }
}
