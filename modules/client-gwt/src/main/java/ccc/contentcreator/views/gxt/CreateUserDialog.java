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
package ccc.contentcreator.views.gxt;


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import ccc.api.dto.GroupDto;
import ccc.api.types.Username;
import ccc.contentcreator.core.Editable;
import ccc.contentcreator.core.Globals;
import ccc.contentcreator.core.GlobalsImpl;
import ccc.contentcreator.core.ValidationResult;
import ccc.contentcreator.core.Validations2;
import ccc.contentcreator.remoting.UniqueUsernameAction;
import ccc.contentcreator.views.CreateUser;
import ccc.contentcreator.widgets.GroupListField;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextField;


/**
 * Dialog for user creation.
 *
 * @author Civic Computing Ltd
 */
public class CreateUserDialog
    extends
        AbstractEditDialog
    implements
        CreateUser {

    private Editable _presenter;

    private static final int LABEL_WIDTH = 150;
    private final TextField<String> _username = new TextField<String>();
    private final TextField<String> _name = new TextField<String>();
    private final TextField<String> _password1 = new TextField<String>();
    private final TextField<String> _password2 = new TextField<String>();
    private final TextField<String> _email = new TextField<String>();
    private final GroupListField _groups;

    /**
     * Constructor.
     *
     * @param allGroups The list of all groups.
     */
    public CreateUserDialog(final Collection<GroupDto> allGroups) {
        super(new GlobalsImpl().uiConstants().createUser(),
             new GlobalsImpl());

        setLabelWidth(LABEL_WIDTH); // Long labels, should fit to one line.

        _username.setFieldLabel(constants().username());
        _username.setAllowBlank(false);
        _username.setMinLength(Globals.MIN_USER_NAME_LENGTH);
        _username.setId("username");
        addField(_username);

        _name.setFieldLabel(constants().fullName());
        _name.setAllowBlank(false);
        _name.setMinLength(Globals.MIN_USER_NAME_LENGTH);
        _name.setId("name");
        addField(_name);

        _email.setFieldLabel(constants().email());
        _email.setAllowBlank(false);
        _email.setId("user_email");
        addField(_email);

        _password1.setPassword(true);
        _password1.setFieldLabel(constants().password());
        _password1.setAllowBlank(false);
        _password1.setId("user_password");
        addField(_password1);

        _password2.setPassword(true);
        _password2.setFieldLabel(constants().confirmPassword());
        _password2.setAllowBlank(false);
        _password2.setId("user_confirmPassword");
        addField(_password2);

        _groups = new GroupListField(allGroups, new HashSet<UUID>());
        addField(_groups);

        setPanelId("UserPanel");
    }


    /** {@inheritDoc} */
    @Override protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>(){
            @Override public void componentSelected(final ButtonEvent ce) {
                getPresenter().save();
            }
        };
    }


    /** {@inheritDoc} */
    public final String getUsername() { return _username.getValue(); }


    /** {@inheritDoc} */
    public final String getName() { return _name.getValue(); }


    /** {@inheritDoc} */
    public final String getPassword1() { return _password1.getValue(); }


    /** {@inheritDoc} */
    public final String getPassword2() { return _password2.getValue(); }


    /** {@inheritDoc} */
    public final String getEmail() { return _email.getValue(); }


    /** {@inheritDoc} */
    @Override
    public Set<UUID> getGroups() {
        final Set<UUID> validRoles = new HashSet<UUID>();
        for (final BaseModelData selected : _groups.getSelection()) {
            validRoles.add(selected.<UUID>get("id"));
        }
        return validRoles;
    }

    /** {@inheritDoc} */
    @Override
    public void show(final Editable presenter) {
        _presenter = presenter;
        super.show();
    }

    /**
     * Accessor.
     *
     * @return Returns the presenter.
     */
    Editable getPresenter() {
        return _presenter;
    }


    /** {@inheritDoc} */
    @Override
    public ValidationResult getValidationResult() {
        final ValidationResult result = new ValidationResult();
        if (!Validations2.notEmpty(getUsername())) {
            result.addError(constants().username()+" "
                +constants().cannotBeEmpty());
        }
        if (!Validations2.notEmpty(getName())) {
            result.addError(constants().nameMustNotBeEmpty());
        }
        if (!Validations2.notEmpty(getEmail())) {
            result.addError(constants().email()+" "
                +constants().cannotBeEmpty());
        }
        if (!Validations2.notEmpty(getPassword1())) {
            result.addError(constants().password()+" "
                +constants().cannotBeEmpty());
        }
        if (!Validations2.notEmpty(getPassword2())) {
            result.addError(constants().confirmPassword()+" "
                +constants().cannotBeEmpty());
        }
        if (!result.getErrors().isEmpty()) {
            return result;
        }
        if (!Validations2.minLength(getUsername(),
            Globals.MIN_USER_NAME_LENGTH)) {
            result.addError(constants().username()+" "
                +constants().isTooShort());
        }
        if (!Validations2.notValidUserName(getUsername())) {
            result.addError(constants().isNotValidUserName());
        }
        if (!Validations2.notValidEmail(getEmail())) {
            result.addError(constants().email()+" "+constants().isNotValid());
        }
        if (!Validations2.match(getPassword1(), getPassword2())) {
            result.addError(constants().passwordsDidNotMatch());
        }
        if (!Validations2.passwordStrength(getPassword1())) {
            result.addError(constants().passwordTooWeak());
        }
        uniqueUsername(new Username(getUsername()), result);
        return result;
    }


    private void uniqueUsername(final Username username,
                                final ValidationResult result) {
        new UniqueUsernameAction(username){
            @Override
            protected void execute(final boolean usernameExists) {
                if (usernameExists) {
                    result.addError(
                        getMessages().userWithUsernameAlreadyExists(username));
                }
            }
        }.execute();
    }


    /** {@inheritDoc} */
    @Override
    public void alert(final String message) { getGlobals().alert(message); }
}
