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


import static ccc.client.core.InternalServices.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import ccc.api.core.Group;
import ccc.client.core.Editable;
import ccc.client.core.Globals;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.ValidationResult;
import ccc.client.gwt.widgets.GroupListField;
import ccc.client.views.CreateUser;

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
    public CreateUserDialog(final Collection<Group> allGroups) {
        super(I18n.uiConstants.createUser(),
            InternalServices.globals);

        setLabelWidth(LABEL_WIDTH); // Long labels, should fit to one line.

        _username.setFieldLabel(constants().username());
        _username.setAllowBlank(false);
        _username.setMinLength(Globals.MIN_USER_NAME_LENGTH);
        addField(_username);

        _name.setFieldLabel(constants().fullName());
        _name.setAllowBlank(false);
        _name.setMinLength(Globals.MIN_USER_NAME_LENGTH);
        addField(_name);

        _email.setFieldLabel(constants().email());
        _email.setAllowBlank(false);
        addField(_email);

        _password1.setPassword(true);
        _password1.setFieldLabel(constants().password());
        _password1.setAllowBlank(false);
        addField(_password1);

        _password2.setPassword(true);
        _password2.setFieldLabel(constants().confirmPassword());
        _password2.setAllowBlank(false);
        addField(_password2);

        _groups = new GroupListField(allGroups, new HashSet<UUID>());
        addField(_groups);

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
    @Override
    public final String getUsername() { return _username.getValue(); }


    /** {@inheritDoc} */
    @Override
    public final String getName() { return _name.getValue(); }


    /** {@inheritDoc} */
    @Override
    public final String getPassword1() { return _password1.getValue(); }


    /** {@inheritDoc} */
    @Override
    public final String getPassword2() { return _password2.getValue(); }


    /** {@inheritDoc} */
    @Override
    public final String getEmail() { return _email.getValue(); }


    /** {@inheritDoc} */
    @Override
    public Set<UUID> getGroups() {
        final Set<UUID> validGroups = new HashSet<UUID>();
        for (final BaseModelData selected : _groups.getSelection()) {
            validGroups.add(selected.<UUID>get("id"));
        }
        return validGroups;
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

        result.addError(
            validator.notEmpty(
                getUsername(), constants().username()));
        result.addError(
            validator.notEmpty(
                getName(), constants().name()));
        result.addError(
            validator.notEmpty(
                getEmail(), constants().email()));
        result.addError(
            validator.notEmpty(
                getPassword1(), constants().password()));
        result.addError(
            validator.notEmpty(
                getPassword2(), constants().confirmPassword()));
        if (!result.getErrors().isEmpty()) {
            return result;
        }

        result.addError(
            validator.notValidUserName(
                getUsername(), constants().username()));
        result.addError(
            validator.notValidEmail(
                getEmail(), constants().email()));
        result.addError(
            validator.passwordStrength(getPassword1()));
        result.addError(
            validator.matchingPasswords(getPassword1(), getPassword2()));
        result.addError(
            validator.minLength(
                getUsername(),
                constants().username(),
                Globals.MIN_USER_NAME_LENGTH));

        if (!result.getErrors().isEmpty()) {
            return result;
        }

        return result;
    }


    /** {@inheritDoc} */
    @Override
    public void alert(final String message) {
        InternalServices.window.alert(message);
    }
}
